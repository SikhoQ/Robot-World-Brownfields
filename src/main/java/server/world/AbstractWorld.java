package server.world;

import java.util.List;

// import ch.qos.logback.classic.db.SQLBuilder;

public abstract class AbstractWorld implements IWorld{

    protected final Position TOP_LEFT = new Position(-100,200);
    protected final Position BOTTOM_RIGHT = new Position(100,-200);

    protected Position position;
    protected Direction currentDirection;

    public AbstractWorld() {
        this.position = CENTRE;
        this.currentDirection = Direction.NORTH;
    }

    public Position getPosition() {
        return this.position;
    }

    public Direction getCurrentDirection() {
        return this.currentDirection;
    };

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    };

    public boolean isNewPositionAllowed(Position position) {
        Position currentPosition = getPosition();
        Obstacle dummyObst = new SquareObstacle(0, 0);
        if (dummyObst.blocksPath(currentPosition, position)){
            return false;
        }else if (!position.isIn(TOP_LEFT, BOTTOM_RIGHT)) {
            return false;
        }
        return true;
    }

    public boolean isAtEdge() {
        return true;
    }
}
