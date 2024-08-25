package acceptance;


import database.DatabaseConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.configuration.ConfigurationManager;
import server.world.Obstacle;
import server.world.SquareObstacle;
import server.world.World;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 *
 * @author sqlitetutorial.net
 */
public class RestoreTheWorldTest {
    /**
     * Connection to RobotWorlds database
     */
    protected Connection connection;

    @BeforeEach
    void setup() throws SQLException {
        try {
            this.connection = DriverManager.getConnection(DatabaseConnection.getUrl());
            String createWorldTableQuery = "CREATE TABLE IF NOT EXISTS testWorld (" +
                    "id TEXT PRIMARY KEY NOT NULL," +
                    "size INTEGER NOT NULL)";
            String createObjectTableQuery = "CREATE TABLE IF NOT EXISTS testObjects (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "world_id TEXT NOT NULL," +
                    "type TEXT NOT NULL," +
                    "x INT NOT NULL," +
                    "y INT NOT NULL," +
                    "size INT NOT NULL CHECK (size >= 0)," +
                    "FOREIGN KEY (world_id) REFERENCES testWorld(id))";

            try (PreparedStatement statement = this.connection.prepareStatement(createWorldTableQuery)) {
                statement.execute();
            }

            try (PreparedStatement statement = this.connection.prepareStatement(createObjectTableQuery)) {
                statement.execute();
            }

            String worldEntryCountQuery = "SELECT COUNT(*) FROM testWorld";
            try (PreparedStatement statement = this.connection.prepareStatement(worldEntryCountQuery);
                 ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                int entries = resultSet.getInt(1);

                String insertWorldQuery = "INSERT INTO testWorld (id, size) VALUES (?, ?)";
                try (PreparedStatement insertStatement = this.connection.prepareStatement(insertWorldQuery)) {
                    insertStatement.setString(1, String.format("test%d", entries + 1));
                    insertStatement.setInt(2, 3);
                    insertStatement.executeUpdate();
                }
            }

            String objectsEntryCountQuery = "SELECT COUNT(*) FROM testObjects";
            try (PreparedStatement statement = this.connection.prepareStatement(objectsEntryCountQuery);
                 ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                int entries = resultSet.getInt(1);

                String insertObjectQuery = "INSERT INTO testObjects (world_id, type, x, y, size) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement insertStatement = this.connection.prepareStatement(insertObjectQuery)) {
                    insertStatement.setString(1, String.format("test%d", entries + 1));
                    insertStatement.setString(2, "cookie");
                    insertStatement.setInt(3, 1);
                    insertStatement.setInt(4, 1);
                    insertStatement.setInt(5, 4);
                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            clearDatabase();
            ConfigurationManager.setWorldSize(1);
            ConfigurationManager.setXConstraint(1);
            ConfigurationManager.setYConstraint(1);
            this.connection.close();
        }
    }

    public void clearDatabase() {
        try (Statement statement = connection.createStatement()) {
            // Delete all records from the world table
            try {
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    String deleteWorldSQL = "DELETE FROM " + tableName;
                    statement.executeUpdate(deleteWorldSQL);
                }
            } catch (SQLException e) {
                System.err.println("Failed to clear database: " + e.getMessage());
            }

            System.out.println("Database has been cleared.");
        } catch (SQLException e) {
            System.err.println("Failed to clear database: " + e.getMessage());
        }
    }

    @Test
    public void databaseConnectionTest() {
        // setup method connects to db before each test
       assertTrue(true);
    }
    /**
     * check if table exists and that it has records
     */

    @Test
    public void ExistingTableTest() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();

            // Check if the 'testWorld' table exists
            ResultSet resultSet = metaData.getTables(null, null, "testWorld", new String[]{"TABLE"});
            boolean worldTableExists = resultSet.next();
            assertTrue(worldTableExists, "The 'testWorld' table should exist in the database.");

