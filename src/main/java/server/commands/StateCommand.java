package server.commands;

import java.util.HashMap;

import server.ClientHandler;
import server.response.Response;
import server.response.StandardResponse;

public class StateCommand extends Command {
    public StateCommand() {
        super("state");
    }

    @Override
    public Response execute(ClientHandler target) {
        return new StandardResponse(new HashMap<>(){}, target.getRobot().getState());
    }
}
