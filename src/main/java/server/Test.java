package server;

import com.fasterxml.jackson.databind.JsonNode;
import server.configuration.ConfigurationManager;
import server.json.JsonHandler;
import server.response.LaunchResponse;
import server.response.Response;
import server.world.State;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) throws IOException {
//        JsonNode config = JsonHandler.deserializeJsonFile(new File("src/main/java/server/configuration/Configuration.json"));
//        HashMap data = new HashMap<>();
//        data.put("position", new int[]{0,0});
//        data.put("visibility", config.get("world").get("visibility"));
//        data.put("reload", config.get("world").get("reload"));
//        data.put("repair", config.get("world").get("repair"));
//        data.put("shields", config.get("world").get("shields"));

//        State currentState = new State(new int[]{0,0}, "NORTH", 10, 20, "NORMAL");
////        System.out.println(data);
////        System.out.println(currentState);
//        Response launchResponse = new LaunchResponse(data, currentState.getState());
////        System.out.println(launchResponse);
//
//        String jsonString = JsonHandler.serializeResponse(launchResponse);
//        System.out.println(jsonString);

        ConfigurationManager configurationManager = new ConfigurationManager();
        configurationManager.getPort();

    }
}
