package database;

import server.world.World;
import server.world.WorldObject;

import java.util.List;
import java.util.Map;

public interface WorldRepository {
    boolean save(String worldName, int worldSize, List<WorldObject> worldObjects);
    Map<Integer, List<Map<String, List<Integer>>>> loadWorld(String worldName);
    String removeWorld(String worldName);
}
