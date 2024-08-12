package database;

import java.sql.*;

/**
 * The DatabaseConnection class provides methods to connect to an SQLite database,
 * create a table if it does not exist, and retrieve data from the table.
 */
public class DatabaseConnection {

    public static final String DB_NAME = "RobotWorlds.db";
    private static final String URL = "jdbc:sqlite:RobotWorlds.db";
    public static final String TABLE_WORLD = "world";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SIZE_X = "size_x";
    public static final String COLUMN_SIZE_Y = "size_y";

    public static String getUrl(){
        return URL;
    }
      /**
       * The main method establishes a connection to the SQLite database, creates the
      'world' table if it does not exist, and retrieves and prints data from the table.
       */
    public static void main(String[] args) {

        try (Connection conn = DriverManager.getConnection(URL)) {

            // Load the SQLite JDBC driver
            DriverManager.registerDriver(new org.sqlite.JDBC());

            // Establish the connection
            Statement statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_WORLD +
                    " (" + COLUMN_ID + " integer, " +
                    COLUMN_SIZE_X + " integer, " +
                    COLUMN_SIZE_Y + " integer" +
                    ")");

            //  prints data from the table.
            statement.execute("SELECT * FROM " + TABLE_WORLD);
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                System.out.println(results.getString(COLUMN_ID) + " " +
                        results.getInt(COLUMN_SIZE_X) + " " +
                        results.getString(COLUMN_SIZE_Y));
            }

            results.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
