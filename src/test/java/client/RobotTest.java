package client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.robots.Robot;
import client.robots.util.State;

import static org.junit.jupiter.api.Assertions.*;

class RobotTest {

    private Robot robot;

    @BeforeEach
    void setUp() {
        robot = new Robot("Robot1");
    }

    @Test
    void getShields() {
        assertEquals(0, robot.getShields());
    }

    @Test
    void getShots() {
        assertEquals(10, robot.getShots());
    }

    @Test
    void getName() {
        assertEquals("Robot1", robot.getName());
    }

    // @Test
    // void getVisibility() {
    //     assertEquals(0, Robot.getVisibility());
    // }

    @Test
    void setVisibility() {
        Robot.setVisibility(5);
        assertEquals(5, Robot.getVisibility());
    }

    // @Test
    // void getReload() {
    //     assertEquals(0, Robot.getReload());
    // }

    @Test
    void setReload() {
        Robot.setReload(3);
        assertEquals(3, Robot.getReload());
    }

    // @Test
    // void getRepair() {
    //     assertEquals(0, Robot.getRepair());
    // }

    @Test
    void setRepair() {
        Robot.setRepair(2);
        assertEquals(2, Robot.getRepair());
    }

    @Test
    void setName() {
        robot.setName("NewRobot");
        assertEquals("NewRobot", robot.getName());
    }

    @Test
    void getKind() {
        assertNull(robot.getKind());
    }

    @Test
    void setKind() {
        robot.setKind("Android");
        assertEquals("Android", robot.getKind());
    }

    @Test
    void setShields() {
        robot.setShields(50);
        assertEquals(50, robot.getShields());
    }

    @Test
    void setShots() {
        robot.setShots(5);
        assertEquals(5, robot.getShots());
    }

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
