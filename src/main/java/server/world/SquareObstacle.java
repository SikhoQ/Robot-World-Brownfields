package server.world;

import java.util.ArrayList;
import java.util.List;

public class SquareObstacle implements Obstacle{
    private Position position;
    public static List<Position> obstacles = new ArrayList<>();

    private static int size = 5;

    public SquareObstacle(int x, int y) {
        this.position = new Position(x, y);
        obstacles.add(position);
    }

    public int getBottomLeftX() {
        return this.position.getX();
    }

    public int getBottomLeftY() {
        return this.position.getY();
    }

    public int getSize() {
        return size;
    }

    /**
     * Checks if this obstacle blocks access to the specified position.
     * @param position the position to check
     * @return return `true` if the x,y coordinate falls within the obstacle's area
     */
    // public static boolean blocksPosition(Position position, Robot robot) {
    //     // Check for obstacles
    //     for (Position obstacle: obstacles) {
    //         if ((obstacle.getX() <= position.getX() && position.getX() < obstacle.getX() + size) &&
    //                 (obstacle.getY() <= position.getY() && position.getY() < obstacle.getY() + size)) {
    //             return true;
    //         }
    //     }
    
    //     // Check for other robots
    //     for (Robot otherRobot: World.robots) {
    //         // Ignore the current robot
    //         if (otherRobot == robot) {
    //             continue;
    //         }
    
    //         // Calculate the other robot's boundary
    //         int otherX1 = otherRobot.getPosition().getX();
    //         int otherY1 = otherRobot.getPosition().getY();
    //         int otherX2 = otherX1 + size;
    //         int otherY2 = otherY1 + size;

    //         // System.out.println("position: " + position);
    //         // System.out.println("otherX1 <= position.getX(): " + (otherX1 <= position.getX()) + " position.getX() < otherX2: " +( position.getX() < otherX2));
    //         // System.out.println("otherY1 <= position.getY(): " + (otherY1 <= position.getY() )+ " position.getY() < otherY2): " +( position.getY() < otherY2));
    //         // System.out.println();
    
    //         // Check if the current position is inside the other robot's boundary
    //         if ((otherX1 <= position.getX() && position.getX() < otherX2) &&
    //                 (otherY1 <= position.getY() && position.getY() < otherY2)) {
    //             return true;
    //         }
    //     }
        
    //     return false;
    // }

    public static Object[] blocksPosition(Position position, Robot robot) {
        // Check for obstacles
        for (Position obstacle: obstacles) {
            if ((obstacle.getX() <= position.getX() && position.getX() < obstacle.getX() + size) &&
                    (obstacle.getY() <= position.getY() && position.getY() < obstacle.getY() + size)) {
                return new Object[]{true};
            }
        }
    
        // Check for other robots
        for (Robot otherRobot: World.robots) {
            // Ignore the current robot
            if (otherRobot == robot) {
                continue;
            }
    
            // Calculate the other robot's boundary
            int otherX1 = otherRobot.getPosition().getX();
            int otherY1 = otherRobot.getPosition().getY();
            int otherX2 = otherX1 + size;
            int otherY2 = otherY1 + size;

            // System.out.println("position: " + position);
            // System.out.println("otherX1 <= position.getX(): " + (otherX1 <= position.getX()) + " position.getX() < otherX2: " +( position.getX() < otherX2));
            // System.out.println("otherY1 <= position.getY(): " + (otherY1 <= position.getY() )+ " position.getY() < otherY2): " +( position.getY() < otherY2));
            // System.out.println();
    
            // Check if the current position is inside the other robot's boundary
            if ((otherX1 <= position.getX() && position.getX() < otherX2) &&
                    (otherY1 <= position.getY() && position.getY() < otherY2)) {
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

    public static Object[] blocksXPath(Position a, Position b, Robot robot) {
        if ( b.getX() > a.getX()) { // robot moving up
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


    @Override
    public String toString() {
        int endposX = getBottomLeftX() + 5;
        int endposY = getBottomLeftY() + 5;
        return "-At position " + getBottomLeftX() + "," + getBottomLeftY() + " " + "(" +  endposX + "," + endposY+ ")";
    }
}
