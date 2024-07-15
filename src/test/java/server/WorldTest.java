package server;

import org.junit.jupiter.api.Test;

import server.world.Obstacle;
import server.world.World;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class WorldTest {
    private World world;

    @Test
    public void testCreateObstacles() {
        world = new World();

        List<Obstacle> obstacles = world.createObstacles();
        assertNotNull(obstacles);
        assertFalse(obstacles.isEmpty());
    }

    @Test
    public void testGetObstacles() {
        world = new World();

        List<Obstacle> obstacles = world.getObstacles();
        assertNotNull(obstacles);
    }

}
