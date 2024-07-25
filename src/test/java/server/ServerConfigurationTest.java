package server;

import org.junit.jupiter.api.Test;
import server.world.World;

import static org.junit.jupiter.api.Assertions.*;

public class ServerConfigurationTest {
    @Test
    public void testDefaultConfiguration() {
        Server server = new Server();
        String[] args = {};
        server.configureServer(args);
        World world = new World();

        assertEquals(1, world.getWorldSize());
        assertEquals(5000, server.getPort());
        assertTrue(world.getObstacles().isEmpty());
    }
}
