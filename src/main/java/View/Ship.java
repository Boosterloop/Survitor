package View;

import Model.Hole;
import Model.Pokemon;
import Model.Wizard;

import java.awt.*;
import java.net.URL;

/**
 * Implements the ship map
 */
public class Ship extends Map {

    private Image background;

    /**
     * Constructor
     * @param timeout time to survive on the map
     */
    public Ship(int timeout) {
        super(timeout);
        final URL url = Thread.currentThread().getContextClassLoader().getResource("mapBoat.jpg");
        background =  Toolkit.getDefaultToolkit().getImage(url).getScaledInstance(MainFrame.WIDTH, MainFrame.HEIGHT,
                                                                                  Image.SCALE_DEFAULT);
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
    public void visit(Wizard wizard){
        wizard.moveStraight();
    }

    /**
     * Visit a hole obstacle
     * @param hole to visit
     */
    public void  visit(Hole hole){
        // Do nothing
    }

    /**
     * Visit a pokemon obstacle
     * @param pokemon to visit
     */
    public void visit(Pokemon pokemon){
        pokemon.moveStraight();
    }
}
