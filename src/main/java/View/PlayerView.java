package View;

import Model.GamePanel;
import Model.element.Hole;
import Model.element.Wizard;
import Model.element.Pokemon;
import Model.visitor.Visitor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.URL;

public class PlayerView implements Visitor {
    public static final int PV_MAX = 100;

    private int dx;
    private int dy;
    private int w = 50;
    private int h = 50;
    private int x;
    private int y;
    private Image image;
    private GamePanel gp;
    private int pv = PV_MAX;

    public PlayerView(GamePanel gp) {
        this.gp = gp;
        resetPos();
        loadImage();
    }

    private void loadImage() {

        final URL url = Thread.currentThread().getContextClassLoader().getResource("smiley.png");
        image =  Toolkit.getDefaultToolkit().getImage(url).getScaledInstance(w,h, Image.SCALE_DEFAULT);

    }

    // moves player if border not reached.
    public void move() {

        if (x + dx > 0 && x + dx < gp.getWidth() - w) {
            x += dx;
        }

        if (y + dy > 0 && y + dy < gp.getHeight() - h) {
            y += dy;
        }
    }
    // reset pos to center of screen
    public void resetPos() {
        x = (MainFrame.WIDTH - w) / 2;
        y = (MainFrame.HEIGHT - h) / 2;
    }

    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }

    public int getWidth() {

        return w;
    }

    public int getHeight() {

        return h;
    }

    public Image getImage() {

        return image;
    }

    public int getPv() {
        return pv;
    }

    public void getDamage(int damage) {

        pv -= damage;
        if (pv <= 0) {
            pv = 0;
        }
        System.out.println("took damages, pv left: " + pv);
    }
    public boolean isDead () {return pv <= 0;}

    public void resurrect() {
        pv = PV_MAX;
        resetPos();
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -5;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 5;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -5;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 5;
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }

    public void visit(Wizard wizard) {
        getDamage(22);
    }

    public void visit(Hole hole) {
        getDamage(PV_MAX);
    }

    public void visit(Pokemon pokemon) {
        getDamage(10);
    }
}
