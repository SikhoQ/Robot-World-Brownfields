package server.response;

import java.util.HashMap;

public class RobotsResponse extends Response{

    public RobotsResponse(HashMap data) {
        super("OK", data);
    }
}
