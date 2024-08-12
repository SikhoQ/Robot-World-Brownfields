package client.commands;

import client.request.Request;
import client.robots.Robot;

public class RestoreCommand extends Command {
//    retrieve a record from saved world table
//    ask client to specify the primary key(name) of the world they want to restore
//    start server with specified world configurations
    public RestoreCommand() {super("restore");}

    @Override
    public Request execute(Robot target) {
        return new Request(target.getName(), "restore", new String[]{});
    }

}