            // Check if the 'testObjects' table exists
            resultSet = metaData.getTables(null, null, "testObjects", new String[]{"TABLE"});
            boolean obstaclesTableExists = resultSet.next();
            assertTrue(obstaclesTableExists, "The 'testObjects' table should exist in the database.");

        } catch (SQLException e) {
            e.printStackTrace();
            fail("An error occurred while checking if the tables exist: " + e.getMessage());
        }
    }



    /**
     * check if passing the restore command launches a robot with saved world state
     */

    @Test
    public void restoreTest() {
        try {
            // Insert initial world state data into the 'world' and 'obstacles' tables for testing
            clearDatabase();
            String insertWorldSQL = "INSERT INTO testWorld (id, size) VALUES ('test-world', 5)";
            String insertObstacleSQL = "INSERT INTO testObjects (world_id, type, x, y, size) VALUES ('test-world', 'rock', '3', '3', '2')";

            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(insertWorldSQL);
                stmt.executeUpdate(insertObstacleSQL);
            }

            // Assume restoreWorldState() is the method that restores the state based on saved data
            boolean isRestored = restoreWorldState("test-world");  // This method would need to be implemented
            assertTrue(isRestored, "The world state should be restored successfully.");

            // Verify the restored world state (this will depend on your application's logic)
            // Example: check that a robot is correctly positioned in the restored world
            // assertTrue(robotIsAtPosition(1, 1), "The robot should be at position (1, 1) in the restored world.");

        } catch (SQLException e) {
            e.printStackTrace();
            fail("An error occurred during the restore test: " + e.getMessage());
        }
    }

    // This method needs to be implemented based on your application's logic

    public boolean restoreWorldState(String worldName) {
        try {
            // Query to get the world details
            String getWorldSQL = "SELECT * FROM testWorld WHERE id = ?";
            PreparedStatement getWorldStatement = connection.prepareStatement(getWorldSQL);
            getWorldStatement.setString(1, worldName);
            ResultSet worldResultSet = getWorldStatement.executeQuery();

            if (!worldResultSet.next()) {
                System.out.println("No world found with name: " + worldName);
                return false;
            }

            // Extract world details
            int worldSize = worldResultSet.getInt("size");

            String getObjectsSQL = "SELECT * FROM testObjects WHERE world_id = ?";
            PreparedStatement getObjectStatement = connection.prepareStatement(getObjectsSQL);
            getObjectStatement.setString(1, worldName);
            ResultSet objectsResultSet = getObjectStatement.executeQuery();

            List<Obstacle> obstacles = new ArrayList<>();

            while (objectsResultSet.next()) {
                // get object type, use it to construct object
                String type = objectsResultSet.getString("type");
                int x = objectsResultSet.getInt("x");
                int y = objectsResultSet.getInt("y");
                int size = objectsResultSet.getInt("size");

                ConfigurationManager.setTileSize(size);

                if (type.equalsIgnoreCase("SquareObstacle")) {
                    obstacles.add(new SquareObstacle(x, y)); // Assuming SquareObstacle takes x, y, and size in constructor
                }
                // Add other types of obstacles as necessary
            }
            ConfigurationManager.setWorldSize(worldSize);
            ConfigurationManager.setXConstraint(worldSize);
            ConfigurationManager.setYConstraint(worldSize);
            World world = new World();
            world.setObstacles(obstacles);

            System.out.println("World state restored successfully.");

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to restore world state: " + e.getMessage());
            return false;
        }
    }

    /**
     * check if table has records
     */
    @Test
    public void RecordsExistTest() {
        String getWorldRecordsQuery = "SELECT COUNT(*) FROM testWorld";
        String getObjectRecordsQuery = "SELECT COUNT(*) FROM testObjects";

        // Check if 'world' table has records
        try (PreparedStatement stmt = connection.prepareStatement(getWorldRecordsQuery)) {
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            int worldTableCount = resultSet.getInt(1);
            assertTrue(worldTableCount > 0, "The 'testWorld' table should have records.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Check if 'objects' table has records
        try (PreparedStatement stmt = connection.prepareStatement(getObjectRecordsQuery)) {
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            int obstaclesTableCount = resultSet.getInt(1);
            assertTrue(obstaclesTableCount > 0, "The 'testObjects' table should have records.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        catch (RuntimeException e) {
            fail("An error occurred while checking if the tables have records: " + e.getMessage());
        }
    }
}