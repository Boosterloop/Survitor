package Model.element;

import Model.visitor.Visitor;

/**
 * Implements a hole obstacle
 */
public class Hole extends Obstacle {

    /**
     * Constructor
     * @param size of hole
     */
    public Hole(int size){
        super(size, size/2);
    }

    /**
     * Accept a visitor
     * @param visitor to accept
     */
    public void accept(Visitor visitor){
        visitor.visit(this);
    }

    /**
     * Get the image's path
     * @return image's path
     */
    public String getImagePath() {
        return "trou.png";
    }

    /**
     * Calculate the radius
     * @param angle to calculate the radius
     * @return radius
     */
    public int getRadius(double angle) {
        return ovalRadius(angle);
    }

    /**
     * Set size of hole
     * @param size of hole
     */
    public void setSize(int size) {
        width = size;
        height = size/2;
        loadImage();
    }

    /**
     * Getter for size
     * @return size of hole
     */
    public int getSize() {
        return width;
    }
}
