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
    protected int vx = 0;
    protected int vy = 0;
    private long lastCollision = 0;
    private int safeZoneSize = 100;

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

    // calls itself if obstacle overlaps with player starting zone.
    // -> too big obstacles causes infinite recursion.
    public void newRandomPos () {
        x = abs(rand.nextInt() % (MainFrame.WIDTH - width - 60)) + 30;
        y = abs(rand.nextInt() % (MainFrame.HEIGHT - height - 60)) + 30;

        if (  (x > MainFrame.WIDTH/2 - safeZoneSize - width && x < MainFrame.WIDTH/2 + safeZoneSize)
           || (y > MainFrame.HEIGHT/2 - safeZoneSize - height && y < MainFrame.HEIGHT/2 + safeZoneSize))
            newRandomPos();
    }

    // moves obstacle of vx and vy, and change their sign if field border is met.
    public void moveStraight () {
        if (x + vx >= MainFrame.WIDTH - width || x + vx <= 0)
            vx = -vx;
        if (y + vy >= MainFrame.HEIGHT - height - 20 || y + vy <= 0)
            vy = -vy;
        x += vx;
        y += vy;
    }

    protected void loadImage() {

        final URL url = Thread.currentThread().getContextClassLoader().getResource(getImagePath());
        image =  Toolkit.getDefaultToolkit().getImage(url).getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    public long getLastCollision() {
        return lastCollision;
    }

    public void setLastCollision(long lastCollision) {
        this.lastCollision = lastCollision;
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

    public Image getImage() {
        return image;
    }

    public abstract String getImagePath ();

    // child classes must override this method to return their radius depending on the angle.
    // commodity methods for simple shapes are defined below and can be called by subclasses.
    public abstract int getRadius (double angle);

    protected int ovalRadius (double angle) {
        int w2 = width*width;
        int h2 = height*height;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        return (int)Math.sqrt((w2*h2)/(sin*sin*w2+cos*cos*h2)) / 2;
    }

    protected int roundRadius() {
        return width / 2;
    }

    protected int rectangleRadius (double angle) {
        double absAngle = Math.abs(angle);
        if (absAngle < 0.5)
            return width/2;
        if (absAngle < 1)
            return (int)(Math.sqrt((double)width*width+height*height)/2);
        return height/2;
    }


}
