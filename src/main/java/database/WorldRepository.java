package database;

import server.world.WorldObject;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface WorldRepository {
    boolean save(String worldName, int worldSize, List<WorldObject> worldObjects);
    Map<Integer, List<Map<String, List<Integer>>>> loadWorld(String worldName);
    String removeWorld(String worldName);
    String removeEverything(Connection connection);
}
