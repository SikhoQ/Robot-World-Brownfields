package client.robots;

public class Fighter extends Robot {
    public Fighter(String name) {
        super(name);
        setKind("fighter");
        setShields(3);
        setShots(25);
    }
}
