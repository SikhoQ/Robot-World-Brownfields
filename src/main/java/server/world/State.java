package server.world;

import java.util.Arrays;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class State {

    private int[] position;
    private String direction;
    private int shields;
    private int shots;
    private String status;
    private HashMap<String, Object> state;

    public State(int[] position, String direction, int shields, int shots, String status) {
        this.position = position;
        this.direction = direction;
        this.shields = shields;
        this.shots = shots;
        this.status = status;
        this.setState();
    }

    private void setState() {
        this.state = new HashMap<String, Object>();
        this.state.put("position", position);
        this.state.put("direction", direction);
        this.state.put("shields", shields);
        this.state.put("shots", shots);
        this.state.put("status", status);
    }

    @JsonIgnore
    public HashMap<String, Object> getStateAsHashMap() {
        return this.state;
    }

    @Override
    public String toString() {
        return "State{" +
                "position=" + Arrays.toString(position) +
                ", direction='" + direction + '\'' +
                ", shields=" + shields +
                ", shots=" + shots +
                ", status='" + status + '\'' +
                '}';
    }
}
