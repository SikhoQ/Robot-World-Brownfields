package server;

import org.junit.jupiter.api.Test;
import server.world.util.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Position class.
 */
public class PositionTest {

    /**
     * Tests the getX method to ensure it returns the correct X coordinate.
     */
    @Test
    void testGetPositionX() {
        Position position = new Position(3, 5);
        assertEquals(3, position.getX());
    }

    /**
     * Tests the getY method to ensure it returns the correct Y coordinate.
     */
    @Test
    void testGetPositionY() {
        Position position = new Position(3, 5);
        assertEquals(5, position.getY());
    }

    /**
     * Tests the asArray method to ensure it returns the position as an array.
     */
    @Test
    void testPositionAsArray() {
        Position position = new Position(3, 5);
        int[] expected = { 3, 5 };
        assertArrayEquals(expected, position.asArray());
    }

    /**
     * Tests the equals method to ensure correct equality checks between positions.
     */
    @Test
    void testEquals() {
        Position position1 = new Position(3, 5);
        Position position2 = new Position(3, 5);
        Position position3 = new Position(4, 6);

        assertEquals(position1, position2);
        assertNotEquals(position1, position3);
    }

    /**
     * Tests the isIn method to check if a position is within a given rectangular area.
     */
    @Test
    void testIsIn() {
        Position position = new Position(3, 5);
        Position topLeft = new Position(2, 6);
        Position bottomRight = new Position(4, 4);

        assertTrue(position.isIn(topLeft, bottomRight));

        topLeft = new Position(2, 4);
        bottomRight = new Position(4, 6);

        assertFalse(position.isIn(topLeft, bottomRight));
    }

    /**
     * Tests the toString method to ensure it returns the correct string representation of the position.
     */
    @Test
    void testToString() {
        Position position = new Position(3, 5);
        assertEquals("[3,5]", position.toString());
    }
}
