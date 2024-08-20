package server.world;

import java.util.ArrayList;
import java.util.List;

import server.world.util.Position;


public class SquareObstacle implements Obstacle {
    
    private Position position;
    public static List<Position> obstacles = new ArrayList<>();

    private static int size = World.getWorldConfiguration().getTileSize();

    /**
     * Creates a new SquareObstacle at the specified position.
     *
     * @param x the x-coordinate of the obstacle
     * @param y the y-coordinate of the obstacle
     */
    public SquareObstacle(int x, int y) {
        this.position = new Position(x, y);
        obstacles.add(position);
    }

    /**
     * Returns the x-coordinate of the bottom-left corner of the obstacle.
     *
     * @return the x-coordinate of the bottom-left corner
     */
    @Override
    public int getBottomLeftX() {
        return this.position.getX();
    }

    /**
     * Returns the y-coordinate of the bottom-left corner of the obstacle.
     *
     * @return the y-coordinate of the bottom-left corner
     */
    @Override
    public int getBottomLeftY() {
        return this.position.getY();
    }

    /**
     * Returns the size of the obstacle.
     *
     * @return the size of the obstacle
     */
    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getX() {
        return this.getBottomLeftX();
    }

    @Override
    public int getY() {
        return this.getBottomLeftY();
    }
    /**
     * Checks if the obstacle blocks the specified position.
     *
     * @param position the position to check
     * @param robot    the robot to ignore during the check
     * @return an array with the result of the check: [true] if the position is blocked by the obstacle,
     *         [true, otherRobot] if the position is blocked by another robot, or [false] if the position is not blocked
     */
    public static Object[] blocksPosition(Position position, Robot robot) {
        int positionX = position.getX();
        int positionY = position.getY();

        // Check for obstacles
        for (Position obstacle : obstacles) {
            int obstacleRight = obstacle.getX() + size;
            int obstacleTop = obstacle.getY() + size*2;

            boolean isOverlapX = (obstacle.getX() <= positionX && positionX < obstacleRight + size) ||
                                (positionX <= obstacle.getX() && obstacle.getX() < positionX + size);
            
            boolean isOverlapY = (obstacle.getY() <= positionY && positionY < obstacleTop) ||
                                (positionY <= obstacle.getY() && obstacle.getY() < positionY + size);

            if (isOverlapX && isOverlapY) {
                return new Object[]{true};
            }
        }


        // Check for other robots
        for (Robot otherRobot : World.robots) {
            // Ignore the current robot
            if (otherRobot == robot) {
                continue;
            }
        
            // Calculate the center coordinates of each square
            int otherRobotX = otherRobot.getPosition().getX();
            int otherRobotY = otherRobot.getPosition().getY();
            int robotPositionX = position.getX();
            int robotPositionY = position.getY();

            // Calculate the distance between the centers of the squares
            int distanceX = Math.abs(otherRobotX - robotPositionX);
            int distanceY = Math.abs(otherRobotY - robotPositionY);
        
            // Check if the current position is inside the other robot's boundary
            if (distanceX < size*2 && distanceY < size*2) {
                return new Object[]{true, otherRobot};
            }
        }

        return new Object[]{false};
    }
    

    /**
     * Checks if this obstacle blocks the path that goes from coordinate (x1, y1) to (x2, y2).
     * Since our robot can only move in horizontal or vertical lines (no diagonals yet), we can assume that either x1==x2 or y1==y2.
     * @param a first position
     * @param b second position
     * @return `true` if this obstacle is in the way
     */
    public static Object[] blocksPath(Position a, Position b, Robot robot) {
        if (a.getX() == b.getX()) { // y is changing
            return blocksYPath(a, b, robot);
        }
        else {
            return blocksXPath(a, b, robot);
        }
    }

    /**
     * Checks if the obstacle blocks the path from position a to position b.
     *
     * @param a     the starting position
     * @param b     the ending position
     * @param robot the robot to ignore during the check
     * @return an array with the result of the check: [true] if the path is blocked by the obstacle,
     *         [true, otherRobot] if the path is blocked by another robot, or [false] if the path is not blocked
     */
    public static Object[] blocksYPath(Position a, Position b, Robot robot) {
        if ( b.getY() > a.getY()) { // moving up
            for (int i=a.getY(); i <= b.getY(); i++) {
                Object[] result = blocksPosition(new Position(a.getX(), i), robot);
                if ((boolean) result[0]) {
                    return result;
                }
            }
        }
        else{
            for (int i=a.getY(); i >= b.getY(); i--) {
                Object[] result = blocksPosition(new Position(a.getX(), i), robot);
                if ((boolean) result[0]) {
                    return result;
                }
            }
        }
        return new Object[]{false};
    }

    // a = oldPos, b = newPos
    public static Object[] blocksXPath(Position a, Position b, Robot robot) {
        if ( b.getX() > a.getX()) { // robot moving to the right
            for (int i=a.getX(); i <= b.getX(); i++) {
                Object[] result = blocksPosition(new Position(i, a.getY()), robot);
                if ((boolean) result[0]) {
                    return result;
                }
            }
        }
        else{
            for (int i=a.getX(); i >= b.getX(); i--) {
                Object[] result = blocksPosition(new Position(i, a.getY()), robot);
                if ((boolean) result[0]) {
                    return result;
                }
            }
        }
        return new Object[]{false};
    }

    /**
     * Returns a string representation of the SquareObstacle.
     *
     * @return a string representation of the SquareObstacle
     */
    @Override
    public String toString() {
        int endposX = getBottomLeftX() + size;
        int endposY = getBottomLeftY() + size;
        return "-At position (" + getBottomLeftX() + "," + getBottomLeftY() + ") to " + "(" +  endposX + "," + endposY+ ")";
    }
}
