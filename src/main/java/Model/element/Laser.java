package Model.element;

import Model.visitor.Visitor;

public class Laser extends Obstacle {
    private int angularSpeed;

    public Laser(int x, int y, int angularSpeed){
        super(x, y);
        this.angularSpeed = angularSpeed;
    }

    public void accept(Visitor visitor){
        visitor.visit(this);
    }

}
