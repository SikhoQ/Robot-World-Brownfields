package server.commands;

import java.util.HashMap;

import server.json.JsonHandler;
import server.ClientHandler;
import server.response.ErrorResponse;
import server.response.Response;
import server.response.StandardResponse;
import server.world.Robot;
import server.world.World;

public class FireCommand extends Command{
    private ClientHandler clientHandler;

    public FireCommand() {
        super("fire");
    }

    private Response fire(Robot robot, World world) {
        robot.decreaseShots();

        Object[] result = world.fireGun(robot, robot.getBulletDistance());
        if (result.length == 2) { // blocked by another robot.
            Robot robotHit = (Robot) result[1]; // get the robot shot.
            ClientHandler robotHiClientHandler = robotHit.getClientHandler();
            int distance = robot.getDistance(robotHit);

            // decrease shields & tell the robot shot that they've been shot:
            robotHit.decreaseSheilds(); 
            Response robotHitResponse = new StandardResponse(new HashMap<>(){{ 
                put("message", "You've been shot."); }}, robotHit.getState());
            String jsonStr = JsonHandler.serializeResponse(robotHitResponse);
            robotHiClientHandler.sendToClient(jsonStr);


            // braodcast to everyone else (that someone has been shot and their sheilds decreased).
            for (ClientHandler cH : ClientHandler.clientHanders) {
                if (cH.getRobot() != null && cH != robotHit.getClientHandler()) {
                    Response res = new StandardResponse(new HashMap<>(){{
                        put("message", "enemy state changed");
                        put("robotName", robotHit.getName());
                        put("robotState", robotHit.getState());
                    }}, null);
                    cH.sendToClient(JsonHandler.serializeResponse(res));
                }
            }


            // braodcast to everyone else (that someone fired their gun and hit).
            for (ClientHandler cH : ClientHandler.clientHanders) {
                if (cH.getRobot() != null && cH != this.clientHandler) {
                    Response res = new StandardResponse(new HashMap<>(){{
                        put("message", "an enemy fired gun");
                        put("robotName", robot.getName());
                        // put("robotState", robot.getState());
                        put("distance", distance); 
                    }}, null);
                    cH.sendToClient(JsonHandler.serializeResponse(res));
                }
            }


            HashMap<String, Object> data = new HashMap<>(){{
                put("message", "Hit");
                put("distance", distance);
                put("robot", robotHit.getName());
                put("state", robotHit.getState());
            }};
            return new StandardResponse(data, robot.getState());
        }
        else { 
            // either bullet is blocked an obstacle or not blocked at all.

            // braodcast to everyone else (that someone fired their gun and missed).
            for (ClientHandler cH : ClientHandler.clientHanders) {
                if (cH.getRobot() != null && cH != this.clientHandler) {
                    Response res = new StandardResponse(new HashMap<>(){{
                        put("message", "an enemy fired gun");
                        put("robotName", robot.getName());
                        put("distance", robot.getBulletDistance()); 
                        // put("robotState", robot.getState());
                    }}, null);
                    cH.sendToClient(JsonHandler.serializeResponse(res));
                }
            }
            return new StandardResponse(new HashMap<>() {{ put("message", "Miss"); }}, robot.getState());
        }
    }
    
    @Override
    public Response execute(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;

        Robot robot = clientHandler.getRobot();
        World world = clientHandler.getWorld();
        robot.setStatus("NORMAL");

        if (robot.getShots() > 0) {
            return fire(robot, world);
        }

        return new ErrorResponse("No bullets in gun");
    }
    
}
