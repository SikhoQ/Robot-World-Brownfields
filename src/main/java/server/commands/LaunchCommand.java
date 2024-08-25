package server.commands;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import server.ClientHandler;
import server.configuration.ConfigurationManager;
import server.json.JsonHandler;
import server.response.ErrorResponse;
import server.response.StandardResponse;
import server.world.Obstacle;
import server.world.Robot;
import server.world.SquareObstacle;
import server.world.World;
import server.response.Response;
import server.world.util.Position;

public class LaunchCommand extends Command {

    private Robot robot;
    private String robotName;
    private String kind;
    private int shields;
    private int shots;

    /**
     * Constructs a LaunchCommand object with the specified robot name and arguments.
     *
     * @param robotName The name of the robot to launch.
     * @param args      The arguments containing the robot kind, shields, and shots.
     */
    public LaunchCommand(String robotName, JsonNode args) {
        super("launch");
        // Extract each element in the args array:
        this.robotName = robotName;
        this.kind = args.get(0).asText();
        int maxShields = ConfigurationManager.getMaxShields();
        int robotShields = 0;
        this.shields = Math.min(robotShields, maxShields);
        this.shots = 0;
    }

    @Override
    public Response execute(ClientHandler clientHandler) {

        // create robot.
        try {
            this.robot = new Robot(robotName, kind, shields, shots, clientHandler);
            if (this.robot.getPosition() == null) {
                return new ErrorResponse("No more space in this world");
            }
        } catch (IllegalArgumentException e) {
            return new ErrorResponse("No more space in this world");
        }

        // if robot with same name already exists, return corresponding error response
        if (clientHandler.getWorld().robotInWorld(this.robot)) {
            return new ErrorResponse("Too many of you in this world");
        }
        List<Obstacle> worldObstacles = clientHandler.getWorld().getObstacles();
        ArrayList<Robot> worldRobots = clientHandler.getWorld().getRobots();

        // List to hold all objects in world, i.e. obstacles and robots (for now...)
        List<Object> worldObjects = new ArrayList<>(worldObstacles);
        worldObjects.addAll(worldRobots);

        // List of all positions in the world
        ArrayList<Position> worldPositions = clientHandler.getWorld().getWorldPositions();
//        Position thisRobotPosition = clientHandler.getRobot().getPosition();
        for (Object object: worldObjects) {
            Robot robot = clientHandler.getRobot();
            Position thisRobotPosition = null;
            if (robot != null) {
                thisRobotPosition = clientHandler.getRobot().getPosition();
            }

            if (thisRobotPosition != null) {
                // check if any obstacle blocks position, remove position from List if true
                if (object.getClass().getSimpleName().equals("SquareObstacle")) {
                    Position obstaclePosition = new Position(((SquareObstacle) object).getBottomLeftX(), ((SquareObstacle) object).getBottomLeftY());
                    if (obstaclePosition.equals(thisRobotPosition)) {
                        worldPositions.remove(obstaclePosition);
                    }
                }

                // check if any robot blocks position, remove position from List if true
                if (object.getClass().getSimpleName().equals("Robot")) {
                    Position otherRobotPosition = ((Robot) object).getPosition();
                    if (otherRobotPosition.equals(thisRobotPosition)) {
                        worldPositions.remove(otherRobotPosition);
                    }
                }
            }
        }
        if (worldPositions.isEmpty()) {
            return new ErrorResponse("No more space in this world");
        }


        // only add robot if it is not already in world.
        clientHandler.getWorld().addRobotToWorld(robot);
        // store robot into robot variable in clientHandler. this way each instance of ClientHandler is connected to a single instance of robot.
        clientHandler.setRobot(robot);

        return new StandardResponse(clientHandler.getRobot().getData(), clientHandler.getRobot().getState());
    }
}