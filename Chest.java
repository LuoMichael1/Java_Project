import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Chest extends Interactible {

    static final int CHEST_WIDTH = 2;
    static final int CHEST_HEIGHT = 2;

    static ArrayList<Chest> chests = new ArrayList<>();

    public void loadImages() {

        try {

            image = ImageIO.read(getClass().getResourceAsStream("objects/chest.png"));
            System.out.println("sdafsdfds");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Chest(int x, int y) {

        tileX = x;
        tileY = y;

    }

    public void draw(Graphics2D graphic, PlayerMovable player, InteractivePanel gamePanel) {

        graphic.drawImage(image, tileX * gamePanel.TILE_SIZE - player.x + player.getDrawX(),
                tileY * gamePanel.TILE_SIZE - player.y + player.getDrawY(),
                gamePanel.TILE_SIZE * CHEST_WIDTH,
                gamePanel.TILE_SIZE * CHEST_HEIGHT, null);
    }

    static void checkCollision(PlayerMovable player, InteractivePanel gamePanel) {

        // Get current tile
        int currentTileY = Math.max(0, player.hitbox.centerY / gamePanel.TILE_SIZE);
        int currentTileX = Math.max(0, player.hitbox.centerX / gamePanel.TILE_SIZE);

        for (Chest chest : chests) {

            if (chest.tileY + 1 == currentTileY) {
                if (chest.tileX == currentTileX || chest.tileX + 1 == currentTileX) {

                    System.out.println("You opened a chest");
                }
            }
        }
    }
}
