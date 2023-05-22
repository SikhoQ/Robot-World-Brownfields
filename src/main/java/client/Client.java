package client;

import client.commands.Command;
import client.commands.LaunchCommand;
import client.commands.QuitCommand;
import client.commands.ReloadCommand;
import client.commands.RepairCommand;
import client.request.Request;
import client.json.JsonHandler;
import client.robots.Fighter;
import client.robots.Robot;
import client.robots.Sniper;
import client.robots.Venom;
import client.robots.util.State;
import client.userInterface.text.TextInterface;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private Robot robot;
    private boolean paused;
    private static Command currentCommand;

    private OutputStream outputStream;
    private InputStream inputStream;

    private TextInterface textInterface;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.outputStream = socket.getOutputStream();
            this.inputStream = socket.getInputStream();
            this.paused = false;
            this.textInterface = new TextInterface(this);
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

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public void sendMessage() {
        try {
            connectClientToServer();

            while (socket.isConnected()) {
                String userInput = textInterface.getUserInput();
                handleUserInput(userInput);
            }
        } catch (IOException e) {
            closeEverything(socket, inputStream, outputStream);
        }
    }

    public void handleUserInput(String userInput) throws IOException {
        // create a command that when executed will return a request.
        if (!paused) {
            try {
                if (userInput.equalsIgnoreCase("help")) {
                    Command.help();
                    return;
                }
                else if (userInput.equalsIgnoreCase("clear")) {
                    Command.currentCommand = "clear";
                    Command.clear();
                    textInterface.output("terminal has been cleared.");
                    return;
                }
                else{
                    currentCommand = Command.create(userInput);
                }
            } catch (IllegalArgumentException e) {
                Command.currentCommand = "error";
                textInterface.output(e.getMessage());
                return;
            }
        }
        else { // repairing or reloading
            return;
        }

        // if the command is launch
        if (currentCommand instanceof LaunchCommand) {
            String result = instantiateRobot(userInput);
            if (result.equals("already launched")) {
                return;
            }
        }

        // if the command is repair or reload
        if (currentCommand instanceof RepairCommand || currentCommand instanceof ReloadCommand) {
            paused = true;
        }

        // user should not be able to do anything but 'quit' if robot is not launched.
        if (robot != null || currentCommand instanceof QuitCommand) {
            Request request = currentCommand.execute(robot);
            String requestJsonString = JsonHandler.serializeRequest(request);
            sendToServer(requestJsonString);
        }
        // robot has not been launched, and command is not launch or quit.
        else if (!(currentCommand instanceof QuitCommand)) {
            textInterface.output("Please launch a robot into the world first.");
        }
    }

    public String instantiateRobot(String userInput) {
        if (robot == null) { // user hasn't launched robot yet.
            String[] args = userInput.toLowerCase().trim().split(" ");
            switch (args[1]) {
                case "venom":
                    robot = new Venom(args[2]);
                    break;
                case "fighter":
                    robot = new Fighter(args[2]);
                default:
                    robot = new Sniper(args[2]);
            }
            return "continue launching";
        } else {
            // robot has already been launched so break out of this method.4
            textInterface.output("you have already launched a robot into the world.");
            return "already launched";
        }
    }

    public void sendToServer(String requestJsonString) throws IOException {
        this.outputStream.write(requestJsonString.getBytes());
    }

    public void connectClientToServer() throws IOException {
        Request connectionRequest = new Request(String.valueOf(socket.getInetAddress()), "connect", new String[] {});
        String connectionRequestString = JsonHandler.serializeRequest(connectionRequest);
        sendToServer(connectionRequestString);
        Command.currentCommand = "connect";
    }

    public void listenFormessage() {
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

        // handle response server sends after initial 'connect' request.
        if (Command.currentCommand.equals("connect")) {
            textInterface.output(response);
            return;
        }

        // handle response sender sends when you quit or server shuts down.
        if (currentCommand instanceof QuitCommand || msgStr.contains("disconnected.")) {
            String outputStr = currentCommand instanceof QuitCommand ? "Shutting down..." : msgStr;
            textInterface.output(outputStr);
            closeEverything(getSocket(), inputStream, outputStream);
            System.exit(0);
        }

        // handle response server sends when you've been shot.
        if (responseJson.get("result").asText().equals("OK") && 
            msgStr.equals("You've been shot.")) {
                
            robot.setState(JsonHandler.updateState(responseJson));
            if (robot.getShields() < 0) { 
                System.out.println("You are dead!!! GoodBye.");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.exit(0); 
            }else{
                textInterface.output(response);
            }
            return;
        }

        // A new robot has been launched into the worlds
        if (robot != null && msgStr != null && msgStr.contains("new robot")) {
            String enemyName = responseJson.get("data").get("robotName").asText();
            String enemyKind = responseJson.get("data").get("robotKind").asText();
            State enemyState = JsonHandler.getState(responseJson.get("data").get("robotState"));

            Robot enemyRobot = new Robot(enemyName, enemyKind, enemyState);
            robot.addEnemy(enemyRobot);
            robot.addEnemyName(enemyName);
            return;
        }

        // All the robots currently in world. Server sends this after you launch.
        if (robot != null && msgStr != null && msgStr.equals("robots currently in world")) {
            JsonNode robotsNode = responseJson.get("data").get("robots");

            if (robot != null &&  robotsNode != null) {
                for (int i=0; i < robotsNode.size(); i++) {

                    String enemyName = robotsNode.get(i).get("robotName").asText();
                    String enemyKind = robotsNode.get(i).get("robotKind").asText();
                    State enemyState = JsonHandler.getState(robotsNode.get(i).get("robotState"));
        
                    Robot enemyRobot = new Robot(enemyName, enemyKind, enemyState);
                    robot.addEnemy(enemyRobot);
                    robot.addEnemyName(enemyName);
                }
            }
            return;
        }

        // handle response server sends when another player quits. (should also do this if that player dies)
        if (robot != null && msgStr != null && msgStr.equals("remove enemy")) {
            robot.removeEnemy(responseJson.get("data").get("robotName").asText());
            return;
        }

        // handle response server sends when an enemy's state changes.
        if (robot != null && msgStr != null && msgStr.equals("enemy state changed")){
            String enemyName =  responseJson.get("data").get("robotName").asText();
            State enemyState = JsonHandler.getState(responseJson.get("data").get("robotState"));
            robot.updateEnemyState(enemyName, enemyState);
            return;
        }
        
        // handle response server sends when an enemy fires a gun.
        if (robot != null && msgStr != null && msgStr.contains("enemy fired gun")) {
            String enemyName =  responseJson.get("data").get("robotName").asText();
            int bulletDistance =  responseJson.get("data").get("distance").asInt();
            textInterface.getGui().getEnemyPlayer(enemyName).fire(bulletDistance);
            return;
        }

        // handle the rest of the responses.
        if (responseJson.get("result").asText().equals("OK") && currentCommand != null) {
            switch (currentCommand.getName()) {
                case "launch":
                    robot.setState(JsonHandler.updateState(responseJson));
                    Robot.setReload(responseJson.get("data").get("reload").asInt());
                    Robot.setRepair(responseJson.get("data").get("repair").asInt());
                    Robot.setVisibility(responseJson.get("data").get("visibility").asInt());
                    textInterface.output(response);
                    break;
                case "forward":
                case "back":
                case "turn":
                case "fire":
                    robot.setState(JsonHandler.updateState(responseJson));
                    textInterface.output(response);
                    break;
                case "repair":
                    System.out.println("Repairing...");
                    try {
                        Thread.sleep(Robot.getRepair() * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.setState(JsonHandler.updateState(responseJson));
                    textInterface.output(response);
                    paused = false;
                    break;
                case "reload":
                    System.out.println("Reloading...");
                    try {
                        Thread.sleep(Robot.getReload()* 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.setState(JsonHandler.updateState(responseJson));
                    textInterface.output(response);
                    paused = false;
                    break;

                default:
                    textInterface.output(response);
            }
        } else {
            // handle an error response for launch. set robot variable back to null.
            if (currentCommand instanceof LaunchCommand) {
                this.robot = null;
            }
            Command.currentCommand = "error";
            textInterface.output(msgStr);
            return;
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
        Socket socket = args.length == 2?  
            new Socket(args[0], Integer.parseInt(args[1])) : new Socket("localhost", 8147);
        Client client = new Client(socket);
        client.listenFormessage();
        client.sendMessage();
    }
}
