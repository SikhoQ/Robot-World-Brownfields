package server.world;

import java.util.ArrayList;
import java.util.List;

public class WorldObjects {
    private final ArrayList<Object> objects;

    public WorldObjects(World world) {
        final List<Obstacle> obstacles = world.getObstacles();
        final List<Robot> robots = world.getRobots();
        final ArrayList<Object> worldObjects = new ArrayList<>(obstacles);
        worldObjects.addAll(robots);
        this.objects = worldObjects;
    }

    public ArrayList<Object> getObjects() {
        return this.objects;
    }
}
