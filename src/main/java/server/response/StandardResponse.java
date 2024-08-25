package server.response;


import java.util.HashMap;

/**
 * Represents a standard response object with a result, data, and state.
 */
public class StandardResponse extends Response {
    private HashMap state;

    /**
     * Constructs a StandardResponse object with the specified data and state.
     *
     * @param data  The data associated with the response.
     * @param state The state associated with the response.
     */
    public StandardResponse(HashMap data, HashMap state) {
        super("OK", data);
        this.state = state;
    }

    /**
     * Returns the state associated with the response.
     *
     * @return The state associated with the response.
     */
    public HashMap getState() {
        return state;
    }

    /**
     * Returns a string representation of the StandardResponse object.
     *
     * @return A string representation of the StandardResponse object.
     */
    @Override
    public String toString() {
        return "StandardResponse{" + "\n" +
                "result=" + getResult() + "\n" +
                "data=" + getData() + "\n" +
                "state=" + state + "\n" +
                '}';
    }
}
