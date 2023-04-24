package server.commands;


import java.util.List;

import server.ClientHandler;
import server.world.Obstacle;

public class DumpCommand  extends Command{

    public DumpCommand() {
        super("dump");
    }

    public String dump(ClientHandler target){
        StringBuilder string = new StringBuilder();
        string.append("Robots:\n");
        string.append(ClientHandler.clientHanders);
        string.append(("\nObstacles:\n"));
   
        List<Obstacle> obstacles = target.getWorld().getObstacles();

        for (Obstacle obstacle: obstacles){
            string.append(obstacle.toString()).append("\n");
        }

        return string.toString();
    }

    @Override
    public boolean execute(ClientHandler target) {
        target.sendToClient(target.getName() + ": " + "What's currently in the world.\n" + dump(target) + "\n" + target.getName() + ": What must I do next?");
        return true;
    }
    
}
