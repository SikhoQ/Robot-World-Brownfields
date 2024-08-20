package server.world;

import server.configuration.ConfigurationManager;

import java.util.ArrayList;
import java.util.List;

public class WorldObjects {
    private final ArrayList<WorldObject> objects;

    public WorldObjects(World world) {
        final List<Obstacle> obstacles = world.getObstacles();
        final List<Robot> robots = world.getRobots();
        final ArrayList<WorldObject> worldObjects = new ArrayList<>();
        this.objects = convertObjects(obstacles, robots, worldObjects);
    }

    private ArrayList<WorldObject> convertObjects(List<Obstacle> obstacles, List<Robot> robots, ArrayList<WorldObject> worldObjects) {
        ArrayList<WorldObject> objects = new ArrayList<>();
        for (Robot robot: robots) {
            int x = robot.getPosition().getX();
            int y = robot.getPosition().getY();
            int size = ConfigurationManager.getTileSize();
            String type = robot.getClass().getSimpleName();
            WorldObject worldObject = new WorldObject(type, size, x, y);
            objects.add(worldObject);
        }
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
