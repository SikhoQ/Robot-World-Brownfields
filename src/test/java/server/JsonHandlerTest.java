package server;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import server.json.JsonHandler;
import server.response.BasicResponse;
import server.response.Response;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the JsonHandler class.
 */
class JsonHandlerTest {

    /**
     * Tests the serialization of a Response object to a JSON string.
     */
    @Test
    void serializeResponse() {
        Response response = new BasicResponse("Hello, World!");
        String jsonString = JsonHandler.serializeResponse(response);
        assertEquals("{\"result\":\"OK\",\"data\":{\"message\":\"Hello, World!\"}}", jsonString);
    }

    /**
     * Tests deserialization of a JSON file into a JsonNode object.
     */
    @Test
    void deserializeJsonFile() throws IOException {
        File file = new File("src/test/java/server/sample.json");
        JsonNode jsonNode = JsonHandler.deserializeJsonFile(file);
        assertNotNull(jsonNode);
    }

    /**
     * Tests deserialization of a JSON string into a JsonNode object.
     */
    @Test
    void deserializeJsonString() {
        String jsonString = "{\"message\":\"Hello, World!\"}";
        JsonNode jsonNode = JsonHandler.deserializeJsonTString(jsonString);
        assertNotNull(jsonNode);
        assertEquals("Hello, World!", jsonNode.get("message").asText());
    }

    /**
     * Tests whether a string is a valid JSON string.
     */
    @Test
    void isJsonString() {
        String validJson = "{\"message\":\"Hello, World!\"}";
        String invalidJson = "Hello, World!";
        assertTrue(JsonHandler.isJsonString(validJson));
        assertFalse(JsonHandler.isJsonString(invalidJson));
    }
}
