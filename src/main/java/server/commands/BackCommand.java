package server.commands;

import java.util.HashMap;

import server.ClientHandler;
import server.json.JsonHandler;
import server.response.Response;
import server.response.StandardResponse;
import server.world.Robot;
import server.world.World;
import server.world.util.UpdateResponse;

public class BackCommand extends Command {

    public BackCommand(String argument) {
        super("back", argument);
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        World world = clientHandler.getWorld();
        Robot robot = clientHandler.getRobot();
        int nrSteps = Integer.parseInt(getArgument());
        String message;
        
        robot.setStatus("NORMAL");

        if (world.updatePosition(robot, -nrSteps)[0] == UpdateResponse.SUCCESS) {
            message = "Done";
        }
        else if (world.updatePosition(robot, -nrSteps)[0] == UpdateResponse.FAILED_OBSTRUCTED) {
            message = "Obstructed";
        }
        else {
            message = "Outside safezone";
        }

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

        
        return new StandardResponse(new HashMap<>() {{ put("message", message); }}, robot.getState());
    }
}
