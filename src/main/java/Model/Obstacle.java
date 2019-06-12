package Model;

import View.MainFrame;

import java.awt.*;
import java.net.URL;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * Define an obstacle
 */
public abstract class Obstacle implements Visitable {
    protected int width;
    protected int height;
    protected int x;
    protected int y;
    protected int vx = 0;
    protected int vy = 0;
    private long lastCollision = 0;
    private int safeZoneSize = 100;

    protected static Random rand = new Random();
    private Image image;

    /**
     * Constructor
     * @param width of obstacle
     * @param height of obstacle
     */
    public Obstacle(int width, int height){
        this.width = width;
        this.height = height;
        loadImage();
        newRandomPos();
    }

    /**
     * Move obstacle to new coordinates
     * @param x coordinate
     * @param y coordinate
     */
    public void move (int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Generate a new position for the obstacle. If it overlaps the player, the function is called again to find a new position.
     */
    public void newRandomPos () {
        x = abs(rand.nextInt() % (MainFrame.WIDTH - width - 60)) + 30;
        y = abs(rand.nextInt() % (MainFrame.HEIGHT - height - 60)) + 30;

        if (  (x > MainFrame.WIDTH/2 - safeZoneSize - width && x < MainFrame.WIDTH/2 + safeZoneSize)
           || (y > MainFrame.HEIGHT/2 - safeZoneSize - height && y < MainFrame.HEIGHT/2 + safeZoneSize))
            // calls itself if obstacle overlaps with player starting zone.
            // -> too big obstacles causes infinite recursion.
            newRandomPos();
    }

    /**
     * Move obstacle of vx and vy, and change their sign if field border is met
     */
    public void moveStraight () {
        if (x + vx >= MainFrame.WIDTH - width || x + vx <= 0)
            vx = -vx;
        if (y + vy >= MainFrame.HEIGHT - height - 20 || y + vy <= 0)
            vy = -vy;
        x += vx;
        y += vy;
    }

    /**
     * Load the obstacle's image
     */
    void loadImage() {

        final URL url = Thread.currentThread().getContextClassLoader().getResource(getImagePath());
        image =  Toolkit.getDefaultToolkit().getImage(url).getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    /**
     * Getter for lastCollision
     * @return lastCollision
     */
    public long getLastCollision() {
        return lastCollision;
    }

    /**
     * Set lastCollision
     * @param lastCollision when last collision happened
     */
    public void setLastCollision(long lastCollision) {
        this.lastCollision = lastCollision;
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
     * Getter for the x coordinate
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for the y coordinate
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    public int getVx() {
        return vx;
    }

    public int getVy() {
        return vy;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    /**
     * Getter for the image
     * @return image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Getter for the image's path. Child classes must override this method to define the path of their image.
     * @return image's path
     */
    public abstract String getImagePath();

    /**
     * Return the radius.
     * child classes must override this method to return their radius depending on the angle.
     * commodity methods for simple shapes are defined below and can be called by subclasses.
     * @param angle to calculate the radius
     * @return radius
     */
    public abstract int getRadius (double angle);

    /**
     * Calculate the radius for oval shapes
     * @param angle to calculate the radius
     * @return radius
     */
    int ovalRadius (double angle) {
        int w2 = width*width;
        int h2 = height*height;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        return (int)Math.sqrt((w2*h2)/(sin*sin*w2+cos*cos*h2)) / 2;
    }

    /**
     * Calculate the radius for round shapes
     * @return radius
     */
    int roundRadius() {
        return width / 2;
    }

    /**
     * Calculate the radius for rectangular shapes
     * @param angle to calculate the radius
     * @return radius
     */
     int rectangleRadius (double angle) {
        double absAngle = Math.abs(angle);
        if (absAngle < 0.5)
            return width/2;
        if (absAngle < 1)
            return (int)(Math.sqrt((double)width*width+height*height)/2);
        return height/2;
    }
}
