package server.response;


import java.util.HashMap;


public class StandardResponse extends Response {
    private HashMap state;

    public StandardResponse(HashMap data, HashMap state) {
        super("OK", data);
        this.state = state;
    }
    public HashMap getState() {
        return state;
    }

    @Override
    public String toString() {
        return "StandardResponse{" + "\n" +
                "result=" + getResult() + "\n" +
                "data=" + getData() + "\n" +
                "state=" + state + "\n" +
                '}';
    }
}
