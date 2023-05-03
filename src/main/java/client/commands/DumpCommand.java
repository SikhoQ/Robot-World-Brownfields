package client.commands;

import client.Robot;
import client.request.Request;

public class DumpCommand extends Command {

    public DumpCommand(){
        super("dump");
    }
 
    @Override
    public Request execute(Robot target) {
        return new Request(target.getName(), "dump", new String[]{});
    }
    
}
