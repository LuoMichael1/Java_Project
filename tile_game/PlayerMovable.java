package tile_game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Set;

import javax.imageio.ImageIO;

import main.Main;

public class PlayerMovable extends Entity {

    KeyHandler keyHandler;

    Set<Integer> collisionTiles = TileManager.getCollisionTiles();

    private final int SPRITE_OFFSET = -(InteractivePanel.getTileSize() / 2 * 3);
    private final int SPRITE_SCALE = 4;

    private int drawX = Main.WIDTH / 2;
    private int drawY = Main.HEIGHT / 2;

    int C = 63;
    int R = 43;
    String[][] map;

    boolean inVent;
    boolean hasOrb;

    public PlayerMovable(KeyHandler keyHandler) {

        this.keyHandler = keyHandler;

        setDefaultValues();
        getPlayerImage();
        loadMap("maps/base-map2.csv");
    }

    public void setDefaultValues() {

        x = 34 * InteractivePanel.getTileSize();
        y = 6 * InteractivePanel.getTileSize();
        speed = 6;
        direction = "right";
        hitbox = new Hitbox(y + InteractivePanel.getTileSize() / 4, x + InteractivePanel.getTileSize() / 4,
                InteractivePanel.getTileSize() / 2,
                InteractivePanel.getTileSize() / 4 * 3);
        map = new String[R][C];

        up = new BufferedImage[5];
        down = new BufferedImage[5];
        left = new BufferedImage[5];
        right = new BufferedImage[5];
    }

    public void getPlayerImage() {

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

    public void loadMap(String file) {

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(file)));

            for (int i = 0; i < R; i++) {
                map[i] = reader.readLine().split(",");
                // System.out.println(Arrays.toString(map[i]));
            }

        } catch (Exception e) {
            // System.out.println(":P");
            e.printStackTrace();
            ;
        }
    }

    public void update() {

        // Generate new coordinates of player hitbox
        hitbox.update(y + InteractivePanel.getTileSize() / 4, x + InteractivePanel.getTileSize() / 4,
                InteractivePanel.getTileSize() / 2,
                InteractivePanel.getTileSize() / 4 * 3);

        // Get current tile
        int currentTileY = Math.max(0, hitbox.centerY / InteractivePanel.getTileSize());
        int currentTileX = Math.max(0, hitbox.centerX / InteractivePanel.getTileSize());

        Point[] neighbors = new Point[] {
                new Point(currentTileX, currentTileY - 1),
                new Point(currentTileX, currentTileY + 1),
                new Point(currentTileX - 1, currentTileY),
                new Point(currentTileX + 1, currentTileY) };
        String[] directions = new String[] { "up", "down", "left", "right" };
        int[][] movement = new int[][] { { -speed, 0 }, { speed, 0 }, { 0, -speed }, { 0, speed } };
        int[] keyEvents = new int[] { KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D };

        for (int i = 0; i < neighbors.length; i++) {

            if (!inVent) {

                if (keyHandler.isKeyHeld(keyEvents[i])) {
                    if (!TileManager.getTiles()[Integer.parseInt(map[neighbors[i].y][neighbors[i].x])].collision
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

            if (spriteCounter >= 10) {

                spriteNum = (spriteNum == 4) ? 1 : spriteNum + 1;
                spriteCounter = 0;
            }
        }
    }

    public void setLocation(int x, int y) {

        this.x = x;
        this.y = y;
    }

    public int getDrawX() {
        return drawX;
    }

    public int getDrawY() {
        return drawY;
    }

    public int getCurrentTileX() {

        return Math.max(0, hitbox.centerX / InteractivePanel.getTileSize());
    }

    public int getCurrentTileY() {

        return Math.max(0, hitbox.centerY / InteractivePanel.getTileSize());
    }
}
