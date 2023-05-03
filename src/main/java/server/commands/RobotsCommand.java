package server.commands;

import java.util.HashMap;

import server.ClientHandler;
import server.response.Response;
import server.response.RobotsResponse;

public class RobotsCommand  extends Command{

    public RobotsCommand() {
        super("robots");
    }

    // private HashMap getRobots(ClientHandler target){
    //     HashMap data = new HashMap<>();
    //     data.put("robots", ClientHandler.robots);
    //     return data;
    // }

    @Override
    public Response execute(ClientHandler target) {
        return new RobotsResponse(new HashMap<>() {{ put("robots", ClientHandler.robots); }});
    }

}
