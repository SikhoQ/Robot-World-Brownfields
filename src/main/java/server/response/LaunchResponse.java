package server.response;


import java.util.HashMap;


public class LaunchResponse extends Response {
    private HashMap state;

    public LaunchResponse(HashMap data, HashMap state) {
        super("OK", data);
        this.state = state;
    }
    public HashMap getState() {
        return state;
    }

    @Override
    public String toString() {
        return "LaunchResponse{" + "\n" +
                "result=" + getResult() + "\n" +
                "data=" + getData() + "\n" +
                "state=" + state + "\n" +
                '}';
    }
}
