package server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import server.commands.Command;
import server.json.JsonHandler;
import server.response.BasicResponse;
import server.response.ErrorResponse;
import server.response.LaunchResponse;
import server.response.Response;
import server.world.IWorld;
import server.world.Robot;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHanders = new ArrayList<>();
    public static ArrayList<Robot> robots = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader; 
    private BufferedWriter bufferedWriter; 
    private String robotName; 
    private IWorld world;
    private Robot robot;
    private String currentCommand;

    public ClientHandler(Socket socket, IWorld world) {
        try {
            this.socket = socket;
            // used to write data to client.
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter((socket.getOutputStream())));
            // used to read data from client.
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
//            this.robotName = bufferedReader.readLine();
            this.world = world;
            clientHanders.add(this);
            // handle initial connect request from server.
            handleRequest(this.bufferedReader.readLine());

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public Socket getSocket() {
        return this.socket;
    }

    public BufferedReader getBufferedReader() {
        return this.bufferedReader;
    }
    public BufferedWriter getBufferedWriter() {
        return this.bufferedWriter;
    }
    public String getName() {
        return this.robotName;
    }
    public IWorld getWorld() {
        return this.world;
    }


    @Override
    public void run() {
        String requestFromCleint;
        while (socket.isConnected()) {
            try {
                requestFromCleint = bufferedReader.readLine();
                System.out.println("req frm client: " + requestFromCleint);
                handleRequest(requestFromCleint);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
        // if client disconnects unexpecedly.
        closeEverything(socket, bufferedReader, bufferedWriter);
        removeClientHandler();
        removeRobot(robot);
    }

    public void handleRequest(String request) {
        try {
            Command newCommand = Command.create(request);
            Response response = newCommand.execute(this);

            String responseJsonString = JsonHandler.serializeResponse(response);
            sendToClient(responseJsonString);

            // if command is 'quit' disconnect everything.
            if (currentCommand.equals("quit")) {
                closeEverything(getSocket(), getBufferedReader(), getBufferedWriter());
            }

        }
        catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse("Unsupported command");
            String responseJsonString = JsonHandler.serializeResponse(errorResponse);
            sendToClient(responseJsonString);
        }
    }

    public Response createResponse(String command) {
        switch (command) {
            case "connect":
                return new BasicResponse("Connected to server");
            case "launch":
                return new LaunchResponse(robot.getData(), robot.getState());
            default:
                throw new IllegalStateException("Unexpected value: " + command);
        }
    }

    public Response createErrorResponse(String command) {
        switch (command) {
            case "connect":
                return new ErrorResponse("Failed to connect");
            case "launch":
                return new ErrorResponse("Too many of you in this world");
            default:
                return new ErrorResponse("An error occurred");
        }
    }
    
    public static void addRobotToWorld(Robot robot) {
        robots.add(robot);
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public Robot getRobot() { return robot; }

    public void setCurrentCommand(String command) { this.currentCommand = command; }

     public void sendToClient(String message){
        try {
            this.bufferedWriter.write(message);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
     }

    public void removeClientHandler() {
        clientHanders.remove(this);
    }

    public void removeRobot(Robot robot) {
        robots.remove(robot);
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
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
