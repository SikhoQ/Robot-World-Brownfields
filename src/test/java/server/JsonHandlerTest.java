package server;

import com.fasterxml.jackson.databind.JsonNode;

import server.json.JsonHandler;
import server.response.BasicResponse;
import server.response.Response;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonHandlerTest {

    @Test
    void serializeResponse() {
        // Create a sample Response object
        Response response = new BasicResponse("Hello, World!");

        // Call the serializeResponse method
        String jsonString = JsonHandler.serializeResponse(response);

        // Verify the serialized JSON string
        assertEquals("{\"result\":\"OK\",\"data\":{\"message\":\"Hello, World!\"}}", jsonString);
    }

    @Test
    void deserializeJsonFile() throws IOException {
        // Create a sample JSON file
        File file = new File("src/test/java/server/sample.json");

        // Call the deserializeJsonFile method
        JsonNode jsonNode = JsonHandler.deserializeJsonFile(file);

        // Verify the deserialized JSON node
        assertNotNull(jsonNode);
    }

    @Test
    void deserializeJsonString() {
        // Create a sample JSON string
        String jsonString = "{\"message\":\"Hello, World!\"}";

        // Call the deserializeJsonString method
        JsonNode jsonNode = JsonHandler.deserializeJsonTString(jsonString);

        // Verify the deserialized JSON node
        assertNotNull(jsonNode);
        assertEquals("Hello, World!", jsonNode.get("message").asText());
    }

    @Test
    void isJsonString() {
        // Valid JSON string
        String validJson = "{\"message\":\"Hello, World!\"}";

        // Invalid JSON string
        String invalidJson = "Hello, World!";

        // Call the isJsonString method
        assertTrue(JsonHandler.isJsonString(validJson));
        assertFalse(JsonHandler.isJsonString(invalidJson));
    }
}
