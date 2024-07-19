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
                "  \"robot\": \"HAL\"," +
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
}


