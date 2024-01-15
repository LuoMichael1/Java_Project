package tile_game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Chest extends Interactible {

    private static final int WIDTH = 2;
    private static final int HEIGHT = 2;

    private static final String IMAGE_PATH = "objects/chest.png";
    private static final String IMAGE_PATH_CHEST_STAND = "objects/chest-stand.png";
    private static final String CHEST_COORDINATES_PATH = "tile_game/maps/chest-coordinates.csv";

    private static final int chestStandX = 30;
    private static final int chestStandY = 2;

    private static BufferedImage chestStandImage;

    static ArrayList<Chest> chests = new ArrayList<>();
    public static int giveCards = 0;

    private boolean isOpened = false;
    private boolean isObjective = false;

    public static void loadChests() {

        try {

            String line;
            BufferedReader reader = new BufferedReader(new FileReader(CHEST_COORDINATES_PATH));

            while ((line = reader.readLine()) != null) {
                
                String[] parts = line.split(",");
                
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                boolean isObjective = parts[2].equals("objective");

                Chest chest = new Chest(x, y);
                chest.loadImages();
                chest.isObjective = isObjective;
                Chest.chests.add(chest);
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
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

                    if (chest.isObjective && !OrbStand.checkCompletion()) {
                        
                        return false;
                    }

                    if (gamePanel.getKeyHandler().isKeyPressed(KeyEvent.VK_SPACE) && !chest.isOpened) {
                        
                        System.out.println("You opened a chest");
                        chest.isOpened = true;
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
