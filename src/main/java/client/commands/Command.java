package client.commands;

import client.robots.Robot;
import client.request.Request;


public abstract class Command {
    private final String name;

    public Command(String name){
        this.name = name.trim().toLowerCase();
    }

    public String getName() {                  
        return name;
    }

    public static Command create(String instruction) {
        String[] args = instruction.toLowerCase().trim().split(" ");

        switch (args[0]){
            case "off":
            case "quit":
            case "shutdown":
                return new QuitCommand();
            case "launch":
            try{
                return new LaunchCommand(args[1], args[2]);
            }catch(ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Command: " + args[0] + "requires 2 arguments: <kind> and <name>.");
            }
            case "state":
                return new StateCommand();
            case "fire":
                return new FireCommand();

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
