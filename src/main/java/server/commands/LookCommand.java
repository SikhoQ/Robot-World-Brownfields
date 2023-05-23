package server.commands;

import java.util.*;

import server.response.StandardResponse;
import server.world.*;
import server.world.util.Position;
import server.ClientHandler;
import server.response.Response;

public class LookCommand extends Command {

    public LookCommand() {
        super("look");
    }

    @Override
    public Response execute(ClientHandler clientHandler) {

        server.world.Robot robot = clientHandler.getRobot();
        List<Position> obstacles = SquareObstacle.obstacles;
        List<Robot>  robots =  clientHandler.getWorld().getRobots();
        int visibility = Integer.valueOf(World.getWorldConfiguration().getVisibility());
        int edge = World.getWorldConfiguration().getXConstraint();

        List<Object> objects = new ArrayList<>();

        // NORTH
        // loop through obstacles
        for (Position obstaclePos : obstacles) {
            if (inBoundary(robot.getPosition(), obstaclePos, "NORTH")
                && robot.getDistanceY(obstaclePos) <= visibility
                && robot.getPosition().getY() < obstaclePos.getY()) {
                Map<String, Object> object = new HashMap<>(){{
                    put("direction", "NORTH");
                    put("type", "OBSTACLE");
                    put("distance", robot.getDistanceY(obstaclePos));
                }};
                objects.add(object);
            }
        }

        // loop through robots
        for (Robot otherRobot : robots) {
            if (inBoundary(robot.getPosition(), otherRobot.getPosition(), "NORTH")
                && robot.getDistance(otherRobot) <= visibility
                && robot.getPosition().getY() < otherRobot.getPosition().getY()
                && otherRobot != robot) {
                Map<String, Object> object = new HashMap<>(){{
                    put("direction", "NORTH");
                    put("type", "ROBOT");
                    put("distance", robot.getDistance(otherRobot));
                }};
                objects.add(object);
            }
        }

        // Check top edge
        if ((robot.getPosition().getY() + visibility) > edge) {
            Map<String, Object> object = new HashMap<>(){{
                put("direction", "NORTH");
                put("type", "EDGE");
                put("distance", edge - robot.getPosition().getY());
            }};
            objects.add(object);
        }

        // EAST
        // loop through obstacles
        for (Position obstaclePos : obstacles) {
            if (inBoundary(robot.getPosition(), obstaclePos, "EAST")
                    && robot.getDistanceX(obstaclePos) <= visibility
                    && robot.getPosition().getX() < obstaclePos.getX()) {
                Map<String, Object> object = new HashMap<>(){{
                    put("direction", "EAST");
                    put("type", "OBSTACLE");
                    put("distance", robot.getDistanceX(obstaclePos));
                }};
                objects.add(object);
            }
        }

        // loop through robots
        for (Robot otherRobot : robots) {
            if (inBoundary(robot.getPosition(), otherRobot.getPosition(), "EAST")
                    && robot.getDistance(otherRobot) <= visibility
                    && robot.getPosition().getX() < otherRobot.getPosition().getX()
                    && otherRobot != robot) {
                Map<String, Object> object = new HashMap<>(){{
                    put("direction", "EAST");
                    put("type", "ROBOT");
                    put("distance", robot.getDistance(otherRobot));
                }};
                objects.add(object);
            }
        }

        // Check right edge
        if ((robot.getPosition().getX() + visibility) > edge) {
            Map<String, Object> object = new HashMap<>(){{
                put("direction", "EAST");
                put("type", "EDGE");
                put("distance", edge - robot.getPosition().getX());
            }};
            objects.add(object);
        }

        // SOUTH
        // loop through obstacles
        for (Position obstaclePos : obstacles) {
            if (inBoundary(robot.getPosition(), obstaclePos, "SOUTH")
                    && robot.getDistanceY(obstaclePos) <= visibility
                    && robot.getPosition().getY() > obstaclePos.getY()) {
                Map<String, Object> object = new HashMap<>(){{
                    put("direction", "SOUTH");
                    put("type", "OBSTACLE");
                    put("distance", robot.getDistanceY(obstaclePos));
                }};
                objects.add(object);
            }
        }

        // loop through robots
        for (Robot otherRobot : robots) {
            if (inBoundary(robot.getPosition(), otherRobot.getPosition(), "SOUTH")
                    && robot.getDistance(otherRobot) <= visibility
                    && robot.getPosition().getY() > otherRobot.getPosition().getY()
                    && otherRobot != robot) {
                Map<String, Object> object = new HashMap<>(){{
                    put("direction", "SOUTH");
                    put("type", "ROBOT");
                    put("distance", robot.getDistance(otherRobot));
                }};
                objects.add(object);
            }
        }

        // Check bottom edge
        if ((robot.getPosition().getY() - visibility) < -edge) {
            Map<String, Object> object = new HashMap<>(){{
                put("direction", "SOUTH");
                put("type", "EDGE");
                put("distance", Math.abs(robot.getPosition().getY() + edge));
            }};
            objects.add(object);
        }


        // WEST
        // loop through obstacles
        for (Position obstaclePos : obstacles) {
            if (inBoundary(robot.getPosition(), obstaclePos, "WEST")
                    && robot.getDistanceX(obstaclePos) <= visibility
                    && robot.getPosition().getX() > obstaclePos.getX()) {
                Map<String, Object> object = new HashMap<>(){{
                    put("direction", "WEST");
                    put("type", "OBSTACLE");
                    put("distance", robot.getDistanceX(obstaclePos));
                }};
                objects.add(object);
            }
        }

        // loop through robots
        for (Robot otherRobot : robots) {
            if (inBoundary(robot.getPosition(), otherRobot.getPosition(), "WEST")
                    && robot.getDistance(otherRobot) <= visibility 
                    && robot.getPosition().getX() > otherRobot.getPosition().getX()
                    && otherRobot != robot) {
                Map<String, Object> object = new HashMap<>(){{
                    put("direction", "WEST");
                    put("type", "ROBOT");
                    put("distance", robot.getDistance(otherRobot));
                }};
                objects.add(object);
            }
        }

        // Check left edge
        if ((robot.getPosition().getX() - visibility) < -edge) {
            Map<String, Object> object = new HashMap<>(){{
                put("direction", "WEST");
                put("type", "EDGE");
                put("distance", Math.abs(robot.getPosition().getX() + edge));
            }};
            objects.add(object);
        }

        HashMap<String,Object> data = new HashMap<>() {{ put("objects", objects); }};

        return  new StandardResponse(data, robot.getState());
    }


    public boolean inBoundary(Position robotPos, Position objectPos, String direction) {
        int size = World.getWorldConfiguration().getTileSize();
        int robotX = robotPos.getX() - size;
        int robbotY = robotPos.getY() - size;

        switch (direction) {
            case "NORTH":
            case "SOUTH":
                if (objectPos.getX() >= robotX && objectPos.getX() <= robotX + size || 
                    objectPos.getX() + size >= robotX && objectPos.getX() + size <= robotX + size) {
                    return true;
                }
                break;

            case "EAST":
            case "WEST":
                if (objectPos.getY() >= robbotY && objectPos.getY() < robbotY + size || 
                    objectPos.getX() + size >= robbotY && objectPos.getX() + size <= robbotY+ size) {
                    return true;
                } 
                break;
        }
        return false;
    }


}
