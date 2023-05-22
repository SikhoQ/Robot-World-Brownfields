package server.world;

import server.configuration.ConfigurationManager;
import server.world.util.Position;
import server.world.util.UpdateResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
* Enum used to track direction
*/
enum Direction {
    NORTH, EAST, SOUTH, WEST
}


public class World {
    public static  ConfigurationManager worldConfiguration = new ConfigurationManager();
    protected final Position TOP_LEFT = new Position(-worldConfiguration.getXConstraint(), worldConfiguration.getYConstraint());
    protected final Position BOTTOM_RIGHT = new Position(worldConfiguration.getXConstraint(), -worldConfiguration.getYConstraint());
    private final Position CENTRE =  new Position(0, 0);

    private List<Obstacle> obstacles;
    public static ArrayList<Robot> robots;
    

    public World(){
        robots = new ArrayList<>();
        this.obstacles = createObstacles();
    }

    public static ConfigurationManager getWorldConfiguration() {
        return worldConfiguration;
    }

    private int randomInt(int start, int stop) {
        Random random = new Random();
        return start + random.nextInt(stop - start + 1);
    }

    public List<Obstacle> createObstacles() {
        List<Obstacle> obstacles = new ArrayList<>();
        int numberOfObstacles= randomInt(5,15);
        for (int i = 0; i < numberOfObstacles ; i++) {
            int x = randomInt(TOP_LEFT.getX(), BOTTOM_RIGHT.getX());
            int y = randomInt(BOTTOM_RIGHT.getY(), TOP_LEFT.getY());
            // rather check if the position is blocked.
            if (!SquareObstacle.obstacles.contains(new Position(x, y))) {
                SquareObstacle obstacle = new SquareObstacle(x, y);
                obstacles.add(obstacle);
            }
        }
        return obstacles;
    }

    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }

    public void showObstacles() {
        List<Obstacle> obstacles = getObstacles();
        if (!obstacles.isEmpty()) {
            System.out.println("There are some obstacles:");
            for (int i=0; i < obstacles.size(); i++) {
                System.out.println(obstacles.get(i));
            }
        }
    }

    
    public ArrayList<Robot> getRobots() {
        return robots;
    }
    
    public void addRobotToWorld(Robot robot) {
        robots.add(robot);
    }

    public void removeRobot(Robot robot) {
        robots.remove(robot);
    }

    public boolean robotInWorld(Robot robot) {
        for (Robot robotInWorld : robots) {
            if (robot.getName().equalsIgnoreCase(robotInWorld.getName())) {
                return true;
            }
        }
        return false;
    }

    public Object[] updatePosition(Robot robot, int nrSteps) { // remove isBullet from this method

        int increment = (nrSteps > 0)? 1 : -1;
        Object[] updateResponse = null;

        // move robot by 1 / -1 step until it reaches its destination or it is obstructed / out of safezone
        for (int i=1; i<=Math.abs(nrSteps); i++) {
            updateResponse = updatePosition_helper(robot, increment, false); 
            if (updateResponse[0] != UpdateResponse.SUCCESS) { //obstructed or out of world.
                return updateResponse;
            }
        }

        return updateResponse;
    };

    public Object[] fireGun(Robot robot, int nrSteps) {
        return updatePosition_helper(robot, nrSteps, true); 
    }

    Object[] updatePosition_helper(Robot robot, int nrSteps, Boolean isBullet) {
        int newX = robot.getPosition().getX();
        int newY = robot.getPosition().getY();

        if (Direction.NORTH.equals(robot.getDirection())) {
            newY = newY + nrSteps;
        }
        else if (Direction.EAST.equals(robot.getDirection())) {
            newX = newX + nrSteps;
        }
        else if (Direction.SOUTH.equals(robot.getDirection())) {
            newY = newY - nrSteps;
        }
        else if (Direction.WEST.equals(robot.getDirection()) ) {
            newX = newX - nrSteps;
        }

        Position newPosition = new Position(newX, newY);

        Object[] result = SquareObstacle.blocksPath(robot.getPosition(), newPosition, robot);

        if ((boolean) result[0]) { // path is blocked, either by an obstacle (result.length == 1) || by other robot (result.length == 2).
            if (result.length == 1) {
                return new Object[]{UpdateResponse.FAILED_OBSTRUCTED};
            }else{
                return new Object[]{UpdateResponse.FAILED_OBSTRUCTED, result[1]}; // also return the otherRobot
            }
        }
        else if (newPosition.isIn(this.TOP_LEFT,this.BOTTOM_RIGHT)){ 
            // if path not blocked and in constraint box, check result length. 
            // Only update position if robot is moving not bullet. How? isBullet - true/false.
            if (!isBullet) {
                robot.setPosition(newPosition);
            }
            return new Object[]{UpdateResponse.SUCCESS};
        }

        return new Object[]{UpdateResponse.FAILED_OUTSIDE_WORLD};
    };


    public void updateDirection(Robot robot, boolean turnRight){
        if (turnRight) {
            switch (String.valueOf(robot.getDirection())) {
                case "NORTH":
                    robot.setDirection(Direction.EAST);
                    break;
                case "EAST":
                    robot.setDirection(Direction.SOUTH);
                    break;
                case "SOUTH":
                    robot.setDirection(Direction.WEST);
                    break;
                case "WEST":
                    robot.setDirection(Direction.NORTH);
                    break;
            }
        }
        else {
            switch (String.valueOf(robot.getDirection())) {
                case "NORTH":
                    robot.setDirection(Direction.WEST);
                    break;
                case "EAST":
                    robot.setDirection(Direction.NORTH);
                    break;
                case "SOUTH":
                    robot.setDirection(Direction.EAST);
                    break;
                case "WEST":
                    robot.setDirection(Direction.SOUTH);
                    break;
            }
        }
    }

}
