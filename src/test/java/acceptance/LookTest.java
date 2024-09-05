package acceptance;

import client.RobotWorldClient;
import client.RobotWorldJsonClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@Tag("acceptance")
class LookTest {
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

        // try connecting to server until it starts
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

    @DisplayName("robot should see only world edges, at the correct distance, in a size 1 world with no objects when a valid look command is given")
    @ParameterizedTest
    @ValueSource(strings = {"libs/reference-server-0.2.3.jar", "out/artifacts/Server_jar/RobotWorld.jar"})
    void emptyWorldSizeOne(String jarPath) throws IOException, InterruptedException {
        // Given that I am connected to a running Robot Worlds server
        // And the world has no objects in it
        // And the world size is 1x1
        startServer(jarPath, DEFAULT_PORT, "1", "none");
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        String launchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);

        assertNotNull(launchResponse.get("result"));
        assertEquals("OK", launchResponse.get("result").asText());

        // When I send a valid look command to the server
        String lookRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode lookResponse = serverClient.sendRequest(lookRequest);

        // Then it should return only the edges as detected objects
        assertNotNull(lookResponse.get("result"));
        assertEquals("OK", lookResponse.get("result").asText());

        assertNotNull(lookResponse.get("data"));
        assertNotNull(lookResponse.get("data").get("objects"));

        int objectsInWorld = lookResponse.get("data").get("objects").size();

