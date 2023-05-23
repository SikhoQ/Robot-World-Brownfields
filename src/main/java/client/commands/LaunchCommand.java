package client.commands;


import client.robots.Robot;
import client.request.Request;


public class LaunchCommand extends Command{
    private String make;
    private String robotName;

    public LaunchCommand(String make, String name) {
        super("launch");
        this.make =  make;
        this.robotName = name;
    }

    @Override
    public Request execute(Robot target) {
        String sheilds = String.valueOf(target.getShields());
        String shots = String.valueOf( target.getShots());
        return new Request(robotName, "launch", new String[]{make, sheilds, shots});
    }
}
