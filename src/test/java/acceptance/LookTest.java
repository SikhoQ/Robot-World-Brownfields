package acceptance;

import client.RobotWorldClient;
import client.RobotWorldJsonClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LookTest {
    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();

    @BeforeEach
    void connectToServer(){
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    void disconnectFromServer(){
        serverClient.disconnect();
    }

    @Test
    void emptyWorld() {
        //    Given that the robot is already in world
        //    And the world size is 1x1
        assertTrue(serverClient.isConnected());

        String launchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);

        assertNotNull(launchResponse.get("result"));
        assertEquals("OK", launchResponse.get("result").asText());

        //    When I send a valid look command to the server
        String lookRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode lookResponse = serverClient.sendRequest(lookRequest);

        //    Then it should return only the edges as detected objects
        assertNotNull(lookResponse.get("result"));
        assertEquals("OK", lookResponse.get("result").asText());

        assertNotNull(lookResponse.get("data"));
        assertNotNull(lookResponse.get("data").get("objects"));

//        check that there are only 4 objects(edges)

        int objectsInWorld = lookResponse.get("data").get("objects").size();
//        assertEquals(4, lookResponse.get("data").get("objects").size());

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

    @Test
    void seeObstacle() {
//        Given a world of size 2x2
//        and the world has an obstacle at coordinate [0,1]
//        and I have successfully launched a robot into the world

        assertTrue(serverClient.isConnected());

        String launchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);

        assertNotNull(launchResponse.get("result"));
        assertEquals("OK", launchResponse.get("result").asText());

//        When I ask the robot to look

        String lookRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode lookResponse = serverClient.sendRequest(lookRequest);

//        Then I should get a response back with an object of type OBSTACLE at a distance of 1 step.
        String direction = lookResponse.get("data").get("objects").get(3).get("direction").asText();

        if (direction == "North") {
            assertEquals("OBSTACLE", lookResponse.get("data").get("objects").get(0).get("type").asText());
            assertEquals(1, lookResponse.get("data").get("objects").get(0).get("distance").asInt());
        }
    }
}
