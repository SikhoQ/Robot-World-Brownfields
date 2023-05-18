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
            case "look":
                return new LookCommand();
            case "forward":
                try{
                    return new ForwardCommand(args[1]);
                }catch(ArrayIndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("Command: " + args[0] + "requires argument steps");
                }
            case "back":
                try{
                    return new BackCommand(args[1]);
                }catch(ArrayIndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("Command: " + args[0] + "requires argument steps");
                }
            case "left":
            case "right":
                    return new TurnCommand(args[0]);
            default:
                throw new IllegalArgumentException("Unsupported command: " + args[0]);
        }
    }

    public abstract Request execute(Robot target);

    @Override
    public String toString() {
        return this.getName() + " ";
    }

    public abstract boolean execute(Robot target);

    public abstract boolean execute(Robot target);

    public abstract boolean execute(Robot target);

    public abstract boolean execute(Robot target);

    public abstract boolean execute(Robot target);

    public abstract boolean execute(Robot target);
}
