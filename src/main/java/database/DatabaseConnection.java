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
    public static final String OBSTACLE = "obstacle";
    public static final String OBSTACLE_TYPE = "obstacle_type";

    public static String getUrl(){
        return URL;
    }

    public static boolean worldExists(String worldName) {
        String query = "SELECT COUNT(*) FROM " + TABLE_WORLD + " WHERE " + COLUMN_ID + " = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, worldName);
            ResultSet rs = stmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveWorld(String worldName, String worldData) {
        if (worldExists(worldName)) {
            System.out.println("World with the name '" + worldName + "' already exists.");
            return false;
        }

        String query = "INSERT INTO " + TABLE_WORLD + " (" + COLUMN_ID + ", data) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, worldName);
            stmt.setString(2, worldData);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String restoreWorld(String worldName) {
        String query = "SELECT data FROM " + TABLE_WORLD + " WHERE " + COLUMN_ID + " = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, worldName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("data");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
