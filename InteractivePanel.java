import javax.swing.*;
import java.awt.*;

public class InteractivePanel extends JPanel implements Runnable {

    final int ORIGINAL_TILE_SIZE = 8;
    final int SCALE = 8;
    final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;

    // Define screen as 4:3 ratio
    final int MAX_SCREEN_COL = 16;
    final int MAX_SCREEN_ROW = 12;
    final int WINDOW_WIDTH = 1280;
    final int WINDOW_HEIGHT = 720;

    Thread gameThread;
    KeyHandler keyHandler = new KeyHandler();

    int FPS = 60;

    PlayerMovable player = new PlayerMovable(this, keyHandler);
    TileManager tile = new TileManager(this, player);

    public InteractivePanel() {

        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // Improve render performance
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {

            System.out.println(player.spriteCounter);

            update();

            repaint();

            try {

                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000; // Convert nanoseconds to miliseconds

                remainingTime = Math.max(0, remainingTime);

                nextDrawTime += drawInterval;

                Thread.sleep((long) remainingTime);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {

        player.update();

    }

    public void paintComponent(Graphics graphic) {

        super.paintComponent(graphic);

        Graphics2D altGraphic = (Graphics2D) graphic;

        tile.draw(altGraphic);
        player.draw(altGraphic);

        altGraphic.dispose();
    }

    public static void main(String args[]) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Game Name");

        InteractivePanel panel = new InteractivePanel();
        window.add(panel);

        window.pack();

        window.setLocationRelativeTo(null); // Set window location to center
        window.setVisible(true);

        panel.startGameThread();
    }
}
