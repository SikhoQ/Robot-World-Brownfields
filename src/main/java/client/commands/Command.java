package client.commands;

import client.robots.Robot;
import client.request.Request;


public abstract class Command {
    private final String name;
    public static String currentCommand;

    public Command(String name){
        this.name = name.trim().toLowerCase();
        currentCommand = this.name;
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
                    throw new IllegalArgumentException("Command: " + args[0] 
                        + " requires 2 arguments: <kind> and <name>.");
                }
            case "state":
                return new StateCommand();
            case "fire":
                return new FireCommand();
            case "look":
                return new LookCommand();
            case "reload":
                return new ReloadCommand();  
            case "repair":
                return new RepairCommand();
            case "forward":
                try{
                    return new ForwardCommand(args[1]);
                }catch(ArrayIndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("Command: " + args[0] 
                    + " requires argument steps");
                }
            case "back":
                try{
                    return new BackCommand(args[1]);
                }catch(ArrayIndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("Command: " + args[0] 
                    + " requires argument steps");
                }
            case "left":
            case "right":
                    return new TurnCommand(args[0]);

            default:
                throw new IllegalArgumentException("Unsupported command: " + args[0]);
        }
    }

    public abstract Request execute(Robot target);

    public static void help() {
        System.out.println("\nI can understand these commands:\n" +
        "OFF  - Shut down robot\n" +
        "HELP - provide information about commands\n" +
        "FORWARD - move forward by specified number of steps, e.g. 'FORWARD 10'\n" +
        "BACK - move back by specified number of steps, e.g. 'BACK 10'\n" +
        "LEFT - turn robot to the left\n" +
        "RIGHT - turn robot to the right\n" +
        "FIRE - the robot will fire a shot in the direction it is facing over the distance it is configured\n" +
        "LAUNCH - launch a robot into the world\n" +
        "STATE - ask the world for the state of the robot\n" +
        "LOOK - look around in the world\n" +
        "REPAIR - instruct the robot to repair its shields\n" +
        "RELOAD - instruct the robot to reload its weapons\n");
    }
    
    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public String toString() {
        return this.getName() + " ";
    }
}


