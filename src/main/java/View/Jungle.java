package View;

import Model.element.Laser;
import Model.element.Pokemon;
import Model.element.Trou;

import java.awt.*;
import java.net.URL;

public class Jungle extends Map {

    private Image background;
    private int holeVisitCount = 0;

    public Jungle(int timeout) {
        super(timeout);
        final URL url = Thread.currentThread().getContextClassLoader().getResource("mapJungle.jpg");
        background =  Toolkit.getDefaultToolkit().getImage(url);
    }

    @Override
    public Image getBg() {
        return background;
    }

    public void visit (Laser laser){

    }
    public void visit (Trou trou){
        if(holeVisitCount ++ % 101 == 0)
            trou.setSize(trou.getSize()+5);
    }

    public void visit (Pokemon pokemon){
        pokemon.moveRandom();
    }
}
