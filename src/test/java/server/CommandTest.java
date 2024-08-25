package server;

import client.commands.*;
import client.robots.Robot;
import client.request.Request;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Command class and its subclasses.
 */
class CommandTest {

    /**
     * Tests creating a QuitCommand object using the Command.create method.
     */
    @Test
    void create_quitCommand_returnQuitCommandObject() {
        Command command = Command.create("quit");
        assertTrue(command instanceof QuitCommand);
    }

    /**
     * Tests creating a LaunchCommand object using the Command.create method with arguments.
     */
    @Test
    void create_launchCommandWithArgs_returnLaunchCommandObject() {
        Command command = Command.create("launch drone1 drone2");
        assertTrue(command instanceof LaunchCommand);
        LaunchCommand launchCommand = (LaunchCommand) command;
        assertEquals("drone1", launchCommand.getKind());
        assertEquals("launch", launchCommand.getName());
    }

    /**
     * Tests creating a StateCommand object using the Command.create method.
     */
    @Test
    void create_stateCommand_returnStateCommandObject() {
        Command command = Command.create("state");
        assertTrue(command instanceof StateCommand);
    }

    /**
     * Tests creating an unsupported command to check for IllegalArgumentException.
     */
    @Test
    void create_unsupportedCommand_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Command.create("unsupported");
        });
    }

    /**
     * Tests the execute method of Command to ensure it returns a Request object.
     */
    @Test
    void execute_executeCommandOnRobot_returnRequestObject() {
        Command command = new StateCommand();
        Robot robot = new Robot("hal");
        Request request = command.execute(robot);
        assertNotNull(request);
    }

    /**
     * Tests the getName method to ensure it returns the command name.
     */
    @Test
    void getName_commandWithName_returnName() {
        Command command = new QuitCommand();
        assertEquals("quit", command.getName());
    }
}
