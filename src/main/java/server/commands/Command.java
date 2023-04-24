package server.commands;

import server.ClientHandler;

public abstract class Command {
    private final String name;

    public abstract boolean execute(ClientHandler clientHandler);

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
            case "quit":
                return new ShutdownCommand();
            case "robots":
                return new RobotsCommand();
            case "dump":
                return new DumpCommand();
            // case "look":
            //     return new LookCommand(args[1]);
            // case "state":
            //     return new StateCommand(args[1]);
            default:
                throw new IllegalArgumentException("Unsupported command: " + command);
        }
    }

    @Override
    public String toString() {
        return this.getName() + " ";
    }
}
