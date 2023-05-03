package client;

import client.commands.Command;
import client.commands.LaunchCommand;
import client.commands.QuitCommand;
import client.request.Request;
import client.json.JsonHandler;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Scanner scanner;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    // private String username;
    private Robot robot;
    private static String currentCommand;

    private String stringToOutput;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter((socket.getOutputStream()))); // write to server
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // read from server.
            this.scanner = new Scanner(System.in);
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    public void sendMessage() {
        try {
            connectClientToServer();

            while (socket.isConnected()) {
                String userInput = scanner.nextLine();
                handleUserInput(userInput);
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void handleUserInput(String userInput) throws IOException {
        // create a command that when executed will return a request.
        // System.out.println(userInput);
        Command newCommand;
        try {
            newCommand = Command.create(userInput);
            System.out.println("command: " + newCommand);
        } catch (IllegalArgumentException e) { 
            // if illegal command, tell user and break out of method.
            System.out.println(e.getMessage());
            return;
        }
    
        
        // if the command is launch
        if (newCommand instanceof LaunchCommand) {
            if (robot == null) { // user hasn't launched robot yet.
                String[] args = userInput.split(" ");
                // System.out.println(args);
                robot = new Robot(args[2]);
            }
            else {
                // robot has already been launched so break out of this method.4
                System.out.println(robot + " has already been launched into the world.");
                return;
            }
        }

        if (robot != null || newCommand instanceof QuitCommand) {
            Request request = newCommand.execute(robot);
            // System.out.println("req obj: " + request);
            String requestJsonString = JsonHandler.serializeRequest(request);
            // System.out.println("req: " +requestJsonString);
            writeToBuffer(requestJsonString);
        }
        // robot has not been launched, and command is not launch or quit.
        else if (!(newCommand instanceof QuitCommand)) {
            System.out.println("Robot must be initialized first with the launch command.");
        }
    }

    public void writeToBuffer(String requestJsonString) throws IOException{
        bufferedWriter.write(requestJsonString);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public void connectClientToServer() throws IOException {
        // send initial request to connect to server...
        Request connectionRequest = new Request(String.valueOf(socket.getLocalSocketAddress()), "connect", new String[]{});
        String connectionRequestString = JsonHandler.serializeRequest(connectionRequest);
        writeToBuffer(connectionRequestString);
        currentCommand = "connect";
    }

    public void listenFormessage() {
        // listens for messages sent from server.
        new Thread(new Runnable() {
            @Override
            public void run() {
                String responseFromServer;

                while(socket.isConnected()) {
                    try {
                        responseFromServer = bufferedReader.readLine();
                    //    System.out.println("response: " + responseFromServer);
                        handleResponse(responseFromServer);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
            
        }).start();
    }

    public void handleResponse(String response) {
        JsonNode responseJson = JsonHandler.deserializeJsonTString(response);
        if (currentCommand == "quit") {
            System.out.println(response);
            closeEverything(getSocket(), getBufferedReader(), getBufferedWriter());
            System.exit(0);
        }
        if (responseJson.get("result").asText().equals("OK")) {
            System.out.println(response);
            output("Connected to server.");
        }
        else{
            System.out.println(response);
        }
    }

    public static void setCurrentCommand(String command) { currentCommand = command; }

    public void setRobot(Robot robot) { this.robot = robot; }

    private void output(String outputString) {
//        System.out.println(robot + "> " + outputString);
        System.out.println(robot + "> What should I do next?");
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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

    public static void main(String[] args) throws IOException {
        // change to get port number from command-line
        Socket socket = new Socket("localhost",5000);
        Client client =  new Client(socket);
        client.listenFormessage();
        client.sendMessage();
    }
}
