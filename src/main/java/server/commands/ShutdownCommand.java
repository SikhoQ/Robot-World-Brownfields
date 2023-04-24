package server.commands;

import server.ClientHandler;

// import client.Client;

public class ShutdownCommand extends Command{

    public ShutdownCommand() {
        super("quit");
    }

    @Override
    public boolean execute(ClientHandler target) {
        target.sendToClient(target.getName() + ": " + "shutting down...");
        target.closeEverything(target.getSocket(), target.getBufferedReader(), target.getBufferedWriter());
        return false;
    }
    
}
