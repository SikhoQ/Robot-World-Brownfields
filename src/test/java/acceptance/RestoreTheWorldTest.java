package acceptance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.TestClassOrder;


class RestoreTheWorldTest {

    // check if database connection exists
    @Test
    void connectToDatabase() {
        try( final Connection connection = DriverManager.getConnection(null) ){
            System.out.println( "Connected to database " );
            // runTest( connection );
        }catch( SQLException e ){
            System.err.println( e.getMessage() );
        }

    }


    // Given that I have a world saved in a database already
    // Check if there is a world saved




    // When I send a restore command to the server


    // Then the world should be restored to the initial state(default)
    // Update the database with the default world state

}
