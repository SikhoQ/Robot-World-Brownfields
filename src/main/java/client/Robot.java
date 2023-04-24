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
    }

    @Override
    public String toString() {
       return this.name;
    }

    public String getName() {
        return name;
    }
}