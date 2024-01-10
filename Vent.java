import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class Vent extends Interactible {

    static final int WIDTH = 2;
    static final int HEIGHT = 2;

    static final String IMAGE_PATH = "objects/vent.png";

    static ArrayList<Vent> vents = new ArrayList<>();

    HashMap<String, Vent> connections = new HashMap<>();

    String name;

    public void loadImages() {

        // System.out.println("vent img loaded");
        super.loadImages(IMAGE_PATH);
    }

    public Vent(int x, int y, String name) {

        super(x, y, WIDTH, HEIGHT);

        this.name = name;
    }

    public void connect(String direction, Vent vent) {
        connections.put(direction, vent);
    }

    static boolean checkCollision(PlayerMovable player, InteractivePanel gamePanel) {

        // Get current tile
        int currentTileY = player.getCurrentTileY();
        int currentTileX = player.getCurrentTileX();

        for (Vent vent : vents) {

            if (vent.tileY == currentTileY || vent.tileY + 1 == currentTileY) {
                if (vent.tileX == currentTileX || vent.tileX + 1 == currentTileX) {

                    // Check if the player pressed the spacebar
                    if (gamePanel.keyHandler.isKeyPressed(KeyEvent.VK_SPACE)) {
                        System.out.println("You entered a vent");
                        player.inVent = (player.inVent == false) ? true : false;
                    }

                    if (player.inVent) {

                        // Check if the player is pressing WASD keys
                        if (gamePanel.keyHandler.isKeyPressed(KeyEvent.VK_W)) {
                            // Move the player up inside the vent
                            Vent connectedVent = vent.connections.get("up");
                            if (connectedVent != null) {
                                //
                            }
                        } else if (gamePanel.keyHandler.isKeyPressed(KeyEvent.VK_A)) {
                            // Move the player left inside the vent
                            Vent connectedVent = vent.connections.get("left");
                            if (connectedVent != null) {
                                player.setLocation(connectedVent.tileX * InteractivePanel.getTileSize(),
                                        connectedVent.tileY * InteractivePanel.getTileSize());
                            }
                        } else if (gamePanel.keyHandler.isKeyPressed(KeyEvent.VK_S)) {
                            // Move the player down inside the vent
                            Vent connectedVent = vent.connections.get("down");
                            if (connectedVent != null) {
                                //
                            }
                        } else if (gamePanel.keyHandler.isKeyPressed(KeyEvent.VK_D)) {
                            // Move the player right inside the vent
                            Vent connectedVent = vent.connections.get("right");
                            if (connectedVent != null) {
                                player.setLocation(connectedVent.tileX * InteractivePanel.getTileSize(),
                                        connectedVent.tileY * InteractivePanel.getTileSize());
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public String getName() {

        return name;
    }
}
