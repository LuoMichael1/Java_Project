// This class is responsible for handling all the orb stands.
// By Alec

package tile_game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class OrbStand extends Interactable {

    private static final int WIDTH = 2;
    private static final int HEIGHT = 2;

    private static final String IMAGE_PATH_EMPTY = "objects/orb-stand.png";
    private static final String IMAGE_PATH_FULL = "objects/orb-stand-full.png";
    private static final String IMAGE_PATH_ORB = "objects/orb.png";
    private static final String ORB_COORDINATES_PATH = "tile_game/maps/orb-stands.csv";

    private BufferedImage emptyImage;
    private BufferedImage fullImage;
    private BufferedImage orbImage;

    public static ArrayList<OrbStand> stands = new ArrayList<>();
    private static int objectivesCompleted = 1;
    private static final int TARGET_OBJECTIVES = 3;

    private boolean hasOrb;
    private boolean objective;

    // Load all the images
    public void loadImages() {

        try {

            emptyImage = ImageIO.read(getClass().getResourceAsStream(IMAGE_PATH_EMPTY));
            fullImage = ImageIO.read(getClass().getResourceAsStream(IMAGE_PATH_FULL));
            orbImage = ImageIO.read(getClass().getResourceAsStream(IMAGE_PATH_ORB));

            System.out.println("orbs loaded");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("orbs not loaded");
        }
    }

    public static void loadOrbStands() {

        // reads in orbs in format:
        // x,y,hasOrb,isObjective
        // "orb" means hasOrb = true
        // "objective" means objective = true

        try {

            String line;
            BufferedReader reader = new BufferedReader(new FileReader(ORB_COORDINATES_PATH));

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length >= 3) {

                    int tileX = Integer.parseInt(parts[0]);
                    int tileY = Integer.parseInt(parts[1]);
                    boolean hasOrb = parts[2].equals("orb");
                    boolean objective = parts[3].equals("objective");

                    OrbStand stand = new OrbStand(tileX, tileY, hasOrb, objective);
                    stand.loadImages();
                    stands.add(stand);
                }
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private OrbStand(int x, int y, boolean hasOrb, boolean objective) {

        super(x, y, WIDTH, HEIGHT);

        this.hasOrb = hasOrb;
        this.objective = objective;
    }

    // Check for collisions and whether or not player is pressing the spacebar
    public static boolean checkCollision(int currentTileX, int currentTileY, PlayerMovable player,
            InteractivePanel gamePanel) {

        for (OrbStand stand : stands) {

            if (stand.tileY == currentTileY || stand.tileY + 1 == currentTileY) {
                if (stand.tileX == currentTileX || stand.tileX + 1 == currentTileX) {

                    // Check if the player pressed the spacebar
                    if (gamePanel.isKeyPressed(KeyEvent.VK_SPACE)) {

                        if (stand.hasOrb && !player.isHasOrb()) {
                            System.out.println("You took an orb");
                            stand.hasOrb = false;
                            player.setHasOrb(true);

                            if (stand.objective) {
                                objectivesCompleted--;
                            }

                        } else if (player.isHasOrb() && !stand.hasOrb) {

                            System.out.println("You placed an orb");
                            stand.hasOrb = true;
                            player.setHasOrb(false);

                            if (stand.objective) {
                                objectivesCompleted++;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    // Check for completion of objectives
    public static boolean checkCompletion() {

        return objectivesCompleted == TARGET_OBJECTIVES;
    }

    // Draw all orb stands and orbs
    public void draw(Graphics2D graphic, PlayerMovable player) {

        if (hasOrb) {

            graphic.drawImage(fullImage, tileX * InteractivePanel.getTileSize() - player.x + player.getDrawX(),
                    tileY * InteractivePanel.getTileSize() - player.y + player.getDrawY(),
                    InteractivePanel.getTileSize() * WIDTH,
                    InteractivePanel.getTileSize() * HEIGHT, null);
        } else {

            graphic.drawImage(emptyImage, tileX * InteractivePanel.getTileSize() - player.x + player.getDrawX(),
                    tileY * InteractivePanel.getTileSize() - player.y + player.getDrawY(),
                    InteractivePanel.getTileSize() * WIDTH,
                    InteractivePanel.getTileSize() * HEIGHT, null);
        }

        if (player.isHasOrb() && !player.isInVent()) {

            int yoffset = -20;

            graphic.drawImage(orbImage, player.getDrawX() - InteractivePanel.getTileSize() / 2,
                    player.getDrawY() - InteractivePanel.getTileSize() + yoffset,
                    InteractivePanel.getTileSize() * WIDTH,
                    InteractivePanel.getTileSize() * HEIGHT, null);
        }
    }
}
