package server;

import org.junit.jupiter.api.Test;
import server.world.World;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for server configuration.
 */
public class ServerConfigurationTest {

    /**
     * Tests the default server configuration to ensure default values are set correctly.
     */
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
