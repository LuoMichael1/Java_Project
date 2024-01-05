import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Interactible {

    int tileX, tileY;
    final int WIDTH, HEIGHT;
    BufferedImage image;

    public Interactible(int tileX, int tileY, int width, int height) {

        this.tileX = tileX;
        this.tileY = tileY;
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    public void loadImages(String imagePath) {

        try {

            image = ImageIO.read(getClass().getResourceAsStream(imagePath));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D graphic, PlayerMovable player, InteractivePanel gamePanel) {

        graphic.drawImage(image, tileX * gamePanel.TILE_SIZE - player.x + player.getDrawX(),
                tileY * gamePanel.TILE_SIZE - player.y + player.getDrawY(),
                gamePanel.TILE_SIZE * WIDTH,
                gamePanel.TILE_SIZE * HEIGHT, null);
    }
}