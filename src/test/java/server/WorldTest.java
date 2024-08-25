package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.world.Obstacle;
import server.world.World;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the World class.
 */
public class WorldTest {

    private World world;

    @BeforeEach
    public void setup() {
        world = new World();
    }

    /**
     * Tests the creation of obstacles in the world.
     */
    @Test
    public void testCreateObstacles() {
        List<Obstacle> obstacles = world.createObstacles();
        assertNotNull(obstacles);
        assertFalse(obstacles.isEmpty());
    }

    /**
     * Tests retrieval of obstacles from the world.
     */
    @Test
    public void testGetObstacles() {
        List<Obstacle> obstacles = world.getObstacles();
        assertNotNull(obstacles);
    }
}
