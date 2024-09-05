package acceptance;

import client.RobotWorldClient;
import client.RobotWorldJsonClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Tag("acceptance")
public class MovementTest {
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

        while (true) {
            try {
                serverClient.connect(DEFAULT_IP, port);
                break;
            } catch (RuntimeException ignored) {}
        }

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

    @DisplayName("valid forward command should return OK result with correct robot position")
    @ParameterizedTest
    @ValueSource(strings = {"libs/reference-server-0.2.3.jar", "out/artifacts/Server_jar/RobotWorld.jar"})
    void validForwardMovementShouldSucceed(String jarPath) throws IOException, InterruptedException {
        // Given that I am connected to a running Robot Worlds server
        // And the world has no objects in it
        // And the world is of size 1x1
        startServer(jarPath, DEFAULT_PORT, "1", "none");
        assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        // When I send a command for "HAL" to move forward by 5 steps
        String requestForward5 = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"forward\"," +
                "  \"arguments\": [\"5\"]" +
                "}";
        JsonNode responseForward = serverClient.sendRequest(requestForward5);

        // Then I should get an "OK" response with the message "At the North edge"
        assertNotNull(responseForward.get("result"));
        assertEquals("OK", responseForward.get("result").asText());

        // And the position information returned should be at co-ordinates [0,0]
        assertNotNull(responseForward.get("state"));
        assertNotNull(responseForward.get("state").get("position"));
        assertEquals(0, responseForward.get("state").get("position").get(0).asInt());
        assertEquals(0, responseForward.get("state").get("position").get(1).asInt());
        assertNotNull(responseForward.get("data"));
        assertNotNull(responseForward.get("data").get("message"));
        assertEquals("At the NORTH edge", responseForward.get("data").get("message").asText());

    }

    @DisplayName("valid back command should return OK result with correct robot position")
    @ParameterizedTest
    @ValueSource(strings = {"out/artifacts/Server_jar/RobotWorld.jar"})
    void validBackwardMovementShouldSucceed(String jarPath) throws IOException, InterruptedException {
        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        startServer(jarPath, DEFAULT_PORT, "1", "none");
        assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        // When I send a command for "HAL" to move back by 5 steps
        String requestForward5 = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"back\"," +
                "  \"arguments\": [\"5\"]" +
                "}";
        JsonNode responseForward = serverClient.sendRequest(requestForward5);

        // Then I should get an "OK" response with the message "At the North edge"
        assertNotNull(responseForward.get("result"));
        assertEquals("OK", responseForward.get("result").asText());

        // And the position information returned should be at co-ordinates [0,0]
        assertNotNull(responseForward.get("state"));
        assertNotNull(responseForward.get("state").get("position"));
        assertEquals(0, responseForward.get("state").get("position").get(0).asInt());
        assertEquals(0, responseForward.get("state").get("position").get(1).asInt());
        assertNotNull(responseForward.get("data"));
        assertNotNull(responseForward.get("data").get("message"));
        assertEquals("At the SOUTH edge", responseForward.get("data").get("message").asText());

    }
}