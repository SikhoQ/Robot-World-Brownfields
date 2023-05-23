package client;

import client.commands.*;
import client.robots.Robot;
import client.request.Request;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void create_quitCommand_returnQuitCommandObject() {
        Command command = Command.create("quit");
        assertTrue(command instanceof QuitCommand);
    }

    @Test
    void create_launchCommandWithArgs_returnLaunchCommandObject() {
        Command command = Command.create("launch drone1 drone2");
        assertTrue(command instanceof LaunchCommand);
        LaunchCommand launchCommand = (LaunchCommand) command;
        assertEquals("drone1", launchCommand.getKind());
        assertEquals("launch", launchCommand.getName());
    }

    @Test
    void create_stateCommand_returnStateCommandObject() {
        Command command = Command.create("state");
        assertTrue(command instanceof StateCommand);
    }

    // Add more tests for other commands...

    @Test
    void create_unsupportedCommand_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Command.create("unsupported");
        });
    }

    @Test
    void execute_executeCommandOnRobot_returnRequestObject() {
        Command command = new StateCommand();
        Robot robot = new Robot("hal");
        Request request = command.execute(robot);
        assertNotNull(request);
    }

    // Add more tests for execute() method...

    @Test
    void getName_commandWithName_returnName() {
        Command command = new QuitCommand();
        assertEquals("quit", command.getName());
    }

    // Add more tests for other methods...

}
