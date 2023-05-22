package server.commands;

import java.util.HashMap;

import server.ClientHandler;
import server.response.Response;
import server.response.StandardResponse;
import server.world.SquareObstacle;

public class ConnectCommand extends Command {
    public ConnectCommand() {
        super("connect");
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        clientHandler.setCurrentCommand(getName());
        return new StandardResponse(new HashMap<>(){{
            put("message", "connected"); 
            put("obstacles", SquareObstacle.obstacles); 
        }}, new HashMap<>(){});
    }
}
