package database;

import server.world.WorldObject;

import java.util.*;
import java.sql.*;

public class SQLiteWorldRepository implements WorldRepository {
    private static final String URL = "jdbc:sqlite:RobotWorlds.db";

    @Override
    public Map<Integer, List<Map<String, List<Integer>>>> loadWorld(String worldName) {
        String worldSelectQuery = "SELECT size FROM world WHERE id = ?";
        String objectsSelectQuery = "SELECT type, x, y, size FROM objects WHERE world_id = ?";

        // List of all objects in this world, objects being a map of its 'type' to a list of its size ints
        List<Map<String, List<Integer>>> worldObjects = new ArrayList<>();
        // map of single object: its 'type' to a list of its  size ints
        Map<String, List<Integer>> oneObject = new HashMap<>();
        // Map of the size of the world to a list of its objects
        Map<Integer, List<Map<String, List<Integer>>>> worldInfo = new HashMap<>();
        int worldSize = -1;

        try (Connection conn = DriverManager.getConnection(URL)) {
            // Load the world
            try (PreparedStatement stmt = conn.prepareStatement(worldSelectQuery)) {
                stmt.setString(1, worldName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    worldSize = rs.getInt("size");
                }
            } catch (SQLException ignored) {}

            if (worldSize == -1)
                return worldInfo;

            try (PreparedStatement stmt = conn.prepareStatement(objectsSelectQuery)) {
                stmt.setString(1, worldName);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String type = rs.getString("type");
                    int x = rs.getInt("x");
                    int y = rs.getInt("y");
                    int size = rs.getInt("size");
                    oneObject.put(type, new ArrayList<>(Arrays.asList(x, y, size)));
                    worldObjects.add(oneObject);
                }
                worldInfo.put(worldSize, worldObjects);
                return  worldInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return worldInfo;
        }
    }

    @Override
    public boolean save(String worldName, int worldSize, List<WorldObject> worldObjects) {
        String worldInsertQuery = "INSERT INTO world (id, size) VALUES (?, ?)";
        String objectInsertQuery = "INSERT INTO objects (world_id, type, x, y, size) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL)) {
            // Save the world
            try (PreparedStatement stmt = conn.prepareStatement(worldInsertQuery)) {
                stmt.setString(1, worldName);
                stmt.setInt(2, worldSize);
                stmt.executeUpdate();
            }

            // Save the objects
            try (PreparedStatement stmt = conn.prepareStatement(objectInsertQuery)) {
                for (WorldObject obj : worldObjects) {
                    stmt.setString(1, worldName);
                    stmt.setString(2, obj.getType());
                    stmt.setInt(3, obj.getX());
                    stmt.setInt(4, obj.getY());
                    stmt.setInt(5, obj.getSize());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String removeWorld(String worldName) {
        String deleteObjectsQuery = "DELETE FROM objects WHERE world_id = ?";
        String deleteWorldQuery = "DELETE FROM world WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL)) {
            // Start a transaction
            conn.setAutoCommit(false);

            // Delete the objects associated with the world
            try (PreparedStatement stmt = conn.prepareStatement(deleteObjectsQuery)) {
                stmt.setString(1, worldName);
                stmt.executeUpdate();
                int updateCount = stmt.getUpdateCount();
            } catch (SQLException ignored) {
                return "no objects";
            }

            // Delete the world itself
            try (PreparedStatement stmt = conn.prepareStatement(deleteWorldQuery)) {
                stmt.setString(1, worldName);
                int rowsAffected = stmt.executeUpdate();

                // If no rows were affected, the world didn't exist
                if (rowsAffected == 0) {
                    conn.rollback();
                    return "no world";
                }
            } catch (SQLException ignored) {
                return "no world";
            }

            // Commit the transaction
            conn.commit();
            return "removed";

        } catch (SQLException ignored) {
            return "not removed";
        }
    }
}
