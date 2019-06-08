package Model.element;

import Model.visitor.Visitor;

public class Pokemon extends Obstacle {
    private int speed;


    public Pokemon (int width, int height, int vitesse){
        super(width, height);
        this.speed = vitesse;
        vx = vitesse * (rand.nextInt()%2);
        vy = vitesse * (rand.nextInt()%2);
    }

    public void accept (Visitor visitor){
        visitor.visit(this);
    }

    public void moveRandom() {
        moveStraight();
        if (rand.nextInt() % 20 == 0) {
            vx = speed * (rand.nextInt()%2);
            vy = speed * (rand.nextInt()%2);
        }
    }

    public String getImagePath() {
        return "voltorb.png";
    }

    public int getRadius(double angle) {
        return roundRadius();
    }

    public int getSpeed() {
        return speed;
    }
}
