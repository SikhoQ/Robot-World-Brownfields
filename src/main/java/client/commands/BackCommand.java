package client.commands;

import client.request.Request;
import client.robots.Robot;

public class BackCommand extends Command {
    private final String argument;

    public BackCommand(String argument) {
        super("back");
        this.argument = argument;
    }

    @Override
    public Request execute(Robot target) {
        return new Request(target.getName(), "back", new String[]{argument});
    }
}
