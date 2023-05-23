package client.request;

import java.util.Arrays;

public class Request {
    private final String robot;
    private final String command;
    private final String[] arguments;

    public Request(String robotName, String command, String[] arguments) {
        this.robot = robotName;
        this.command = command;
        this.arguments = arguments;
    }

    public String getCommand() {
        return command;
    }

    public String getRobot() {
        return robot;
    }

    @Override
    public String toString() {
        return "Request{" +
                "robotName='" + robot + '\'' +
                ", command='" + command + '\'' +
                ", arguments=" + Arrays.toString(arguments) +
                '}';
    }
}
