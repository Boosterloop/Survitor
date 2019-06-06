package View;

import Model.element.Laser;
import Model.element.Pokemon;
import Model.element.Trou;

import java.awt.*;
import java.net.URL;

public class Dungeon extends Map {

    private Image background;
    private int holeVisitCount = 0;
    private PlayerView player;

    public Dungeon(int timeout, PlayerView player) {
        super(timeout);
        final URL url = Thread.currentThread().getContextClassLoader().getResource("mapDungeon.jpg");
        background =  Toolkit.getDefaultToolkit().getImage(url);
        this.player = player;
    }

    @Override
    public Image getBg() {
        return background;
    }


    public void visit (Laser laser){

    }
    public void  visit (Trou trou){
        if(holeVisitCount ++ % 71 == 0)
            trou.setSize(trou.getSize()+5);
    }

    public void visit (Pokemon pokemon){
        int vx, vy;
        vx = player.getX() < pokemon.getX() ? -pokemon.getSpeed() : pokemon.getSpeed();
        vy = player.getY() < pokemon.getY() ? -pokemon.getSpeed() : pokemon.getSpeed();
        pokemon.move(pokemon.getX()+vx, pokemon.getY()+vy);
    }
}
