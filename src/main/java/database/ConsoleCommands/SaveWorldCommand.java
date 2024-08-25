package database.ConsoleCommands;

import server.world.WorldObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SaveWorldCommand implements ServerCommand {
    private Connection connection;
    private String worldName;
    private int worldSize;
    private List<WorldObject> worldObjects;

    public SaveWorldCommand(Connection connection, String name, int worldSize, List<WorldObject> worldObjects) {
        this.connection = connection;
        this.worldName = name;
        this.worldSize = worldSize;
        this.worldObjects = worldObjects;
    }

    @Override
    public boolean execute() {
        boolean autoCommit = true;
        try {
            // Check if auto-commit is disabled
            autoCommit = connection.getAutoCommit();

            // If auto-commit is disabled, start a transaction
            if (!autoCommit) {
                connection.setAutoCommit(false);
            }

            // Insert the world
            String worldInsertQuery = "INSERT INTO world (id, size) VALUES (?, ?)";
            try (PreparedStatement stmt = this.connection.prepareStatement(worldInsertQuery)) {
                stmt.setString(1, worldName);
                stmt.setInt(2, worldSize);
                stmt.executeUpdate();
            }

            // Insert the objects
            String objectInsertQuery = "INSERT INTO objects (world_id, type, x, y, size) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = this.connection.prepareStatement(objectInsertQuery)) {
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

            // Commit transaction if we are manually handling it
            if (!autoCommit) {
                connection.commit();
            }

            return true;
        } catch (SQLException e) {
            // Rollback the transaction in case of an exception
            if (!autoCommit) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Failed to rollback transaction: " + rollbackEx.getMessage());
                }
            }
            System.err.println("SQL Error: " + e.getMessage());
            return false;
        } finally {
            // Reset auto-commit to its original state
            try {
                if (!autoCommit) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Failed to reset auto-commit: " + e.getMessage());
            }
        }
    }

}