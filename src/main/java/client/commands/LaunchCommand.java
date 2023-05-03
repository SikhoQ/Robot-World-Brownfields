package client.commands;


import client.Robot;
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
        return new Request(robotName, "launch", new String[]{make, target.getShields(), target.getShots()});
    }
}
