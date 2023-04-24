package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import server.world.IWorld;
import server.world.TextWorld;

import java.io.IOException;

public class Server {

    protected ServerSocket serverSocket; 
    protected IWorld world;


    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.world = new TextWorld();
    }

    public void startServer() {
        System.out.println("Listening on port 5000...");

        try{
            while (!serverSocket.isClosed()) {
        
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket, world);
                //create client's thread
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

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        Server server =  new Server(serverSocket);
        server.startServer();
    }
}


