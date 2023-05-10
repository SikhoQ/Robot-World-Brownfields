package server.commands;

import com.fasterxml.jackson.databind.JsonNode;
import server.ClientHandler;
import server.json.JsonHandler;
import server.response.Response;

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

    public static Command create(String request) {

        // deserialize the request string into a Json node and then extract the info you need.
        JsonNode requestJson = JsonHandler.deserializeJsonTString(request);
        String command = requestJson.get("command").asText();
        String robotName = requestJson.get("robot").asText();
        System.out.println(command);
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

    public abstract Response execute(ClientHandler clientHandler);

    @Override
    public String toString() {
        return this.getName() + " ";
    }
}
