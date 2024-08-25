package acceptance;

import client.RobotWorldClient;
import client.RobotWorldJsonClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LaunchRobotTests {
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

    @ParameterizedTest
    @ValueSource(strings = {"libs/reference-server-0.2.3.jar", "out/artifacts/Server_jar/RobotWorld.jar"})
    void validLaunchShouldSucceed(String jarPath) throws IOException, InterruptedException {
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

        // And the position should be (x:0, y:0)
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("position"));
        assertEquals(0, response.get("data").get("position").get(0).asInt());
        assertEquals(0, response.get("data").get("position").get(1).asInt());

        // And I should also get the state of the robot
        assertNotNull(response.get("state"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"libs/reference-server-0.2.3.jar", "out/artifacts/Server_jar/RobotWorld.jar"})
    void invalidLaunchShouldFail(String jarPath) throws IOException, InterruptedException {
        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1
        startServer(jarPath, DEFAULT_PORT, "1", "none");
        assertTrue(serverClient.isConnected());

        // When I send an invalid launch request with the command "luanch" instead of "launch"
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"luanch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an error response
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());

        // And the message "Unsupported command"
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        assertTrue(response.get("data").get("message").asText().contains("Unsupported command"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"libs/reference-server-0.2.3.jar", "out/artifacts/Server_jar/RobotWorld.jar"})
    void noMoreSpaceInWorld(String jarPath) throws IOException, InterruptedException {
        // Given that I am connected to a Robot Worlds server
        // And the world is of size 1x1
        startServer(jarPath, DEFAULT_PORT, "1", "none");
        assertTrue(serverClient.isConnected());

        // And every coordinate has an object
        String request = "{" +
                "  \"robot\": \"HAL " + "\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("position"));
        assertEquals(0, response.get("data").get("position").get(0).asInt());
        assertEquals(0, response.get("data").get("position").get(1).asInt());

        // When I send a valid launch request to the server
        String request2 = "{" +
                "  \"robot\": \"HALO\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response2 = serverClient.sendRequest(request2);

        // Then I should get an "ERROR" response
        assertNotNull(response2.get("result"));
        assertEquals("ERROR", response2.get("result").asText());

        // And the message "No more space in this world"
        assertEquals("No more space in this world", response2.get("data").get("message").asText());
    }

    @ParameterizedTest
    @ValueSource(strings = {"libs/reference-server-0.2.3.jar", "out/artifacts/Server_jar/RobotWorld.jar"})
    void nameAlreadyExists(String jarPath) throws IOException, InterruptedException {
        // Given that I am connected to a Robot Worlds server
        // And the world is of size 2x2
        startServer(jarPath, DEFAULT_PORT, "2", "none");
        assertTrue(serverClient.isConnected());

        // And there is already a robot with the same name I chose
        String request1 = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response1 = serverClient.sendRequest(request1);

        assertNotNull(response1.get("result"));
        assertEquals("OK", response1.get("result").asText());

        // When I send a valid launch request to the server with the same robot name "Hal"
        String request2 = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response2 = serverClient.sendRequest(request2);

        // Then I should get an "ERROR" response
        assertNotNull(response2.get("result"));
        assertEquals("ERROR", response2.get("result").asText());

        // And the message "Too many of you in this world"
        assertNotNull(response2.get("data"));
        assertNotNull(response2.get("data").get("message"));
        assertEquals("Too many of you in this world", response2.get("data").get("message").asText());
    }

    @ParameterizedTest
    @ValueSource(strings = {"libs/reference-server-0.2.3.jar", "out/artifacts/Server_jar/RobotWorld.jar"})
    void launchAnotherRobotShouldSucceed(String jarPath) throws IOException, InterruptedException {
        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 2x2
        startServer(jarPath, DEFAULT_PORT, "2", "none");
        assertTrue(serverClient.isConnected());

        // And robot "HAL" has already been launched into the world
        String Request1 = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode Response = serverClient.sendRequest(Request1);
        assertNotNull(Response.get("result"));
        assertEquals("OK", Response.get("result").asText());

        // When I launch a second robot "R2D2" into the world
        String Request2 = "{" +
                "\"robot\": \"R2D2\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"shooter\"]" +
                "}";
        JsonNode Response2 = serverClient.sendRequest(Request2);
        assertNotNull(Response2.get("result"));
        assertEquals("OK", Response2.get("result").asText());

        // Then the launch should be successful
        // and a randomly allocated position of R2D2 should be returned.
        assertNotNull(Response2.get("data"));
        JsonNode position = Response2.get("data").get("position");
        assertNotNull(position);
        assertTrue(position.get(0).asInt() >= -2 && position.get(0).asInt() <= 2);
        assertTrue(position.get(1).asInt() >= -2 && position.get(1).asInt() <= 2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"libs/reference-server-0.2.3.jar", "out/artifacts/Server_jar/RobotWorld.jar"})
    void worldWithoutObstaclesFull(String jarPath) throws IOException, InterruptedException {
//        Given a world of size 2x2
        startServer(jarPath, DEFAULT_PORT, "2", "none");
        assertTrue(serverClient.isConnected());

//        and I have successfully launched 9 robots into the world
        for (int x = 1; x < 10; x++) {
            String request = "{" +
                    "\"robot\": \"HAL " + x + "\"," +
                    "\"command\": \"launch\"," +
                    "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                    "}";
            serverClient.sendRequest(request);
        }
//        When I launch one more robot
        String request2 = "{" +
                "  \"robot\": \"HAL 10\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request2);


//        Then I should get an error response back with the message "No more space in this world"
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());

        // And the message "No more space in this world"assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        assertTrue(response.get("data").get("message").asText().contains("No more space in this world"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"libs/reference-server-0.2.3.jar", "out/artifacts/Server_jar/RobotWorld.jar"})
    void robotsIntoWorldWithObstacle(String jarPath) throws IOException, InterruptedException {
        startServer(jarPath, DEFAULT_PORT, "2", "1,1");
        assertTrue(serverClient.isConnected());

        // Launch 8 robots into the world
        for (int x = 1; x <= 8; x++) {
            String request = "{" +
                    "\"robot\": \"HAL " + x + "\"," +
                    "\"command\": \"launch\"," +
                    "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                    "}";
            JsonNode response = serverClient.sendRequest(request);
            assertNotNull(response, "Launch response for robot HAL " + x + " is null");
            assertEquals("OK", response.get("result").asText(), "Robot launch failed for HAL " + x);



            // Each robot cannot be in position [1,1]
            JsonNode state = response.get("state");
            assertNotNull(state, "State is null for robot HAL " + x);
            JsonNode position = state.get("position");
            assertNotNull(position, "Position is null for robot HAL " + x);
            int posX = position.get(0).asInt();
            int posY = position.get(1).asInt();
            assertFalse(posX == 1 && posY == 1, "Robot HAL " + x + " is in the obstacle position [1,1]");
        }
    }
}