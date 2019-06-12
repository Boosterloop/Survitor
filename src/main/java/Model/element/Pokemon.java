package Model.element;

import Model.visitor.Visitor;

/**
 * Implements a Pokemon obstacle
 */
public class Pokemon extends Obstacle {
    private int speed;

    /**
     * Constructor
     * @param size of Pokemon
     * @param speed of Pokemon
     */
    public Pokemon (int size, int speed){
        super(size, size);
        this.speed = speed;
        vx = speed * (rand.nextInt()%2);
        vy = speed * (rand.nextInt()%2);
    }

    /**
     * Accept a visitor
     * @param visitor to accept
     */
    public void accept (Visitor visitor){
        visitor.visit(this);
    }

    /**
     * Move the Pokemon in a random manner
     */
    public void moveRandom() {
        moveStraight();
        if (rand.nextInt() % 20 == 0) {
            vx = speed * (rand.nextInt()%2);
            vy = speed * (rand.nextInt()%2);
        }
    }

    /**
     * Get the image's path
     * @return image's path
     */
    public String getImagePath() {
        return "voltorb.png";
    }

    /**
     * Returns the radius for a round Shape
     * @param angle to calculate the radius
     * @return radius
     */
    public int getRadius(double angle) {
        return roundRadius();
    }

    /**
     * Getter for the speed
     * @return speed of the Pokemon
     */
    public int getSpeed() {
        return speed;
    }
}
