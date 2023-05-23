package client.commands;

import client.request.Request;
import client.robots.Robot;

public class TurnCommand extends Command{
    private final String argument;

    public TurnCommand(String argument) {
        super("turn");
        this.argument = argument;
    }

    @Override
    public Request execute(Robot target) {
        return new Request(target.getName(), "turn", new String[]{argument});
    }
}
