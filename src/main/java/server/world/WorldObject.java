package server.world;

public class WorldObject implements WorldObjectInterface {
    private final String type;
    private final int positionX;
    private final int positionY;
    private final int size;

    public WorldObject(String type, int size, int x, int y) {
        this.type = type;
        this.positionX = x;
        this.positionY = y;
        this.size = size;
    }

    @Override
    public int getX() {
        return this.positionX;
    }

    @Override
    public int getY() {
        return this.positionY;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    public String getType() {
        return type;
    }
}
