package client.commands;

import client.request.Request;
import client.robots.Robot;

public class ForwardCommand extends Command {
    private String argument;

    public ForwardCommand(String argument) {
        super("forward");
        this.argument = argument;
    }

    @Override
    public Request execute(Robot target) {
        return new Request(target.getName(), "forward", new String[]{argument});
    }  
}
