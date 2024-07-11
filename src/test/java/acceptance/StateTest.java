package acceptance;

import client.RobotWorldClient;
import client.RobotWorldJsonClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StateTest {
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
    void robotExistsInWorld(){
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
    @Test
    @DisplayName("The robot is not in the world.")
    void errorForInvalidRobot() {

        // Given that I just have successfully connected to the Robot Worlds server,
        // but I have not yet launched my robot into the world.
        assertTrue(serverClient.isConnected());

        // When I try to get the state of the robot by sending a request to the server using the state command.

        String stateRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"state\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode stateResponse = serverClient.sendRequest(stateRequest);

        // Then I should get an "error" response from the server,
        // telling me that my robot hasn't been launched yet.
        assertNotNull(stateResponse.get("result"));
        assertEquals("ERROR", stateResponse.get("result").asText());
    }

}
