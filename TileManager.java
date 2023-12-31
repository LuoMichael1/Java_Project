import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.io.*;
import javax.imageio.ImageIO;

public class TileManager {

    InteractivePanel gamePanel;
    Tile[] tile;
    String[][] map;
    int[][] alpha;
    Set<Integer> lightSources = new HashSet<>();
    Set<Integer> nonIlluminable = new HashSet<>();
    Set<Integer> collisionTiles = new HashSet<>();

    private static final int MAX_ALPHA = 255;
    private BufferedImage[] darkImages = new BufferedImage[MAX_ALPHA];

    int C = 63;
    int R = 43;
    int NUM_TILES = 134;

    PlayerMovable player;

    public TileManager(InteractivePanel gamePanel, PlayerMovable player) {

        this.gamePanel = gamePanel;
        this.player = player;

        tile = new Tile[NUM_TILES + 1];
        map = new String[R][C];
        alpha = new int[R][C];

        lightSources.addAll(Arrays.asList(new Integer[] { 114 }));
        nonIlluminable.addAll(Arrays.asList(new Integer[] { 105, 84, 85, 95, 98, 132 }));
        collisionTiles
                .addAll(Arrays.asList(new Integer[] { 105, 84, 85, 95, 98, 132, 0, 96, 103, 108, 111, 112, 113, 114 }));

        getTileImage();
        loadMap("maps/base-map2.csv");
        loadObjects();
        getLighting(map);
    }

    public void loadObjects() {

        loadChests("maps/chest-coordinates.csv");
        loadEnemies("maps/enemy-coordinates.csv");
        loadVents("maps/vent-coordinates.csv");
        OrbStand.loadOrbStands("maps/orb-stands.csv");
        Chest.loadChestStandImage();
    }

