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
 * Represents a command to move the robot forward a specified number of steps.
 * Extends the Command class.
 */
public class ForwardCommand extends Command {

    public ForwardCommand(String argument) {
        
        /**
     * Constructs a ForwardCommand object with the specified argument.
     *
     * @param argument The argument representing the number of steps to move the robot forward.
     */
        super("forward", argument);
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        World world = clientHandler.getWorld();
        Robot robot = clientHandler.getRobot();
        int nrSteps = Integer.parseInt(getArgument());
        String message;
        
        robot.setStatus("NORMAL");

        if (world.updatePosition(robot ,nrSteps)[0] == UpdateResponse.FAILED_OUTSIDE_WORLD) {
            message = "At the NORTH edge";
        }
        else {
            System.out.println("SUCCESS");
            Position robotPosition = robot.getPosition();
            message = String.format("At position [%d, %d]", robotPosition.getX(), robotPosition.getY());
        }

        return new StandardResponse(new HashMap<>() {{ put("message", message); }}, robot.getState());
    }
}
