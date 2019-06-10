package View;

import Model.element.*;

import java.awt.*;
import java.net.URL;

public class Ship extends Map {

    private Image background;

    public Ship(int timeout) {
        super(timeout);
        final URL url = Thread.currentThread().getContextClassLoader().getResource("mapBoat.jpg");
        background =  Toolkit.getDefaultToolkit().getImage(url).getScaledInstance(MainFrame.WIDTH, MainFrame.HEIGHT, Image.SCALE_DEFAULT);
    }

    @Override
    public Image getBg() {
        return background;
    }

    public void visit(Wizard wizard){
        wizard.moveStraight();
    }
    public void  visit(Hole hole){

    }

    public void visit(Pokemon pokemon){
        pokemon.moveStraight();
    }
}
