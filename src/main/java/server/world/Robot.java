package server.world;

import server.configuration.ConfigurationManager;
import java.util.HashMap;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Robot {

    private String status;
    private String name;
    private String kind;
    private int shields;
    private int shots;
    private HashMap<String, Object> state;
    private Position position;
    private Direction direction;
    private ConfigurationManager configurationManager;
    private final int bulletDistance;

    public Robot(String name, String kind, int shields, int shots) {
        this.name = name;
        this.kind = kind;
        this.shields = shields;
        this.shots = shots;
        this.status = "NORMAL";
        this.direction = Direction.NORTH;
        this.bulletDistance = 30;
        this.configurationManager = new ConfigurationManager();
        this.position = getStartingPosition();
        this.state = new State(position.asArray(), String.valueOf(direction), shields, shots, status).getStateAsHashMap();
    }

    private int randomInt(int start, int stop) {
        Random random = new Random();
        return start + random.nextInt(stop - start + 1);
    }

    private Position getStartingPosition() {
        boolean posiionBlocked = true;
        Position randomStartingPosition = new Position(0, 0);

        while (posiionBlocked) {
            int randomX = randomInt(-configurationManager.getXConstraint(), configurationManager.getXConstraint());
            int randomY = randomInt(-configurationManager.getYConstraint(), configurationManager.getYConstraint());
            randomStartingPosition = new Position(randomX, randomY);
            posiionBlocked = (boolean)SquareObstacle.blocksPosition(randomStartingPosition, this)[0];
        }
        return randomStartingPosition;
    }

    @JsonIgnore
    public String getStatus() {
        return status;
    }

    @JsonIgnore
    public int getBulletDistance() {
        return bulletDistance;
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

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @JsonIgnore
    public int getShields() {
        return shields;
    }

    public void setShield(int shield) {
        this.shields = shield;
    }

    @JsonIgnore
    public void decreaseSheilds() {
        if (shields > 0) {
            this.shields--;
            state.replace("shields", shields);
        }
    }

    @JsonIgnore
    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    @JsonIgnore
    public void decreaseShots() {
        if (shots > 0) {
            this.shots--;
            state.replace("shots", shots);
        }
    }

    public HashMap<String, Object> getState() {
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
        state.replace("position", position.asArray());
    }

    @JsonIgnore
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        state.replace("direction", String.valueOf(direction));
    }

    // returns distance from this robot to another robot using the distance formula.
    @JsonIgnore
    public int getDistance(Robot otherRobot){
        return (int) Math.sqrt(Math.pow(otherRobot.getPosition().getX() - this.getPosition().getX(), 2) + 
                               Math.pow(otherRobot.getPosition().getY() - this.getPosition().getY(), 2));
    }

    @JsonIgnore
    public HashMap<String, Object> getData() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("position", position.asArray());
        data.put("visibility", configurationManager.getVisibility());
        data.put("reload", configurationManager.getReload());
        data.put("repair", configurationManager.getRepair());
        data.put("shields", shields);
        return data;
    }

    @Override
    public String toString() {
        return this.name.toUpperCase() + " <" + this.kind + "> " + "at posiion " + position +
        "facing " + direction + ", shots: " + shots + ", sheilds: " + shields + ", status: " + status;
    }
    
}