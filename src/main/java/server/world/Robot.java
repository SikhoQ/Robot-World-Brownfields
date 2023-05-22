package server.world;

import server.ClientHandler;
import server.configuration.ConfigurationManager;
import server.world.util.Position;
import server.world.util.State;

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

    private final int maxShots;
    private final int maxSheilds;
    private final int bulletDistance;
    private final ConfigurationManager configurationManager;
    private final ClientHandler clientHandler;

    public Robot(String name, String kind, int shields, int shots, ClientHandler clientHandler) {
        this.name = name;
        this.kind = kind;
        this.shots = shots;
        this.shields = shields;
        this.maxSheilds = shields;
        this.maxShots = shots;
        this.status = "NORMAL";
        this.direction = Direction.NORTH;
        this.bulletDistance = 100;
        this.configurationManager = new ConfigurationManager();
        this.position = getStartingPosition();
        this.clientHandler = clientHandler;
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

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    @JsonIgnore
    public String getStatus() {
        return status;
    }

    @JsonIgnore
    public int getBulletDistance() {
        return bulletDistance;
    }

    @JsonIgnore
    public int getMaxSheilds() {
        return maxSheilds;
    }

    @JsonIgnore
    public int getMaxShots() {
        return maxShots;
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

    public void setShiels(int shields) {
        this.shields = shields;
        state.replace("shields", shields);
    }

    @JsonIgnore
    public void decreaseSheilds() {
        this.shields--;
        state.replace("shields", shields);
    }

    @JsonIgnore
    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
        state.replace("shots", shots);
    }

    @JsonIgnore
    public void decreaseShots() {
        this.shots--;
        state.replace("shots", shots);
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
    public int getDistance(Position position){
        return (int) Math.sqrt(Math.pow(position.getX() - this.getPosition().getX(), 2) +
                Math.pow(position.getY() - this.getPosition().getY(), 2));
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