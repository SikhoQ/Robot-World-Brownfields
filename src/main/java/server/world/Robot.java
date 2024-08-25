package server.world;

import server.ClientHandler;
import server.configuration.ConfigurationManager;
import server.world.util.Position;
import server.world.util.State;

import java.util.HashMap;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a robot in the game.
 */
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
    private ClientHandler clientHandler = null;

    /**
     * Constructs a new Robot object.
     *
     * @param name          the name of the robot
     * @param kind          the kind of the robot
     * @param shields       the initial number of shields
     * @param shots         the initial number of shots
     * @param clientHandler the client handler associated with the robot
     */
    public Robot(String name, String kind, int shields, int shots, ClientHandler clientHandler) {
        this.name = name;
        this.kind = kind;
        this.shots = shots;
        this.shields = shields;
        this.maxSheilds = shields;
        this.maxShots = shots;
        this.status = "TODO";
        this.direction = Direction.NORTH;
        this.bulletDistance = 100;
        this.configurationManager = new ConfigurationManager();
        this.position = clientHandler.getWorld().getStartingPosition();
        if (this.position != null) {
            this.clientHandler = clientHandler;
            this.state = new State(position.asArray(), String.valueOf(direction), shields, shots, status).getStateAsHashMap();
        }
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

    /**
     * Returns the client handler associated with the robot.
     *
     * @return the client handler
     */
    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    /**
     * Returns the status of the robot.
     *
     * @return the status of the robot
     */
    @JsonIgnore
    public String getStatus() {
        return status;
    }

    /**
     * Returns the distance that bullets fired by this robot can travel.
     *
     * @return the bullet distance
     */
    @JsonIgnore
    public int getBulletDistance() {
        return bulletDistance;
    }

    /**
     * Returns the maximum number of shields for the robot.
     *
     * @return the maximum shields
     */
    @JsonIgnore
    public int getMaxSheilds() {
        return maxSheilds;
    }

    /**
     * Returns the maximum number of shots for the robot.
     *
     * @return the maximum shots
     */
    @JsonIgnore
    public int getMaxShots() {
        return maxShots;
    }

    /**
     * Sets the status of the robot.
     *
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the kind of the robot.
     *
     * @return the kind of the robot
     */
    public String getKind() {
        return kind;
    }

    /**
     * Sets the kind of the robot.
     *
     * @param kind the kind to set
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * Returns the number of shields for the robot.
     *
     * @return the number of shields
     */
    @JsonIgnore
    public int getShields() {
        return shields;
    }

    /**
     * Sets the number of shields for the robot.
     *
     * @param shields the number of shields to set
     */
    public void setShiels(int shields) {
        this.shields = shields;
        state.replace("shields", shields);
    }

    @JsonIgnore
    public void decreaseSheilds() {
        this.shields--;
        state.replace("shields", shields);
    }

    /**
     * Returns the number of shots for the robot.
     *
     * @return the number of shots
     */
    @JsonIgnore
    public int getShots() {
        return shots;
    }

    /**
     * Sets the number of shots for the robot.
     *
     * @param shots the number of shots to set
     */
    public void setShots(int shots) {
        this.shots = shots;
        state.replace("shots", shots);
    }

    /**
     * Decreases the number of shots for the robot by 1.
     */
    @JsonIgnore
    public void decreaseShots() {
        this.shots--;
        state.replace("shots", shots);
    }

    /**
     * Returns the state of the robot as a HashMap.
     *
     * @return the state of the robot
     */
    public HashMap<String, Object> getState() {
        return state;
    }

    /**
     * Sets the state of the robot.
     *
     * @param state the state to set
     */
    public void setState(State state) {
        this.state = state.getStateAsHashMap();
    }

    /**
     * Returns the position of the robot.
     *
     * @return the position of the robot
     */
    @JsonIgnore
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of the robot.
     *
     * @param position the position to set
     */
    public void setPosition(Position position) {
        this.position = position;
        state.replace("position", position.asArray());
    }

    /**
     * Returns the direction the robot is facing.
     *
     * @return the direction of the robot
     */
    @JsonIgnore
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the direction the robot is facing.
     *
     * @param direction the direction to set
     */
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
    public int getDistanceX(Position position){
        return (int) Math.abs(this.getPosition().getX() - position.getX());
    }

    @JsonIgnore
    public int getDistanceY(Position position){
        return (int) Math.abs(this.getPosition().getY() - position.getY());
    }

    /**
     * Returns the data of the robot as a HashMap.
     *
     * @return the data of the robot
     */
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

    /**
     * Returns a string representation of the robot.
     *
     * @return a string representation of the robot
     */
    @Override
    public String toString() {
        return this.name.toUpperCase() + " <" + this.kind + "> " + "at posiion " + position +
        "facing " + direction + ", shots: " + shots + ", sheilds: " + shields + ", status: " + status;
    }
    
}