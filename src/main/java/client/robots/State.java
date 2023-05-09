package client.robots;


public class State {
    private int[] position;
    private String direction;
    private int shields;
    private int shots;
    private String status;

    // getters and setters for each field
    public int[] getPosition() { return position; }

    public void setPosition(int[] position) { this.position = position; }

    public String getDirection() { return direction; }

    public void setDirection(String direction) { this.direction = direction; }

    public int getShields() { return shields; }

    public void setShields(int shields) { this.shields = shields; }

    public int getShots() { return shots; }

    public void setShots(int shots) { this.shots = shots; }

    public String getStatus() { return status; }
    
    public void setStatus(String status) { this.status = status; }
}
