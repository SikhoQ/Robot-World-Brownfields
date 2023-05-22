package server.commands;

import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import server.ClientHandler;
import server.configuration.ConfigurationManager;
import server.json.JsonHandler;
import server.response.ErrorResponse;
import server.response.StandardResponse;
import server.world.Robot;
import server.world.World;
import server.response.Response;

public class LaunchCommand extends Command {

    private Robot robot;
    private String robotName;
    private String kind;
    private int shields;
    private int shots;

    public LaunchCommand(String robotName, JsonNode args) {
        super("launch");
        // Extract each element in the args array:
        this.robotName = robotName;
        this.kind = args.get(0).asText();
        int maxSheilds = new ConfigurationManager().getMaxSheilds();
        int robotSheilds = args.get(1).asInt();
        this.shields = robotSheilds > maxSheilds? maxSheilds : robotSheilds;
        this.shots = args.get(2).asInt();
    }

    @Override
    public Response execute(ClientHandler clientHandler) {

        int maxRobots = World.getWorldConfiguration().getMaxRobots();
        if (clientHandler.getWorld().getRobots().size() >= maxRobots) {
            return new ErrorResponse("No more space in this world");
        }

        // create robot.
        this.robot = new Robot(robotName, kind, shields, shots, clientHandler);

        // only add robot if it is not already in world.
        if (!clientHandler.getWorld().robotInWorld(robot)) {
            clientHandler.getWorld().addRobotToWorld(robot);
            // store robot into robot variable in clientHandler. this way each instance of ClientHandler is connected to a single instance of robot.
            clientHandler.setRobot(robot);

            // tell other robots in server about this robot
            for (ClientHandler cH : ClientHandler.clientHanders) {
                if (cH.getRobot() != null && cH != clientHandler) { // has launched robot into world.
                    Response res = new StandardResponse(new HashMap<>(){{
                        put("message", "new robot launched into world");
                        put("robotName", robot.getName());
                        put("robotKind", robot.getKind());
                        put("robotState", robot.getState());
                    }}, null);
                    cH.sendToClient(JsonHandler.serializeResponse(res));
                }
            }
            
            return new StandardResponse(clientHandler.getRobot().getData(), clientHandler.getRobot().getState());
        }
        else {
            return new ErrorResponse("Too many of you in this world");
        }
    }
}
