package Model;

public abstract class Obstacle {
    private int x;
    private int y;
    public Obstacle(int x, int y){
        this.x = x;
        this.y = y;
    }

    public abstract void accept (Visitor visitor);

}
