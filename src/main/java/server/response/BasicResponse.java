package server.response;

import java.util.HashMap;

public class BasicResponse extends Response {
    public BasicResponse(String message) {
        super("OK", new HashMap<>() {{ put("message", message); }});
    }
}
