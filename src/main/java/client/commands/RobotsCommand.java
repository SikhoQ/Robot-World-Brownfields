package client.commands;

import client.Robot;
import client.request.Request;

public class RobotsCommand extends Command {
    
    public RobotsCommand(){
        super("robots");
    }
 
    @Override
    public Request execute(Robot target) {
        return new Request(target.getName(), "robots", new String[]{});
    }
}
