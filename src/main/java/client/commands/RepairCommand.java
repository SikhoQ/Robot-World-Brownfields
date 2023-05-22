package client.commands;

import client.request.Request;
import client.robots.Robot;

public class RepairCommand extends Command{

    public RepairCommand() {
        super("repair");
    }

    @Override
    public Request execute(Robot target) {
        return new Request(target.getName(), "repair", new String[]{});
    } 
}
