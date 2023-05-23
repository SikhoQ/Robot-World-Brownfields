package server;


import org.junit.jupiter.api.Test;

import server.response.BasicResponse;
import server.response.ErrorResponse;
import server.response.Response;


import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    @Test
    void testErrorResponse() {
        Response response = new ErrorResponse("An error occurred.");
        assertEquals("ERROR", response.getResult());
        assertEquals("An error occurred.", response.getData().get("message"));
        
    }

    @Test
    void testBasicResponse() {
        Response response = new BasicResponse("OK, alright.");
        assertEquals("OK", response.getResult());
        assertEquals("OK, alright.", response.getData().get("message"));
        
    }

}