package Model;

import Model.element.Obstacle;
import Model.element.Pokemon;
import Model.element.Trou;
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
    private final int DELAY = 10;
    private long startTime;
    private Map currentMap;
    private List<Map> maps;
    private int level;
    private long lastTimeStamp;
    private boolean firstCollision = true;
    private Image lostImg;
    private Image winImg;
    private Image nextImg;
    private long deathTime = 0;
    private long endLevelTime = 0;

    public GamePanel() {
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.white);
        setFocusable(true);

        playerView = new PlayerView(this);
        obstacleList =  Arrays.asList(  new Trou(40,40),
                                        new Trou(40,40),
                                        new Pokemon(40,40, 1),
                                        new Pokemon(40,40, 2),
                                        new Pokemon(40,40, 3));
        level = 0;
        final URL lost = Thread.currentThread().getContextClassLoader().getResource("lostScreen.jpg");
        lostImg =  Toolkit.getDefaultToolkit().getImage(lost);
        final URL win = Thread.currentThread().getContextClassLoader().getResource("winScreen.jpg");
        winImg =  Toolkit.getDefaultToolkit().getImage(win);
        final URL next = Thread.currentThread().getContextClassLoader().getResource("nextScreen.jpg");
        nextImg =  Toolkit.getDefaultToolkit().getImage(next);

        timer = new Timer(DELAY, this);
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
            } else { // wait for 3 sec. without blocking the thread
                if ((currentTime - deathTime) / 1000000000 < 3) {
                    drawMessage(g, lostImg);
                } else { // then reset
                    deathTime = 0;
                    level = 0;
                    startTime = currentTime;
                    currentMap = maps.get(level);
                    // TODO move objects & reset pos. player
                    playerView.resurrect();
                    // set size trous
                    ((Trou)obstacleList.get(0)).setSize(40);
                    ((Trou)obstacleList.get(1)).setSize(40);

                }
            }
        }
        // level timeout reached:
        else if ((currentTime - startTime)/1000000000 >= currentMap.getTimeout()) {

            if (endLevelTime == 0) {
                endLevelTime = currentTime;
            }
            else if (level < maps.size()-1) {

                if ((currentTime - endLevelTime) / 1000000000 < 1) {
                    drawMessage(g, nextImg);
                } else {
                    playerView.resetPos();
                    for (Obstacle obstacle : obstacleList)
                        obstacle.newRandomPos();
                    endLevelTime = 0;
                    startTime = currentTime;
                    level++;
                    currentMap = maps.get(level);
                }

            }
            else { // won the game
                if ((currentTime - endLevelTime) / 1000000000 < 3) {
//                    System.out.println("WIN");
                    drawMessage(g, winImg);
                } else {
                    // TODO reinit. better (factorize reset code)
                    endLevelTime = 0;
                    startTime = currentTime;
                    level = 0;
                    currentMap = maps.get(level);
                    playerView.resurrect();
                    // set size trous
                    ((Trou)obstacleList.get(0)).setSize(40);
                    ((Trou)obstacleList.get(1)).setSize(40);
                }
            }
        }
        else {
            doDrawing(g, (currentTime - startTime)/1000000000 );
        }

    }

    private void doDrawing(Graphics g, long elapsedTime) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(currentMap.getBg(), 0, 0, getWidth(), getHeight(), this);
        for (Obstacle obstacle : obstacleList) {
            g2d.drawImage(obstacle.getImage(), obstacle.getX(), obstacle.getY(), this);
        }
        g2d.drawImage(playerView.getImage(), playerView.getX(), playerView.getY(), this);

        // Affichage de la barre de vie
        g.setColor(Color.red);
        int tailleVie = (int)(playerView.getPv() / 100.0 * PlayerView.PV_MAX);
        g.fillRect(5,5, tailleVie, hauteurBarreVie);
        g.setColor(Color.black);
        g.drawRect(5,5, PlayerView.PV_MAX, hauteurBarreVie);

        //TODO display elapsed time

    }

    private void drawMessage(Graphics g, Image image) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, 0, 0, null);
    }


    public void actionPerformed(ActionEvent e) {

        if (deathTime==0 && endLevelTime==0) {
            step();
            checkCollide();
        }
        repaint();

    }

    private void checkCollide() {

        int playerCenterX = playerView.getX() + playerView.getWidth() / 2;
        int playerCenterY = playerView.getY() + playerView.getHeight() / 2;
        int playerRayon = playerView.getWidth() / 2;

        for (Obstacle obstacle : obstacleList) {
            int obstacleCenterX = obstacle.getX() + obstacle.getWidth() / 2;
            int obstacleCenterY = obstacle.getY() + obstacle.getHeight() / 2;
            int obstacleRayon = obstacle.getWidth() / 2;

            double distBetween = Math.sqrt(Math.pow(playerCenterX - obstacleCenterX, 2) + Math.pow(playerCenterY - obstacleCenterY, 2));
            double distAllowed = playerRayon + obstacleRayon;

            if (distBetween <= distAllowed) {
                // Permet d'effectuer la première collision et initialiser le timestamp
//                if (firstCollision) {
//                    firstCollision = false;
//                }
                // Vérifie si la collision est trop récente par rapport à la précédente
//             else
                if (System.nanoTime() - lastTimeStamp < 1000000000) {
                    return;
                }
                System.out.println("COLLISION");
                // Enlève de la vie
                obstacle.accept(playerView);
                lastTimeStamp = System.nanoTime();
            }
        }
    }

    private void step() {

        playerView.move();
        // TODO currentMap.visit (obstacle)
        for (Obstacle obstacle : obstacleList)
            obstacle.accept(currentMap);


        //repaint(playerView.getX()-1, playerView.getY()-1,playerView.getWidth()+2, playerView.getHeight()+2);
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

    public int getWidth() {
        return super.getWidth();
    }

    public int getHeight() {
        return super.getHeight();
    }
}
