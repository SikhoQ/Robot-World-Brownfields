package server.commands;


import java.util.HashMap;

import server.ClientHandler;
import server.response.Response;
import server.response.StandardResponse;
import server.world.Robot;

public class ReloadCommand extends Command{

    public ReloadCommand() {
        super("reload");
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        Robot robot= clientHandler.getRobot();
        robot.setShots(robot.getMaxShots());
        robot.setStatus(this.getName().toUpperCase());
        return new StandardResponse(new HashMap<>() {{ put("message",  "Done"); }}, robot.getState());

    }
    
}
