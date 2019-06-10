package View;

import Model.GamePanel;
import Model.element.Obstacle;

import java.awt.*;
import java.net.URL;
import javax.swing.ImageIcon;


public class ObstacleView {
    private int dx;
    private int dy;
    private int x = 40;
    private int y = 60;
    private int w;
    private int h;
    private Image image;
    private GamePanel gp;
    private Obstacle obstacle;

    public ObstacleView(GamePanel gp, Obstacle obstacle) {
        this.gp = gp;
        this.obstacle = obstacle;

        loadImage();
    }

    private void loadImage() {

        final URL url = Thread.currentThread().getContextClassLoader().getResource(obstacle.getImagePath());
        image =  Toolkit.getDefaultToolkit().getImage(url).getScaledInstance(x, y, Image.SCALE_DEFAULT);


        w = image.getWidth(null);
        h = image.getHeight(null);
    }

//    public void move() {
//        if(x + dx > 0 && x + dx < gp.getWidth() - w) {
//            x += dx;
//        }
//
//        if(y + dy > 0 && y + dy < gp.getHeight() - h) {
//            y += dy;
//        }
//    }

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

}
