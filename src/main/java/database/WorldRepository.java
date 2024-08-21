package database;

import server.world.World;
import server.world.WorldObject;

import java.util.List;

public interface WorldRepository {
    boolean save(String worldName, int worldSize, List<WorldObject> worldObjects);
    World loadWorld(String worldName);
    String removeWorld(String worldName);
}
