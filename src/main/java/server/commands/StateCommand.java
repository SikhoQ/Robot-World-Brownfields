package server.commands;

import server.ClientHandler;
import server.response.Response;
import server.response.StateResponse;

public class StateCommand extends Command {
    public StateCommand() {
        super("state");
    }

    @Override
    public Response execute(ClientHandler target) {
        // target.setCurrentCommand(getName());
        // return new BasicResponse("Connected to server");
        return new StateResponse(target.getRobot().getState());
    }
}
