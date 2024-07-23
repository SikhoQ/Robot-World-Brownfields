package acceptance.world_2x2;



import client.RobotWorldClient;
import client.RobotWorldJsonClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * As a player
 * I want to launch my robot in the online robot world
 * So that I can break the record for the most robot kills
 */
class LaunchRobotTests {
    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();

    @BeforeEach
    void connectToServer() {
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    void disconnectFromServer() {
        serverClient.disconnect();
    }

    @Test
    void launchAnotherRobot() {
        // Given a world of size 2x2
        assertTrue(serverClient.isConnected());

        // and robot "HAL" has already been launched into the world
        String Request1 = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"shooter\",\"0\",\"0\"]" +
                "}";

        JsonNode Response = serverClient.sendRequest(Request1);
        System.out.println(Response);
        assertNotNull(Response.get("result"));
        assertEquals("OK", Response.get("result").asText());

        // When I launch robot "R2D2" into the world
        String Request2 = "{" +
                "\"robot\": \"R2D2\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"shooter\"]" +
                "}";
        JsonNode Response2 = serverClient.sendRequest(Request2);
        System.out.println(Response2);
        assertNotNull(Response2.get("result"));
        assertEquals("OK", Response2.get("result").asText());

        // Then the launch should be successful
        // and a randomly allocated position of R2D2 should be returned.
        assertNotNull(Response2.get("data"));
        JsonNode Position = Response2.get("data").get("position");
        assertNotNull(Position);
        assertFalse(Position.get(0).asInt() >= 0 && Position.get(0).asInt() < 2);
        assertFalse(Position.get(1).asInt() >= 0 && Position.get(1).asInt() < 2);
    }


    @Test
    void worldWithoutObstaclesFull() {
//        Given a world of size 2x2
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
        System.out.println(response);
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());

        // And the message "No more space in this world"assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        assertTrue(response.get("data").get("message").asText().contains("No more space in this world"));
    }

    @Test
    void robotsIntoWorldWithObstacle() {
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


