package acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import database.DatabaseConnection;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class ManyWorldsTest {

    private Connection connection;

    @BeforeEach
    void setup() throws SQLException {
        // Establish connection to the database
        connection = DriverManager.getConnection(DatabaseConnection.getUrl());

        // Clear the database before each test
        clearDatabase();
    }

    public void clearDatabase() {
        try (Statement statement = connection.createStatement()) {
            // Delete all records from the world table
            String deleteWorldSQL = "DELETE FROM " + DatabaseConnection.WORLD_TABLE;
//            statement.executeUpdate(deleteWorldSQL);

            // Optionally, you can add other cleanup operations for additional tables
            // Example: String deleteRobotsSQL = "DELETE FROM " + DatabaseConnection.TABLE_ROBOTS;
            // statement.executeUpdate(deleteRobotsSQL);

            System.out.println("Database has been cleared.");
        } catch (SQLException e) {
            System.err.println("Failed to clear database: " + e.getMessage());
        }
    }


    @Test
    void testSaveWorldWithUniqueNames() {
        String uniqueWorldName = "world1";
        String duplicateWorldName = "world2";
        int sizeX = 5;
        int sizeY = 4;
        String obstacle = "pit";

        // Save a world with a unique name
        boolean isFirstSaveSuccessful = saveWorld(uniqueWorldName, sizeX, sizeY);
        assertTrue(isFirstSaveSuccessful, "The unique world should be saved successfully");

        // Verify the unique world was actually saved in the database
        boolean uniqueWorldExists = checkWorldExists(uniqueWorldName);
        assertTrue(uniqueWorldExists, "The unique world should exist in the database");

        // Save another world with a different unique name
        boolean isSecondSaveSuccessful = saveWorld(duplicateWorldName, sizeX, sizeY);
        assertTrue(isSecondSaveSuccessful, "Another unique world should be saved successfully");

        // Verify the second unique world was actually saved in the database
        boolean secondWorldExists = checkWorldExists(duplicateWorldName);
        assertTrue(secondWorldExists, "The second unique world should exist in the database");

        // Attempt to save a world with a duplicate name
        boolean isDuplicateSaveSuccessful = saveWorld(duplicateWorldName, sizeX, sizeY);
        assertFalse(isDuplicateSaveSuccessful, "The world with a duplicate name should not be saved");

//        // Clean up (delete both worlds)
//        deleteWorld(uniqueWorldName);
//        deleteWorld(duplicateWorldName);
    }

    public boolean saveWorld(String name, int sizeX, int sizeY) {
        try {
            // Check if the world with the given name already exists
            if (checkWorldExists(name)) {
                System.out.println("World with name '" + name + "' already exists. Save operation aborted.");
                return false;  // Indicate that saving was not successful
            }

            String insertWorldSQL = "INSERT INTO " + DatabaseConnection.WORLD_TABLE + " (" +
                    DatabaseConnection.WORLD_COLUMN_ID + ", " +
                    DatabaseConnection.WORLD_COLUMN_SIZE + ") VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertWorldSQL);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, sizeX);
            preparedStatement.setInt(3, sizeY);
            preparedStatement.executeUpdate();

            System.out.println("World saved successfully");
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to save world: " + e.getMessage());
            return false;
        }
    }

    public boolean checkWorldExists(String name) {
        try {
            String checkWorldSQL = "SELECT COUNT(*) FROM " + DatabaseConnection.WORLD_TABLE +
                    " WHERE " + DatabaseConnection.WORLD_COLUMN_ID + " = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(checkWorldSQL);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            int count = resultSet.getInt(1);

            return count > 0;
        } catch (SQLException e) {
            System.err.println("Failed to check world: " + e.getMessage());
            return false;
        }
    }

//    public void deleteWorld(String name) {
//        try {
//            String deleteWorldSQL = "DELETE FROM " + DatabaseConnection.WORLD_TABLE +
//                    " WHERE " + DatabaseConnection.WORLD_COLUMN_ID + " = ?";
//            PreparedStatement preparedStatement = connection.prepareStatement(deleteWorldSQL);
//            preparedStatement.setString(1, name);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            System.err.println("Failed to delete world: " + e.getMessage());
//        }
//    }
}
