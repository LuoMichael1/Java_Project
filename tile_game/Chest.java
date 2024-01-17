// This class handles all attributes and behaviours for the chests that appear on the map. 
// By Alec

package tile_game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Chest extends Interactable {

    // Width and height of every chest
    private static final int WIDTH = 2;
    private static final int HEIGHT = 2;

    private static final String IMAGE_PATH = "objects/chest.png";
    private static final String IMAGE_PATH_CHEST_STAND = "objects/chest-stand.png";
    private static final String CHEST_COORDINATES_PATH = "tile_game/maps/chest-coordinates.csv";

    // Coordinates of the chest stand
    private static final int chestStandX = 30;
    private static final int chestStandY = 2;

    private static BufferedImage chestStandImage;

    // Public list of all chests
    public static ArrayList<Chest> chests = new ArrayList<>();

    // Total amount of cards to give
    private static int giveCards = 0;

    // Track whether or not a chest has been opened and whether or not it needs orbs
    // to open
    private boolean isOpened = false;
    private boolean isObjective = false;

    // Track whether or not to indicate that a card is being added. This should be
    // true when a chest is opened and become false once a key is pressed.
    private static boolean displayWindow = false;

    public static void loadChests() {

        // reads in chests in format: x,y,isObjective
        // with "objective" to indicate that a chest is an objective chest (needs orbs
        // to open)
        // for each chest on each line

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

    private void loadImages() {

        super.loadImages(IMAGE_PATH);
    }

    public static void loadChestStandImage() {

        try {

            chestStandImage = ImageIO.read(Chest.class.getResourceAsStream(IMAGE_PATH_CHEST_STAND));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Chest(int x, int y) {

        super(x, y, WIDTH, HEIGHT);
    }

    // Return true to display "Press space to open". Otherwise return false
    public static boolean checkCollision(int currentTileX, int currentTileY, InteractivePanel gamePanel) {

        for (Chest chest : chests) {

            // check if player is in contact with bottom half of chest
            if (chest.tileY + 1 == currentTileY) {
                if (chest.tileX == currentTileX || chest.tileX + 1 == currentTileX) {

                    if (chest.isObjective && !OrbStand.checkCompletion()) {

                        return false;
                    }

                    // Add a card if spacebar is pressed at a chest
                    if (gamePanel.isKeyPressed(KeyEvent.VK_SPACE) && !chest.isOpened) {

                        displayWindow = true;
                        chest.isOpened = true;
                        giveCards += 1;
                    }
                    return !chest.isOpened;
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

    public static boolean isDisplayWindow() {
        return displayWindow;
    }

    public static void setDisplayWindow(boolean displayWindow) {
        Chest.displayWindow = displayWindow;
    }

    public static int getGiveCards() {
        return giveCards;
    }

    public static void setGiveCards(int giveCards) {
        Chest.giveCards = giveCards;
    }

}
