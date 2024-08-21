package server.world;

import server.configuration.ConfigurationManager;

import java.util.ArrayList;
import java.util.List;

public class WorldObjects {
    private final ArrayList<WorldObject> objects;

    public WorldObjects(World world) {
        final List<Obstacle> obstacles = world.getObstacles();
        final ArrayList<WorldObject> worldObjects = new ArrayList<>();
        this.objects = convertObjects(obstacles, worldObjects);
    }

    private ArrayList<WorldObject> convertObjects(List<Obstacle> obstacles, ArrayList<WorldObject> worldObjects) {
        ArrayList<WorldObject> objects = new ArrayList<>();

        for (Obstacle obstacle: obstacles) {
            int x = obstacle.getBottomLeftX();
            int y = obstacle.getBottomLeftY();
            int size = ConfigurationManager.getTileSize();
            String type = obstacle.getClass().getSimpleName();
            WorldObject worldObject = new WorldObject(type, size, x, y);
            objects.add(worldObject);
        }

        return objects;
    }

    public ArrayList<WorldObject> getObjects() {
        return this.objects;
    }
}
