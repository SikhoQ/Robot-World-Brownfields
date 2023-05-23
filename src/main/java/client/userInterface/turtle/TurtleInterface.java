package client.userInterface.turtle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import client.Client;
import client.robots.Robot;
import client.robots.util.State;
import client.userInterface.text.TextInterface;
import client.userInterface.turtle.util.Turtle;
import client.userInterface.util.Position;

public class TurtleInterface extends TextInterface implements Runnable {

    private Player player;
    private Robot robot;
    private State robotState;
    private Turtle pen;

    private List<Position> obstacles;
    private List<Player> enemyPlayers = new ArrayList<>();
    private List<String> enemyPlayersNames = new ArrayList<>();
    private int visibility;

    private int obstacleSize = 16; // get it from server
    int x_constraint = 200; // get it from server
    int y_constraint = 200; // get it from server

    private final int angle = 90;

    public TurtleInterface(Client client) {
        super(client);
    }

    public Player getPlayer() {
        return player;
    }

    public void createPen() {
        this.pen = new Turtle();
        this.pen.hide();
        this.pen.penColor("white");
        this.pen.shapeSize(1, 1);
    }

    public void showObstacles() {
        displayBox();
        for (int i=0; i < obstacles.size(); i++) {
            drawObstacle(obstacles.get(i));
        }
    }
    

    public void setObstacles(List<Position> obstacles) {
        this.obstacles = obstacles;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public void drawObstacle(Position obstacle) {
        this.pen.up();
        this.pen.setPosition(obstacle.getX(), obstacle.getY());
        this.pen.down();
        this.pen.setDirection(0);
        drawRectangle();
        this.pen.up();
    }

    public void addEnemyPlayers() {
        for (Robot enemy : robot.getEnemyRobots()) {
            if (!enemyPlayersNames.contains(enemy.getName())) {
                Player potentialEnemy = new Enemy(enemy);
                enemyPlayers.add(potentialEnemy);
                enemyPlayersNames.add(enemy.getName());
            }
        }
    }

    public void removeEnemyPlayers() {
        Iterator<Player> iterator = enemyPlayers.iterator();
        while (iterator.hasNext()) {
            Player enemy = iterator.next();

            // remmove enemy robots who quit.
            if (!robot.getEnemyRobots().contains(enemy.getRobot())) {
                iterator.remove();
                enemyPlayersNames.remove(enemy.getRobot().getName());
                enemy.hide();
            }
            
            // also remove robots who are killed.
            if (enemy.getRobot().getShields() < 0) {
                iterator.remove();
                enemyPlayersNames.remove(enemy.getRobot().getName());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                enemy.hide();
            } 
        }
    }

    public Player getEnemyPlayer(String enemyName) {
        int enemyIndex  = enemyPlayersNames.indexOf(enemyName);
        return enemyPlayers.get(enemyIndex);
    }

    public void drawRectangle() {
        this.pen.forward(this.obstacleSize);
        this.pen.left(this.angle);
        this.pen.forward(this.obstacleSize);
        this.pen.left(this.angle);
        this.pen.forward(this.obstacleSize);
        this.pen.left(this.angle);
        this.pen.forward(this.obstacleSize);
    }

    public void displayBox() {
        this.pen.up();
        this.pen.setPosition(-x_constraint - obstacleSize, y_constraint + obstacleSize);
        this.pen.setDirection(0.0);
        this.pen.down();
        this.pen.forward(x_constraint * 2 + obstacleSize*2);
        this.pen.setDirection(270.0);
        this.pen.forward(y_constraint * 2 + obstacleSize*2);
        this.pen.setDirection(180.0);
        this.pen.forward(x_constraint * 2 + obstacleSize*2);
        this.pen.setDirection(90.0);
        this.pen.forward(y_constraint* 2 + obstacleSize*2);
        this.pen.up();
    }

    @Override
    public void run() {
        Turtle.setCanvasSize(500, 500);
        Turtle.bgcolor(Color.black);
        Turtle.getWindow().setResizable(false);

        // draw initial.
        createPen();
        showObstacles();

        player = new Player(client.getRobot(), false);
        player.show();
        robot = client.getRobot();

        while (!gameOver) {
            // update player's position and directio.
            robotState = robot.getState();
            player.setDirection(robotState.getDirection());
            player.setPlayerPosition(robotState.getPosition()[0], robotState.getPosition()[1]);

            // show some state info in title bar.
            Turtle.getWindow().setTitle(
                "[" + robotState.getPosition()[0] + "," + robotState.getPosition()[1] + "] " +
                robot.getName().toUpperCase() + " <" + robot.getKind() + ">" + " ".repeat(10)  +
                "Health: " + robotState.getShields() + " ".repeat(10) +
                "Shots: " + robotState.getShots()
            );

            // draw enemy players & remove any enemy player that quits or is killed.
            addEnemyPlayers();
            removeEnemyPlayers();

            // update enemy players' state
            for (Player enemPlayer : enemyPlayers) {
                State enemyState = enemPlayer.getRobot().getState();
                enemPlayer.setDirection(enemyState.getDirection());
                enemPlayer.setPlayerPosition(enemyState.getPosition()[0], 
                                            enemyState.getPosition()[1]);
                enemPlayer.getPlayer().show();
            }
        }
    }
}
