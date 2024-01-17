package tile_game;

import javax.swing.*;

import main.Main;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class InteractivePanel extends JPanel {

    private static final int ORIGINAL_TILE_SIZE = 8; // number of pixels in each tile
    private static final int SCALE = 8; // multiplier to make tiles appear bigger
    private static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE; // amount of actual pixels each tile takes up on
                                                                     // screen

    private static final int FPS = 60;
    private static final long NANOSECONDS_PER_SECOND = 1000000000;
    private static final long FRAME_DURATION = NANOSECONDS_PER_SECOND / FPS;
    private int frameCount = 0;
    private int updateCount = 0;
    private long lastFrameCounterCheckTime;
    private long lastUpdateCounterCheckTime;

    final int UPS = 60; // Updates per second
    final long UPDATE_TIME = NANOSECONDS_PER_SECOND / UPS; // Update time in nanoseconds

    long previousTime = System.nanoTime();
    double lag = 0.0;

    private Timer timer;
    private KeyHandler keyHandler; // class to handle key inputs
    private PlayerMovable player;
    private TileManager tile;

    private String event;

    public InteractivePanel() {

        this.keyHandler = new KeyHandler();
        this.player = new PlayerMovable(keyHandler);
        this.tile = new TileManager(this, player);

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
                startTimer();
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("focus lost");
                stopTimer();
            }
        });

        InstructionLabel.loadInstructionLabels();

        startTimer();
    }

    private void startTimer() {

        if (timer == null) {

            timer = new Timer(0, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    long currentTime = System.nanoTime();
                    long elapsedTime = currentTime - previousTime;
                    previousTime = currentTime;
                    lag += elapsedTime;

                    while (lag >= UPDATE_TIME) {
                        update(); // Update game logic
                        lag -= UPDATE_TIME;

                        updateCount++;

                        if (updateCount == FPS) { // fps check every 60 frames

                            // FPS is the amount of frames computed divided by the amount of seconds taken
                            // to compute them
                            // 60 / currentTime - lastCheckTime

                            currentTime = System.nanoTime();
                            double timeTakenInSeconds = (currentTime - lastUpdateCounterCheckTime)
                                    / (double) NANOSECONDS_PER_SECOND;
                            System.out.println("update FPS: " + FPS / timeTakenInSeconds);
                            updateCount = 0;
                            lastUpdateCounterCheckTime = currentTime;
                        }
                    }

                    repaint(); // Render the game

                    frameCount++;

                    if (frameCount == FPS) { // fps check every 60 frames

                        // FPS is the amount of frames computed divided by the amount of seconds taken
                        // to compute them
                        // 60 / currentTime - lastCheckTime

                        currentTime = System.nanoTime();
                        double timeTakenInSeconds = (currentTime - lastFrameCounterCheckTime)
                                / (double) NANOSECONDS_PER_SECOND;
                        System.out.println("render FPS: " + FPS / timeTakenInSeconds);
                        frameCount = 0;
                        lastFrameCounterCheckTime = currentTime;
                    }
                }
            });
            timer.start();
        }
    }

    private void stopTimer() {

        if (timer != null) {

            timer.stop();
            timer = null;
        }
    }

    public static int getTileSize() {

        return TILE_SIZE;
    }

    public KeyHandler getKeyHandler() {

        return keyHandler;
    }

    public void update() {

        player.update();

        for (InteractiveEnemy enemy : InteractiveEnemy.InteractiveEnemies) {

            enemy.update(player, this);
        }

        checkCollisions();
        // tile.update();
    }

    public void checkCollisions() {

        if (Chest.checkCollision(player, this))
            event = "chest";
        else if (Vent.checkCollision(player, this))
            event = "vent";
        else if (OrbStand.checkCollision(player, this))
            event = "orb";
        else
            event = "walk";

        if (player.inVent)
            event = "inVent";
    }

    public void paintComponent(Graphics graphic) {

        super.paintComponent(graphic);

        Graphics2D graphic2d = (Graphics2D) graphic;

        tile.draw(graphic2d);
        player.draw(graphic2d);
        this.drawEnemies(graphic2d);
        tile.drawLighting(graphic2d);
        InstructionLabel.drawLine(graphic2d, event);

        graphic2d.dispose();
    }

    public void drawEnemies(Graphics2D g) {

        for (InteractiveEnemy enemy : InteractiveEnemy.InteractiveEnemies) {

            enemy.draw(g, player);
        }
    }
}
