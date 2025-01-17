package server;

import database.DatabaseConnection;
import database.SQLiteWorldRepository;
import server.configuration.ConfigurationManager;
import server.json.JsonHandler;
import server.response.ErrorResponse;
import server.world.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;


/**
 * The ServerHandler class handles commands that come from the server.
 * It implements the Runnable interface to run as a separate thread and wait for
 * server commands from the console.
 */
public class ServerHandler implements Runnable {
    private String command;
    private World world;
    private final SQLiteWorldRepository worldRepository;
    private final Scanner scanner;

    /**
     * Constructs a new ServerHandler object.
     *
     * @param world the world instance
     */
    public ServerHandler(World world, Scanner scanner) {
        this.world = world;
        this.worldRepository = new SQLiteWorldRepository();
        this.scanner = scanner;
    }

    @Override
    public void run() {
        while(true) {
            try {
                command =  this.scanner.nextLine().toLowerCase();
            } catch (NoSuchElementException ignored) {
                System.exit(1);
            }
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

    public void handleDeleteCommand() {
        System.out.println("Enter name of world to delete from database: ");
        String worldName = scanner.nextLine();

        if (worldName.equalsIgnoreCase("all") || DatabaseConnection.worldExists(worldName)) {
            String result = deleteWorld(worldName);
            printFeedback(result, worldName);
        } else {
            System.out.println("World '" + worldName + "' does not exist.");
        }
    }

    public String deleteWorld(String worldName) {
        return worldRepository.removeWorld(worldName);
    }

    public void printFeedback(String result, String worldName) {
        switch (result) {
            case "removed":
                System.out.println("World '" + worldName + "' was removed successfully.");
                break;
            case "removed all":
                System.out.println("Database was cleared successfully.");
                break;
            case "no world":
                System.out.println("World '" + worldName + "' does not exist.");
                break;
            case "not removed":
                System.out.println("Failed to remove world '" + worldName + "'.");
                break;
            default:
                System.out.println("An unexpected error occurred.");
                break;
        }
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
        final Map<Integer, List<Map<String, List<Integer>>>> worldInfo = this.worldRepository.loadWorld(worldName);
        if (!worldInfo.isEmpty()) {
            // get world size and use to config
            final Integer worldSize = worldInfo.keySet().iterator().next();
            ConfigurationManager.setWorldSize(worldSize);
            ConfigurationManager.setXConstraint(worldSize);
            ConfigurationManager.setYConstraint(worldSize);

            final List<Map<String, List<Integer>>> worldObjects = worldInfo.get(worldSize);

            List<Obstacle> obstaclesToAdd = new ArrayList<>();

            for (Map<String, List<Integer>> oneObject: worldObjects) {
                String type = oneObject.keySet().iterator().next();
                int x = oneObject.get(type).get(0);
                int y = oneObject.get(type).get(1);

                if (type.equals("SquareObstacle")) {
                    obstaclesToAdd.add(new SquareObstacle(x, y));
                }

            }
            this.world.setObstacles(obstaclesToAdd);
            System.out.println("* Restored World '" + worldName + "'.. [size: " + worldSize + " x " + worldSize + ", obstacles: " + ConfigurationManager.getObstacles() + ", visibility: " + ConfigurationManager.getVisibility() + "]");
        }else {
            System.out.println("Failed to restore the world '" + worldName + "'. World not found.");
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
        getRobotsString() : "\nThere are no robots in world.";

        string.append(robotsStr);

        List<Obstacle> obstacles = world.getObstacles();

        if (!obstacles.isEmpty()) {
            string.append(("\nObstacles:\n"));

            for (Obstacle obstacle : obstacles) {
                string.append(obstacle.toString()).append("\n");
            }
        }
        else {
            string.append("\n").append("There are no obstacles in this world");
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
