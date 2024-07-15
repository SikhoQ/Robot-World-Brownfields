package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import server.world.World;

/**
 * The Server class represents a server that listens for client connections and manages client handlers.
 * It starts the server, accepts client connections, and creates a separate client handler thread for each client.
 */
public class Server {

    /**
     * Constructs a new Server object.
     *
     * @param serverSocket the server socket
     */
    private ServerSocket serverSocket; 
    private World world;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.world = new World();
    }

    /**
     * Starts the server.
     * Prints the server IP address and port, and starts a separate thread for server commands.
     * Accepts client connections, creates a client handler for each client, and starts a separate thread for each client handler.
     */
    public void startServer() {
        try {
            System.out.println("SERVER <" + InetAddress.getLocalHost().getHostAddress()  + "> " + ": Listening on port 5000..." );
            ServerHandler serverCommands = new ServerHandler(world); // listens for input on server and handles executing server command on a seperate thread.
            Thread serverThread = new Thread(serverCommands);
            serverThread.start();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try{
            while (!serverSocket.isClosed()) {
        
                Socket socket = serverSocket.accept(); // accept a client
                System.out.println("A new client" + " <" + socket.getInetAddress().getHostName() + "> " + "has connected!");
                ClientHandler clientHandler = new ClientHandler(socket, world); // create a clientHandler & sprouts a sperate thread for communicating with client.
                Thread thread = new Thread(clientHandler);
                System.out.println("Dodo");
                thread.start();
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the server socket.
     */
    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main method to run the server.
     * Creates a server socket, creates a Server instance, and starts the server.
     *
     * @param args command line arguments (not used)
     * @throws IOException if an I/O error occurs while creating the server socket
     */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(World.getWorldConfiguration().getPort());
        Server server =  new Server(serverSocket);
        server.startServer();
    }
}