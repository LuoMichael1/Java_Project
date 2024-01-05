import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Chest extends Interactible {

    static final int WIDTH = 2;
    static final int HEIGHT = 2;

    static final String IMAGE_PATH = "objects/chest.png";

    static ArrayList<Chest> chests = new ArrayList<>();

    public void loadImages() {

        super.loadImages(IMAGE_PATH);
    }

    public Chest(int x, int y) {

        super(x, y, WIDTH, HEIGHT);
    }

    static void checkCollision(PlayerMovable player, InteractivePanel gamePanel) {

        // Get current tile
        int currentTileY = player.getCurrentTileY();
        int currentTileX = player.getCurrentTileX();

        for (Chest chest : chests) {

            if (chest.tileY + 1 == currentTileY) {
                if (chest.tileX == currentTileX || chest.tileX + 1 == currentTileX) {

                    System.out.println("You opened a chest");
                }
            }
        }
    }
}
