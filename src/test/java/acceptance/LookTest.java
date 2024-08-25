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

    @ParameterizedTest
    @ValueSource(strings = {"libs/reference-server-0.2.3.jar", "out/artifacts/Server_jar/RobotWorld.jar"})
    void emptyWorldSizeOne(String jarPath) throws IOException, InterruptedException {
        startServer(jarPath, DEFAULT_PORT, "1", "none");
        assertTrue(serverClient.isConnected());

        String launchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);

        assertNotNull(launchResponse.get("result"));
        assertEquals("OK", launchResponse.get("result").asText());

        String lookRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode lookResponse = serverClient.sendRequest(lookRequest);

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

            assertEquals(0, lookResponse.get("data").get("objects").get(0).get("distance").asInt());
            assertEquals(0, lookResponse.get("data").get("objects").get(1).get("distance").asInt());
            assertEquals(0, lookResponse.get("data").get("objects").get(2).get("distance").asInt());
            assertEquals(0, lookResponse.get("data").get("objects").get(3).get("distance").asInt());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"libs/reference-server-0.2.3.jar", "out/artifacts/Server_jar/RobotWorld.jar"})
    void emptyWorldSizeTwo(String jarPath) throws IOException, InterruptedException {
        startServer(jarPath, DEFAULT_PORT, "2", "none");
        assertTrue(serverClient.isConnected());

        String launchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);

        assertNotNull(launchResponse.get("result"));
        assertEquals("OK", launchResponse.get("result").asText());

        String lookRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode lookResponse = serverClient.sendRequest(lookRequest);

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

            assertEquals(1, lookResponse.get("data").get("objects").get(0).get("distance").asInt());
            assertEquals(1, lookResponse.get("data").get("objects").get(1).get("distance").asInt());
            assertEquals(1, lookResponse.get("data").get("objects").get(2).get("distance").asInt());
            assertEquals(1, lookResponse.get("data").get("objects").get(3).get("distance").asInt());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"libs/reference-server-0.2.3.jar", "out/artifacts/Server_jar/RobotWorld.jar"})
    void seeObstacle(String jarPath) throws IOException, InterruptedException {
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

        String lookRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode lookResponse = serverClient.sendRequest(lookRequest);

        String direction = lookResponse.get("data").get("objects").get(3).get("direction").asText();

        if (Objects.equals(direction, "North")) {
            assertEquals("OBSTACLE", lookResponse.get("data").get("objects").get(0).get("type").asText());
            assertEquals(1, lookResponse.get("data").get("objects").get(0).get("distance").asInt());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"libs/reference-server-0.2.3.jar", "out/artifacts/Server_jar/RobotWorld.jar"})
    public void seeObstacleAndRobots(String jarPath) throws IOException, InterruptedException {
        startServer(jarPath, DEFAULT_PORT, "2", "0,1");
        assertTrue(serverClient.isConnected());

        String launchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);

        for(int i = 1; i <= 8; i++) {
            assertTrue(serverClient.isConnected());

            String launchMoreRobots = "{" +
                    "  \"robot\": \"HAL\"," +
                    "  \"command\": \"launch\"," +
                    "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                    "}";
            JsonNode response = serverClient.sendRequest(launchMoreRobots);
        }
        String lookRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode lookResponse = serverClient.sendRequest(lookRequest);
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
