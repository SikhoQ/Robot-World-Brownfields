package server.commands;


import java.util.HashMap;
import java.util.List;

import server.ClientHandler;
import server.response.DumpResponse;
import server.response.Response;
import server.world.Obstacle;

public class DumpCommand  extends Command{

   public DumpCommand() {
       super("dump");
   }

    // public String dump(ClientHandler target){
    //     StringBuilder string = new StringBuilder();
    //     string.append("Robots:\n");
    //     string.append(ClientHandler.robots);
    //     string.append(("\nObstacles:\n"));

    //     List<Obstacle> obstacles = target.getWorld().getObstacles();

    //     for (Obstacle obstacle: obstacles){
    //         string.append(obstacle.toString()).append("\n");
    //     }

    //     return string.toString();
    // }


   private HashMap dump(ClientHandler target){
       HashMap data = new HashMap<>();
       List<Obstacle> obstacles = target.getWorld().getObstacles();
       data.put("robots", ClientHandler.robots);
       data.put("obstacles", obstacles);
       return data;
   }

   @Override
   public Response execute(ClientHandler target) {
    //    target.sendToClient(target.getName() + ": " + "What's currently in the world.\n" + dump(target) + "\n" + target.getName() + ": What must I do next?");
       return new DumpResponse(dump(target));
   }
}
