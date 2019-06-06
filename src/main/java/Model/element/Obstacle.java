package Model.element;

import Model.visitor.Visitable;
import View.MainFrame;

import java.awt.*;
import java.net.URL;
import java.util.Random;

import static java.lang.Math.abs;

public abstract class Obstacle implements Visitable {
    protected int width;
    protected int height;
    protected int x;
    protected int y;

//    protected boolean moved = false;
//    protected boolean changed = false;
    protected static Random rand = new Random();
    private Image image;

    public Obstacle(int width, int height){
        this.width = width;
        this.height = height;
        loadImage();
        newRandomPos();
    }
    public void move (int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void newRandomPos () {
        x = abs(rand.nextInt() % (MainFrame.WIDTH - width - 60)) + 30;
        y = abs(rand.nextInt() % (MainFrame.HEIGHT - height - 60)) + 30;

        if ((x > MainFrame.WIDTH/2 - 50 && x < MainFrame.WIDTH/2 + 50) || (y > MainFrame.HEIGHT/2 - 50 && y < MainFrame.HEIGHT/2 + 50))
            newRandomPos();
    }
    protected void loadImage() {

        final URL url = Thread.currentThread().getContextClassLoader().getResource(getImagePath());
        image =  Toolkit.getDefaultToolkit().getImage(url).getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public abstract String getImagePath ();

//    public boolean hasMoved() {
//        return moved;
//    }
//
//    public boolean hasChanged() {
//        return changed;
//    }

}
