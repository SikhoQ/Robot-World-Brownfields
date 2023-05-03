package server.commands;

import com.fasterxml.jackson.databind.JsonNode;
import server.ClientHandler;
import server.json.JsonHandler;
import server.response.Response;

public abstract class Command {
    private final String name;

    public abstract Response execute(ClientHandler clientHandler);

    public Command(String name){
        this.name = name.trim().toLowerCase();
    }

    public String getName() {                                                                           //<2>
        return name;
    }

    public static Command create(String request) {

        System.out.println("request: " + request);
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
            case "robots":
                return new RobotsCommand();
            case "dump":
                return new DumpCommand();
            default:
                throw new IllegalArgumentException("Unsupported command: " + command);
        }
    }

    @Override
    public String toString() {
        return this.getName() + " ";
    }
}
