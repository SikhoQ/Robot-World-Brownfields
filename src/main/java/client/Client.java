package client;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import client.commands.Command;

import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private Robot robot;

    public Client(Socket socket, String usernameString) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter((socket.getOutputStream()))); // write to server
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // read from server.
            this.username  = usernameString;
            this.robot = new Robot(usernameString);
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    } 

    public void sendMessage() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String command = scanner.nextLine();
                if (command.equals("state")) {
                    executeCommand(command);
                }else{
                    bufferedWriter.write(command);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
               
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void executeCommand(String command) {
        Command newCommand = Command.create(command);
        Boolean executed = newCommand.execute(robot);
    }

    public void listenFormessage() {
        // listens for messages sent from server.
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;

                while(socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                        if (msgFromGroupChat.contains("shutting down...")) {
                            closeEverything(socket, bufferedReader, bufferedWriter);
                            System.exit(0);
                        }
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
            
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your robot name: ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost",5000);
        Client client =  new Client(socket, username);
        client.listenFormessage();
        client.sendMessage();
    }
}
