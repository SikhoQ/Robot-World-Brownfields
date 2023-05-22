package client.userInterface.turtle;

import java.awt.Color;

import client.robots.Robot;
import client.userInterface.turtle.util.Turtle;

public class Player {
    
    private Turtle player;
    private Robot robot;
    private final int angle = 90;
    private final int size = 16; // get it from server.
    private final int constraint = 200;
    Turtle bullet;


    public Player(Robot robot) {

        this.robot = robot;
        player = new Turtle();
        player.hide();
        player.shapeSize(size, size);
        player.shape("arrow");
        player.fillColor(getColor(robot.getKind()));
        player.outlineColor(getColor(robot.getKind()));
        player.setDirection(90);
        player.up();
    }
    
    private Color getColor(String make) {
        switch(make) {
            case "venom":
                return Color.red;
            case "fighter":
                return Color.green;
            default:
                return Color.cyan;
        }
    }

    public Turtle getPlayer() {
        return player;
    }

    public Robot getRobot() {
        return robot;
    }

    public void hide() {
        player.hide();
    }

    public void show() {
        player.show();
    }

    public void moveForward(double steps) {
        player.forward(steps);
    }

    public void moveBack(double steps) {
        player.backward(steps);
    }

    public void turnRight() {
        player.right(angle);
    }

    public void turnLeft() {
        player.left(angle);
    }

    public void setPlayerPosition(int x, int y) {
        player.setPosition(x, y);
    }

    public void setDirection(String direction) {
        switch(direction.toLowerCase()) {
            case "north" :
                player.setDirection(90);
                break;
            case "east":
                player.setDirection(0);
                break;
            case "south" :
                player.setDirection(270);
                break;
            case "west":
                player.setDirection(180);
                break;
        }
    }

    public void fire(int steps) {
        // player's bullets
        bullet = new Turtle();
        bullet.hide();
        bullet.shape("circle");
        bullet.shapeSize(4, 4);
        bullet.outlineColor("yellow");
        bullet.fillColor("yellow");
        bullet.setDirection(angle);
        bullet.up();
        bullet.setPosition(player.getX(), player.getY());
        bullet.setDirection(player.getDirection());
        bullet.show();

        // move bullet smoothly while it is still inside world
        int stepsToGo = steps;
        int bulletSpeed = 5;
        while (stepsToGo > 0 && Math.abs(bullet.getX()) <= constraint && Math.abs(bullet.getY()) <= constraint) {
            bullet.forward(bulletSpeed);
            stepsToGo-= 5;
        }
        bullet.hide();
        bullet = null;
    }
}
