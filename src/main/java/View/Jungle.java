package View;

import Model.element.Hole;
import Model.element.Wizard;
import Model.element.Pokemon;

import java.awt.*;
import java.net.URL;

public class Jungle extends Map {

    private Image background;
    private int holeVisitCount = 0;
    private int speedCount = 0;

    public Jungle(int timeout) {
        super(timeout);
        final URL url = Thread.currentThread().getContextClassLoader().getResource("mapJungle.jpg");
        background =  Toolkit.getDefaultToolkit().getImage(url).getScaledInstance(MainFrame.WIDTH, MainFrame.HEIGHT, Image.SCALE_DEFAULT);
    }

    @Override
    public Image getBg() {
        return background;
    }

    public void visit (Wizard wizard){
        // doubling speed and randomizing directions of visited wizard every 41 visit
        if (++speedCount % 41 == 0) {
            wizard.setRandomSpeed();
            wizard.setVx(wizard.getVx() * 2);
            wizard.setVy(wizard.getVy() * 2);
        }
        // setting speed back to normal 18 frames after
        else if ((speedCount + 18) % 41 == 0) {
            wizard.setVx(wizard.getVx() / 2);
            wizard.setVy(wizard.getVy() / 2);
        }
        wizard.moveStraight();
    }
    // grows the visited hole every 43 visit
    public void visit (Hole hole){
        if(holeVisitCount ++ % 43 == 0)
            hole.setSize(hole.getSize()+5);
    }

    public void visit (Pokemon pokemon){
        pokemon.moveRandom();
    }
}
