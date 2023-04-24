package server.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TextWorld extends AbstractWorld{

    private List<Obstacle> obstacles;

    public TextWorld(){
        super();
        this.obstacles = createObstacles();
    }

    public List<Obstacle> createObstacles() {
        List<Obstacle> obstacles = new ArrayList<>();
        SquareObstacle obstacle = new SquareObstacle(20, 40);
        obstacles.add(obstacle);
        return obstacles;
    }

    @Override
    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }

    @Override
    public void showObstacles() {
        List<Obstacle> obstacles = getObstacles();
        if (!obstacles.isEmpty()) {
            System.out.println("There are some obstacles:");
            for (int i=0; i < obstacles.size(); i++) {
                System.out.println(obstacles.get(i));
            }
        }
    }
}
