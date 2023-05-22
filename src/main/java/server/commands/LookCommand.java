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
        // test, get it from config file
        int visibility = 100;
        int edge = 200;

        List<Object> objects = new ArrayList<>();

        // NORTH
        // loop through obstacles
        for (Position obstaclePos : obstacles) {
            if (inBoundary(robot.getPosition(), obstaclePos, "NORTH")
                && robot.getDistance(obstaclePos) <= visibility) {
                Map<String, Object> object = new HashMap<>(){{
                    put("direction", "NORTH");
                    put("type", "OBSTACLE");
                    put("distance", robot.getDistance(obstaclePos));
                }};
                objects.add(object);
            }
        }

        // loop through robots
        for (Robot otherRobot : robots) {
            if (inBoundary(robot.getPosition(), otherRobot.getPosition(), "NORTH")
                && robot.getDistance(otherRobot) <= visibility) {
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
                put("distance", robot.getDistance(new Position(0, edge)));
            }};
            objects.add(object);
        }

        // EAST
        // loop through obstacles
        for (Position obstaclePos : obstacles) {
            if (inBoundary(robot.getPosition(), obstaclePos, "EAST")
                    && robot.getDistance(obstaclePos) <= visibility) {
                Map<String, Object> object = new HashMap<>(){{
                    put("direction", "EAST");
                    put("type", "OBSTACLE");
                    put("distance", robot.getDistance(obstaclePos));
                }};
                objects.add(object);
            }
        }

        // loop through robots
        for (Robot otherRobot : robots) {
            if (inBoundary(robot.getPosition(), otherRobot.getPosition(), "EAST")
                    && robot.getDistance(otherRobot) <= visibility) {
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
                put("distance", robot.getDistance(new Position(edge, 0)));
            }};
            objects.add(object);
        }

        // SOUTH
        // loop through obstacles
        for (Position obstaclePos : obstacles) {
            if (inBoundary(robot.getPosition(), obstaclePos, "SOUTH")
                    && robot.getDistance(obstaclePos) <= visibility) {
                Map<String, Object> object = new HashMap<>(){{
                    put("direction", "SOUTH");
                    put("type", "OBSTACLE");
                    put("distance", robot.getDistance(obstaclePos));
                }};
                objects.add(object);
            }
        }

        // loop through robots
        for (Robot otherRobot : robots) {
            if (inBoundary(robot.getPosition(), otherRobot.getPosition(), "SOUTH")
                    && robot.getDistance(otherRobot) <= visibility) {
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
                put("distance", robot.getDistance(new Position(0, -edge)));
            }};
            objects.add(object);
        }


        // WEST
        // loop through obstacles
        for (Position obstaclePos : obstacles) {
            if (inBoundary(robot.getPosition(), obstaclePos, "WEST")
                    && robot.getDistance(obstaclePos) <= visibility) {
                Map<String, Object> object = new HashMap<>(){{
                    put("direction", "WEST");
                    put("type", "OBSTACLE");
                    put("distance", robot.getDistance(obstaclePos));
                }};
                objects.add(object);
            }
        }

        // loop through robots
        for (Robot otherRobot : robots) {
            if (inBoundary(robot.getPosition(), otherRobot.getPosition(), "WEST")
                    && robot.getDistance(otherRobot) <= visibility) {
                Map<String, Object> object = new HashMap<>(){{
                    put("direction", "WEST");
                    put("type", "ROBOT");
                    put("distance", robot.getDistance(otherRobot));
                }};
                objects.add(object);
            }
        }

        // Check left edge
        if ((robot.getPosition().getX() - visibility) > -edge) {
            Map<String, Object> object = new HashMap<>(){{
                put("direction", "WEST");
                put("type", "EDGE");
                put("distance", robot.getDistance(new Position(-edge, 0)));
            }};
            objects.add(object);
        }

        HashMap data = new HashMap<>() {{ put("objects", objects); }};

//        System.out.println(data);
        return  new StandardResponse(data, robot.getState());
    }


    public boolean inBoundary(Position robotPos, Position objectPos, String direction) {
        switch (direction) {
            case "NORTH":
            case "SOUTH":
                if (robotPos.getX() <= objectPos.getX() && robotPos.getX() < objectPos.getX()) {
                    return true;
                }
            case "EAST":
            case "WEST":
                if (robotPos.getY() <= objectPos.getY() && robotPos.getY() < objectPos.getY()) {
                    return true;
                }
        }
        return false;
    }


}
