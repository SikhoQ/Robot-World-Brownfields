package server;

import org.junit.jupiter.api.Test;

import server.commands.*;


import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void getName_constructorWithName_returnsName() {
        Command command = new ConnectCommand();
        assertEquals("connect", command.getName());
    }

    // @Test
    // void getArgument_constructorWithArgument_returnsArgument() {
    //     Command command = new LaunchCommand("mzee", "argument");
    //     assertEquals("argument", command.getArgument());
    // }

    @Test
    void create_validConnectCommandString_returnsConnectCommandObject() {
        String connectCommandString = "{\"command\":\"connect\",\"robot\":\"robot1\",\"arguments\":null}";
        Command command = Command.create(connectCommandString);
        assertTrue(command instanceof ConnectCommand);
    }

    // @Test
    // void create_validLaunchCommandString_returnsLaunchCommandObject() {
    //     String launchCommandString = "{\"command\":\"launch\",\"robot\":\"robot1\",\"arguments\":\"arg1\"}";
    //     Command command = Command.create(launchCommandString);
    //     assertTrue(command instanceof LaunchCommand);
    // }

    @Test
    void create_validQuitCommandString_returnsQuitCommandObject() {
        String quitCommandString = "{\"command\":\"quit\",\"robot\":\"robot1\",\"arguments\":null}";
        Command command = Command.create(quitCommandString);
        assertTrue(command instanceof QuitCommand);
    }

    // Add more tests for other command types...

    // @Test
    // void execute_callsClientHandlerExecuteMethod() {
    //     ClientHandler clientHandler = mock(ClientHandler.class);
    //     Command command = new ConnectCommand();
    //     command.execute(clientHandler);
    //     ((Object) verify(clientHandler, times(1))).executeCommand(clientHandler);
    // }

    // private Object verify(ClientHandler clientHandler, Object times) {
    //     return null;
    // }

    // private Object times(int i) {
    //     return null;
    // }

    // private ClientHandler mock(Class<ClientHandler> class1) {
    //     return null;
    // }

    @Test
    void toString_returnsExpectedString() {
        Command command = new ConnectCommand();
        assertEquals("connect ", command.toString());
    }

}
