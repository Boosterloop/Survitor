package View;

import Model.element.Wizard;
import Model.element.Pokemon;
import Model.element.Hole;

import java.awt.*;
import java.net.URL;

public class Dungeon extends Map {

    private Image background;
    private int holeVisitCount = 0;
    private int jumpCount = 0;
    private PlayerView player;

    public Dungeon(int timeout, PlayerView player) {
        super(timeout);
        final URL url = Thread.currentThread().getContextClassLoader().getResource("mapDungeon.jpg");
        background =  Toolkit.getDefaultToolkit().getImage(url).getScaledInstance(MainFrame.WIDTH, MainFrame.HEIGHT, Image.SCALE_DEFAULT);
        this.player = player;
    }

    @Override
    public Image getBg() {
        return background;
    }


    public void visit (Wizard wizard){

        if (jumpCount++ % 47 == 0) {
            int distX = (player.getX() - wizard.getX()) / 2;
            int distY = (player.getY() - wizard.getY()) / 2;
            int newX = player.getX() + distX;
            int newY = player.getY() + distY;
            if (newX > 0 && newX < MainFrame.WIDTH - wizard.getWidth() && newY > 0 && newY < MainFrame.HEIGHT - wizard.getHeight())
                wizard.move(newX, newY);
            else
                wizard.move(wizard.getX() + distX, wizard.getY() + distY);
        }
    }
    public void  visit (Hole hole){

        if(holeVisitCount ++ % 47 == 0)
            hole.setSize(hole.getSize()+10);
    }

    public void visit (Pokemon pokemon){
        //follows player
        int vx, vy;
        vx = player.getX() < pokemon.getX() ? -pokemon.getSpeed() : pokemon.getSpeed();
        vy = player.getY() < pokemon.getY() ? -pokemon.getSpeed() : pokemon.getSpeed();
        pokemon.move(pokemon.getX()+vx, pokemon.getY()+vy);
    }
}
