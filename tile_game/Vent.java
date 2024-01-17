// This is the vent class the handles the vents on the map that allow the player to quickly move around.
// By Alec

package tile_game;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Vent extends Interactable {

    private static final int WIDTH = 2;
    private static final int HEIGHT = 2;

    private static final String IMAGE_PATH = "objects/vent.png";
    private static final String VENT_COORDINATE_PATHS = "tile_game/maps/vent-coordinates.csv";
    private static final String VENT_CONNECTION_PATHS = "tile_game/maps/vent-connections.csv";

    static ArrayList<Vent> vents = new ArrayList<>();

    private HashMap<String, Vent> connections = new HashMap<>();
    private String name;

    // Each vent is represented by a name (e.g. 'leftVent'). Connections are
    // represented as a map for each vent that maps a direction to a different vent
    // (e.g. <"right", "middleVent">)

    public static void loadVents() {

        // Reads vents in format: xCoord,yCoord,name

        try {

            String line;
            BufferedReader reader = new BufferedReader(new FileReader(VENT_COORDINATE_PATHS));

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                int ventX = Integer.parseInt(parts[0]);
                int ventY = Integer.parseInt(parts[1]);
                String ventName = parts[2];

                Vent vent = new Vent(ventX, ventY, ventName);
                vent.loadImages();
                Vent.vents.add(vent);
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        loadVentConnections();
    }

    private static void loadVentConnections() {

        // Reads vent connections in format: vent1,direction,vent2
        // Then connects vent one to vent two via direction

        try {

            String line;
            BufferedReader reader = new BufferedReader(new FileReader(VENT_CONNECTION_PATHS));

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length >= 3) {

                    String vent1Name = parts[0];
                    String direction = parts[1];
                    String vent2Name = parts[2];

                    Vent vent1 = findVentByName(vent1Name);
                    Vent vent2 = findVentByName(vent2Name);

                    if (vent1 != null && vent2 != null) {
                        vent1.connect(direction, vent2);
                    }
                }
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to find a vent inside the vent list from a name
    private static Vent findVentByName(String name) {

        for (Vent vent : Vent.vents) {

            if (vent.getName().equals(name)) {
                return vent;
            }
        }
        return null;
    }

    private void loadImages() {

        super.loadImages(IMAGE_PATH);
    }

    private Vent(int x, int y, String name) {

        super(x, y, WIDTH, HEIGHT);

        this.name = name;
    }

    // Connect other vent to this by mapping direction to vent
    private void connect(String direction, Vent vent) {

        connections.put(direction, vent);
    }

    public static boolean checkCollision(int currentTileX, int currentTileY, PlayerMovable player,
            InteractivePanel gamePanel) {

        for (Vent vent : vents) {

            if (vent.tileY == currentTileY || vent.tileY + 1 == currentTileY) {
                if (vent.tileX == currentTileX || vent.tileX + 1 == currentTileX) {

                    // Check if the player pressed the spacebar
                    if (gamePanel.isKeyPressed(KeyEvent.VK_SPACE)) {
                        System.out.println("You entered a vent");
                        player.setInVent((player.isInVent() == false) ? true : false);
                    }

                    if (player.isInVent()) {

                        // Check if the player is pressing AD keys
                        if (gamePanel.isKeyPressed(KeyEvent.VK_A)) {
                            // Move the player left inside the vent
                            Vent connectedVent = vent.connections.get("left");
                            if (connectedVent != null) {
                                player.setLocation(connectedVent.tileX * InteractivePanel.getTileSize(),
                                        connectedVent.tileY * InteractivePanel.getTileSize());
                            }
                        } else if (gamePanel.isKeyPressed(KeyEvent.VK_D)) {
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
