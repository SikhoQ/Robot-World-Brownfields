package client.commands;

import client.Robot;
import client.request.Request;

public class QuitCommand extends Command {
    public QuitCommand() {
        super("quit");
    }

   @Override
   public Request execute(Robot target) {
       String robotName = (target != null) ? target.getName() : "";
       return new Request(robotName, "quit", new String[]{});
   }
}
