package server.commands;

import client.Client;
import com.fasterxml.jackson.databind.JsonNode;
import server.ClientHandler;
import server.json.JsonHandler;
import server.response.ErrorResponse;
import server.response.LaunchResponse;
import server.response.Response;
import server.world.Robot;

public class LaunchCommand extends Command {

    private Robot robot;

    public LaunchCommand(String robotName, JsonNode args) {
        super("launch");
        System.out.println(args);
        // Extract each element in the args array:
        String kind = args.get(0).asText();
//        System.out.println("kind: " + kind);
        int shield = args.get(1).asInt();
//        System.out.println("shield: " + shield);
        int shots = args.get(2).asInt();
//        System.out.println("shots: " + shots);

        // create a new robot instance and store it inside the robot variable.
        this.robot = new Robot(robotName, kind, shield, shots);
    }

    private boolean robotInWorld(Robot robot) {
        for (Robot robotInWorld : ClientHandler.robots) {
            if (robot.getName().equalsIgnoreCase(robotInWorld.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Response execute(ClientHandler target) {

        if (ClientHandler.robots.size() > 2) {
            return new ErrorResponse("No more space in this world");
        }

        // only add robot if it is not already in world.
        if (!robotInWorld(robot)) {
            // might need to move robots list to world
            ClientHandler.addRobotToWorld(robot);
            // store robot into robot variable in clientHandler. this way each instance of ClientHandler is connected to a single instance of robot.
            target.setRobot(robot);
            return new LaunchResponse(target.getRobot().getData(), target.getRobot().getState());
        }
        else {
            return new ErrorResponse("Too many of you in this world");
        }
    }
}
