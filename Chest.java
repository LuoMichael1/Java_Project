import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Chest extends Interactible {

    static final int WIDTH = 2;
    static final int HEIGHT = 2;

    static final String IMAGE_PATH = "objects/chest.png";
    static final String IMAGE_PATH_CHEST_STAND = "objects/chest-stand.png";

    static final int chestStandX = 30;
    static final int chestStandY = 2;

    static BufferedImage chestStandImage;

    static ArrayList<Chest> chests = new ArrayList<>();
    static int giveCards = 0;

    
    public void loadImages() {

        super.loadImages(IMAGE_PATH);
    }

    public static void loadChestStandImage() {

        try {

            chestStandImage = ImageIO.read(Chest.class.getResourceAsStream(IMAGE_PATH_CHEST_STAND));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Chest(int x, int y) {

        super(x, y, WIDTH, HEIGHT);

    }

    static boolean checkCollision(PlayerMovable player, InteractivePanel gamePanel) {

        // Get current tile
        int currentTileY = player.getCurrentTileY();
        int currentTileX = player.getCurrentTileX();

        for (Chest chest : chests) {

            if (chest.tileY + 1 == currentTileY) {
                if (chest.tileX == currentTileX || chest.tileX + 1 == currentTileX) {

                    if (gamePanel.keyHandler.isKeyPressed(KeyEvent.VK_SPACE)) {
                        System.out.println("You opened a chest");

                        giveCards += 1;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static void drawChestStand(Graphics2D graphic, PlayerMovable player) {

        graphic.drawImage(chestStandImage, chestStandX * InteractivePanel.getTileSize() - player.x + player.getDrawX(),
                chestStandY * InteractivePanel.getTileSize() - player.y + player.getDrawY(),
                InteractivePanel.getTileSize() * WIDTH,
                InteractivePanel.getTileSize() * HEIGHT + InteractivePanel.getTileSize(), null);
    }
}
