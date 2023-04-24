package client.commands;

import client.Robot;

public class StateCommand extends Command{

    public StateCommand(){
        super("state");
    }

    @Override
    public boolean execute(Robot target) {
    System.out.println(target.getName() + ": At position (0,0)");
    System.out.println(target.getName() + ": Is facing NORTH");
    System.out.println(target.getName() + ": What must I do next?");
    return true;
    }

    
}
