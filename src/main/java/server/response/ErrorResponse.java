package server.response;

import java.util.HashMap;

public class ErrorResponse extends Response {
    public ErrorResponse(String message) {
        super("ERROR", new HashMap<>() {{ put("message", message); }});
    }
}
