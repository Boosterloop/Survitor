package View;

import Model.element.Hole;
import Model.element.Wizard;
import Model.element.Pokemon;

import java.awt.*;
import java.net.URL;

/**
 * Implements the jungle map
 */
public class Jungle extends Map {

    private Image background;
    private int holeVisitCount = 0;
    private int speedCount = 0;

    /**
     * Constructor
     * @param timeout time to survive on the map
     */
    public Jungle(int timeout) {
        super(timeout);
        final URL url = Thread.currentThread().getContextClassLoader().getResource("mapJungle.jpg");
        background =  Toolkit.getDefaultToolkit().getImage(url).getScaledInstance(MainFrame.WIDTH, MainFrame.HEIGHT, Image.SCALE_DEFAULT);
    }

    /**
     * Getter for the background image of the map
     * @return background image
     */
    @Override
    public Image getBg() {
        return background;
    }

    /**
     * Visit a wizard obstacle
     * @param wizard to visit
     */
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

    /**
     * Visit a hole obstacle
     * @param hole to visit
     */
    public void visit (Hole hole){
        // grows the visited hole every 43 visit
        if(holeVisitCount ++ % 43 == 0)
            hole.setSize(hole.getSize()+5);
    }

    /**
     * Visit a pokemon obstacle
     * @param pokemon to visit
     */
    public void visit (Pokemon pokemon){
        pokemon.moveRandom();
    }
}
