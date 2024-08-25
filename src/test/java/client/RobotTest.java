package client;

import client.robots.Robot;
import client.robots.util.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Robot class.
 */
class RobotTest {

    private Robot robot;

    @BeforeEach
    void setUp() {
        robot = new Robot("Robot1");
    }

    /**
     * Tests the getShields method to ensure it returns the correct number of shields.
     */
    @Test
    void getShields() {
        assertEquals(0, robot.getShields());
    }

    /**
     * Tests the getShots method to ensure it returns the correct number of shots.
     */
    @Test
    void getShots() {
        assertEquals(10, robot.getShots());
    }

    /**
     * Tests the getName method to ensure it returns the correct name.
     */
    @Test
    void getName() {
        assertEquals("Robot1", robot.getName());
    }

    /**
     * Tests setting and getting the visibility of the robot.
     */
    @Test
    void setVisibility() {
        Robot.setVisibility(5);
        assertEquals(5, Robot.getVisibility());
    }

    /**
     * Tests setting and getting the reload time of the robot.
     */
    @Test
    void setReload() {
        Robot.setReload(3);
        assertEquals(3, Robot.getReload());
    }

    /**
     * Tests setting and getting the repair rate of the robot.
     */
    @Test
    void setRepair() {
        Robot.setRepair(2);
        assertEquals(2, Robot.getRepair());
    }

    /**
     * Tests the setName method to ensure it sets the correct name.
     */
    @Test
    void setName() {
        robot.setName("NewRobot");
        assertEquals("NewRobot", robot.getName());
    }

    /**
     * Tests the getKind method to ensure it returns the kind of the robot.
     */
    @Test
    void getKind() {
        assertNull(robot.getKind());
    }

    /**
     * Tests the setKind method to ensure it sets the correct kind.
     */
    @Test
    void setKind() {
        robot.setKind("Android");
        assertEquals("Android", robot.getKind());
    }

    /**
     * Tests setting and getting the number of shields on the robot.
     */
    @Test
    void setShields() {
        robot.setShields(50);
        assertEquals(50, robot.getShields());
    }

    /**
     * Tests setting and getting the number of shots on the robot.
     */
    @Test
    void setShots() {
        robot.setShots(5);
        assertEquals(5, robot.getShots());
    }

    /**
     * Tests setting and getting the state of the robot.
     */
    @Test
    void setState() {
        State state = new State();
        state.setShots(8);
        state.setShields(20);
        robot.setState(state);
        assertEquals(state, robot.getState());
        assertEquals(8, robot.getShots());
        assertEquals(20, robot.getShields());
    }
}
