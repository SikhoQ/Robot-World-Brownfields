package client;

import client.commands.Command;
import client.commands.LaunchCommand;
import client.commands.QuitCommand;
import client.request.Request;
import client.json.JsonHandler;
import client.robots.Robot;
import client.robots.Sniper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Scanner scanner;
    private Socket socket;
    private Robot robot;
    private static Command currentCommand;

    private OutputStream outputStream;
    private InputStream inputStream;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.scanner = new Scanner(System.in);
            this.outputStream = socket.getOutputStream();
            this.inputStream = socket.getInputStream();
        } catch (IOException e) {
            closeEverything(socket, inputStream, outputStream);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void sendMessage() {
        try {
            connectClientToServer();

            while (socket.isConnected()) {
                String userInput = scanner.nextLine();
                handleUserInput(userInput);
            }
        } catch (IOException e) {
            closeEverything(socket, inputStream, outputStream);
        }
    }

    public void handleUserInput(String userInput) throws IOException {
        // create a command that when executed will return a request.
        try {
            currentCommand = Command.create(userInput);
        } catch (IllegalArgumentException e) {
            output(e.getMessage());
            return;
        }

        // if the command is launch
        if (currentCommand instanceof LaunchCommand) {
            launchRobot(userInput);
        }

        // user should not be able to do anything but 'quit' if robot is not launched.
        if (robot != null || currentCommand instanceof QuitCommand) {
            Request request = currentCommand.execute(robot);
            String requestJsonString = JsonHandler.serializeRequest(request);
            sendToServer(requestJsonString);
        }
        // robot has not been launched, and command is not launch or quit.
        else if (!(currentCommand instanceof QuitCommand)) {
            output("Please launch a robot into the world first.");
        }
    }

    public void launchRobot(String userInput) {
        if (robot == null) { // user hasn't launched robot yet.
            String[] args = userInput.toLowerCase().trim().split(" ");
            // initialize robot based on user's chosen make, if doesn't exist initialize
            // generic robot make.
            switch (args[1]) {
                case "sniper":
                    robot = new Sniper(args[2]);
                    break;
                default:
                    robot = new Robot(args[2]);
            }
        } else {
            // robot has already been launched so break out of this method.4
            output("you have already launched a robot into the world.");
            return;
        }
    }

    public void sendToServer(String requestJsonString) throws IOException {
        this.outputStream.write(requestJsonString.getBytes());
    }

    public void connectClientToServer() throws IOException {
        // send initial request to connect to server...
        Request connectionRequest = new Request(String.valueOf(socket.getInetAddress()), "connect", new String[] {});
        String connectionRequestString = JsonHandler.serializeRequest(connectionRequest);
        sendToServer(connectionRequestString);
    }

    public void listenFormessage() {
        // listens for messages sent from server.
        new Thread(new Runnable() {
            @Override
            public void run() {
                String responseFromServer;

                while (socket.isConnected()) {
                    try {
                        responseFromServer = getResponseFromServer();
                        handleResponse(responseFromServer);
                    } catch (IOException e) {
                        closeEverything(socket, inputStream, outputStream);
                    }
                }
            }

        }).start();
    }

    public String getResponseFromServer() throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = inputStream.read(buffer);
        String response = new String(buffer, 0, bytesRead);
        return response;
    }

    public void handleResponse(String response) {
        JsonNode responseJson = JsonHandler.deserializeJsonTString(response);
        JsonNode msgNode = responseJson.get("data").get("message");
        String msgStr = msgNode == null ? "" : msgNode.asText();

        if (currentCommand instanceof QuitCommand || msgStr.equals("Server has been disconnected.")) {
            String outputStr = currentCommand instanceof QuitCommand ? "Shutting down..." : msgStr;
            output(outputStr);
            closeEverything(getSocket(), inputStream, outputStream);
            System.exit(0);
        }

        if (responseJson.get("result").asText().equals("OK") && currentCommand != null) {
            switch (currentCommand.getName()) {
                case "launch":
                    robot.setState(JsonHandler.updateState(responseJson));
                    Robot.setReload(responseJson.get("data").get("reload").asInt());
                    Robot.setRepair(responseJson.get("data").get("repair").asInt());
                    Robot.setVisibility(responseJson.get("data").get("visibility").asInt());
                    output(response);
                    break;
                case "forward":
                    robot.setState(JsonHandler.updateState(responseJson));
                    output(response);
                    break;
                case "back":
                    robot.setState(JsonHandler.updateState(responseJson));
                    output(response);
                    break;
                case "left":
                    robot.setState(JsonHandler.updateState(responseJson));
                    output(response);
                    break;
                case "right":
                    robot.setState(JsonHandler.updateState(responseJson));
                    output(response);
                    break;
                case "fire":
                    robot.setState(JsonHandler.updateState(responseJson));
                    output(response);
                    break;

                default:
                    output(response);
            }
            // output(response);
        } else {
            // handle an error response for launch. set robot variable back to null.
            if (currentCommand instanceof LaunchCommand) {
                this.robot = null;
            }
            output(response);
        }
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    private void output(String outputString) {
        System.out.println(robot != null ? robot + " > " + outputString : "JHB_45" + "> " + outputString);
        if (!outputString.contains("Shutting") && !outputString.contains("disconnected")) {
            System.out.println(robot != null ? robot + "> What should I do next?" : "JHB_45> What do you want to do?");
        }
    }

    public void closeEverything(Socket socket, InputStream inputStream, OutputStream outputStream) {
        try (socket; inputStream; outputStream) {
            // resources are automatically closed when the try block completes.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // change to get port number from command-line
        // Socket socket = new Socket("localhost",8147);
        Socket socket = new Socket("20.20.10.161", 8147); // Nomah's inet address
        // Socket socket = new Socket("20.20.10.154",8147); // Mzee's inet address
        Client client = new Client(socket);
        client.listenFormessage();
        client.sendMessage();
    }
}
