package server.commands;

import com.fasterxml.jackson.databind.JsonNode;
import server.ClientHandler;
import server.response.ErrorResponse;
import server.response.StandardResponse;
import server.response.Response;
import server.world.Robot;

public class LaunchCommand extends Command {

    private Robot robot;
    private String robotName;
    private String kind;
    private int shield;
    private int shots;

    public LaunchCommand(String robotName, JsonNode args) {
        super("launch");
        // Extract each element in the args array:
        this.robotName = robotName;
        this.kind = args.get(0).asText();
        this.shield = args.get(1).asInt();
        this.shots = args.get(2).asInt();
    }

    @Override
    public Response execute(ClientHandler target) {

        if (target.getWorld().getRobots().size() >= 4) { //add this 4 to configuration file
            return new ErrorResponse("No more space in this world");
        }

        // create robot.
        this.robot = new Robot(robotName, kind, shield, shots);

        // only add robot if it is not already in world.
        if (!target.getWorld().robotInWorld(robot)) {
            // might need to move robots list to world
            target.getWorld().addRobotToWorld(robot);
            // store robot into robot variable in clientHandler. this way each instance of ClientHandler is connected to a single instance of robot.
            target.setRobot(robot);
            return new StandardResponse(target.getRobot().getData(), target.getRobot().getState());
        }
        else {
            return new ErrorResponse("Too many of you in this world");
        }
    }
}
