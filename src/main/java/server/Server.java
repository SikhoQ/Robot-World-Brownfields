package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import database.DatabaseConnection;
import server.configuration.Config;
import server.world.World;
import server.configuration.ConfigurationManager;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class Server {

    @Option(names = {"-p", "--port"}, description = "Port number to listen on (0 - 9999). 5000 by default.")
    private int port = ConfigurationManager.getPort();

    @Option(names = {"-s", "--size"}, description = "Size of world, i.e. one side (0 - 9999). 1 by default.")
    private int worldSize = ConfigurationManager.getWorldSize();

    @Option(names = {"-o", "--obstacles"}, description = "Obstacle position x,y (top-right corner). none by default.")
    private String obstacles = ConfigurationManager.getObstacles();

    private ServerSocket serverSocket;
    private World world;

    public Server() {
    }

    public void configureServer(String[] args) {
        CommandLine.populateCommand(this, args);
        // You can parse and apply the configuration here
        // Example:
        int width = worldSize;
        int height = worldSize;

        ConfigurationManager.setWorldSize(worldSize);
        ConfigurationManager.setXConstraint(width);
        ConfigurationManager.setYConstraint(height);
        ConfigurationManager.setPort(port);
        ConfigurationManager.setObstacles(obstacles);
    }

    public void startServer() {
        try {
            System.out.println("SERVER <" + InetAddress.getLocalHost().getHostAddress() + "> " + ": Listening on port " + port + "...");
            ServerHandler serverCommands = new ServerHandler(this.world);
            Thread serverThread = new Thread(serverCommands);
            serverThread.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new client <" + socket.getInetAddress().getHostName() + "> has connected!");
                ClientHandler clientHandler = new ClientHandler(socket, this.world);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public int getPort() {
        return port;
    }

    public World getWorld() {
        return this.world;
    }

    public static void main(String[] args) throws IOException {
        // initialize database connection
        System.out.println("Connecting to database...");
        try {
            DatabaseConnection.initializeDatabase();
            System.out.println("Connected to database.");
        } catch (RuntimeException e) {
            System.out.println("Error while connecting to database");
            System.exit(1);
        }

        Server server = new Server();
        server.configureServer(args);
        ServerSocket serverSocket = new ServerSocket(ConfigurationManager.getPort());
        server.serverSocket = serverSocket;

        System.out.println("* Creating World.. [size: " + ConfigurationManager.getWorldSize() + " x " + ConfigurationManager.getWorldSize() + ", obstacles: (" + ConfigurationManager.getObstacles() + "), visibility: " + ConfigurationManager.getVisibility() + "]");
        server.world = new World(); // Initialize world with configurations
        server.startServer();
    }
}
