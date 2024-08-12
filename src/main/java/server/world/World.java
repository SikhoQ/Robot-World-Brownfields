package server.world;

import server.configuration.Config;
import server.configuration.ConfigurationManager;
import server.world.util.Position;
import server.world.util.UpdateResponse;
import database.DatabaseConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
* Enum used to track direction
*/
enum Direction {
    NORTH, EAST, SOUTH, WEST
}

/**
 * The World class represents the game world, including its configuration, obstacles, and robots.
 */
public class World {
    public static  ConfigurationManager worldConfiguration = new ConfigurationManager();
    protected final Position WORLD_TOP_LEFT = new Position(-worldConfiguration.getXConstraint(), worldConfiguration.getYConstraint());
    protected final Position WORLD_BOTTOM_RIGHT = new Position(worldConfiguration.getXConstraint(), -worldConfiguration.getYConstraint());
    private final Position CENTRE =  new Position(0, 0);

    private List<Obstacle> obstacles;
    public static ArrayList<Robot> robots;
    
    /**
     * Constructs a new World object.
     * Initializes the list of robots and creates obstacles in the world.
     */
    public World(){
        robots = new ArrayList<>();
        this.obstacles = createObstacles();

        System.out.println();
    }

    /**
     * Returns the world configuration.
     *
     * @return the ConfigurationManager object representing the world configuration
     */
    public static ConfigurationManager getWorldConfiguration() {
        return worldConfiguration;
    }

    private int randomInt(int start, int stop) {
        Random random = new Random();
        return start + random.nextInt(stop - start + 1);
    }

    /**
     * Creates a list of obstacles in the world.
     *
     * @return a list of obstacles
     */
    public List<Obstacle> createObstacles() {
        List<Obstacle> obstacles = new ArrayList<>();

        String obstacle = Config.OBSTACLE;

        if (!obstacle.equalsIgnoreCase("none") && obstacle.contains(",")) {
            try {
                // need to add logic to get bottom-left since these are top-right. Obstacle size = Config.TILE_SIZE
                int x = Integer.parseInt(obstacle.split(",")[0]);
                int y = Integer.parseInt(obstacle.split(",")[1]);

                if (!SquareObstacle.obstacles.contains(new Position(x, y))) {
                    SquareObstacle squareObstacle = new SquareObstacle(x, y);
                    obstacles.add(squareObstacle);
                }
            } catch (NumberFormatException e) {
                System.err.println("Expected integer values for x and y.");
            }
        }
        return obstacles;
    }

    /**
     * Returns the list of obstacles in the world.
     *
     * @return the list of obstacles
     */
    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }

    /**
     * Displays the obstacles in the world.
     * Prints the position of each obstacle to the console.
     */
    public void showObstacles() {
        List<Obstacle> obstacles = getObstacles();
        if (!obstacles.isEmpty()) {
            System.out.println("There are some obstacles:");
            for (int i=0; i < obstacles.size(); i++) {
                System.out.println(obstacles.get(i));
            }
        }
    }

    /**
     * Returns the list of robots in the world.
     *
     * @return the list of robots
     */
    public ArrayList<Robot> getRobots() {
        return robots;
    }

    public int getWorldSize() {
        return ConfigurationManager.getWorldSize();
    }
    /**
     * Adds a robot to the world.
     *
     * @param robot the robot to add
     */
    public void addRobotToWorld(Robot robot) {
        robots.add(robot);
    }

    /**
     * Removes a robot from the world.
     *
     * @param robot the robot to remove
     */
    public void removeRobot(Robot robot) {
        robots.remove(robot);
    }

    /**
     * Checks if a robot is in the world.
     *
     * @param robot the robot to check
     * @return true if the robot is in the world, false otherwise
     */
    public boolean robotInWorld(Robot robot) {
        for (Robot robotInWorld : robots) {
            if (robot.getName().equalsIgnoreCase(robotInWorld.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean saveToDatabase(String worldName) {
        if (worldName == null || worldName.isEmpty()) {
            return false;
        }

        // Check if world already exists
        if (DatabaseConnection.worldExists(worldName)) {
            // Handle existing world scenario
            // Optionally, you could ask for overwrite confirmation or handle accordingly
            System.out.println("World with the name '" + worldName + "' already exists.");
            return false;
        }
        return false;
    }

    public boolean restoreFromDatabase(String worldName) {
        // Use DatabaseConnection class to restore the world with the provided name
        return true; // return true if restore was successful, false otherwise
    }

    /**
     * Updates the position of a robot in the world.
     *
     * @param robot    the robot to update
     * @param nrSteps  the number of steps to move the robot
     * @param isBullet indicates if the update is for a bullet (true) or robot movement (false)
     * @return an array containing the update response, which can be UpdateResponse.SUCCESS, UpdateResponse.FAILED_OBSTRUCTED,
     * or UpdateResponse.FAILED_OUTSIDE_WORLD, and additional information if applicable
     */
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

    /**
     * Helper method to update the position of a robot in the world.
     *
     * @param robot    the robot to update
     * @param nrSteps  the number of steps to move the robot
     * @param isBullet indicates if the update is for a bullet (true) or robot movement (false)
     * @return an array containing the update response, which can be UpdateResponse.SUCCESS or UpdateResponse.FAILED_OBSTRUCTED,
     * and additional information if another robot obstructs the path
     */
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
        else if (newPosition.isIn(this.WORLD_TOP_LEFT,this.WORLD_BOTTOM_RIGHT)){
            // if path not blocked and in constraint box, check result length. 
            // Only update position if robot is moving not bullet. How? isBullet - true/false.
            if (!isBullet) {
                robot.setPosition(newPosition);
            }
            return new Object[]{UpdateResponse.SUCCESS};
        }

        return new Object[]{UpdateResponse.FAILED_OUTSIDE_WORLD};
    };

    /**
     * Updates the direction of a robot in the world.
     *
     * @param robot     the robot to update
     * @param turnRight indicates whether to turn the robot right (true) or left (false)
     */
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
