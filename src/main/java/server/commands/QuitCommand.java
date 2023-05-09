package server.commands;

import server.ClientHandler;
import server.response.BasicResponse;
import server.response.Response;


public class QuitCommand extends Command{

    public QuitCommand() {
        super("quit");
    }

    @Override
    public Response execute(ClientHandler target) {
        target.setCurrentCommand(getName());
        ClientHandler.clientHanders.remove(target);
        target.getWorld().getRobots().remove(target.getRobot());
        return new BasicResponse("Successfully disconnected from server.");
    }
    
}
