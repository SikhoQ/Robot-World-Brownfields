package server.commands;

import java.util.HashMap;

import server.ClientHandler;
import server.response.Response;
import server.response.StandardResponse;
import server.world.World;

public class TurnCommand extends Command{

    public TurnCommand(String argument) {
        super("turn", argument);
    }

    @Override
    public Response execute(ClientHandler target) {
        World world = target.getWorld();
        Boolean turnRight = getArgument().equals("right")? true : false;
        world.updateDirection(target.getRobot(), turnRight);
        target.getRobot().setStatus("NORMAL");
        return new StandardResponse(new HashMap<>() {{ put("message", "Done"); }}, target.getRobot().getState());
    }
    
}
