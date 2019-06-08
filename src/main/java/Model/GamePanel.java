package Model;

import Model.element.Hole;
import Model.element.Wizard;
import Model.element.Obstacle;
import Model.element.Pokemon;
import View.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {
    private final int hauteurBarreVie = 10;

    private Timer timer;
    private PlayerView playerView;
    private List<Obstacle> obstacleList;
    private final int FRAMES_DELAY = 30;
    private final int SHOW_POS_TIME = 1000;
    private final int SHOW_SCREEN_TIME = 1;
    private long startTime;
    private Map currentMap;
    private List<Map> maps;
    private int level;
    private Image lostImg;
    private Image winImg;
    private Image nextImg;
    private long deathTime = 0;
    private long endLevelTime = 0;
    private boolean showNewPos = true;

    public GamePanel() {
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.white);
        setFocusable(true);

        playerView = new PlayerView(this);
        createObstacles();
        level = 0;
        final URL lost = Thread.currentThread().getContextClassLoader().getResource("lostScreen.jpg");
        lostImg =  Toolkit.getDefaultToolkit().getImage(lost);
        final URL win = Thread.currentThread().getContextClassLoader().getResource("winScreen.jpg");
        winImg =  Toolkit.getDefaultToolkit().getImage(win);
        final URL next = Thread.currentThread().getContextClassLoader().getResource("nextScreen.jpg");
        nextImg =  Toolkit.getDefaultToolkit().getImage(next);

        timer = new Timer(FRAMES_DELAY, this);
        timer.start();

        startTime = System.nanoTime();
        maps = Arrays.asList(new Ship(5), new Jungle(5), new Dungeon(5, playerView));
        currentMap = maps.get(level);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        long currentTime = System.nanoTime();

        if (playerView.isDead()) {
            if (deathTime == 0) {
                deathTime = currentTime;
                drawMessage(g, lostImg);
            } else { // wait without blocking the thread
                if ((currentTime - deathTime) / 1000000000 < SHOW_SCREEN_TIME) {
                    drawMessage(g, lostImg);
                } else { // then reset
                    changeLevel(currentTime, true);
                }
            }
        }
        else if (showNewPos && (currentTime - startTime)/1000000 >= SHOW_POS_TIME)
            showNewPos = false;
        // level timeout reached:
        else if ((currentTime - startTime)/1000000 >= currentMap.getTimeout() * 1000 + SHOW_POS_TIME) {

            if (endLevelTime == 0) {
                endLevelTime = currentTime;
            }
            else if (level < maps.size()-1) {

                if ((currentTime - endLevelTime) / 1000000000 < SHOW_SCREEN_TIME) {
                    drawMessage(g, nextImg);
                } else {
                    changeLevel(currentTime, false);
                }

            }
            else { // won the game
                if ((currentTime - endLevelTime) / 1000000000 < SHOW_SCREEN_TIME) {
//                    System.out.println("WIN");
                    drawMessage(g, winImg);
                } else {
                    // TODO reinit. better (factorize reset code)
                    changeLevel(currentTime, true);
                }
            }
        }
        else {
            doDrawing(g, (currentTime - startTime)/1000000.0 - SHOW_POS_TIME );
        }

    }

    private void doDrawing(Graphics g, double elapsedTime) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(currentMap.getBg(), 0, 0, this);
        for (Obstacle obstacle : obstacleList) {
            g2d.drawImage(obstacle.getImage(), obstacle.getX(), obstacle.getY(), this);
        }
        g2d.drawImage(playerView.getImage(), playerView.getX(), playerView.getY(), this);

        // Affichage de la barre de vie
        g.setColor(Color.black);
        g.fillRect(5,5, PlayerView.PV_MAX, hauteurBarreVie);
        g.setColor(Color.red);
        int tailleVie = (int)(playerView.getPv() / 100.0 * PlayerView.PV_MAX);
        g.fillRect(5,5, tailleVie, hauteurBarreVie);

        // display elapsed time
        g.setColor(Color.WHITE);
        g.fillRect(MainFrame.WIDTH - 105,5, 100, hauteurBarreVie);
        g.setColor(Color.BLUE);
        int time = elapsedTime < 0 ? 0 : (int)((elapsedTime/(currentMap.getTimeout() * 10)));

        g.fillRect(MainFrame.WIDTH - 105,5, time, hauteurBarreVie);
        g.setColor(Color.BLACK);
        g.drawRect(MainFrame.WIDTH - 105,5, PlayerView.PV_MAX, hauteurBarreVie);
    }

    private void drawMessage(Graphics g, Image image) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, 0, 0, null);
    }


    public void actionPerformed(ActionEvent e) {

        if (deathTime==0 && endLevelTime==0 && !showNewPos) {
            playerView.move();
            for (Obstacle obstacle : obstacleList)
                obstacle.accept(currentMap);
            checkCollide();
        }
        repaint();

    }

    private void checkCollide() {

        int playerRadius = playerView.getWidth() / 2;
        int playerCenterX = playerView.getX() + playerRadius;
        int playerCenterY = playerView.getY() + playerRadius;
        long currentTime = System.nanoTime();

        for (Obstacle obstacle : obstacleList) {

            int obstacleCenterX = obstacle.getX() + obstacle.getWidth() / 2;
            int obstacleCenterY = obstacle.getY() + obstacle.getHeight() / 2;
            int distX = playerCenterX - obstacleCenterX;
            int distY = playerCenterY - obstacleCenterY;

            // TODO correct angle when ratio > 1 and when /0
            double angle = distY == 0 ? 1 : Math.atan( (double)distY / (double) distX);
            int obstacleRadius = obstacle.getRadius(angle);

            // multiplying elements by themselves is faster than calling pow(x,2).
            double distBetween = Math.sqrt( (double) distX * distX + distY * distY);
            double distAllowed = playerRadius + obstacleRadius;

            if (distBetween <= distAllowed) {
                if ( currentTime - obstacle.getLastCollision() < 1000000000) {
                    return;
                }
                System.out.println("Collision with " + obstacle.getClass().getSimpleName());
                // Enlève de la vie
                obstacle.accept(playerView);
                obstacle.setLastCollision(System.nanoTime());
            }
        }
    }


    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            playerView.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            playerView.keyPressed(e);
        }
    }

    private void createObstacles () {
        obstacleList =  Arrays.asList(
                new Hole(60),
                new Hole(40),
                new Hole(80),
                new Pokemon(40,40, 2),
                new Pokemon(40,40, 3),
                new Pokemon(40,40, 4),
                new Pokemon(40,40, 4),
                new Wizard(40, 40, 4),
                new Wizard(40, 40, 4),
                new Wizard(40, 40, 3));
    }

    private void changeLevel (long currentTime, boolean reset) {


        endLevelTime = 0;
        startTime = currentTime;
        level = reset ? 0 : level + 1;
        currentMap = maps.get(level);
        showNewPos = true;

        if (reset) {
            playerView.resurrect();
            createObstacles();
            deathTime = 0;
        }
        else {
            playerView.resetPos();
            for (Obstacle obstacle : obstacleList)
                obstacle.newRandomPos();
        }

    }

    public int getWidth() {
        return super.getWidth();
    }

    public int getHeight() {
        return super.getHeight();
    }
}
