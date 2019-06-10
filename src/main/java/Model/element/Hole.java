package Model.element;

import Model.visitor.Visitor;

public class Hole extends Obstacle {
    public Hole(int size){
        super(size, size/2);
    }
    public void accept(Visitor visitor){
        visitor.visit(this);
    }

    public String getImagePath() {
        return "trou.png";
    }

    public int getRadius(double angle) {
        return ovalRadius(angle);
    }

    public void setSize(int size) {
        width = size;
        height = size/2;
        loadImage();
    }

    public int getSize() {
        return width;
    }
}
