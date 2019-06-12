package View;

import Model.element.Wizard;
import Model.element.Pokemon;
import Model.element.Hole;

import java.awt.*;
import java.net.URL;

/**
 * Implements the dungeon map
 */
public class Dungeon extends Map {

    private Image background;
    private int holeVisitCount = 0;
    private int jumpCount = 0;
    private PlayerView player;

    /**
     * Constructor
     * @param timeout time to survive on the map
     * @param player to make Pokemon follow him
     */
    public Dungeon(int timeout, PlayerView player) {
        super(timeout);
        final URL url = Thread.currentThread().getContextClassLoader().getResource("mapDungeon.jpg");
        background =  Toolkit.getDefaultToolkit().getImage(url).getScaledInstance(MainFrame.WIDTH, MainFrame.HEIGHT, Image.SCALE_DEFAULT);
        this.player = player;
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
        // makes the wizard jump from half of its distance to the player, on the opposite side if in the
        // field, on the same side otherwise.
        if (jumpCount++ % 47 == 0) {
            int distX = (player.getX() - wizard.getX()) / 2;
            int distY = (player.getY() - wizard.getY()) / 2;
            int oppositeX = player.getX() + distX;
            int oppositeY = player.getY() + distY;
            if (oppositeX > 0 && oppositeX < MainFrame.WIDTH - wizard.getWidth() && oppositeY > 0 && oppositeY < MainFrame.HEIGHT - wizard.getHeight())
                wizard.move(oppositeX, oppositeY);
            else
                wizard.move(wizard.getX() + distX, wizard.getY() + distY);
        }
    }

    /**
     * Visit a hole obstacle
     * @param hole to visit
     */
    public void  visit (Hole hole){
        // grows the visited hole every 47 visit
        if(holeVisitCount ++ % 47 == 0)
            hole.setSize(hole.getSize()+10);
    }

    /**
     * Visit a pokemon obstacle
     * @param pokemon to visit
     */
    public void visit (Pokemon pokemon){
        //follows player
        int dx, dy;
        dx = player.getX() < pokemon.getX() ? -pokemon.getSpeed() : pokemon.getSpeed();
        dy = player.getY() < pokemon.getY() ? -pokemon.getSpeed() : pokemon.getSpeed();
        pokemon.move(pokemon.getX()+dx, pokemon.getY()+dy);
    }
}
