package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

import server.json.JsonHandler;
import server.response.ErrorResponse;
import server.world.World;
import server.world.Obstacle;
import server.world.Robot;

/**
 * The ServerHandler class handles commands that come from the server.
 * It implements the Runnable interface to run as a separate thread and wait for
 * server commands from the console.
 */
public class ServerHandler implements Runnable {
    private Scanner scanner;
    private String command;
    private World world;

    /**
     * Constructs a new ServerHandler object.
     *
     * @param world the world instance
     */
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
                case "clear":
                    clear();
                    break;
                default:
                    System.out.println("Unsupported command: " + command);
            }
        }
    }

    /**
     * Handles the quit command.
     * Sends a server disconnect response to all client handlers and shuts down the
     * server.
     */
    public void quit() {
        ErrorResponse response = new ErrorResponse("Server has been disconnected.");
        String responseJsonString = JsonHandler.serializeResponse(response);

        for (ClientHandler clientHandler : ClientHandler.clientHanders) {
            clientHandler.sendToClient(responseJsonString);
        }

        output("Shutting down...");
        System.exit(0);
    }

    /**
     * Handles the robots command.
     * Prints the list of robots currently in the world.
     */
    public void robots() {
        if (world.getRobots().size() < 1) {
            output("There are currently no robots in this world.");
        }else{
            output(getRobotsString());
        }
    }

    /**
     * Returns a string representation of the robots in the world.
     *
     * @return a string representation of the robots
     */
    public String getRobotsString() {
        StringBuilder string = new StringBuilder();
        string.append("\nHere are the robots in this world:\n");
        for (Robot robot : world.getRobots()) {
            string.append("\t- " + robot + "\n");
        }
        return string.toString();
    }

    /**
     * Handles the dump command.
     * Prints information about the robots and obstacles in the world.
     */
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

    public void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Outputs a string to the console with the server's IP address.
     *
     * @param string the string to output
     */
    private void output(String string) {
        try {
            System.out.println("SERVER <" + InetAddress.getLocalHost().getHostAddress()  + "> : " + string);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
