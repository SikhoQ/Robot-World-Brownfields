package database;

import java.sql.*;

public class DatabaseConnection {

    public static final String DB_NAME = "RobotWorlds.db";
    private static final String URL = "jdbc:sqlite:RobotWorlds.db";
    public static final String TABLE_CONTACTS = "world";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SIZE_X = "size_x";
    public static final String COLUMN_SIZE_Y= "size_y";


    public static void main(String[] args) {

        try (Connection conn = DriverManager.getConnection(URL)) {
            // Load the SQLite JDBC driver (make sure the driver is in your classpath)
            DriverManager.registerDriver(new org.sqlite.JDBC());

            // Establish the connection
            Statement statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS +
                    " (" + COLUMN_ID + " integer, " +
                    COLUMN_SIZE_X + " integer, " +
                    COLUMN_SIZE_Y+ " integer" +
                    ")");

            statement.execute("SELECT * FROM world");
            ResultSet results = statement.getResultSet();
            while(results.next()) {
                System.out.println(results.getString("id") + " " +
                        results.getInt("size_x") + " " +
                        results.getString("size_y"));
            }

            results.close();

            statement.close();

        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }
}





















