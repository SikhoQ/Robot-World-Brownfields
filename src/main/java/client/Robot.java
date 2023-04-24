package client;

import java.util.Arrays;
import java.util.List;

import server.world.IWorld;

public class Robot {

    private String status;
    private String name;
    private IWorld world;

    public Robot(String name) {
        this.name = name;
        // this.status = "Ready";
        // this.world = new TextWorld();
    }

    // public Robot(String name, String world) {
    //     this.name = name;
    //     this.status = "Ready";
    //     // this.world = new TurtleWorld();
    // }

    // public String getStatus() {
    //     return this.status;
    // }

    // public IWorld getWorld() { return this.world; }

    // public boolean handleCommand(Command command) {
    //     return command.execute(this);
    // }

    @Override
    public String toString() {
       return this.name;
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