import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    final int longueurBarreVie = 70;
    final int hauteurBarreVie = 10;

    private Timer timer;
    private PlayerView playerView;
    private ObstacleView obstacleView;
    private final int DELAY = 10;
    private long startTime;
    private Map currentMap;
    private List<Map> maps;
    private int level;
    private long lastTimeStamp;
    private boolean firstCollision = true;

    public GamePanel() {
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.white);
        setFocusable(true);

        playerView = new PlayerView(this);
        obstacleView = new ObstacleView(this);

        timer = new Timer(DELAY, this);
        timer.start();

        startTime = System.currentTimeMillis();
        maps = Arrays.asList(new Map[] {new Map(5)});
        currentMap = maps.get(0);
        level = 1;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (System.currentTimeMillis() - startTime >= currentMap.getTimeout() * 1000) {
            if (level < maps.size()) {
                level++;
                currentMap = maps.get(level);
            } else {
                System.out.println("WIN");
            }
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doDrawing(g);
        repaint();
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(playerView.getImage(), playerView.getX(), playerView.getY(), this);
        g2d.drawImage(obstacleView.getImage(), obstacleView.getX(), obstacleView.getY(), this);

        // Affichage de la barre de vie
        g.setColor(Color.red);
        int tailleVie = (int)(playerView.getPointsDeVie() / 100.0 * longueurBarreVie);
        g.fillRect(5,5,tailleVie, hauteurBarreVie);
        g.setColor(Color.black);
        g.drawRect(5,5,longueurBarreVie, hauteurBarreVie);
    }

    public void actionPerformed(ActionEvent e) {
        step();

        checkCollide();
    }

    private void checkCollide() {

        int playerCenterX = playerView.getX() + playerView.getWidth() / 2;
        int playerCenterY = playerView.getY() + playerView.getHeight() / 2;
        int playerRayon = playerView.getWidth() / 2;

        int obstacleCenterX = obstacleView.getX() + obstacleView.getWidth() / 2;
        int obstacleCenterY = obstacleView.getY() + obstacleView.getHeight() / 2;
        int obstacleRayon = obstacleView.getWidth() / 2;

        double distBetween = Math.sqrt(Math.pow(playerCenterX - obstacleCenterX, 2) + Math.pow(playerCenterY - obstacleCenterY, 2));
        double distAllowed = playerRayon + obstacleRayon;

        if (distBetween <= distAllowed) {
            // Permet d'effectuer la première collision et initialiser le timestamp
            if(firstCollision) {
                firstCollision = false;
            }
            // Vérifie si la collision est trop récente par rapport à la précédente
            else if(System.currentTimeMillis() - lastTimeStamp < 1000) {
                return;
            }
            System.out.println("COLLISION");
            // Enlève de la vie
            playerView.setPointsDeVie(playerView.getPointsDeVie() - 10);
            lastTimeStamp = System.currentTimeMillis();
        }
    }

    private void step() {

        playerView.move();

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
