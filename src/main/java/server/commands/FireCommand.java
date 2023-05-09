package server.commands;

import java.util.HashMap;


import server.ClientHandler;
import server.response.Response;
import server.response.StandardResponse;
import server.world.Robot;
import server.world.World;

public class FireCommand extends Command{

    public FireCommand() {
        super("fire");
    }

    @Override
    public Response execute(ClientHandler clientHandler) {

        Robot robot = clientHandler.getRobot();
        World world = clientHandler.getWorld();
        robot.decreaseShots();

        Object[] result = world.updatePosition(robot, robot.getBulletDistance(), true);
        if (result.length == 2) { // blocked by another robot.
            Robot robotHit = (Robot) result[1];
            int distance = robot.getDistance(robotHit);
            robotHit.decreaseSheilds();

            HashMap<String, Object> data = new HashMap<>(){{
                put("message", "Hit");
                put("distance", distance);
                put("robot", robotHit.getName());
                put("state", robotHit.getState());
            }};

            return new StandardResponse(data, robot.getState());
        }
        else { // either bullet is blocked an obstacle or not blocked at all.
            return new StandardResponse(new HashMap<>() {{ put("message", "Miss"); }}, robot.getState());
        }
    }
    
}
