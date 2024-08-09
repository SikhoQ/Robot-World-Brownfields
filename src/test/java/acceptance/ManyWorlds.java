package acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//src/main/java/database

class ManyWorlds {
//    private DatabaseConnection databaseConnection;

    @BeforeEach
    //

    @Test
    void connectToDatabase() {
        try( final Connection connection = DriverManager.getConnection(null) ){
            System.out.println( "Connected to database " );
            // runTest( connection );
        }catch( SQLException e ){
            System.err.println( e.getMessage() );
        }

    }
}
