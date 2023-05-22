package client.commands;

import client.request.Request;
import client.robots.Robot;

public class ReloadCommand extends Command{

    public ReloadCommand() {
        super("reload");
    }

    @Override
    public Request execute(Robot target) {
        return new Request(target.getName(), "reload", new String[]{});
    }
    
}
