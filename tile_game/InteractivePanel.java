// This class is responsible for running the main game loop of the map.
// Structure of this class is loosely inspired by this video: https://www.youtube.com/watch?v=VpH33Uw-_0E
// The code in this class is original and significantly different from the video.
// By Alec

package tile_game;

import javax.swing.*;

import main.Main;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class InteractivePanel extends JPanel implements KeyListener {

    private static final int ORIGINAL_TILE_SIZE = 8; // number of pixels in each tile
    private static final int SCALE = 8; // multiplier to make tiles appear bigger
    private static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE; // amount of actual pixels each tile takes up on
                                                                     // screen

    private static final long NANOSECONDS_PER_SECOND = 1000000000;
    private int frameCount = 0;
    private long lastFrameCounterCheckTime;

    private final long UPDATE_TIME = NANOSECONDS_PER_SECOND / Main.FPS; // Update time in nanoseconds

    private long previousTime = System.nanoTime();
    private double lag = 0.0;

    private Timer timer;
    private PlayerMovable player;
    private TileManager tile;

    private HashSet<Integer> pressedKeys = new HashSet<>();
    private HashSet<Integer> handledKeys = new HashSet<>();

    private String event;

    public InteractivePanel() {

        this.player = new PlayerMovable(this);
        this.tile = new TileManager(this, player);

        this.setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(this);

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

    // semi-fixed timestep loop (main game loop)
    // reference: https://gafferongames.com/post/fix_your_timestep/
    // This code is orignal but inspired by this article.
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

                        update();
                        lag -= UPDATE_TIME;
                    }

                    repaint(); // Render the game

                    frameCount++;

                    if (frameCount == Main.FPS) { // fps check every 60 frames

                        // FPS is the amount of frames computed divided by the amount of seconds taken
                        // to compute them
                        // 60 / currentTime - lastCheckTime

                        currentTime = System.nanoTime();
                        double timeTakenInSeconds = (currentTime - lastFrameCounterCheckTime)
                                / (double) NANOSECONDS_PER_SECOND;
                        System.out.println("render FPS: " + Main.FPS / timeTakenInSeconds);
                        frameCount = 0;
                        lastFrameCounterCheckTime = currentTime;
                    }
                }
            });

            timer.start();
        }
    }

    // Stop the game loop
    private void stopTimer() {

        if (timer != null) {

            timer.stop();
            timer = null;
        }
    }

    // Actual number of pixles each takes up.
    public static int getTileSize() {

        return TILE_SIZE;
    }

    // Update game state and check for collisions
    private void update() {

        player.update();

        for (InteractiveEnemy enemy : InteractiveEnemy.enemies) {

            enemy.update(player, this);
        }

        checkCollisions();
    }

    // Check for collision events
    private void checkCollisions() {

        int currentTileX = player.getCurrentTileX();
        int currentTileY = player.getCurrentTileY();

        if (Chest.checkCollision(currentTileX, currentTileY, this))
            event = "chest";
        else if (Vent.checkCollision(currentTileX, currentTileY, player, this))
            event = "vent";
        else if (!OrbStand.checkCompletion() && OrbStand.checkCollision(currentTileX, currentTileY, player, this))
            event = "orb";
        else if (Chest.isDisplayWindow())
            event = "openChest";
        else
            event = "walk";

        if (player.isInVent())
            event = "inVent";
    }

    // Draw all game elements
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

    // Draw all enemies
    private void drawEnemies(Graphics2D g) {

        for (InteractiveEnemy enemy : InteractiveEnemy.enemies) {

            enemy.draw(g, player);
        }
    }

    // Reset the player's location
    public void resetPlayer() {

        player.setDefaultLocation();
    }

    // Methods to handle key inputs
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();
        pressedKeys.add(code);
        Chest.setDisplayWindow(false);
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();
        pressedKeys.remove(code);
        handledKeys.remove(code);
    }

    // Do not register key input if that key as already been pressed
    public boolean isKeyPressed(int keyCode) {

        if (pressedKeys.contains(keyCode) && !handledKeys.contains(keyCode)) {
            handledKeys.add(keyCode);
            return true;
        }
        return false;
    }

    // Check if key is being held down
    public boolean isKeyHeld(int keyCode) {

        return pressedKeys.contains(keyCode);
    }
}
