package client.userInterface.text;

import com.fasterxml.jackson.databind.JsonNode;

import client.Client;
import client.commands.Command;
import client.json.JsonHandler;
import client.robots.Robot;
import client.userInterface.turtle.TurtleInterface;
import client.userInterface.util.Position;

import java.util.List;
import java.util.Scanner;

public class TextInterface {
    protected final Client client;
    private List<Position> obstacles;
    protected boolean gameOver = false;
    private TurtleInterface gui;
    private Scanner scanner;

    public TextInterface(Client client) {
        this.client = client;
        this.scanner = new Scanner(System.in);
    }

    public List<Position> getObstacles() {
        return obstacles;
    }

    public TurtleInterface getGui() {
        return gui;
    }

    public String getUserInput() {
        return scanner.nextLine();
    }

    public void output(String outputString) {

        Robot robot = client.getRobot();
        JsonNode jsonStr = null;

        if (Command.currentCommand.equals("connect")) { // initial connect
            JsonNode jsonString = JsonHandler.deserializeJsonTString(outputString);
            this.obstacles = JsonHandler.deserializeObstacles(jsonString.get("data").get("obstacles"));

            System.out.println("JHB_45> Connected to server. Type: 'launch <robootMake> <robotName> to launch robot into world.\n" +
                    " ".repeat(8) + "[sniper, venom, fighter]" ); 
            System.out.println("JHB_45> What do you want to do?");
            return;
        }

        if (JsonHandler.isJsonString(outputString)) {
            jsonStr = JsonHandler.deserializeJsonTString(outputString);
        }

        // if user quit or server disconnects
        if (outputString.contains("Shutting") || outputString.contains("disconnected.")) {
            
            System.out.println(robot != null ? robot + " > " + outputString : "JHB_45" + "> " + outputString);
            gameOver = true;

        }
        else if ((jsonStr != null &&
            jsonStr.get("data").get("message") != null) && 
            jsonStr.get("data").get("message").asText().contains("been shot")) {

                System.out.println(robot + "> You've been shot.");
            }
        else{

            System.out.print(robot != null ? robot + "> " : "JHB_45" + "> ");

            switch(Command.currentCommand) {
                case "launch":
                    if (JsonHandler.isJsonString(outputString)) { // no error occurred.

                        int visibility = jsonStr.get("data").get("visibility").asInt();

                        gui = new TurtleInterface(client);
                        Thread guiThread = new Thread(gui);

                        gui.setObstacles(obstacles);
                        gui.setVisibility(visibility);

                        guiThread.start();
                        System.out.println("robot has been launched");
                        break;
                    }
                    else {
                        System.out.println(outputString);
                    }
                    break;

                case "state":
                    JsonNode state = JsonHandler.deserializeJsonTString(outputString).get("state");
                    int shields = state.get("shields").asInt();
                    int shots = state.get("shots").asInt();
                    String status = state.get("status").asText();

                    System.out.println(shields + " shields, " 
                        + shots + " shots, status is " + status + ".");
                    break;

                case "forward":
                    JsonNode data = JsonHandler.deserializeJsonTString(outputString).get("data");
                    String message = data.get("message").asText();
                    if(message.equals( "Done")){
                        System.out.println("moved forward!");
                    }else{
                        System.out.println("You've been obstructed.");
                    }
                    break;

                case "back":
                    data = JsonHandler.deserializeJsonTString(outputString).get("data");
                    message = data.get("message").asText();
                    if(message.equals( "Done")){
                        System.out.println("moved back!");
                    }else{
                        System.out.println("You've been obstructed.");
                    }
                    break;
                case "turn":
                    data = JsonHandler.deserializeJsonTString(outputString).get("data");
                    message = data.get("message").asText();
                    if(message.equals("Done")){
                        System.out.println("turned!");
                    }
                    break;
                case "repair":
                    data = JsonHandler.deserializeJsonTString(outputString).get("data");
                    state = JsonHandler.deserializeJsonTString(outputString).get("state");
                    message = data.get("message").asText();
                    
                    if(message.equals("Done")){
                        System.out.println("shields have been repaired.");
                    }
                    break;
                case "reload":
                    data = JsonHandler.deserializeJsonTString(outputString).get("data");
                    state = JsonHandler.deserializeJsonTString(outputString).get("state");
                    message = data.get("message").asText();

                    if(message.equals("Done")){
                        System.out.println("shots have been reloaded.");
                    }
                    break;
                case "fire":
                    data = jsonStr.get("data");
                    message = data.get("message").asText();

                    if(message.equals("Hit")){
                        int distance = data.get("distance").asInt();
                        String robotHit = data.get("robot").asText();

                        gui.getPlayer().fire(distance);

                        System.out.println("You shot " + robotHit + " " + distance 
                            + " steps away from you. At position " 
                            + data.get("state").get("position") + ".");
                    }else{
                        gui.getPlayer().fire(Robot.bulletDistance); 
                        System.out.println(message);
                    }
                    break;

                default:
                    System.out.println(outputString);
                    
            }

            // prompt user for next command.
            System.out.println(robot != null ? robot + "> What should I do next?" : "JHB_45> What do you want to do?");
        }
    }
}
