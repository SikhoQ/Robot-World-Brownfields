package server.commands;

import server.ClientHandler;
import server.response.Response;
import server.response.StandardResponse;
import server.world.Robot;
import server.world.World;
import server.world.util.Position;
import server.world.util.UpdateResponse;

import java.util.HashMap;

/**
 * Represents a command to move the robot backwards in the world.
 * Extends the Command class and overrides the execute method to execute the back command.
 */
public class BackCommand extends Command {

    /** Constructs a BackCommand object with the specified argument.
     */
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

        if (world.updatePosition(robot ,nrSteps)[0] == UpdateResponse.FAILED_OUTSIDE_WORLD) {
            message = "At the SOUTH edge";
        }
        else {
            Position robotPosition = robot.getPosition();
            message = String.format("At position [%d, %d]", robotPosition.getX(), robotPosition.getY());
        }

        return new StandardResponse(new HashMap<>() {{ put("message", message); }}, robot.getState());
    }
}
