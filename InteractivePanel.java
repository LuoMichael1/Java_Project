import javax.swing.*;
import java.awt.*;

public class InteractivePanel extends JPanel implements Runnable {

    final int ORIGINAL_TILE_SIZE = 8;
    final int SCALE = 8;
    final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;

    // Define screen as 4:3 ratio
    // final int MAX_SCREEN_COL = 16;
    // final int MAX_SCREEN_ROW = 12;

    Thread gameThread;
    KeyHandler keyHandler = new KeyHandler();

    int FPS = 60;
    int counter = 0;

    PlayerMovable player = new PlayerMovable(this, keyHandler);
    TileManager tile = new TileManager(this, player);

    public InteractivePanel() {

        this.setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // Improve render performance
        
        this.setFocusable(true);
        this.addKeyListener(keyHandler);
        startGameThread();
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

        for (InteractiveEnemy enemy : InteractiveEnemy.InteractiveEnemies) {

            enemy.update(player, this);
        }

        // after 1 seconds you enter battle
        counter++;
        System.out.println(counter);
        if (counter == 60 * 1) {
            // Main.nextCard();
            counter = 0;

            // needs to grab focus (from what? idk) so I just put it here
            grabFocus();
        }

        Chest.checkCollision(player, this);
        Vent.checkCollision(player, this);
        InteractiveEnemy.checkCollision(player, this);
    }

    public void paintComponent(Graphics graphic) {

        super.paintComponent(graphic);

        Graphics2D altGraphic = (Graphics2D) graphic;

        tile.draw(altGraphic);
        tile.drawChests(altGraphic);
        tile.drawVents(altGraphic);
        tile.drawEnemies(altGraphic);
        player.draw(altGraphic);
        tile.drawLighting(altGraphic);

        altGraphic.dispose();
    }
    /*
     * public static void main(String args[]) {
     * 
     * JFrame window = new JFrame();
     * window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     * window.setResizable(false);
     * window.setTitle("Game Name");
     * 
     * 
     * window.add(panel);
     * 
     * window.pack();
     * 
     * window.setLocationRelativeTo(null); // Set window location to center
     * window.setVisible(true);
     * 
     * panel.startGameThread();
     * }
     */
}
