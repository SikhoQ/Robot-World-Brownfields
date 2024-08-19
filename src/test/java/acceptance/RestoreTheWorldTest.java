package acceptance;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;

/**
 *
 * @author sqlitetutorial.net
 */
public class RestoreTheWorldTest {
    /**
     * Connection to RobotWorlds database
     */
    Connection conn = null;
    String url = "jdbc:sqlite:RobotWorlds.db";

    @Test
    public void connectionTest() {
//        Connection conn = null;
        try {
            // db parameters
//            String url = "jdbc:sqlite:RobotWorlds.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * check if table exists and that it has records
     */

    @Test
    public void ExistingTableTest() {
//        check if world table exists
        try (final Statement stmt = conn.createStatement()) {
            conn = DriverManager.getConnection(url);
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