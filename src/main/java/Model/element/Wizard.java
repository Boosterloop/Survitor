package Model.element;

import Model.visitor.Visitor;

/**
 * Implements a Wizard obstacle
 */
public class Wizard extends Obstacle {
    private int speed;

    /**
     * Constructor
     * @param width of the wizard
     * @param height of the wizard
     * @param speed of the wizard
     */
    public Wizard(int width, int height, int speed){
        super(width, height);
        // speed should not be 0
        this.speed = (speed == 0) ? 1 : speed;
        setRandomSpeed();
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
        return "wizard.png";
    }

    /**
     * Returns the radius for a rectangular Shape
     * @param angle to calculate the radius
     * @return radius
     */
    public int getRadius(double angle) {
        return rectangleRadius(angle);
    }

    /**
     * Getter for the speed
     * @return speed of the wizard
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Set a random speed for the wizard, preventing any direction from being 0. (moves in diagonals)
     */
    public void setRandomSpeed() {
        vx = rand.nextInt() % 2 * speed;
        vy = rand.nextInt() % 2 * speed;
        // forcing to move in both directions (diagonals)
        if (vx == 0 || vy == 0) {
            setRandomSpeed();
        }
    }
}
