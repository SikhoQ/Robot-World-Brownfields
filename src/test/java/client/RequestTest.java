package client;

import client.request.Request;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    @Test
    void getRequestCommand_returnCommand() {
        Request request = new Request("robot1", "launch", new String[]{"arg1", "arg2"});
        assertEquals("launch", request.getCommand());
    }

    @Test
    void getRequestRobot_returnRobotName() {
        Request request = new Request("robot1", "launch", new String[]{"arg1", "arg2"});
        assertEquals("robot1", request.getRobot());
    }

    @Test
    void getRequestArguments_returnArguments() {
        Request request = new Request("robot1", "launch", new String[]{"arg1", "arg2"});
        assertArrayEquals(new String[]{"arg1", "arg2"}, request.getArguments());
    }

    // Add more tests...

}
