package server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import server.commands.Command;
import server.world.IWorld;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHanders = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader; 
    private BufferedWriter bufferedWriter; 
    private String robotName; 
    private IWorld world;

    public ClientHandler(Socket socket, IWorld world) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter((socket.getOutputStream()))); 
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
            this.robotName = bufferedReader.readLine();
            this.world = world;
            clientHanders.add(this);
            handleCommand("greet");

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public Socket getSocket() {
        return this.socket;
    }

    public BufferedReader getBufferedReader() {
        return this.bufferedReader;
    }
    public BufferedWriter getBufferedWriter() {
        return this.bufferedWriter;
    }
    public String getName() {
        return this.robotName;
    }
    public IWorld getWorld() {
        return this.world;
    }


    @Override
    public void run() {
        String commandFromCleint;
        while (socket.isConnected()) {
            try {
                commandFromCleint = bufferedReader.readLine();
                handleCommand(commandFromCleint);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void handleCommand(String command) {
        if (command.equals("greet")) {
            System.out.println("hi");
            sendToClient(robotName + ": " + "Hello Kiddo!"+ "\n" + robotName + ": What must I do next?");
        }else{
            Command newCommand = Command.create(command);
            Boolean executed = newCommand.execute(this);
            // sendToClient(robotName + ": What must I do next?");
        }
        // Command newCommand = Command.create(command);
        // Boolean executed = newCommand.execute(this);
        // if (command.equals("greet")) {
        //     sendToClient(robotName + " : " + "Hello Kiddo!");
        // }
        // else if (command.equals("quit")) {
        //     sendToClient(robotName + " : " + "shutting down...");
        //     closeEverything(socket, bufferedReader, bufferedWriter);
        // } else if (command.equals("robots")) {
        //     sendToClient(robotName + " : " + "Here are all the robots in the world:\n"+ clientHanders);
        // }
        // else {
        //     sendToClient(robotName + " : " + "Cannot understand request");
        // }

    }

     public void sendToClient(String message){
        try {
            this.bufferedWriter.write(message);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
     }

    public void removeClientHandler() {
        clientHanders.remove(this);
        // handleCommand("SERVER: " + robotName + " has left the chat!");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 

    @Override
    public String toString() {
       return this.robotName;
    }
}
