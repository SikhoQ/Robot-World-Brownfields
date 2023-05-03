package client.commands;

import client.Client;
import client.Robot;
import client.request.Request;


public abstract class Command {
    private final String name;

    public Command(String name){
        this.name = name.trim().toLowerCase();
        Client.setCurrentCommand(name);
    }

    public String getName() {                                                                           //<2>
        return name;
    }

    public static Command create(String instruction) {
        String[] args = instruction.toLowerCase().trim().split(" ");
//        System.out.println(Arrays.toString(args));

        switch (args[0]){
            case "off":
            case "quit":
            case "shutdown":
                return new QuitCommand();
            case "launch":
                return new LaunchCommand(args[1], args[2]);
            case "state":
                return new StateCommand();
            case "dump":
                return new DumpCommand();
            case "robots":
                return new RobotsCommand();

            default:
                throw new IllegalArgumentException("Unsupported command: " + args[0]);
        }
    }

    public abstract Request execute(Robot target);

    @Override
    public String toString() {
        return this.getName() + " ";
    }
}
