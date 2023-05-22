package client.commands;

import client.robots.Robot;
import client.request.Request;

public class StateCommand extends Command{

   public StateCommand(){
       super("state");
   }

   @Override
   public Request execute(Robot target) {
       return new Request(target.getName(), "state", new String[]{});
   }
}
