package tile_game;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Vent extends Interactible {

    static final int WIDTH = 2;
    static final int HEIGHT = 2;

    static final String IMAGE_PATH = "objects/vent.png";
    private static final String VENT_COORDINATE_PATHS = "tile_game/maps/vent-coordinates.csv";
    private static final String VENT_CONNECTION_PATHS = "tile_game/maps/vent-connections.csv";

    static ArrayList<Vent> vents = new ArrayList<>();

    HashMap<String, Vent> connections = new HashMap<>();

    String name;

    public static void loadVents() {

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

    public static void loadVentConnections() {

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

    private static Vent findVentByName(String name) {
        for (Vent vent : Vent.vents) {
            if (vent.getName().equals(name)) {
                return vent;
            }
        }
        return null;
    }

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
                    if (gamePanel.getKeyHandler().isKeyPressed(KeyEvent.VK_SPACE)) {
                        System.out.println("You entered a vent");
                        player.inVent = (player.inVent == false) ? true : false;
                    }

                    if (player.inVent) {

                        // Check if the player is pressing WASD keys
                        if (gamePanel.getKeyHandler().isKeyPressed(KeyEvent.VK_W)) {
                            // Move the player up inside the vent
                            Vent connectedVent = vent.connections.get("up");
                            if (connectedVent != null) {
                                //
                            }
                        } else if (gamePanel.getKeyHandler().isKeyPressed(KeyEvent.VK_A)) {
                            // Move the player left inside the vent
                            Vent connectedVent = vent.connections.get("left");
                            if (connectedVent != null) {
                                player.setLocation(connectedVent.tileX * InteractivePanel.getTileSize(),
                                        connectedVent.tileY * InteractivePanel.getTileSize());
                            }
                        } else if (gamePanel.getKeyHandler().isKeyPressed(KeyEvent.VK_S)) {
                            // Move the player down inside the vent
                            Vent connectedVent = vent.connections.get("down");
                            if (connectedVent != null) {
                                //
                            }
                        } else if (gamePanel.getKeyHandler().isKeyPressed(KeyEvent.VK_D)) {
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
