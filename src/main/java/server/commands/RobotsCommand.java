package server.commands;

import server.ClientHandler;

public class RobotsCommand  extends Command{

    public RobotsCommand() {
        super("robots");
    }

    @Override
    public boolean execute(ClientHandler target) {
        target.sendToClient(target.getName()+ ": " + "Here are all the robots in the world:\n"+ ClientHandler.clientHanders + "\n" + target.getName() + ": What must I do next?");
        return true;
    }
    
}
