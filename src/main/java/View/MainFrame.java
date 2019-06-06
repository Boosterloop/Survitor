package View;

import Model.GamePanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public MainFrame() {
        add(new GamePanel());

        setTitle("Survitor");
        setSize(WIDTH, HEIGHT);

        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String ... args) {
        System.out.println("Survitor");
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}
