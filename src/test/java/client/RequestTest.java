package client;

import client.request.Request;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Request class.
 */
class RequestTest {

    /**
     * Tests the getCommand method to ensure it returns the command correctly.
     */
    @Test
    void getRequestCommand_returnCommand() {
        Request request = new Request("robot1", "launch", new String[]{"arg1", "arg2"});
        assertEquals("launch", request.getCommand());
    }

    /**
     * Tests the getRobot method to ensure it returns the robot name correctly.
     */
    @Test
    void getRequestRobot_returnRobotName() {
        Request request = new Request("robot1", "launch", new String[]{"arg1", "arg2"});
        assertEquals("robot1", request.getRobot());
    }

    /**
     * Tests the getArguments method to ensure it returns the arguments correctly.
     */
    @Test
    void getRequestArguments_returnArguments() {
        Request request = new Request("robot1", "launch", new String[]{"arg1", "arg2"});
        assertArrayEquals(new String[]{"arg1", "arg2"}, request.getArguments());
    }
}
