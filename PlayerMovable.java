import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

public class PlayerMovable extends Entity {

    InteractivePanel gamePanel;
    KeyHandler keyHandler;

    Set<Integer> collisionTiles = new HashSet<>();

    static int drawX;
    static int drawY;

    int C = 63;
    int R = 43;
    String[][] map;

    public PlayerMovable(InteractivePanel gamePanel, KeyHandler keyHandler) {

        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        map = new String[R][C];

        setDefaultValues();
        getPlayerImage();
        loadMap("maps/base-map2.csv");
    }

    public void setDefaultValues() {

        x = 6 * gamePanel.TILE_SIZE;
        y = 6 * gamePanel.TILE_SIZE;
        speed = 6;
        direction = "right";

        drawX = Main.WIDTH / 2;
        drawY = Main.HEIGHT / 2;

        collisionTiles
                .addAll(Arrays.asList(new Integer[] { 105, 84, 85, 95, 98, 132, 0, 96, 103, 108, 111, 112, 113, 114 }));
    }

    public void getPlayerImage() {

        try {

            up1 = ImageIO.read(getClass().getResourceAsStream("player/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("player/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("player/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("player/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("player/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("player/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("player/boy_right_2.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String file) {

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(file)));

            for (int i = 0; i < R; i++) {
                map[i] = reader.readLine().split(",");
                System.out.println(Arrays.toString(map[i]));
            }

        } catch (Exception e) {
            System.out.println(":P");
            e.printStackTrace();
            ;
        }
    }

    public void update() {

        // Generate new coordinates of player hitbox
        hitbox = new Hitbox(y + gamePanel.TILE_SIZE / 4, x + gamePanel.TILE_SIZE / 4, gamePanel.TILE_SIZE / 2,
                gamePanel.TILE_SIZE / 2);

        // Get current tile
        int currentTileY = Math.max(0, hitbox.centerY / gamePanel.TILE_SIZE);
        int currentTileX = Math.max(0, hitbox.centerX / gamePanel.TILE_SIZE);

        if (keyHandler.up == true) {
            if (!collisionTiles.contains(Integer.parseInt(map[currentTileY - 1][currentTileX]))
                    || !hitbox.intersects(
                            new Hitbox((currentTileY - 1) * gamePanel.TILE_SIZE, currentTileX * gamePanel.TILE_SIZE,
                                    gamePanel.TILE_SIZE, gamePanel.TILE_SIZE)))
                y -= speed;
            direction = "up";
            spriteCounter++;
        } else if (keyHandler.down == true) {
            if (!collisionTiles.contains(Integer.parseInt(map[currentTileY + 1][currentTileX]))
                    || !hitbox.intersects(
                            new Hitbox((currentTileY + 1) * gamePanel.TILE_SIZE, currentTileX * gamePanel.TILE_SIZE,
                                    gamePanel.TILE_SIZE, gamePanel.TILE_SIZE)))
                y += speed;
            direction = "down";
            spriteCounter++;
        } else if (keyHandler.left == true) {
            if (!collisionTiles.contains(Integer.parseInt(map[currentTileY][currentTileX - 1]))
                    || !hitbox.intersects(
                            new Hitbox(currentTileY * gamePanel.TILE_SIZE, (currentTileX - 1) * gamePanel.TILE_SIZE,
                                    gamePanel.TILE_SIZE, gamePanel.TILE_SIZE)))
                x -= speed;
            direction = "left";
            spriteCounter++;
        } else if (keyHandler.right == true) {
            if (!collisionTiles.contains(Integer.parseInt(map[currentTileY][currentTileX + 1]))
                    || !hitbox.intersects(
                            new Hitbox(currentTileY * gamePanel.TILE_SIZE, (currentTileX + 1) * gamePanel.TILE_SIZE,
                                    gamePanel.TILE_SIZE, gamePanel.TILE_SIZE)))
                x += speed;
            direction = "right";
            spriteCounter++;
        }
    }

    public void draw(Graphics2D altGraphic) {

        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                } else
                    image = up2;
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                } else
                    image = down2;
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                } else
                    image = left2;
                break;

            case "right":
                if (spriteNum == 1) {
                    image = right1;
                } else
                    image = right2;
                break;
        }

        // altGraphic.drawImage(image, x, y, gamePanel.TILE_SIZE, gamePanel.TILE_SIZE,
        // null);
        // Draw player sprite at center of screen
        altGraphic.drawImage(image, drawX, drawY, gamePanel.TILE_SIZE, gamePanel.TILE_SIZE, null);

        if (spriteCounter >= 10) {
            if (spriteNum == 1)
                spriteNum = 2;
            else
                spriteNum = 1;

            spriteCounter = 0;
        }
    }
}
