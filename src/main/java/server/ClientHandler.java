package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import server.commands.Command;
import server.json.JsonHandler;
import server.response.ErrorResponse;
import server.response.Response;
import server.world.Robot;
import server.world.World;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHanders = new ArrayList<>();
    // public static ArrayList<Robot> robots = new ArrayList<>();
    private Socket socket;
    private String robotName;
    private World world;
    private Robot robot;
    private String currentCommand;

    // use OutputStream & InputStream instead of bufferedReader & bufferedWriter
    private OutputStream outputStream;
    private InputStream inputStream;

    public ClientHandler(Socket socket, World world) {
        try {
            this.socket = socket;
            this.world = world;
            clientHanders.add(this);

            this.outputStream = socket.getOutputStream();
            this.inputStream = socket.getInputStream();

            handleRequest(getRequestFromClient()); // initial connect request (might remove this, for compatibility reasons, no connect req, just connect).
        } 
        catch (IOException e) {
            closeEverything(socket, inputStream, outputStream);
        }
    }
    
    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public String getName() {
        return this.robotName;
    }

    public World getWorld() {
        return this.world;
    }

    @Override
    public void run() {
        String requestFromCleint;
        while (socket.isConnected()) {
            try {
                // get request from client.
                requestFromCleint = getRequestFromClient();
                // System.out.println("req from client: " + requestFromCleint);
                if (JsonHandler.isJsonString(requestFromCleint)) {
                    handleRequest(requestFromCleint); 
                }
            } catch (IOException e) {
                closeEverything(socket, inputStream, outputStream);
                break;
            }
        }
        // if client disconnects unexpecedly.
        closeEverything(socket, inputStream, outputStream);
        removeClientHandler();
        world.removeRobot(robot);
    }

    public String getRequestFromClient() throws IOException{
        byte[] buffer = new byte[1024];
        int bytesRead = inputStream.read(buffer);
        // No data was read, so return an empty string
        if (bytesRead == -1) { return ""; }
        String request = new String(buffer, 0, bytesRead);
        return request;
    }

    public void handleRequest(String request) {
        try {
            Command newCommand = Command.create(request);
            Response response = newCommand.execute(this);

            String responseJsonString = JsonHandler.serializeResponse(response);
            sendToClient(responseJsonString);

            // if command is 'quit' disconnect everything.
            if (currentCommand.equals("quit")) {
                closeEverything(getSocket(), inputStream, outputStream);
            }

        } 
        catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse("Unsupported command");
            String responseJsonString = JsonHandler.serializeResponse(errorResponse);
            sendToClient(responseJsonString);
        }
        catch (NullPointerException e) {}
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setCurrentCommand(String command) {
        this.currentCommand = command;
    }

    public void sendToClient(String message) {
        try {
            this.outputStream.write(message.getBytes());
        } 
        catch (IOException e) {
            closeEverything(socket, inputStream, outputStream);
        }
    }

    public void removeClientHandler() {
        clientHanders.remove(this);
    }

    public void closeEverything(Socket socket, InputStream inputStream, OutputStream outputStream) {
        removeClientHandler();
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.robotName;
    }
}
