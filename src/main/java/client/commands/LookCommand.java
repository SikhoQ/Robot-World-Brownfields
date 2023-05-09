package client.commands;

import client.request.Request;
import client.robots.Robot;

public class LookCommand extends Command {
    public LookCommand() {
        super("quit");
    }

    @Override
    public Request execute(Robot target) {
        return new Request(target.getName(), "look", new String[] {});
    }
}
