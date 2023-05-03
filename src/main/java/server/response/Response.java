package server.response;

import java.util.HashMap;

public abstract class Response {

    private String result;
    private HashMap data;

    public Response(String result, HashMap data) {
        this.result = result;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public HashMap getData() {
        return data;
    }


    @Override
    public String toString() {
        return "StandardResponse{" +
                "result='" + result + '\'' +
                ", data=" + data +
                '}';
    }
}
