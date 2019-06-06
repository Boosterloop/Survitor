package Model.element;

import Model.visitor.Visitor;

public class Trou extends Obstacle {
    public Trou (int width, int height){
        super(width, height);
    }
    public void accept(Visitor visitor){
        visitor.visit(this);
    }

    public String getImagePath() {
        return "trou.png";
    }

    public void setSize(int size) {
        width = size;
        height = size;
        loadImage();
    }

    public int getSize() {
        return width;
    }
}