    public void loadChests(String file) {

        ArrayList<Point> chestCoordinates = new ArrayList<Point>();

        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream(file)));

            for (int i = 0; i < R; i++) {
                String[] line = reader.readLine().split(",");
                chestCoordinates.add(new Point(Integer.parseInt(line[0]), Integer.parseInt(line[1])));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Point point : chestCoordinates) {

            Chest chest = new Chest(point.x, point.y);
            chest.loadImages();
            Chest.chests.add(chest);
        }
    }

    public void loadVents(String file) {

        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream(file)));

            for (int i = 0; i < R; i++) {
                String[] line = reader.readLine().split(",");

                int ventX = Integer.parseInt(line[0]);
                int ventY = Integer.parseInt(line[1]);
                String ventName = line[2];

                Vent vent = new Vent(ventX, ventY, ventName);
                vent.loadImages();
                Vent.vents.add(vent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        loadVentConnections("maps/vent-connections.csv");
    }

    public void loadVentConnections(String file) {

        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(file));

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

    public void loadEnemies(String file) {

        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream(file)));

            for (int i = 0; i < R; i++) {
                String[] line = reader.readLine().split(",");
                int x = Integer.parseInt(line[0]);
                int y = Integer.parseInt(line[1]);
                String type = line[2];

                ArrayList<Point> triggers = new ArrayList<>();
                for (int j = 3; j < line.length; j += 2) {

                    triggers.add(new Point(Integer.parseInt(line[j]), Integer.parseInt(line[j + 1])));
                }

                InteractiveEnemy enemy = new InteractiveEnemy(x, y, gamePanel, type, triggers);
                InteractiveEnemy.InteractiveEnemies.add(enemy);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLighting(String[][] map) {

        ArrayList<Point> lightSourcePositions = new ArrayList<>();

        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {

                if (lightSources.contains(Integer.parseInt(map[i][j]))) {

                    lightSourcePositions.add(new Point(i, j));
                }

                alpha[i][j] = 200;
            }
        }

        for (Point position : lightSourcePositions) {

            alpha[position.x][position.y] = 0;
            getSurroundingAlpha(position);
        }

        for (int alpha = 0; alpha < MAX_ALPHA; alpha++) {
            darkImages[alpha] = createDarkImage(alpha);
        }
    }

    public void getSurroundingAlpha(Point start) {

        final int MAX_ILLUMINATION_DISTANCE = 6;

        Set<Point> visited = new HashSet<>();
        ArrayList<Point> queue = new ArrayList<>();
        Map<Point, Integer> distance = new HashMap<>();

        queue.add(start);
        visited.add(start);
        distance.put(start, 0);

        int i = 0;

        while (i < queue.size()) {

            Point current = queue.get(i);
            i++;

            if (distance.get(current) > MAX_ILLUMINATION_DISTANCE)
                break;

            for (Point neighbor : getNeighbors(current)) {

                if (!visited.contains(neighbor)
                        && !nonIlluminable.contains(Integer.parseInt(map[neighbor.x][neighbor.y]))) {

                    int temp = distance.get(current) + 1;

                    if (temp < distance.getOrDefault(neighbor, Integer.MAX_VALUE)) {

                        distance.put(neighbor, temp);
                        alpha[neighbor.x][neighbor.y] = alpha[neighbor.x][neighbor.y]
                                - alpha[neighbor.x][neighbor.y] / temp;

                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }
    }

    public ArrayList<Point> getNeighbors(Point current) {

        ArrayList<Point> neighbors = new ArrayList<>();

        if (current.x != 0)
            neighbors.add(new Point(current.x - 1, current.y));
        if (current.y != 0)
            neighbors.add(new Point(current.x, current.y - 1));
        if (current.x < R - 1)
            neighbors.add(new Point(current.x + 1, current.y));
        if (current.y < C - 1)
            neighbors.add(new Point(current.x, current.y + 1));

        return neighbors;
    }

    public void getTileImage() {

        try {

            for (int i = 0; i <= NUM_TILES; i++) {

                tile[i] = new Tile();
                tile[i].image = ImageIO
                        .read(getClass().getResourceAsStream("base-map/tile" + String.format("%03d", i) + ".png"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String file) {

        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream(file)));

            for (int i = 0; i < R; i++) {
                map[i] = reader.readLine().split(",");
                // System.out.println(Arrays.toString(map[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
    }

    private BufferedImage createDarkImage(int alpha) {

        // Create a new image
        BufferedImage darkImage = new BufferedImage(InteractivePanel.getTileSize(), InteractivePanel.getTileSize(),
                BufferedImage.TYPE_INT_ARGB);

        // Add a black graphic to the image
        Graphics2D g2 = darkImage.createGraphics();

        g2.setColor(new Color(0, 0, 0, alpha));
        g2.fillRect(0, 0, InteractivePanel.getTileSize(), InteractivePanel.getTileSize());
        g2.dispose();

        return darkImage;
    }

    public void draw(Graphics2D graphic) {

        // Convert the player's coordinates into the range of visible tiles
        int start_i = Math.max(0, (player.y - player.getDrawY()) / InteractivePanel.getTileSize());
        int end_i = Math.min(R, start_i + Main.HEIGHT / InteractivePanel.getTileSize() + 2);

        int start_j = Math.max(0, (player.x - player.getDrawX()) / InteractivePanel.getTileSize());
        int end_j = Math.min(C, start_j + Main.WIDTH / InteractivePanel.getTileSize() + 2);

        // Draw only the visible tiles
        for (int i = start_i; i < end_i; i++) {
            for (int j = start_j; j < end_j; j++) {
                graphic.drawImage(tile[Math.max(0, Integer.parseInt(map[i][j]))].image,
                        j * InteractivePanel.getTileSize() - player.x + player.getDrawX(),
                        i * InteractivePanel.getTileSize() - player.y + player.getDrawY(),
                        InteractivePanel.getTileSize(),
                        InteractivePanel.getTileSize(), null);
            }
        }
    }

    public void drawChests(Graphics2D g) {

        for (Chest chest : Chest.chests) {

            chest.draw(g, player, gamePanel);
        }
    }

    public void drawVents(Graphics2D g) {

        for (Vent vent : Vent.vents) {

            vent.draw(g, player, gamePanel);
        }
    }

    public void drawEnemies(Graphics2D g) {

        for (InteractiveEnemy enemy : InteractiveEnemy.InteractiveEnemies) {

            enemy.draw(g, player, gamePanel);
        }
    }

    public void drawLighting(Graphics2D graphic) {

        // Convert the player's coordinates into the range of visible tiles
        int start_i = Math.max(0, (player.y - player.getDrawY()) / InteractivePanel.getTileSize());
        int end_i = Math.min(R, start_i + Main.HEIGHT / InteractivePanel.getTileSize() + 2);

        int start_j = Math.max(0, (player.x - player.getDrawX()) / InteractivePanel.getTileSize());
        int end_j = Math.min(C, start_j + Main.WIDTH / InteractivePanel.getTileSize() + 2);

        // Draw only the visible tiles
        for (int i = start_i; i < end_i; i++) {
            for (int j = start_j; j < end_j; j++) {

                // Overlay transparent black tiles to represent darkness
                graphic.drawImage(darkImages[alpha[i][j]],
                        j * InteractivePanel.getTileSize() - player.x + player.getDrawX(),
                        i * InteractivePanel.getTileSize() - player.y + player.getDrawY(), null);
            }
        }
    }
}
