package acceptance;

import client.RobotWorldClient;
import client.RobotWorldJsonClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class StateTest {
    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();
    private Process serverProcess;

    @AfterEach
    void tearDown() {
        stopServer();
        serverClient.disconnect();
    }

    private void startServer(String jarPath, int port, String size, String obstacle) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "-jar", jarPath,
                "-p", String.valueOf(port),
                "-s", size,
                "-o", obstacle
        );
        serverProcess = processBuilder.start();

        // Wait for the server to start
        Thread.sleep(1000);

        // Connect to the server
        serverClient.connect(DEFAULT_IP, port);

        // Verify connection
        assertTrue(serverClient.isConnected(), "Failed to connect to the server");
    }

    private void stopServer() {
        if (serverProcess != null) {
            serverProcess.destroy();
            try {
                serverProcess.waitFor();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"out/artifacts/Server_jar/RobotWorld.jar", "libs/reference-server-0.2.3.jar"})
    void robotExistsInWorld(String jarPath) throws IOException, InterruptedException {
        startServer(jarPath, DEFAULT_PORT, "1", "none");
        assertTrue(serverClient.isConnected());

        //    Given that I already exist in the World server
        String launchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"2\",\"2\"]" +
                "}";
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);

        assertNotNull(launchResponse.get("result"));
        assertEquals("OK", launchResponse.get("result").asText());

        //    When I send a Valid state request to the Server
        String stateRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"state\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode stateResponse = serverClient.sendRequest(stateRequest);

        //    Then I should get a valid response from the server
        assertNotNull(stateResponse.get("result"));
        assertEquals("OK", stateResponse.get("result").asText());
        //    And I should get the state of the robot
        assertNotNull(stateResponse.get("state"));
        assertNotNull(stateResponse.get("state").get("position"));
        assertEquals(0, stateResponse.get("state").get("position").get(0).asInt());
        assertEquals(0, stateResponse.get("state").get("position").get(1).asInt());

        assertNotNull(stateResponse.get("state").get("direction"));
        assertEquals("NORTH", stateResponse.get("state").get("direction").asText());

        assertNotNull(stateResponse.get("state").get("shields"));
        assertEquals(0,stateResponse.get("state").get("shields").asInt());

        assertNotNull(stateResponse.get("state").get("shots"));
        assertEquals(0,stateResponse.get("state").get("shots").asInt());

        assertNotNull(stateResponse.get("state").get("status"));
        assertEquals("TODO", stateResponse.get("state").get("status").asText());
    }
}
