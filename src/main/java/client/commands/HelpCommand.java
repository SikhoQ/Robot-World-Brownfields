package client.commands;

public abstract class HelpCommand extends Command {

    public HelpCommand() {
        super("help");
    }

    @Override
    public boolean execute(Robot target) {
        target.setStatus("I can understand these commands:\n" +
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
                
        return true;
    }
}

