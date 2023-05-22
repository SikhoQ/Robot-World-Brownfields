package server.commands;

import java.util.HashMap;

import server.response.*;
import server.world.Robot;
import server.ClientHandler;

public class RepairCommand extends Command{

    public RepairCommand() {
        super("repair");
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        Robot robot = clientHandler.getRobot();
        robot.setShiels(robot.getMaxSheilds());
        robot.setStatus(this.getName().toUpperCase());
        return new StandardResponse(new HashMap<>() {{ put("message", "Done"); }}, robot.getState());
    }
    
}
