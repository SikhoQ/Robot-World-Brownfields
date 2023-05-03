package server.response;

import java.util.HashMap;

public class StateResponse extends Response{
    private HashMap state;

    public StateResponse(HashMap state) {
        super("OK", new HashMap<>());
        this.state = state;
    }
    
    public HashMap getState() {
        return state;
    }
}
