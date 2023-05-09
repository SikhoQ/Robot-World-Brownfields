package client.json;

import client.request.Request;
import client.robots.State;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.File;
import java.io.IOException;

public class JsonHandler {

    private static ObjectMapper objectMapper = new ObjectMapper();

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

    public static JsonNode deserializeJsonFile(File file) throws IOException {
        // deserialize json file into JsonNode.
        JsonNode jsonNode =  objectMapper.readTree(file);
        return jsonNode;
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

    public static int[] convertToIntArray(String intArray) {
        int[] result = null;

        try {
            result = objectMapper.readValue(intArray, int[].class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static State updateState(JsonNode responseJson) {
        State state = null;
        try {
            state = objectMapper.readValue(responseJson.get("state").toString(), State.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return state;
    }
}
