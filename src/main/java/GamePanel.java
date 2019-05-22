import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    private Timer timer;
    private PlayerView playerView;
    private ObstacleView obstacleView;
    private final int DELAY = 10;
    private long startTime;
    private Map currentMap;
    private List<Map> maps;
    private int level;

    public GamePanel() {
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
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

        //Toolkit.getDefaultToolkit().sync();
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(playerView.getImage(), playerView.getX(), playerView.getY(), this);
        g2d.drawImage(obstacleView.getImage(), obstacleView.getX(), obstacleView.getY(), this);
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
            System.out.println("COLLISION");
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
