package client.robots;


public class Robot {

    private String name;
    private int shields;
    private int shots;;
    private State state;
    private String kind = "Generic";

    // class variables, the same for all robots.
    private static int reload;
    private static int repair;
    private static int visibility;

    public Robot(String name) {
        this.name = name;
        this.shields = 0;
        this.shots = 10;
    }

    public int getShields() {
        return shields;
    }

    public int getShots() {
        return shots;
    }

    public String getName() {
        return name;
    }

    public static int getVisibility() {
        return visibility;
    }

    public static void setVisibility(int visibility) {
        Robot.visibility = visibility;
    }

    public static int getReload() {
        return reload;
    }

    public static void setReload(int reload) {
        Robot.reload = reload;
    }

    public static int getRepair() {
        return repair;
    }

    public static void setRepair(int repair) {
        Robot.repair = repair;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setShields(int shields) {
        this.shields = shields;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public void setState(State state) {
        this.state = state;
        // automatically update shots and shields as well.
        setShots(state.getShots());
        setShields(state.getShields());
    }

    @Override
    public String toString() {
        return this.state.getPosition() != null?
                (
                "[" + this.state.getPosition()[0]+ "," + this.state.getPosition()[1] + "] "
                + this.name.toUpperCase() + " <" + this.kind + "> " + this.state.getStatus() 
                )
                : 
                this.name;
    }
}