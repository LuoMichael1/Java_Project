// This class represents the player that moves on the map.
// Structure of this class is loosely inspired by this video: https://www.youtube.com/watch?v=wT9uNGzMEM4
// The code in this class is original and significantly different from the video.
// By Alec

package tile_game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.Main;

public class PlayerMovable extends Entity {

    private InteractivePanel keyHandler;

    private static final int SPRITE_OFFSET = -(InteractivePanel.getTileSize() / 2 * 3);
    private static final int SPRITE_SCALE = 4;

    private static final int STARTING_X = 33; // by number of tiles
    private static final int STARTING_Y = 6;

    private static final int NUM_SPRITES = 4;

    private static final String[] directions = new String[] { "up", "down", "left", "right" };
    private static final int[] keyEvents = new int[] { KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D };
    private int[][] movement;

    private int drawX = Main.WIDTH / 2;
    private int drawY = Main.HEIGHT / 2;

    private boolean inVent;
    private boolean hasOrb;

    public PlayerMovable(InteractivePanel keyHandler) {

        this.keyHandler = keyHandler;

        setDefaultValues();
        getPlayerImage();
    }

    private void setDefaultValues() {

        up = new BufferedImage[NUM_SPRITES + 1];
        down = new BufferedImage[NUM_SPRITES + 1];
        left = new BufferedImage[NUM_SPRITES + 1];
        right = new BufferedImage[NUM_SPRITES + 1];

        speed = 6;
        movement = new int[][] { { -speed, 0 }, { speed, 0 }, { 0, -speed }, { 0, speed } };

        setDefaultLocation();
    }

    // Reset the player back to the default location
    public void setDefaultLocation() {

        x = STARTING_X * InteractivePanel.getTileSize();
        y = STARTING_Y * InteractivePanel.getTileSize();
        direction = "right";
        hitbox = new Hitbox(y + InteractivePanel.getTileSize() / 4, x + InteractivePanel.getTileSize() / 4,
                InteractivePanel.getTileSize() / 2,
                InteractivePanel.getTileSize() / 4 * 3);
    }

    // Load all player sprite images
    private void getPlayerImage() {

        try {

            for (int i = 1; i <= 4; i++) {

                up[i] = ImageIO.read(getClass().getResourceAsStream("walk/walk up" + i + ".png"));
                down[i] = ImageIO.read(getClass().getResourceAsStream("walk/walk down" + i + ".png"));
                left[i] = ImageIO.read(getClass().getResourceAsStream("walk/walk left" + i + ".png"));
                right[i] = ImageIO.read(getClass().getResourceAsStream("walk/walk right" + i + ".png"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update position of the player based on key presses
    public void update() {

        // Generate new coordinates of player hitbox to be around the size/shape of the
        // player sprite
        hitbox.update(y + InteractivePanel.getTileSize() / 4, x + InteractivePanel.getTileSize() / 4,
                InteractivePanel.getTileSize() / 2,
                InteractivePanel.getTileSize() / 4 * 3);

        // Get current tile
        int currentTileY = getCurrentTileY();
        int currentTileX = getCurrentTileX();

        Point[] neighbors = new Point[] {
                new Point(currentTileX, currentTileY - 1),
                new Point(currentTileX, currentTileY + 1),
                new Point(currentTileX - 1, currentTileY),
                new Point(currentTileX + 1, currentTileY) };

        for (int i = 0; i < neighbors.length; i++) {

            if (!inVent) {

                if (keyHandler.isKeyHeld(keyEvents[i])) {
                    if (!TileManager.getTiles()[TileManager.getMap()[neighbors[i].y][neighbors[i].x]].collision
                            || !hitbox.intersects(TileManager.getTileHitboxes()[neighbors[i].y][neighbors[i].x])) {
                        y += movement[i][0];
                        x += movement[i][1];
                    }
                    direction = directions[i];
                    spriteCounter++;
                }
            }
        }
    }

    // Draw the player sprice
    public void draw(Graphics2D graphic) {

        if (!inVent) {

            BufferedImage image = null;

            switch (direction) {
                case "up":
                    image = up[spriteNum];
                    break;
                case "down":
                    image = down[spriteNum];
                    break;
                case "left":
                    image = left[spriteNum];
                    break;
                case "right":
                    image = right[spriteNum];
                    break;
            }

            // Draw player sprite at center of screen
            graphic.drawImage(image, drawX + SPRITE_OFFSET, drawY + SPRITE_OFFSET,
                    InteractivePanel.getTileSize() * SPRITE_SCALE,
                    InteractivePanel.getTileSize() * SPRITE_SCALE,
                    null);

            if (spriteCounter >= 10) { // Change sprite every 10 frames

                spriteNum = (spriteNum == NUM_SPRITES) ? 1 : spriteNum + 1;
                spriteCounter = 0;
            }
        }
    }

    // Change the player's location
    public void setLocation(int x, int y) {

        this.x = x;
        this.y = y;
    }

    // Get real coordinates of place to draw the player
    public int getDrawX() {

        return drawX;
    }

    public int getDrawY() {

        return drawY;
    }

    // Get current tile coordinates
    public int getCurrentTileX() {

        return Math.max(0, hitbox.getCenterX() / InteractivePanel.getTileSize());
    }

    public int getCurrentTileY() {

        return Math.max(0, hitbox.getCenterY() / InteractivePanel.getTileSize());
    }

    // These methods check what state the player is in and can modify the state.
    public boolean isInVent() {

        return inVent;
    }

    public void setInVent(boolean inVent) {

        this.inVent = inVent;
    }

    public boolean isHasOrb() {

        return hasOrb;
    }

    public void setHasOrb(boolean hasOrb) {

        this.hasOrb = hasOrb;
    }
}
