package View;

import Model.element.*;

import java.awt.*;
import java.net.URL;

public class Ship extends Map {

    private Image background;

    public Ship(int timeout) {
        super(timeout);
        final URL url = Thread.currentThread().getContextClassLoader().getResource("mapBoat.jpg");
        background =  Toolkit.getDefaultToolkit().getImage(url);
    }

    @Override
    public Image getBg() {
        return background;
    }

    public void visit(Laser laser){

    }
    public void  visit(Trou trou){

    }

    public void visit(Pokemon pokemon){
        pokemon.moveStraight();
    }
}
