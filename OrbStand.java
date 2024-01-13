import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class OrbStand extends Interactible {

    static final int WIDTH = 2;
    static final int HEIGHT = 2;

    static final String IMAGE_PATH_EMPTY = "objects/orb-stand.png";
    static final String IMAGE_PATH_FULL = "objects/orb-stand-full.png";
    static final String IMAGE_PATH_ORB = "objects/orb.png";

    BufferedImage emptyImage;
    BufferedImage fullImage;
    BufferedImage orbImage;

    static ArrayList<OrbStand> stands = new ArrayList<>();
    static int objectivesCompleted = 1;
    static final int TARGET_OBJECTIVES = 3;

    boolean hasOrb;
    boolean objective;

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

    public static void loadOrbStands(String file) {

        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(file));

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    int tileX = Integer.parseInt(parts[0]);
                    int tileY = Integer.parseInt(parts[1]);
                    boolean hasOrb = (parts[2].equals("orb")) ? true : false;
                    boolean objective = (parts[3].equals("objective")) ? true : false;

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

    public OrbStand(int x, int y, boolean hasOrb, boolean objective) {

        super(x, y, WIDTH, HEIGHT);

        this.hasOrb = hasOrb;
        this.objective = objective;
    }

    static boolean checkCollision(PlayerMovable player, InteractivePanel gamePanel) {

        // Get current tile
        int currentTileY = player.getCurrentTileY();
        int currentTileX = player.getCurrentTileX();

        for (OrbStand stand : stands) {

            if (stand.tileY == currentTileY || stand.tileY + 1 == currentTileY) {
                if (stand.tileX == currentTileX || stand.tileX + 1 == currentTileX) {

                    // Check if the player pressed the spacebar
                    if (gamePanel.getKeyHandler().isKeyPressed(KeyEvent.VK_SPACE)) {

                        if (stand.hasOrb && !player.hasOrb) {
                            System.out.println("You took an orb");
                            stand.hasOrb = false;
                            player.hasOrb = true;

                            if (stand.objective) {
                                objectivesCompleted--;
                            }

                        } else if (player.hasOrb && !stand.hasOrb) {

                            System.out.println("You placed an orb");
                            stand.hasOrb = true;
                            player.hasOrb = false;

                            if (stand.objective) {
                                objectivesCompleted++;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        checkCompletion();
        return false;
    }

    static void checkCompletion() {

        if (objectivesCompleted == TARGET_OBJECTIVES) {

            System.out.println("The chest opens");
        }
    }

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

        if (player.hasOrb) {

            int yoffset = -20;

            graphic.drawImage(orbImage, player.getDrawX() - InteractivePanel.getTileSize() / 2,
                    player.getDrawY() - InteractivePanel.getTileSize() + yoffset,
                    InteractivePanel.getTileSize() * WIDTH,
                    InteractivePanel.getTileSize() * HEIGHT, null);
        }
    }
}
