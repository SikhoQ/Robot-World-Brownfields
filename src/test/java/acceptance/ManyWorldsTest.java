package acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ManyWorldsTest {

    private Connection connection;

    @BeforeEach
    void setup() throws SQLException {
        // Establish connection to the database
        connection = DriverManager.getConnection(DatabaseConnection.getUrl());

        // Clear the table before each test to avoid conflicts
        connection.createStatement().execute("DELETE FROM " + DatabaseConnection.TABLE_WORLD);
    }

    @Test
    void testSaveWorldWithUniqueName() {
        String worldName = "UniqueWorld";
        int sizeX = 100;
        int sizeY = 100;

        // Simulate the SAVE command
        boolean isSaved = saveWorld(worldName, sizeX, sizeY);

        // Assert that the world was saved successfully
        assertTrue(isSaved, "The world should be saved successfully");

        // Verify the world was actually saved in the database
        boolean worldExists = checkWorldExists(worldName);
        assertTrue(worldExists, "The world should exist in the database");

        // Clean up
        deleteWorld(worldName);
    }

    private boolean saveWorld(String name, int sizeX, int sizeY) {
        try {
            String insertWorldSQL = "INSERT INTO " + DatabaseConnection.TABLE_WORLD + " (" +
                    DatabaseConnection.COLUMN_ID + ", " +
                    DatabaseConnection.COLUMN_SIZE_X + ", " +
                    DatabaseConnection.COLUMN_SIZE_Y + ") VALUES (?, ?, ?)";

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

    private boolean checkWorldExists(String name) {
        try {
            String checkWorldSQL = "SELECT COUNT(*) FROM " + DatabaseConnection.TABLE_WORLD +
                    " WHERE " + DatabaseConnection.COLUMN_ID + " = ?";
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

    private void deleteWorld(String name) {
        try {
            String deleteWorldSQL = "DELETE FROM " + DatabaseConnection.TABLE_WORLD +
                    " WHERE " + DatabaseConnection.COLUMN_ID + " = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteWorldSQL);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to delete world: " + e.getMessage());
        }
    }
}
