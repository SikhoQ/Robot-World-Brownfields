package acceptance;

import org.junit.jupiter.api.*;
import database.DatabaseConnection;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("acceptance")
class DBManyWorldsTest {

    private Connection connection;

    @BeforeEach
    void setup() {
        try {
            this.connection = DriverManager.getConnection(DatabaseConnection.getUrl());
            clearDatabase();
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        clearDatabase();
        // Close the connection after each test
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private void clearDatabase() {
        try (Statement statement = connection.createStatement()) {
            // Fetch all table names and delete all records from each table
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    statement.executeUpdate("DELETE FROM " + tableName);
                }
            }
            System.out.println("Database has been cleared.");
        } catch (SQLException e) {
            System.err.println("Failed to clear database: " + e.getMessage());
        }
    }

    @DisplayName("Saving worlds with unique names should succeed")
    @Test
    void testSaveWorldWithUniqueNames() {
        // Given that I have an existing robot worlds database
        // And it is set up with relevant tables
        // And the tables are empty
        String uniqueWorldName = "world1";
        String duplicateWorldName = "world2";
        int size = 3;

        // When I save a world in an empty database table
        assertTrue(saveWorld(uniqueWorldName, size), "The unique world should be saved successfully");

        // Then the world should exist in the database
        assertTrue(checkWorldExists(uniqueWorldName), "The unique world should exist in the database");

        // When I save a world with a unique name
        assertTrue(saveWorld(duplicateWorldName, size), "Another unique world should be saved successfully");

        // Then the world should exist in the database
        assertTrue(checkWorldExists(duplicateWorldName), "The second unique world should exist in the database");

        // When I save a world with a duplicate name
        // The world should not exist in the database
        assertFalse(saveWorld(duplicateWorldName, size), "The world with a duplicate name should not be saved");
    }

    public boolean saveWorld(String name, int worldSize) {
        String insertWorldSQL = "INSERT INTO " + DatabaseConnection.WORLD_TABLE + " (" +
                DatabaseConnection.WORLD_COLUMN_ID + ", " +
                DatabaseConnection.WORLD_COLUMN_SIZE + ") VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertWorldSQL)) {
            // Check if the world with the given name already exists
            if (checkWorldExists(name)) {
                System.out.println("World with name '" + name + "' already exists. Save operation aborted.");
                return false;
            }

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, worldSize);
            preparedStatement.executeUpdate();
            System.out.println("World saved successfully");
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to save world: " + e.getMessage());
            return false;
        }
    }

    public boolean checkWorldExists(String name) {
        String checkWorldSQL = "SELECT COUNT(*) FROM " + DatabaseConnection.WORLD_TABLE +
                " WHERE " + DatabaseConnection.WORLD_COLUMN_ID + " = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(checkWorldSQL)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Failed to check world: " + e.getMessage());
            return false;
        }
    }
}
