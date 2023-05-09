package client.commands;

import client.request.Request;
import client.robots.Robot;

public class FireCommand extends Command {

    public FireCommand() {
        super("fire");
    }

    @Override
    public Request execute(Robot target) {
        return new Request(target.getName(), "fire", new String[]{});
    }  
}
