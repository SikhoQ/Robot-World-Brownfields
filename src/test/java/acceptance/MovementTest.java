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
class MovementTest {
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
    void validForwardMovementShouldSucceed() {
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
        assertNotNull(responseForward.get("data"));
        assertNotNull(responseForward.get("data").get("position"));
        assertEquals(0, responseForward.get("data").get("position").get(0).asInt());
        assertEquals(0, responseForward.get("data").get("position").get(1).asInt());
        assertEquals("At the NORTH edge", responseForward.get("data").get("message").asText());

    }
}