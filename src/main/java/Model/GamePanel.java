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

/**
 * Implements the game
 */
public class GamePanel extends JPanel implements ActionListener {
    private final int barHeight = 10;

    private Timer timer;
    private PlayerView playerView;
    private List<Obstacle> obstacleList;
    private final int FRAMES_DELAY = 30;  // ms
    private final int SHOW_POS_TIME = 1000; // ms
    private final int SHOW_SCREEN_TIME = 1;  // sec
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

    /**
     * Constructor
     */
    public GamePanel() {
        initBoard();
    }

    /**
     * Initialises the game
     */
    private void initBoard() {

        setBackground(Color.white);
        setFocusable(true);

        playerView = new PlayerView(this);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                playerView.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                playerView.keyReleased(e);
            }
        });
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
        maps = Arrays.asList(new Ship(15), new Jungle(15), new Dungeon(15, playerView));
        currentMap = maps.get(level);
    }

    /**
     * Paint the panel and its content, depending on the state of the game
     * @param g Graphics
     */
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
        // if time for showing new positions is over: (showNewPos blocks movement)
        else if (showNewPos && (currentTime - startTime)/1000000 >= SHOW_POS_TIME)
            showNewPos = false;
        // level timeout reached:
        else if ((currentTime - startTime)/1000000 >= currentMap.getTimeout() * 1000 + SHOW_POS_TIME) {

            if (endLevelTime == 0) {
                endLevelTime = currentTime;
            }
            // if some level left
            if (level < maps.size()-1) {
                // display changing level screen for SHOW_SCREEN_TIME sec
                if ((currentTime - endLevelTime) / 1000000000 < SHOW_SCREEN_TIME) {
                    drawMessage(g, nextImg);
                } else { // then change level
                    changeLevel(currentTime, false);
                }
            }
            else { // won the game : display win screen and reset.
                if ((currentTime - endLevelTime) / 1000000000 < SHOW_SCREEN_TIME) {
//                    System.out.println("WIN");
                    drawMessage(g, winImg);
                } else {
                    changeLevel(currentTime, true);
                }
            }
        }
        // in any other case, draws the situation. (currentMap + player + obstacles)
        else {
            doDrawing(g, (currentTime - startTime)/1000000.0 - SHOW_POS_TIME );
        }

    }

    /**
     * Draw the player, obstacles, health and time bars
     * @param g Graphics
     * @param elapsedTime elapsed time since the beginning of the level
     */
    private void doDrawing(Graphics g, double elapsedTime) {
        Graphics2D g2d = (Graphics2D) g;
        // drawing map, obstacles and player
        g2d.drawImage(currentMap.getBg(), 0, 0, this);
        for (Obstacle obstacle : obstacleList) {
            g2d.drawImage(obstacle.getImage(), obstacle.getX(), obstacle.getY(), this);
        }
        g2d.drawImage(playerView.getImage(), playerView.getX(), playerView.getY(), this);

        // Draw health bar
        g.setColor(Color.black);
        g.fillRect(5,5, PlayerView.PV_MAX, barHeight);
        g.setColor(Color.red);
        int healthWidth = (int)(playerView.getPv() / 100.0 * PlayerView.PV_MAX);
        g.fillRect(5,5, healthWidth, barHeight);

        // display elapsed time
        g.setColor(Color.WHITE);
        g.fillRect(MainFrame.WIDTH - 105,5, 100, barHeight);
        g.setColor(Color.BLUE);
        int time = elapsedTime < 0 ? 0 : (int)((elapsedTime/(currentMap.getTimeout() * 10)));
        g.fillRect(MainFrame.WIDTH - 105,5, time, barHeight);
        // outlines
        g.setColor(Color.BLACK);
        g.drawRect(MainFrame.WIDTH - 105,5, PlayerView.PV_MAX, barHeight);
        g.drawRect(5,5, PlayerView.PV_MAX, barHeight);
    }

    /**
     * Draw a message screen
     * @param g Graphics
     * @param image to draw
     */
    private void drawMessage(Graphics g, Image image) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, 0, 0, null);
    }


    /**
     *
     * @param e the event fired by the timer.
     */
    public void actionPerformed(ActionEvent e) {

        // the 3 moments where nothing should move: player is dead, level has ended and next has not started.
        if (deathTime == 0 && endLevelTime == 0 && !showNewPos) {
            playerView.move();
            for (Obstacle obstacle : obstacleList)
                obstacle.accept(currentMap);
            checkCollide();
        }
        repaint();

    }

    /**
     * Check the collisions
     */
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

            double angle = distY == 0 ? 1 : Math.atan( (double)distY / distX);
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


    /**
     * Create the obstacles
     */
    private void createObstacles () {
        obstacleList =  Arrays.asList(
                new Hole(60),
                new Hole(40),
                new Hole(80),
                new Pokemon(40, 2),
                new Pokemon(40, 3),
                new Pokemon(40, 4),
                new Pokemon(40, 4),
                new Wizard(40, 40, 4),
                new Wizard(40, 40, 4),
                new Wizard(40, 40, 3));
    }

    /**
     * Change to next level or reset to first level
     * @param currentTime time at which start the next level
     * @param reset if we want to reset to first level
     */
    private void changeLevel (long currentTime, boolean reset) {

        endLevelTime = 0;
        showNewPos = true;
        startTime = currentTime;
        level = reset ? 0 : level + 1;
        currentMap = maps.get(level);

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

    /**
     * Getter for width
     * @return width
     */
    public int getWidth() {
        return super.getWidth();
    }

    /**
     * Getter for height
     * @return height
     */
    public int getHeight() {
        return super.getHeight();
    }
}
