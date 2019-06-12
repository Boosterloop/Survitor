package View;

import Model.GamePanel;
import Model.element.Hole;
import Model.element.Wizard;
import Model.element.Pokemon;
import Model.visitor.Visitor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.URL;

/**
 * Implements the player
 */
public class PlayerView implements Visitor {
    public static final int PV_MAX = 100;

    private int dx;
    private int dy;
    private int width = 50;
    private int height = 50;
    private int x;
    private int y;
    private Image image;
    private GamePanel gp;
    private int pv = PV_MAX;

    /**
     * Constructor
     * @param gp GamePanel
     */
    public PlayerView(GamePanel gp) {
        this.gp = gp;
        resetPos();
        loadImage();
    }

    /**
     * Load player's image
     */
    private void loadImage() {

        final URL url = Thread.currentThread().getContextClassLoader().getResource("smiley.png");
        image =  Toolkit.getDefaultToolkit().getImage(url).getScaledInstance(width, height, Image.SCALE_DEFAULT);

    }

    /**
     * Move the player
     */
    public void move() {

        if (x + dx > 0 && x + dx < gp.getWidth() - width) {
            x += dx;
        }

        if (y + dy > 0 && y + dy < gp.getHeight() - height) {
            y += dy;
        }
    }

    /**
     * Reset player's position to center of screen
     */
    public void resetPos() {
        x = (MainFrame.WIDTH - width) / 2;
        y = (MainFrame.HEIGHT - height) / 2;
    }

    /**
     * Getter for x coordinate
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for y coordinate
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Getter for width
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Getter for height
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Getter for the image
     * @return image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Getter for health points
     * @return health points
     */
    public int getPv() {
        return pv;
    }

    /**
     * Calculate the new value for health points
     * @param damage health points lost
     */
    public void getDamage(int damage) {

        pv -= damage;
        if (pv <= 0) {
            pv = 0;
        }
        System.out.println("took damages, pv left: " + pv);
    }

    /**
     * Check if the player is dead
     * @return true if he is, false if not
     */
    public boolean isDead () {
        return pv <= 0;
    }

    /**
     * Resurrect player
     */
    public void resurrect() {
        pv = PV_MAX;
        resetPos();
    }

    /**
     * Check if the keys to move the player were pressed
     * @param e event
     */
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

    /**
     * Check if the keys to move the player were released
     * @param e event
     */
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

    /**
     * Visit a wizard obstacle
     * @param wizard to visit
     */
    public void visit(Wizard wizard) {
        getDamage(22);
    }

    /**
     * Visit a hole obstacle
     * @param hole to visit
     */
    public void visit(Hole hole) {
        getDamage(PV_MAX);
    }

    /**
     * Visit a pokemon obstacle
     * @param pokemon to visit
     */
    public void visit(Pokemon pokemon) {
        getDamage(10);
    }
}
