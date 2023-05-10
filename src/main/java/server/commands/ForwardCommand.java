package server.commands;

import java.util.HashMap;

import server.ClientHandler;
import server.response.Response;
import server.response.StandardResponse;
import server.world.UpdateResponse;
import server.world.World;

public class ForwardCommand extends Command {

    public ForwardCommand(String argument) {
        super("forward", argument);
    }

    @Override
    public Response execute(ClientHandler target) {
        World world = target.getWorld();
        int nrSteps = Integer.parseInt(getArgument());
        String message;
        
        target.getRobot().setStatus("NORMAL");

        if (world.updatePosition(target.getRobot() ,nrSteps, false)[0] == UpdateResponse.SUCCESS) {
            message = "Done";
        }
        else if (world.updatePosition(target.getRobot() ,nrSteps, false)[0] == UpdateResponse.FAILED_OBSTRUCTED) {
            message = "Obstructed";
        }
        else {
            message = "Outside safezone";
        }
        return new StandardResponse(new HashMap<>() {{ put("message", message); }}, target.getRobot().getState());
    }
}
