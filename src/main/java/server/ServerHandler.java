package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import database.SQLiteWorldRepository;
import server.json.JsonHandler;
import server.response.ErrorResponse;
import server.world.*;
import database.DatabaseConnection;

/**
 * The ServerHandler class handles commands that come from the server.
 * It implements the Runnable interface to run as a separate thread and wait for
 * server commands from the console.
 */
public class ServerHandler implements Runnable {
    private Scanner scanner;
    private String command;
    private World world;
    private final SQLiteWorldRepository worldRepository;

    /**
     * Constructs a new ServerHandler object.
     *
     * @param world the world instance
     */
    public ServerHandler(World world) {
        this.world = world;
        this.worldRepository = new SQLiteWorldRepository();
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
                case "save":
                    handleSaveCommand();
                    break;
                case "restore":
                    handleRestoreCommand();
                    break;
                case "delete":
                    handleDeleteCommand();
                    break;
                default:
                    System.out.println("Unsupported command: " + command);
            }
        }
    }

    private void handleDeleteCommand() {
        System.out.println("Enter name of world to delete from database: ");
        String worldName = scanner.nextLine();

    }

    public void handleSaveCommand() {
        System.out.print("Enter name to save this world in: ");
        String worldName = scanner.nextLine();
        boolean shouldOverwrite = true;
        if (DatabaseConnection.worldExists(worldName)) {
            shouldOverwrite = DatabaseConnection.promptOverwrite(worldName);
        }

        if (shouldOverwrite) {
            // after getting the name, use this.world to get the size and objects
            final int worldSize = this.world.getWorldSize();
            WorldObjects worldObjects = new WorldObjects(this.world);
            final ArrayList<WorldObject> objects = worldObjects.getObjects();
            // call worldRepo save method with these 3 args
            final String worldRemoved = this.worldRepository.removeWorld(worldName);
            switch (worldRemoved) {
                case "removed":
                case "no objects":
                case "no world":
                    boolean successfullySaved = this.worldRepository.save(worldName, worldSize, objects);
                    if (successfullySaved) {
                        System.out.println("World saved successfully.");
                    } else {
                        System.out.println("Failed to save the world.");
                    }
                    break;
                case "not removed":
                    System.out.print("Failed to remove world '" + worldName + "' from database. ");
                    System.out.println("Run 'save' command with a different world name.");
                    break;
            }
        }
    }

    public void handleRestoreCommand() {
        System.out.print("Enter world name to restore: ");
        String worldName = scanner.nextLine();
        World world = this.worldRepository.loadWorld(worldName);

        if (world != null) {
            System.out.println("World restored successfully.");
        } else {
            System.out.println("Failed to restore the world '" + worldName + "'");
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
        if (world.getRobots().isEmpty()) {
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
        String robotsStr = !world.getRobots().isEmpty() ?
        getRobotsString() : "There are currently no robots in world.";

        string.append(robotsStr);
        string.append(("\nObstacles:\n"));

        List<Obstacle> obstacles = world.getObstacles();
        if (obstacles != null) {
            for (Obstacle obstacle : obstacles) {
                string.append(obstacle.toString()).append("\n");
            }
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
