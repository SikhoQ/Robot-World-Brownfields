
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

    /**
     * Constructs a new Client object with the specified socket.
     *
     * @param socket The socket used for communication with the server.
     */
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

    /**
     * Returns the socket used for communication with the server.
     *
     * @return The socket object.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Returns the input stream associated with the socket.
     *
     * @return The input stream.
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Returns the output stream associated with the socket.
     *
     * @return The output stream.
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    /**
     * Sends a message to the server.
     */
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
    /**
     * Handles the clear command
     */
    public void handleClear() {
        Command.currentCommand = "clear";
        Command.clear();
        textInterface.output("terminal has been cleared.");
    }
    /**
     * Handles errors
     */
    public void handleError(IllegalArgumentException e){
        Command.currentCommand = "error";
        textInterface.output(e.getMessage());
    }
    /**
     * Handles the execution
     */
    public void handleExecution() throws IOException{
        Request request = currentCommand.execute(robot);
        String requestJsonString = JsonHandler.serializeRequest(request);
        sendToServer(requestJsonString);
    }
//    public void launchCommand(String userInput){
//        String result = instantiateRobot(userInput);
//        if (result.equals("already launched")){
//            return;
//        }
//    }

    /**
     * Handles the user input received from the client by creating a command that when executed will return a request.
     * The Request object is converted a JSON string and sent to server.
     * Ignores user input if reloading or repairing...
     *
     * @param userInput The user input string.
     * @throws IOException If an I/O error occurs.
     */
    public void handleUserInput(String userInput) throws IOException {
        if (paused) {
            return;
        }
    
        try {
            processCommand(userInput);
        } catch (IllegalArgumentException e) {
            handleError(e);
        }
    }
    
    private void processCommand(String userInput) throws IOException {
        switch (userInput.toLowerCase()) {
            case "help":
                Command.help();
                return;
            case "clear":
                handleClear();
                return;
            default:
                currentCommand = Command.create(userInput);
                break;
        }
    
        if (currentCommand instanceof LaunchCommand) {
            if (instantiateRobot(userInput).equals("already launched")) {
                return;
            }
        }
    
        if (currentCommand instanceof RepairCommand || currentCommand instanceof ReloadCommand) {
            paused = true;
        }
    
        if (robot != null || currentCommand instanceof QuitCommand) {
            handleExecution();
        } else {
            textInterface.output("Please launch a robot into the world first.");
        }
    }    

    /**
     * Instantiates a robot based on the user input.
     * If user enters invalid type, Sniper is instantiated.
     * Will not instatiate if a robot has already been launched.
     *
     * @param userInput The user input string.
     */
    public String instantiateRobot(String userInput) {
        if (robot == null) { // user hasn't launched robot yet.
            String[] args = userInput.toLowerCase().trim().split(" ");
            switch (args[1]) {
                case "venom":
                    robot = new Venom(args[2]);
                    break;
                case "fighter":
                    robot = new Fighter(args[2]);
                    break;
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

    /**
     * Sends a request JSON string to the server.
     *
     * @param requestJsonString The JSON string representing the request.
     * @throws IOException If an I/O error occurs.
     */
    public void sendToServer(String requestJsonString) throws IOException {
        this.outputStream.write(requestJsonString.getBytes());
    }

    /**
     * Establishes the initial connection between the client and the server.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void connectClientToServer() throws IOException {
        Request connectionRequest = new Request(String.valueOf(socket.getInetAddress()), "connect", new String[] {});
        String connectionRequestString = JsonHandler.serializeRequest(connectionRequest);
        sendToServer(connectionRequestString);
        Command.currentCommand = "connect";
    }

    /**
     * Listens for messages sent from the server in a separate thread.
     */
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

    /**
     * Retrieves the response from the server.
     *
     * @return The response string from the server.
     * @throws IOException If an I/O error occurs.
     */
    public String getResponseFromServer() throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = inputStream.read(buffer);
        String response = new String(buffer, 0, bytesRead);
        return response;
    }

    /**
     * Handles the response received from the server.
     *
     * @param response The response string from the server.
     */
    public void handleResponse(String response) {
        JsonNode responseJson = JsonHandler.deserializeJsonTString(response);
        JsonNode msgNode = responseJson.get("data").get("message");
        String msgStr = msgNode == null ? "" : msgNode.asText();

        if (handleConnectResponse(response, msgStr)) return;
        if (handleQuitResponse(response, msgStr)) return;
        if (handleShotResponse(responseJson, msgStr)) return;
        if (handleNewRobotResponse(responseJson, msgStr)) return;
        if (handleRobotsInWorldResponse(responseJson, msgStr)) return;
        if (handleRemoveEnemyResponse(responseJson, msgStr)) return;
        if (handleEnemyStateChangedResponse(responseJson, msgStr)) return;
        if (handleEnemyFiredGunResponse(responseJson, msgStr)) return;
        handleDefaultResponse(responseJson, response, msgStr);
    }

    private boolean handleConnectResponse(String response, String msgStr) {
        if (Command.currentCommand.equals("connect")) {
            textInterface.output(response);
            return true;
        }
        return false;
    }

    private boolean handleQuitResponse(String response, String msgStr) {
        if (currentCommand instanceof QuitCommand || msgStr.contains("disconnected.")) {
            String outputStr = currentCommand instanceof QuitCommand ? "Shutting down..." : msgStr;
            textInterface.output(outputStr);
            closeEverything(getSocket(), inputStream, outputStream);
            System.exit(0);
            return true;
        }
        return false;
    }

    private boolean handleShotResponse(JsonNode responseJson, String msgStr) {
        if (responseJson.get("result").asText().equals("OK") && msgStr.equals("You've been shot.")) {
            robot.setState(JsonHandler.updateState(responseJson));
            if (robot.getShields() < 0) {
                System.out.println("You are dead!!! GoodBye.");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            } else {
                textInterface.output(responseJson.toString());
            }
            return true;
        }
        return false;
    }

    private boolean handleNewRobotResponse(JsonNode responseJson, String msgStr) {
        if (isRobotAndMessageValid(msgStr)) {
            processNewRobot(responseJson);
            return true;
        }
        return false;
    }
    
    private boolean isRobotAndMessageValid(String msgStr) {
        return robot != null && msgStr != null && msgStr.contains("new robot");
    }
    
    private void processNewRobot(JsonNode responseJson) {
        String enemyName = responseJson.get("data").get("robotName").asText();
        String enemyKind = responseJson.get("data").get("robotKind").asText();
        State enemyState = JsonHandler.getState(responseJson.get("data").get("robotState"));
    
        Robot enemyRobot = new Robot(enemyName, enemyKind, enemyState);
        robot.addEnemy(enemyRobot);
        robot.addEnemyName(enemyName);
    }
    

    private boolean handleRobotsInWorldResponse(JsonNode responseJson, String msgStr) {
    if (isRobotsInWorldResponseValid(msgStr)) {
        processRobotsInWorld(responseJson);
        return true;
    }
    return false;
}

private boolean isRobotsInWorldResponseValid(String msgStr) {
    return robot != null && msgStr != null && msgStr.equals("robots currently in world");
}

private void processRobotsInWorld(JsonNode responseJson) {
    JsonNode robotsNode = responseJson.get("data").get("robots");
    if (robotsNode != null) {
        for (int i = 0; i < robotsNode.size(); i++) {
            String enemyName = robotsNode.get(i).get("robotName").asText();
            String enemyKind = robotsNode.get(i).get("robotKind").asText();
            State enemyState = JsonHandler.getState(robotsNode.get(i).get("robotState"));

            Robot enemyRobot = new Robot(enemyName, enemyKind, enemyState);
            robot.addEnemy(enemyRobot);
            robot.addEnemyName(enemyName);
        }
    }
}


    private boolean handleRemoveEnemyResponse(JsonNode responseJson, String msgStr) {
        if (robot != null && msgStr != null && msgStr.equals("remove enemy")) {
            robot.removeEnemy(responseJson.get("data").get("robotName").asText());
            return true;
        }
        return false;
    }

    private boolean handleEnemyStateChangedResponse(JsonNode responseJson, String msgStr) {
        if (robot != null && msgStr != null && msgStr.equals("enemy state changed")) {
            String enemyName = responseJson.get("data").get("robotName").asText();
            State enemyState = JsonHandler.getState(responseJson.get("data").get("robotState"));
            robot.updateEnemyState(enemyName, enemyState);
            return true;
        }
        return false;
    }

    private boolean handleEnemyFiredGunResponse(JsonNode responseJson, String msgStr) {
        if (robot != null && msgStr != null && msgStr.contains("enemy fired gun")) {
            String enemyName = responseJson.get("data").get("robotName").asText();
            int bulletDistance = responseJson.get("data").get("distance").asInt();
            textInterface.getGui().getEnemyPlayer(enemyName).fire(bulletDistance);
            return true;
        }
        return false;
    }

    private void handleDefaultResponse(JsonNode responseJson, String response, String msgStr) {
        if (isValidResponse(responseJson)) {
            handleValidResponse(responseJson, response);
        } else {
            handleErrorResponse(msgStr);
        }
    }
    
    private boolean isValidResponse(JsonNode responseJson) {
        return responseJson.get("result").asText().equals("OK") && currentCommand != null;
    }
    
    private void handleValidResponse(JsonNode responseJson, String response) {
        switch (currentCommand.getName()) {
            case "launch":
                handleLaunchResponse(responseJson, response);
                break;
            case "forward":
            case "back":
            case "turn":
            case "fire":
                handleMovementResponse(responseJson, response);
                break;
            case "repair":
                handleRepairResponse(responseJson, response);
                break;
            case "reload":
                handleReloadResponse(responseJson, response);
                break;
            default:
                textInterface.output(response);
                break;
        }
    }
    
    private void handleLaunchResponse(JsonNode responseJson, String response) {
        robot.setState(JsonHandler.updateState(responseJson));
        Robot.setReload(responseJson.get("data").get("reload").asInt());
        Robot.setRepair(responseJson.get("data").get("repair").asInt());
        Robot.setVisibility(responseJson.get("data").get("visibility").asInt());
        textInterface.output(response);
    }
    
    private void handleMovementResponse(JsonNode responseJson, String response) {
        robot.setState(JsonHandler.updateState(responseJson));
        textInterface.output(response);
    }
    
    private void handleRepairResponse(JsonNode responseJson, String response) {
        System.out.println("Repairing...");
        try {
            Thread.sleep(Robot.getRepair() * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.setState(JsonHandler.updateState(responseJson));
        textInterface.output(response);
        paused = false;
    }
    
    private void handleReloadResponse(JsonNode responseJson, String response) {
        System.out.println("Reloading...");
        try {
            Thread.sleep(Robot.getReload() * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.setState(JsonHandler.updateState(responseJson));
        textInterface.output(response);
        paused = false;
    }
    
    private void handleErrorResponse(String msgStr) {
        if (currentCommand instanceof LaunchCommand) {
            this.robot = null;
        }
        Command.currentCommand = "error";
        textInterface.output(msgStr);
    }
    



    /**
     * Closes the socket, input stream, and output stream.
     *
     * @param socket        The socket to close.
     * @param inputStream   The input stream to close.
     * @param outputStream  The output stream to close.
     */
    public void closeEverything(Socket socket, InputStream inputStream, OutputStream outputStream) {
        try (socket; inputStream; outputStream) {
            // resources are automatically closed when the try block completes.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The entry point of the client application.
     *
     * @param args The command-line arguments.
     * @throws IOException If an I/O error occurs.
     */
    public static void main(String[] args) throws IOException {
        Socket socket = args.length == 2?
                new Socket(args[0], Integer.parseInt(args[1])) : new Socket("localhost", 5000);
        Client client = new Client(socket);
        client.listenFormessage();
        client.sendMessage();
    }
}
