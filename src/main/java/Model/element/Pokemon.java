package Model.element;

import Model.visitor.Visitor;
import View.MainFrame;

public class Pokemon extends Obstacle {
    private int speed;
    private int vx ;
    private int vy ;

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
        if (rand.nextInt() % 50 == 0) {
            vx = speed * (rand.nextInt()%2);
            vy = speed * (rand.nextInt()%2);
        }
    }
    public void moveStraight () {
        if (x + vx >= MainFrame.WIDTH - width || x + vx <= 0)
            vx = -vx;
        if (y + vy >= MainFrame.HEIGHT - height || y + vy <= 0)
            vy = -vy;
        x += vx;
        y += vy;
    }

    public String getImagePath() {
        return "voltorb.png";
    }

    public int getSpeed() {
        return speed;
    }
}
