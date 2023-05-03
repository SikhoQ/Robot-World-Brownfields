package server.response;

import java.util.HashMap;

public class DumpResponse extends Response {

    public DumpResponse(HashMap data) {
        super("OK", data);
    }

}
