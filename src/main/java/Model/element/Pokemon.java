package Model.element;

import Model.visitor.Visitor;

/**
 * Implements a Wizard obstacle
 */
public class Pokemon extends Obstacle {
    private int speed;

    // if we only use a round pokemon we should make a constructor with "size" like Hole
    // (and call the class "Voltorb" or "roundPkmn")
    /**
     * Constructor
     * @param width of Pokemon
     * @param height of Pokemon
     * @param speed of Pokemon
     */
    public Pokemon (int width, int height, int speed){
        super(width, height);
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
     * Calculate the radius for a round Pokemon
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
