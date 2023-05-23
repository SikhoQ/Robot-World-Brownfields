package server.commands;

import java.util.HashMap;

import server.ClientHandler;
import server.json.JsonHandler;
import server.response.*;
import server.response.Response;
import server.world.Robot;

/**
     * Constructs a LaunchCommand object with the specified robot name and arguments.
     *
     * @param robotName The name of the robot to launch.
     * @param args      The arguments containing the robot kind, shields, and shots.
     */
public class QuitCommand extends Command{

    public QuitCommand() {
        super("quit");
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        clientHandler.setCurrentCommand(getName());
        ClientHandler.clientHanders.remove(clientHandler);

        Robot robot = clientHandler.getRobot();
        clientHandler.getWorld().getRobots().remove(robot);

        // broadcast to other clients
        for (ClientHandler cH : ClientHandler.clientHanders) {
            if (cH.getRobot() != null && cH != clientHandler) { // has launched robot into world.
                Response res = new StandardResponse(new HashMap<>(){{
                    put("message", "remove enemy");
                    put("robotName", robot.getName());
                }}, null);
                cH.sendToClient(JsonHandler.serializeResponse(res));
            }
        }

        return new BasicResponse("Successfully disconnected from server.");
    }
    
}
