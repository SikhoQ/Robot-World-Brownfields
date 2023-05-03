package server.commands;

import server.ClientHandler;
import server.response.BasicResponse;
import server.response.Response;

// import client.Client;

public class QuitCommand extends Command{

    public QuitCommand() {
        super("quit");
    }

    @Override
    public Response execute(ClientHandler target) {
        target.setCurrentCommand(getName());
//        target.sendToClient(target.getName() + ": " + "shutting down...");
//        target.closeEverything(target.getSocket(), target.getBufferedReader(), target.getBufferedWriter());
//        return false;
//        return null;
        ClientHandler.clientHanders.remove(target);
        ClientHandler.robots.remove(target.getRobot());
        return new BasicResponse("Successfully disconnected from server.");
    }
    
}
