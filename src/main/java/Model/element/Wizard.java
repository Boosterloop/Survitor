package Model.element;

import Model.visitor.Visitor;

public class Wizard extends Obstacle {
    private int speed;

    // speed should not be 0
    public Wizard(int width, int height, int speed){
        super(width, height);
        this.speed = (speed == 0) ? 1 : speed;
        setRandomSpeed();
    }

    public void accept(Visitor visitor){
        visitor.visit(this);
    }

    public String getImagePath() {
        return "wizard.png";
    }

    public int getRadius(double angle) {
        return rectangleRadius(angle);
    }

    public int getSpeed() {
        return speed;
    }
    public void setRandomSpeed() {
        vx = rand.nextInt() % 2 * speed;
        vy = rand.nextInt() % 2 * speed;
        // forcing to move in both directions (diagonals)
        if (vx == 0 || vy == 0) setRandomSpeed();
    }
}
