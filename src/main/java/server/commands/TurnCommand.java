package server.commands;

import java.util.HashMap;

import server.ClientHandler;
import server.json.JsonHandler;
import server.response.Response;
import server.response.StandardResponse;
import server.world.Robot;
import server.world.World;

public class TurnCommand extends Command{

    public TurnCommand(String argument) {
        super("turn", argument);
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        World world = clientHandler.getWorld();
        Robot robot = clientHandler.getRobot();
        Boolean turnRight = getArgument().equals("right")? true : false;
        world.updateDirection(robot, turnRight);
        robot.setStatus("NORMAL");

        // broadcast to other clients
        for (ClientHandler cH : ClientHandler.clientHanders) {
            if (cH.getRobot() != null && cH != clientHandler) { // has launched robot into world.
                Response res = new StandardResponse(new HashMap<>(){{
                    put("message", "enemy state changed");
                    put("robotName", robot.getName());
                    put("robotState", robot.getState());
                }}, null);
                cH.sendToClient(JsonHandler.serializeResponse(res));
            }
        }

        return new StandardResponse(new HashMap<>() {{ put("message", "Done"); }}, robot.getState());
    }
    
}
