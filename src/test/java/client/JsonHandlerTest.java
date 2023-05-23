package client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import client.json.JsonHandler;
import client.request.Request;
import client.robots.util.State;


public class JsonHandlerTest {

    @Test
    public void testSerializeRequest() {
        // Create a sample Request object
        Request request = new Request("Robot1", "command", new String[]{"arg1", "arg2"});

        // Serialize the Request object
        String jsonString = JsonHandler.serializeRequest(request);

        // Verify the serialized JSON string
        String expectedJsonString = "{\"robot\":\"Robot1\",\"command\":\"command\",\"arguments\":[\"arg1\",\"arg2\"]}";
        Assertions.assertEquals(expectedJsonString, jsonString);
    }


    @Test
    public void testDeserializeJsonString() {
        // Create a sample JSON string
        String jsonString = "{\"robot\":\"Robot1\",\"command\":\"command\",\"arguments\":[\"arg1\",\"arg2\"]}";

        // Deserialize the JSON string
        JsonNode jsonNode = JsonHandler.deserializeJsonTString(jsonString);

        // Verify the deserialized JsonNode
        Assertions.assertNotNull(jsonNode);
    }



    @Test
    public void testConvertToIntArray() {
        // Create a sample JSON string representing an integer array
        String intArrayString = "[1, 2, 3, 4, 5]";

        // Convert the JSON string to an int array
        int[] intArray = JsonHandler.convertToIntArray(intArrayString);

        // Verify the converted int array
        int[] expectedIntArray = {1, 2, 3, 4, 5};
        Assertions.assertArrayEquals(expectedIntArray, intArray);
    }

    @Test
    public void testUpdateState() {
        // Create a sample response JsonNode
        String responseJsonString = "{\"state\":{\"position\":[1,2],\"status\":\"active\"}}";
        JsonNode responseJson = JsonHandler.deserializeJsonTString(responseJsonString);

        // Update the State object based on the response JsonNode
        State state = JsonHandler.updateState(responseJson);

        // Verify the updated State object
        Assertions.assertNotNull(state);
        Assertions.assertArrayEquals(new int[]{1, 2}, state.getPosition());
        Assertions.assertEquals("active", state.getStatus());
    }
}
