package server.commands;

import java.util.HashMap;

import server.ClientHandler;
import server.response.Response;
import server.response.StandardResponse;
import server.world.Robot;

public class StateCommand extends Command {

    /**
     * Constructs a StateCommand object.
     * Calls the superclass constructor to set the command name.
     */
    public StateCommand() {
        super("state");
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        Robot robot = clientHandler.getRobot();
        robot.setStatus("NORMAL");
        return new StandardResponse(new HashMap<>(){}, robot.getState());
    }
}
