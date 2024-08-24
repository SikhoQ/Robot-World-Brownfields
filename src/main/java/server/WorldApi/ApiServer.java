package server.WorldApi;


import database.DatabaseConnection;
import io.javalin.Javalin;

public class ApiServer {
    private final Javalin apiServer;
    private final ApiHandler apiHandler;

    public ApiServer() {
        this.apiServer = Javalin.create();
        this.apiHandler = new ApiHandler();

        // Register API endpoints
        this.apiServer.get("/loadWorld", apiHandler::loadWorld);
        this.apiServer.post("/saveWorld", apiHandler::saveWorld);
        this.apiServer.delete("/removeWorld", apiHandler::removeWorld);
    }

    public Javalin start() {
        return this.apiServer.start(7000);
    }

    public Javalin stop() {
        return this.apiServer.stop();
    }

    public static void main(String[] args) {
        ApiServer apiServer = new ApiServer();
        apiServer.start();
        DatabaseConnection.initializeDatabase();
    }
}


