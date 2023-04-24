package client.commands;

import client.Robot;

public abstract class Command {
    private final String name;

    public abstract boolean execute(Robot target);

    public Command(String name){
        this.name = name.trim().toLowerCase();
    }


    public String getName() {                                                                           //<2>
        return name;
    }

    public static Command create(String instruction) {
        String command= instruction.toLowerCase().trim();
//        System.out.println(Arrays.toString(args));
//        System.out.println(Arrays.toString(args[1].split("")));

        switch (command){
            case "state":
                return new StateCommand();
    
            default:
                throw new IllegalArgumentException("Unsupported command: " + command);
        }
    }

    @Override
    public String toString() {
        return this.getName() + " ";
    }
}
