package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import server.world.World;


public class Server {

    private ServerSocket serverSocket; 
    private World world;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.world = new World();
    }

    public void startServer() {
        try {
            System.out.println("SERVER <" + InetAddress.getLocalHost().getHostAddress()  + "> " + ": Listening on port 8147..." );
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
                thread.start();
            }
        } 
        catch (IOException e) {
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

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(World.getWorldConfiguration().getPort());
        Server server =  new Server(serverSocket);
        server.startServer();
    }
}