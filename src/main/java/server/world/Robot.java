package server.world;

import server.configuration.ConfigurationManager;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Robot {

    private String status;
    private String name;
    private IWorld world;
    private String kind;
    private int shield;
    private int shots;
    private HashMap state;
    private Position position;
    private IWorld.Direction direction;
    private ConfigurationManager configurationManager;

    public Robot(String name, String kind, int shields, int shots) {
        this.name = name;
        this.kind = kind;
        this.shield = shields;
        this.shots = shots;
        this.status = "NORMAL";
        this.position = new Position(0,0);
        this.direction = IWorld.Direction.NORTH;
        this.configurationManager = new ConfigurationManager();
        this.state = new State(position.getPositionArray(), String.valueOf(direction), shields, shots, status).getStateAsHashMap();
    }

    @JsonIgnore
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public IWorld getWorld() {
        return world;
    }

    public void setWorld(IWorld world) {
        this.world = world;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @JsonIgnore
    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    @JsonIgnore
    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public HashMap getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state.getStateAsHashMap();
    }

    @JsonIgnore
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @JsonIgnore
    public IWorld.Direction getDirection() {
        return direction;
    }

    public void setDirection(IWorld.Direction direction) {
        this.direction = direction;
    }

    @JsonIgnore
    public HashMap getData() {
        HashMap data = new HashMap<>();
        data.put("position", position.getPositionArray());
        data.put("visibility", configurationManager.getVisibility());
        data.put("reload", configurationManager.getReload());
        data.put("repair", configurationManager.getRepair());
        data.put("shields", shield);
        return data;
    }

    @Override
    public String toString() {
       return this.name;
    }
}