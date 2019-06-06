package Model.element;

import Model.visitor.Visitor;

public class Laser extends Obstacle {
    private int angularSpeed;

    public Laser(int width, int height, int angularSpeed){
        super(width, height);
        this.angularSpeed = angularSpeed;
    }

    public void accept(Visitor visitor){
        visitor.visit(this);
    }

    public String getImagePath() {
        return "laser.png";
    }
}
