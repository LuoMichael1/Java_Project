import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.io.*;
import javax.imageio.ImageIO;

public class TileManager {

    private static final int MAX_ALPHA = 255;
    private static final int MAX_LIGHT_ALPHA = 60;
    private static final int MAX_DARK_ALPHA = 200;
    private static final int MAX_ILLUMINATION_DISTANCE = 7;

    private InteractivePanel gamePanel;
    private PlayerMovable player;

    private static Tile[] tile;
    private static Hitbox[][] tileHitboxes;
    private int[][] map;
    private int[][] alpha;
    private int[][] lightAlpha;

    private Set<Integer> lightSources = new HashSet<>();
    private Set<Integer> nonIlluminable = new HashSet<>();
    private static Set<Integer> collisionTiles = new HashSet<>();
    private ArrayList<Point> lightSourcePositions = new ArrayList<>();
    private ArrayList<Integer> lightSourceStatus = new ArrayList<>();

    private BufferedImage[] darkImages = new BufferedImage[MAX_ALPHA];
    private BufferedImage[] lightImages = new BufferedImage[MAX_ALPHA];

    private final int NUM_COLS = 63;
    private final int NUM_ROWS = 43;
    private final int NUM_TILES = 134;

    private int frameCounter;

    public TileManager(InteractivePanel gamePanel, PlayerMovable player) {

        this.gamePanel = gamePanel;
        this.player = player;

        tile = new Tile[NUM_TILES + 1];
        map = new int[NUM_ROWS][NUM_COLS];
        alpha = new int[NUM_ROWS][NUM_COLS];
        lightAlpha = new int[NUM_ROWS][NUM_COLS];
        tileHitboxes = new Hitbox[NUM_ROWS][NUM_COLS];

        lightSources.addAll(Arrays.asList(new Integer[] { 114 }));
        nonIlluminable.addAll(Arrays.asList(new Integer[] { 105, 84, 85, 95, 98, 132 }));
        collisionTiles
                .addAll(Arrays.asList(new Integer[] { 105, 84, 85, 95, 98, 132, 0, 96, 103, 108, 111, 112, 113, 114, 7,
                        91, 94, 106, 107, 133, 134, 130 }));

        frameCounter = 0;

        getTileImage();
        loadMap("maps/base-map2.csv");
        generateHitboxes();
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

            String line;
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream(file)));

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                chestCoordinates.add(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
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

            String line;
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream(file)));

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                int ventX = Integer.parseInt(parts[0]);
                int ventY = Integer.parseInt(parts[1]);
                String ventName = parts[2];

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

            String line;
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream(file)));

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                String type = parts[2];

                ArrayList<Point> triggers = new ArrayList<>();
                for (int j = 3; j < parts.length; j += 2) {

                    triggers.add(new Point(Integer.parseInt(parts[j]), Integer.parseInt(parts[j + 1])));
                }

                InteractiveEnemy enemy = new InteractiveEnemy(x, y, gamePanel, type, triggers);
                InteractiveEnemy.InteractiveEnemies.add(enemy);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLighting(int[][] map) {

        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {

                if (lightSources.contains(map[i][j])) {

                    lightSourcePositions.add(new Point(j, i));
                    lightSourceStatus.add(0);
                }

                alpha[i][j] = MAX_DARK_ALPHA;
            }
        }

        for (Point position : lightSourcePositions) {

            alpha[position.y][position.x] = 0;
            lightAlpha[position.y][position.x] = MAX_LIGHT_ALPHA;

            double variation = 0;
            getSurroundingAlpha(position, variation);
        }

        for (int alpha = 0; alpha < MAX_ALPHA; alpha++) {
            darkImages[alpha] = createImage(alpha, 0, 0, 10);
            lightImages[alpha] = createImage(alpha, 80, 80, 0);
        }
    }

    public void getSurroundingAlpha(Point start, double variation) {

        boolean[][] visited = new boolean[NUM_ROWS][NUM_COLS];
        LinkedList<Point> queue = new LinkedList<>();
        Map<Point, Integer> distance = new HashMap<>();

        queue.add(start);
        visited[start.y][start.x] = true;
        distance.put(start, 0);

        while (!queue.isEmpty()) {

            Point current = queue.poll();

            if (distance.get(current) > MAX_ILLUMINATION_DISTANCE)
                break;

            for (Point neighbor : getNeighbors(current)) {

                if (!visited[neighbor.y][neighbor.x]
                        && !nonIlluminable.contains(map[neighbor.y][neighbor.x])) {

                    int temp = distance.get(current) + 1;

                    if (temp < distance.getOrDefault(neighbor, Integer.MAX_VALUE)) {

                        distance.put(neighbor, temp);
                        alpha[neighbor.y][neighbor.x] = alpha[neighbor.y][neighbor.x]
                                - alpha[neighbor.y][neighbor.x] / temp;
                        lightAlpha[neighbor.y][neighbor.x] = Math.max(0,
                                MAX_LIGHT_ALPHA - alpha[neighbor.y][neighbor.x] / 3);

                        visited[neighbor.y][neighbor.x] = true;
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
        if (current.x < NUM_COLS - 1)
            neighbors.add(new Point(current.x + 1, current.y));
        if (current.y < NUM_ROWS - 1)
            neighbors.add(new Point(current.x, current.y + 1));

        return neighbors;
    }

    public void getTileImage() {

        try {

            for (int i = 0; i <= NUM_TILES; i++) {

                tile[i] = new Tile();
                tile[i].image = ImageIO
                        .read(getClass().getResourceAsStream("base-map/tile" + String.format("%03d", i) + ".png"));
                tile[i].collision = (collisionTiles.contains(i)) ? true : false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateHitboxes() {

        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {

                if (tile[map[i][j]].collision) {

                    tileHitboxes[i][j] = new Hitbox(i * InteractivePanel.getTileSize(),
                            j * InteractivePanel.getTileSize(), InteractivePanel.getTileSize(),
                            InteractivePanel.getTileSize());
                }
            }
        }
    }

    public void loadMap(String file) {

        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream(file)));

            for (int i = 0; i < NUM_ROWS; i++) {
                String[] line = reader.readLine().split(",");

                for (int j = 0; j < NUM_COLS; j++) {
                    map[i][j] = Integer.parseInt(line[j]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
    }

    private BufferedImage createImage(int alpha, int r, int g, int b) {

        // Create a new image
        BufferedImage darkImage = new BufferedImage(InteractivePanel.getTileSize(), InteractivePanel.getTileSize(),
                BufferedImage.TYPE_INT_ARGB);

        // Add a black graphic to the image
        Graphics2D g2 = darkImage.createGraphics();

        g2.setColor(new Color(r, g, b, alpha));
        g2.fillRect(0, 0, InteractivePanel.getTileSize(), InteractivePanel.getTileSize());
        g2.dispose();

        return darkImage;
    }

    public void draw(Graphics2D graphic) {

        drawTiles(graphic);
        Chest.drawChestStand(graphic, player);
        drawChests(graphic);
        drawVents(graphic);
        drawStands(graphic);
    }

    public void drawTiles(Graphics2D graphic) {

        // Convert the player's coordinates into the range of visible tiles
        int start_i = Math.max(0, (player.y - player.getDrawY()) / InteractivePanel.getTileSize());
        int end_i = Math.min(NUM_ROWS, start_i + Main.HEIGHT / InteractivePanel.getTileSize() + 2);

        int start_j = Math.max(0, (player.x - player.getDrawX()) / InteractivePanel.getTileSize());
        int end_j = Math.min(NUM_COLS, start_j + Main.WIDTH / InteractivePanel.getTileSize() + 2);

        // Draw only the visible tiles
        for (int i = start_i; i < end_i; i++) {
            for (int j = start_j; j < end_j; j++) {
                graphic.drawImage(tile[Math.max(0, map[i][j])].image,
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

    public void drawStands(Graphics2D g) {

        for (OrbStand stand : OrbStand.stands) {

            stand.draw(g, player);
        }
    }

    public void drawLighting(Graphics2D graphic) {

        // Convert the player's coordinates into the range of visible tiles
        int start_i = Math.max(0, (player.y - player.getDrawY()) / InteractivePanel.getTileSize());
        int end_i = Math.min(NUM_ROWS, start_i + Main.HEIGHT / InteractivePanel.getTileSize() + 2);

        int start_j = Math.max(0, (player.x - player.getDrawX()) / InteractivePanel.getTileSize());
        int end_j = Math.min(NUM_COLS, start_j + Main.WIDTH / InteractivePanel.getTileSize() + 2);

        // Draw only the visible tiles
        for (int i = start_i; i < end_i; i++) {
            for (int j = start_j; j < end_j; j++) {

                // Overlay transparent black tiles to represent darkness
                graphic.drawImage(darkImages[alpha[i][j]],
                        j * InteractivePanel.getTileSize() - player.x + player.getDrawX(),
                        i * InteractivePanel.getTileSize() - player.y + player.getDrawY(), null);

                graphic.drawImage(lightImages[lightAlpha[i][j]],
                        j * InteractivePanel.getTileSize() - player.x + player.getDrawX(),
                        i * InteractivePanel.getTileSize() - player.y + player.getDrawY(), null);
            }
        }
    }

    public void update() {

        if (frameCounter >= 10) {

            int chance = (int) (Math.random() * 2);

            if (chance != 0)
                return;

            int start_i = Math.max(0,
                    player.y - Main.WIDTH / InteractivePanel.getTileSize() - MAX_ILLUMINATION_DISTANCE);
            int end_i = Math.min(NUM_ROWS,
                    player.y + Main.WIDTH / InteractivePanel.getTileSize() + MAX_ILLUMINATION_DISTANCE);

            int start_j = Math.max(0,
                    player.x - Main.HEIGHT / InteractivePanel.getTileSize() - MAX_ILLUMINATION_DISTANCE);
            int end_j = Math.min(NUM_ROWS,
                    player.x + Main.HEIGHT / InteractivePanel.getTileSize() + MAX_ILLUMINATION_DISTANCE);

            for (int i = 0; i < lightSourcePositions.size(); i++) {

                chance = (int) (Math.random() * 3);

                if (chance != 0)
                    continue;

                Point lightSource = lightSourcePositions.get(i);

                System.out.println(lightSource.x + " " + lightSource.y);

                int variation = (lightSourceStatus.get(i) == 0) ? -5 : 5;
                lightSourceStatus.set(i, 1 - lightSourceStatus.get(i));

                start_i = Math.max(0, lightSource.y - MAX_ILLUMINATION_DISTANCE);
                start_j = Math.max(0, lightSource.x - MAX_ILLUMINATION_DISTANCE);

                end_i = Math.min(NUM_ROWS, lightSource.y
                        + MAX_ILLUMINATION_DISTANCE);
                end_j = Math.min(NUM_COLS, lightSource.x
                        + MAX_ILLUMINATION_DISTANCE);

                for (int y = start_i; y < end_i; y++) {
                    for (int x = start_j; x < end_j; x++) {

                        if (!nonIlluminable.contains(map[y][x])) {
                            if (alpha[y][x] - variation >= 0)
                                alpha[y][x] -= variation;

                            if (lightAlpha[y][x] - variation >= 0)
                                lightAlpha[y][x] -= variation;
                        }
                    }
                }

            }
            frameCounter = 0;
        }
        frameCounter++;
    }

    public static Set<Integer> getCollisionTiles() {
        return collisionTiles;
    }

    public static Tile[] getTiles() {
        return tile;
    }

    public static Hitbox[][] getTileHitboxes() {
        return tileHitboxes;
    }
}
