package acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class ManyWorlds {

    @BeforeEach
    void setup() {
        // Any setup needed before each test
    }

    @Test
    void connectToDatabase() {
        try {
            // Use the getter method to retrieve the URL
            final Connection connection = DriverManager.getConnection(DatabaseConnection.getUrl());

            System.out.println("Connected to database");
            // Optionally, run some test queries
            // runTest(connection);

            connection.close();  // Ensure the connection is closed
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
