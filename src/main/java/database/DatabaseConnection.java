package database;

import java.sql.*;

/**
 * The DatabaseConnection class provides methods to connect to an SQLite database,
 * create a table if it does not exist, and retrieve data from the table.
 */
public class DatabaseConnection {

    public static final String DB_NAME = "RobotWorlds.db";
    private static final String URL = "jdbc:sqlite:RobotWorlds.db";
    public static final String WORLD_TABLE = "world";
    public static final String OBJECTS_TABLE = "objects";

    public static final String WORLD_COLUMN_ID = "id";
    public static final String WORLD_COLUMN_SIZE = "size";

    public static final String OBJECTS_COLUMN_ID = "id";
    public static final String OBJECTS_COLUMN_TYPE = "type";
    public static final String OBJECTS_COLUMN_X = "x";
    public static final String OBJECTS_COLUMN_Y = "y";
    public static final String OBJECTS_COLUMN_SIZE = "size";

    public static String getUrl(){
        return URL;
    }

    public static boolean worldExists(String worldName) {
        String query = "SELECT COUNT(*) FROM " + WORLD_TABLE + " WHERE " + WORLD_COLUMN_ID + " = ?";
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

        String query = "INSERT INTO " + WORLD_TABLE + " (" + WORLD_COLUMN_ID + ", data) VALUES (?, ?)";
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
        String query = "SELECT data FROM " + WORLD_TABLE + " WHERE " + WORLD_COLUMN_ID + " = ?";
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
     * 'world' table if it does not exist, and retrieves and prints data from the table.
     */
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL)) {

            // Load the SQLite JDBC driver
            DriverManager.registerDriver(new org.sqlite.JDBC());

            // Establish the connection
            Statement statement = conn.createStatement();

            // Create the 'world' table if it does not exist.
            statement.execute("CREATE TABLE IF NOT EXISTS " + WORLD_TABLE +
                    " (" + WORLD_COLUMN_ID + " TEXT PRIMARY KEY, " +
                    WORLD_COLUMN_SIZE + " INT NOT NULL" +
                    ")");

            // Create the 'objects' table if it does not exist.
            statement.execute("CREATE TABLE IF NOT EXISTS " + OBJECTS_TABLE +
                    " (" + OBJECTS_COLUMN_ID + " INT PRIMARY KEY, " +
                    OBJECTS_COLUMN_TYPE + " TEXT NOT NULL, " +
                    OBJECTS_COLUMN_X + " INT NOT NULL, " +
                    OBJECTS_COLUMN_Y + " INT NOT NULL, " +
                    OBJECTS_COLUMN_SIZE + " INT NOT NULL" +
                    ")");

            // Retrieve and check data from the 'world' table.
            ResultSet world_results = statement.executeQuery("SELECT * FROM " + WORLD_TABLE);
            if (!world_results.next()) {
                System.out.println("No data found in the 'world' table.");
            } else {
                do {
                    System.out.println("World ID: " + world_results.getString(WORLD_COLUMN_ID) + " " +
                            "Size: " + world_results.getInt(WORLD_COLUMN_SIZE));
                } while (world_results.next());
            }
            world_results.close();

            // Retrieve and check data from the 'objects' table.
            ResultSet objects_results = statement.executeQuery("SELECT * FROM " + OBJECTS_TABLE);
            if (!objects_results.next()) {
                System.out.println("No data found in the 'objects' table.");
            } else {
                do {
                    System.out.println("Object ID: " + objects_results.getInt(OBJECTS_COLUMN_ID) + " " +
                            "Type: " + objects_results.getString(OBJECTS_COLUMN_TYPE) + " " +
                            "X: " + objects_results.getInt(OBJECTS_COLUMN_X) + " " +
                            "Y: " + objects_results.getInt(OBJECTS_COLUMN_Y) + " " +
                            "Size: " + objects_results.getInt(OBJECTS_COLUMN_SIZE));
                } while (objects_results.next());
            }
            objects_results.close();

            // Clean up
            statement.close();

        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
