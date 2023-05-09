package server;

// MOVE THIS OUT OF COMMANDS AND RENAME IT TO SERVERHANDLER.

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

import server.json.JsonHandler;
import server.response.ErrorResponse;
import server.world.World;
import server.world.Obstacle;
import server.world.Robot;

/* A class to handle all commands that come from the server. */
public class ServerHandler implements Runnable {
    private Scanner scanner;
    private String command;
    private World world;

    public ServerHandler(World world) {
        this.world = world;
    }

    @Override
    public void run() {
        this.scanner = new Scanner(System.in);
        while(true) {
            command =  scanner.nextLine().toLowerCase();
            switch (command) {
                case "dump":
                    dump();
                    break;
                case "robots":
                    robots();
                    break;
                case "quit":
                    quit();
                    break;
                default:
                    System.out.println("Unsupported command: " + command);
            }
        }
    }

    // tell clients that server is diconnecting, then disconnect it.
    public void quit() {
        ErrorResponse response = new ErrorResponse("Server has been disconnected.");
        String responseJsonString = JsonHandler.serializeResponse(response);

        for (ClientHandler clientHandler : ClientHandler.clientHanders) {
            clientHandler.sendToClient(responseJsonString);
        }

        output("Shutting down...");
        System.exit(0);
    }

    public void robots() {
        if (world.getRobots().size() < 1) {
            output("Ther are no ronots cureently in world.");
        }else{
            output(getRobotsString());
        }
    }

    public String getRobotsString() {
        StringBuilder string = new StringBuilder();
        string.append("\nHere are the robots currently in this world:\n");
        for (Robot robot : world.getRobots()) {
            string.append("\t- " + robot + "\n");
        }
        return string.toString();
    }

    public void dump() {
        StringBuilder string = new StringBuilder();
        String robotsStr = world.getRobots().size() > 0? 
        getRobotsString() : "There are currently no robots in world.";

        string.append(robotsStr);
        string.append(("\nObstacles:\n"));

        List<Obstacle> obstacles = world.getObstacles();

        for (Obstacle obstacle: obstacles){
            string.append(obstacle.toString()).append("\n");
        }

        output(string.toString());
    } 

    private void output(String string) {
        try {
            System.out.println("SERVER <" + InetAddress.getLocalHost().getHostAddress()  + "> : " + string);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
