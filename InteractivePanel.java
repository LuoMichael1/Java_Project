import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class InteractivePanel extends JPanel implements Runnable {

    static boolean active;

    private static final int ORIGINAL_TILE_SIZE = 8; // number of pixels in each tile
    private static final int SCALE = 8; // multiplier to make tiles appear bigger
    private static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE; // amount of actual pixels each tile takes up on
                                                                     // screen

    private Thread gameThread; // thread to run the game loop
    KeyHandler keyHandler = new KeyHandler(); // class to handle key inputs

    private static final int FPS = 60;
    public static final double NANOSECONDS_PER_SECOND = 1000000000;
    public static final double FRAME_DURATION = NANOSECONDS_PER_SECOND / FPS;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    PlayerMovable player = new PlayerMovable(this, keyHandler);
    TileManager tile = new TileManager(this, player);

    public InteractivePanel() {
        this.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentShown(ComponentEvent e) {
                InteractivePanel.this.requestFocusInWindow();
            }

            public void componentHidden(ComponentEvent e) {
            }

        });

        this.setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // Improve render performance

        this.setFocusable(true);
        this.addKeyListener(keyHandler);

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                System.out.println("focus gained");
                lock.lock();
                try {
                    condition.signal();
                } finally {
                    lock.unlock();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("focus lost");
                active = false;
            }
        });

        startGameThread();
    }

    public static int getTileSize() {
        return TILE_SIZE;
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        long lastTime = System.nanoTime();
        double delta = 0.0;
        while (gameThread != null) {
            lock.lock();
            try {
                while (!hasFocus()) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                lock.unlock();
            }

            long now = System.nanoTime();
            delta += (now - lastTime) / FRAME_DURATION;
            lastTime = now;
            if (delta >= 1.0) {

                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {

        player.update();

        for (InteractiveEnemy enemy : InteractiveEnemy.InteractiveEnemies) {

            enemy.update(player, this);
        }

        Chest.checkCollision(player, this);
        Vent.checkCollision(player, this);
        OrbStand.checkCollision(player, this);
    }

    public void paintComponent(Graphics graphic) {

        super.paintComponent(graphic);

        Graphics2D altGraphic = (Graphics2D) graphic;

        tile.draw(altGraphic);
        Chest.drawChestStand(altGraphic, player, this);
        tile.drawChests(altGraphic);
        tile.drawVents(altGraphic);
        drawStands(altGraphic);
        tile.drawEnemies(altGraphic);
        player.draw(altGraphic);
        tile.drawLighting(altGraphic);

        altGraphic.dispose();
    }

    public void drawStands(Graphics2D g) {

        for (OrbStand stand : OrbStand.stands) {

            stand.draw(g, player, this);
        }
    }
}
