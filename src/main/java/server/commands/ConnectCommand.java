package server.commands;

import java.util.HashMap;

import server.ClientHandler;
import server.response.Response;
import server.response.StandardResponse;
import server.world.SquareObstacle;


/**
 * Represents a "connect" command in the game.
 * Inherits from the Command class and provides the implementation for the connect command.
 * When executed, sets the current command in the client handler and returns a response indicating successful connection.
 */
public class ConnectCommand extends Command {
    public ConnectCommand() {
        super("connect");
    }

    /**
     * Executes the connect command for the given client handler.
     * Sets the current command in the client handler to "connect".
     * Returns a response indicating successful connection, including a message and obstacle information.
     * @param clientHandler the client handler executing the command
     * @return a Response object representing the result of executing the connect command
     */

    @Override
    public Response execute(ClientHandler clientHandler) {
        clientHandler.setCurrentCommand(getName());
        return new StandardResponse(new HashMap<>(){{
            put("message", "connected"); 
            put("obstacles", SquareObstacle.obstacles); 
        }}, new HashMap<>(){});
    }
}
