package server.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.response.Response;

import java.io.File;
import java.io.IOException;

public class JsonHandler {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String serializeResponse(Response response) {
        String jsonString = null;
        try {
            // serialize java response object into json string.
            jsonString = objectMapper.writeValueAsString(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public static JsonNode deserializeJsonFile(File file) throws IOException {
//        JsonNode jsonNode;
        // deserialize json file into JsonNode.
        JsonNode jsonNode =  objectMapper.readTree(file);
        return jsonNode;
    }

    public static JsonNode deserializeJsonTString(String jsonString) {
        JsonNode jsonNode = null;
        try{
            // deserialize json string into JsonNode.
            jsonNode =  objectMapper.readTree(jsonString);
//            System.out.println(employee.get("name").asText() + "\n" + employee.get("city").asText());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return jsonNode;
    }
}
