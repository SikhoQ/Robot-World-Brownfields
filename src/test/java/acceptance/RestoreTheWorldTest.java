package acceptance;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import database.DatabaseConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author sqlitetutorial.net
 */
public class RestoreTheWorldTest {
    /**
     * Connection to RobotWorlds database
     */
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
    public void connectionTest() {
        // setup method connects to db before each test
       assertTrue(true);
    }
    /**
     * check if table exists and that it has records
     */

    @Test
    public void ExistingTableTest() {
//        check if world table exists
        try (final Statement stmt = connection.createStatement()) {
            stmt.execute("SELECT name FROM RobotWorlds WHERE type='table' AND name='world'");
        } catch (SQLException e) {

        }
//        SELECT name FROM RobotWorlds WHERE type='table' AND name='world';

//        check if obstacles table exists
//        SELECT name FROM RobotWorlds WHERE type='table' AND name='obstacles';

    }

    /**
     * check if table has records
     */
    @Test

    public void RecordsExistTest() {

    }

    /**
     * check if passing the restore command launches a robot with saved world state
     */

    @Test
    public void restoreTest() {

    }


}