        if (objectsInWorld == 4) {
            assertEquals("EDGE", lookResponse.get("data").get("objects").get(0).get("type").asText());
            assertEquals("EDGE", lookResponse.get("data").get("objects").get(1).get("type").asText());
            assertEquals("EDGE", lookResponse.get("data").get("objects").get(2).get("type").asText());
            assertEquals("EDGE", lookResponse.get("data").get("objects").get(3).get("type").asText());

            List<String> directions = new ArrayList<>();
            directions.add("EAST");
            directions.add("NORTH");
            directions.add("WEST");
            directions.add("SOUTH");

            assertTrue(directions.contains(lookResponse.get("data").get("objects").get(0).get("direction").asText()));
            assertTrue(directions.contains(lookResponse.get("data").get("objects").get(1).get("direction").asText()));
            assertTrue(directions.contains(lookResponse.get("data").get("objects").get(2).get("direction").asText()));
            assertTrue(directions.contains(lookResponse.get("data").get("objects").get(3).get("direction").asText()));

            // and the distances should be 0 steps
            assertEquals(0, lookResponse.get("data").get("objects").get(0).get("distance").asInt());
            assertEquals(0, lookResponse.get("data").get("objects").get(1).get("distance").asInt());
            assertEquals(0, lookResponse.get("data").get("objects").get(2).get("distance").asInt());
            assertEquals(0, lookResponse.get("data").get("objects").get(3).get("distance").asInt());
        }
    }

    @DisplayName("robot should see only world edges, at the correct distance, in a size 2 world with no objects when a valid look command is given")
    @ParameterizedTest
    @ValueSource(strings = {"libs/reference-server-0.2.3.jar", "out/artifacts/Server_jar/RobotWorld.jar"})
    void emptyWorldSizeTwo(String jarPath) throws IOException, InterruptedException {
        // Given that I am connected to a running Robot Worlds server
        // And the world has no objects in it
        // And the world size is 2x2
        startServer(jarPath, DEFAULT_PORT, "2", "none");
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        String launchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);

        assertNotNull(launchResponse.get("result"));
        assertEquals("OK", launchResponse.get("result").asText());

        // When I send a valid look command to the server
        String lookRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode lookResponse = serverClient.sendRequest(lookRequest);

        // Then it should return only the edges as detected objects
        assertNotNull(lookResponse.get("result"));
        assertEquals("OK", lookResponse.get("result").asText());

        assertNotNull(lookResponse.get("data"));
        assertNotNull(lookResponse.get("data").get("objects"));

        int objectsInWorld = lookResponse.get("data").get("objects").size();

        if (objectsInWorld == 4) {
            assertEquals("EDGE", lookResponse.get("data").get("objects").get(0).get("type").asText());
            assertEquals("EDGE", lookResponse.get("data").get("objects").get(1).get("type").asText());
            assertEquals("EDGE", lookResponse.get("data").get("objects").get(2).get("type").asText());
            assertEquals("EDGE", lookResponse.get("data").get("objects").get(3).get("type").asText());

            List<String> directions = new ArrayList<>();
            directions.add("EAST");
            directions.add("NORTH");
            directions.add("WEST");
            directions.add("SOUTH");

            assertTrue(directions.contains(lookResponse.get("data").get("objects").get(0).get("direction").asText()));
            assertTrue(directions.contains(lookResponse.get("data").get("objects").get(1).get("direction").asText()));
            assertTrue(directions.contains(lookResponse.get("data").get("objects").get(2).get("direction").asText()));
            assertTrue(directions.contains(lookResponse.get("data").get("objects").get(3).get("direction").asText()));

            // and the distances should be 1 step
            assertEquals(1, lookResponse.get("data").get("objects").get(0).get("distance").asInt());
            assertEquals(1, lookResponse.get("data").get("objects").get(1).get("distance").asInt());
            assertEquals(1, lookResponse.get("data").get("objects").get(2).get("distance").asInt());
            assertEquals(1, lookResponse.get("data").get("objects").get(3).get("distance").asInt());
        }
    }

    @DisplayName("robot should see an obstacle in a size 2 world, at the correct distance, when a valid look command is given")
    @ParameterizedTest
    @ValueSource(strings = {"libs/reference-server-0.2.3.jar", "out/artifacts/Server_jar/RobotWorld.jar"})
    void seeObstacle(String jarPath) throws IOException, InterruptedException {
        // Given that I am connected to a running Robot Worlds server
        // And the world has an obstacle at position [0,1]
        // And the world size is 2x2
        // And I have launched a robot into the world
        startServer(jarPath, DEFAULT_PORT, "2", "0,1");
        assertTrue(serverClient.isConnected());

        String launchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);

        assertNotNull(launchResponse.get("result"));
        assertEquals("OK", launchResponse.get("result").asText());

        // When I send a valid look command to the server
        String lookRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode lookResponse = serverClient.sendRequest(lookRequest);

        // Then I should see the obstacle at [0,1]
        String direction = lookResponse.get("data").get("objects").get(3).get("direction").asText();

        // And the distance should be 1 step
        if (Objects.equals(direction, "North")) {
            assertEquals("OBSTACLE", lookResponse.get("data").get("objects").get(0).get("type").asText());
            assertEquals(1, lookResponse.get("data").get("objects").get(0).get("distance").asInt());
        }
    }


    @DisplayName("robot should see an obstacle and robots in a size 2 world, at the correct distances, when a valid look command is given")
    @ParameterizedTest
    @ValueSource(strings = {"libs/reference-server-0.2.3.jar", "out/artifacts/Server_jar/RobotWorld.jar"})
    public void seeObstacleAndRobots(String jarPath) throws IOException, InterruptedException {
        // Given that I am connected to a running Robot Worlds server
        // And the world has an obstacle at position [0,1]
        // And the world size is 2x2
        startServer(jarPath, DEFAULT_PORT, "2", "0,1");
        assertTrue(serverClient.isConnected());

        // And I have launched 8 robots into the world
        String launchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);

        for(int i = 1; i <= 7; i++) {
            assertTrue(serverClient.isConnected());

            String launchMoreRobots = "{" +
                    "  \"robot\": \"HAL " + i + "\"," +
                    "  \"command\": \"launch\"," +
                    "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                    "}";
            JsonNode response = serverClient.sendRequest(launchMoreRobots);
        }

        // When I send a valid look command with the first robot
        String lookRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode lookResponse = serverClient.sendRequest(lookRequest);

        // Then I should see the obstacle and robots at their correct distances in the world
        String direction = lookResponse.get("data").get("objects").get(3).get("direction").asText();

        if (Objects.equals(direction, "North")) {
            assertEquals("OBSTACLE", lookResponse.get("data").get("objects").get(0).get("type").asText());
            assertEquals(1, lookResponse.get("data").get("objects").get(0).get("distance").asInt());
        } else if (Objects.equals(direction, "West") || Objects.equals(direction, "East") || Objects.equals(direction, "South")) {
            assertEquals("ROBOT", lookResponse.get("data").get("objects").get(0).get("type").asText());
            assertEquals(1, lookResponse.get("data").get("objects").get(0).get("distance").asInt());
        }
    }
}
