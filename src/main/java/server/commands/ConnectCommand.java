package server.commands;

import server.ClientHandler;
import server.response.BasicResponse;
import server.response.Response;

public class ConnectCommand extends Command {
    public ConnectCommand() {
        super("connect");
    }

    @Override
    public Response execute(ClientHandler target) {
        target.setCurrentCommand(getName());
        return new BasicResponse("Connected to server");
    }
}
