package client.robots;

public class Sniper extends Robot{
    public Sniper(String name) {
        super(name);
        setKind("sniper");
        setShields(1);
        setShots(20);
    }
}
