import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PlayerMovable extends Entity {

    InteractivePanel gamePanel;
    KeyHandler keyHandler;

    static int drawX;
    static int drawY;

    public PlayerMovable(InteractivePanel gamePanel, KeyHandler keyHandler) {

        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {

        x = 100;
        y = 100;
        speed = 6;
        direction = "right";

        drawX = gamePanel.WINDOW_WIDTH / 2;
        drawY = gamePanel.WINDOW_HEIGHT / 2;
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

    public void update() {

        if (keyHandler.up == true) {
            y -= speed;
            direction = "up";
            spriteCounter++;
        } else if (keyHandler.down == true) {
            y += speed;
            direction = "down";
            spriteCounter++;
        } else if (keyHandler.left == true) {
            x -= speed;
            direction = "left";
            spriteCounter++;
        } else if (keyHandler.right == true) {
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
