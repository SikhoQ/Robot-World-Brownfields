package server;

import org.junit.jupiter.api.Test;
import server.response.BasicResponse;
import server.response.ErrorResponse;
import server.response.Response;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Response class and its subclasses.
 */
class ResponseTest {

    /**
     * Tests the ErrorResponse class to ensure it sets and retrieves error messages correctly.
     */
    @Test
    void testErrorResponse() {
        Response response = new ErrorResponse("An error occurred.");
        assertEquals("ERROR", response.getResult());
        assertEquals("An error occurred.", response.getData().get("message"));
    }

    /**
     * Tests the BasicResponse class to ensure it sets and retrieves basic messages correctly.
     */
    @Test
    void testBasicResponse() {
        Response response = new BasicResponse("OK, alright.");
        assertEquals("OK", response.getResult());
        assertEquals("OK, alright.", response.getData().get("message"));
    }
}
