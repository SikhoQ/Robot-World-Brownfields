package server.commands;

import com.fasterxml.jackson.databind.JsonNode;
import server.ClientHandler;
import server.json.JsonHandler;
import server.response.Response;

/**
 * Represents a command in the game.
 * A command consists of a name and an optional argument.
 * Provides methods to retrieve the name and argument of the command.
 * Provides a static factory method to create a Command object based on a request string.
 * Subclasses of Command implement the execute() method to perform specific actions for the command.
 */
public abstract class Command {
    private final String name;
    private String argument;

    public Command(String name){
        this.name = name.trim().toLowerCase();
        this.argument = "";
    }

    public Command(String name, String argument) {
        this(name);
        this.argument = argument.trim();
    }

    public String getName() {                                                                      
        return name;
    }

    public String getArgument() {
        return argument;
    }

    /**
     * Creates a Command object based on the provided request string.
     * Parses the request string into a JSON node and extracts the command, robot name, and arguments.
     * Returns a Command object corresponding to the command type.
     * @param request the request string containing the command information
     * @return a Command object based on the command type in the request string
     * @throws IllegalArgumentException if the command in the request is not supported
     */
    public static Command create(String request) {

        // deserialize the request string into a Json node and then extract the info you need.
        JsonNode requestJson = JsonHandler.deserializeJsonTString(request);
        String command = requestJson.get("command").asText();
        String robotName = requestJson.get("robot").asText();
        JsonNode args = requestJson.get("arguments");

        switch (command){
            case "connect":
                return new ConnectCommand();
            case "launch":
                return new LaunchCommand(robotName, args);
            case "quit":
                return new QuitCommand();
            case "state":
                return new StateCommand();
            case "fire":
                return new FireCommand();
            case "look":
                return new LookCommand();
            case "repair":
                return new RepairCommand();
            case "reload":
                return new ReloadCommand();    
            case "forward":
                return new ForwardCommand(args.get(0).asText());
            case "back":
                return new BackCommand(args.get(0).asText());
            case "turn":
                return new TurnCommand(args.get(0).asText());
            default:
                throw new IllegalArgumentException("Unsupported command: " + command);
        }
    }

    /**
     * Executes the command for the given client handler.
     * Subclasses must implement this method to perform the specific actions for the command.
     * @param clientHandler the client handler executing the command
     * @return a Response object representing the result of executing the command
     */
    public abstract Response execute(ClientHandler clientHandler);

    /**
     * Returns a string representation of the Command.
     * @return a string representation of the Command
     */
    @Override
    public String toString() {
        return this.getName() + " ";
    }
}
