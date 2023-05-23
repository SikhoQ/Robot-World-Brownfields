package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import server.world.Obstacle;
import server.world.World;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class WorldTest {
    private World world;

    @BeforeEach
    public void setup() {
        world = new World();
    }

    @Test
    public void testCreateObstacles() {
        List<Obstacle> obstacles = world.createObstacles();
        assertNotNull(obstacles);
        assertFalse(obstacles.isEmpty());
    }

    @Test
    public void testGetObstacles() {
        List<Obstacle> obstacles = world.getObstacles();
        assertNotNull(obstacles);
    }

}
