package client;

import server.world.IWorld;

public class Robot {

    private String status;
    private String name;
    private IWorld world;
    private String kind;
    private String shields;
    private String shots;

    public Robot(String name) {
        this.name = name;
        this.shields = "0";
        this.shots = "10";
    }

    @Override
    public String toString() {
       return this.name;
    }

    public String getShields() {
        return shields;
    }

    public String getShots() {
        return shots;
    }

    // public void setStatus(String status) {
    //     this.status = status;
    // }

    public String getName() {
        return name;
    }

    // public void showObstacles() {
    //     world.showObstacles();
    // }
}