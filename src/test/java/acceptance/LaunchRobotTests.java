package acceptance;

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
    void validLaunchShouldSucceed() {
        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
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

    @Test
    void invalidLaunchShouldFail() {
        // Given that I am connected to a running Robot Worlds server
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

    @Test
    void noMoreSpaceInWorld(){
        // Given that I am connected to a Robot Worlds server
        // And the world is of size 1x1
        assertTrue(serverClient.isConnected());

        //        And every coordinate has an object
        String request1 = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response1 = serverClient.sendRequest(request1);

        assertNotNull(response1.get("result"));
        assertEquals("OK", response1.get("result").asText());
        assertNotNull(response1.get("data"));
        assertNotNull(response1.get("data").get("position"));
        assertEquals(0, response1.get("data").get("position").get(0).asInt());
        assertEquals(0, response1.get("data").get("position").get(1).asInt());

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

    @Test
    void nameAlreadyExists(){
        //        Given that I am connected to a Robot Worlds server
        //        And the world is of size 1x1
        assertTrue(serverClient.isConnected());

        //        And there is already a robot with the same name I chose\there is already a robot named "Hal" in the world
        String request1 = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response1 = serverClient.sendRequest(request1);

        assertNotNull(response1.get("result"));
        assertEquals("OK", response1.get("result").asText());

        //        When I send a valid launch request to the server with the same robot name "Hal"
        String request2 = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode response2 = serverClient.sendRequest(request2);

        //        Then I should get an "ERROR" response
        assertNotNull(response2.get("result"));
        assertEquals("ERROR", response2.get("result").asText());

        //        And the message "Too many of you in this world"
        assertNotNull(response2.get("data"));
        assertEquals("Too many of you in this world", response2.get("data").get("message").asText());
    }
}