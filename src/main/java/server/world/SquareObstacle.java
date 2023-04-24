package server.world;

import java.util.ArrayList;
import java.util.List;

public class SquareObstacle implements Obstacle{
    private Position position;
    public static List<Position> obstacles = new ArrayList<>();

    private int obstacleSize = 5;

    public SquareObstacle(int x, int y) {
        this.position = new Position(x, y);
        if (x != 0 && y != 0) {
            obstacles.add(position);
        }
    }

    public int getBottomLeftX() {
        return this.position.getX();
    }

    public int getBottomLeftY() {
        return this.position.getY();
    }

    public int getSize() {
        return this.obstacleSize;
    }

    public boolean blocksPosition(Position position) {
        for (Position obstacle: obstacles) {
            if ((obstacle.getX() <= position.getX() && position.getX() < obstacle.getX() + this.obstacleSize) &&
                    (obstacle.getY() <= position.getY() && position.getY() < obstacle.getY() + this.obstacleSize)) {
                return true;
            }
        }
        return false;
    }

    public boolean blocksPath(Position a, Position b) {
        if (a.getX() == b.getX()) { // y is changing
            return blocksYPath(a, b);
        }
        else {
            return blocksXPath(a, b);
        }
    }

    public boolean blocksYPath(Position a, Position b) {
        if ( b.getY() > a.getY()) { // robot moving up
            for (int i=a.getY(); i <= b.getY(); i++) {
                if (blocksPosition(new Position(a.getX(), i))) {
                    return true;
                }
            }
        }
        else{
            for (int i=a.getY(); i <= b.getY(); i--) {
                if (blocksPosition(new Position(a.getX(), i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean blocksXPath(Position a, Position b) {
        if ( b.getX() > a.getX()) { // robot moving up
            for (int i=a.getX(); i <= b.getX(); i++) {
                if (blocksPosition(new Position(i, a.getY()))) {
                    return true;
                }
            }
        }
        else{
            for (int i=a.getX(); i <= b.getX(); i--) {
                if (blocksPosition(new Position(i, a.getY()))) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public String toString() {
        int endposX = getBottomLeftX() + 5;
        int endposY = getBottomLeftY() + 5;
        return "-At position " + getBottomLeftX() + "," + getBottomLeftY() + " " + "(" +  endposX + "," + endposY+ ")";
    }
}
