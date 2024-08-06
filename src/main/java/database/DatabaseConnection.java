package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:RobotWorlds.db";

    public static void main(String[] args) {

        try (Connection conn = DriverManager.getConnection(URL)) {
            // Load the SQLite JDBC driver (make sure the driver is in your classpath)
            DriverManager.registerDriver(new org.sqlite.JDBC());
//            Class.forName("org.sqlite.JDBC");

            // Establish the connection

            Statement statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS world (id INTEGER PRIMARY KEY, size_x INTEGER, size_y INTEGER)");
            statement.execute("INSERT INTO world(id,size_x,size_y) VALUES(16,0,0)");
            statement.execute("SELECT * FROM world");

            statement.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}