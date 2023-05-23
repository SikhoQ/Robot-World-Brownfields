package client.json;

import client.request.Request;
import client.robots.util.State;
import client.userInterface.util.Position;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String serializeRequest(Request request) {
        String jsonString = null;
        try {
            // serialize java response object into json string.
            jsonString = objectMapper.writeValueAsString(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public static JsonNode deserializeJsonTString(String jsonString) {
        JsonNode jsonNode = null;
        try{
            // deserialize json string into JsonNode.
            jsonNode =  objectMapper.readTree(jsonString);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return jsonNode;
    }

    public static List<Position> deserializeObstacles(JsonNode obstaclesArray) {
        List<Position> obstacles = new ArrayList<>();

        for (int i = 0; i < obstaclesArray.size(); i++) {
            JsonNode obstacle = obstaclesArray.get(i);
            int x = obstacle.get("x").asInt();
            int y = obstacle.get("y").asInt();
            Position position = new Position(x, y);
            obstacles.add(position);
        }

        return obstacles;

    }

    public static State updateState(JsonNode responseJson) {
        State state = null;
        try {
            state = objectMapper.readValue(responseJson.get("state").toString(), State.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return state;
    }

    public static State getState(JsonNode stateNode) {
        State state = null;
        try {
            state = objectMapper.readValue(stateNode.toString(), State.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return state;
    }

    public static Boolean isJsonString(String string) {
        try {
            objectMapper.readTree(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
