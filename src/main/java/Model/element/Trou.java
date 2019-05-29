package Model.element;

import Model.element.Obstacle;
import Model.visitor.Visitor;

public class Trou extends Obstacle {
    private int size;
    public Trou (int x, int y, int size){
        super(x, y);
        this.size=size;
    }
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
