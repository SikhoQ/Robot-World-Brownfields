package client;

import org.junit.jupiter.api.Test;

/**
 * As a player
 * I want my robot to be able to view an area around it in all four directions(North, South, West, East).
 * So that it can see all the world obstacles and robots around it.
 */

class LookTests {
    /**
     *     get state(gives position of robot)
     *     check for any robots or obstacles in world in look range
     *     check for robots and obstacles in path from current robot position
     *     to (x+70:y), (x-70:y), (x:y-70), (x:y+70)
     *     place obstacles at certain mock positions
      * @param position
     * @return
     */

    public static final int VISIBILITY = 70;

    @Test
    void worldIsEmpty() {

    }
}
