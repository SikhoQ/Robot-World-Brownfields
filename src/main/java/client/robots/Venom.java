package client.robots;

public class Venom extends Robot{
    public Venom(String name) {
        super(name);
        setKind("venom");
        setShields(15);
        setShots(5);
    }
}
