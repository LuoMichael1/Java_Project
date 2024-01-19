// This class handles all the tiles and lighting.
// Structure of this class is loosely inspired by this video: https://www.youtube.com/watch?v=ugzxCcpoSdE
// The code in this class is original and significantly different from the video.
// By Alec

package tile_game;

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

import main.Main;

public class TileManager {

    private static final int MAX_ALPHA = 255;
    private static final int MAX_LIGHT_ALPHA = 60;
    private static final int MAX_DARK_ALPHA = 200;
    private static final int MAX_ILLUMINATION_DISTANCE = 7;

    private InteractivePanel gamePanel;
    private PlayerMovable player;

    private static Tile[] tile;
    private static Hitbox[][] tileHitboxes;
    private static int[][] map;

    private int[][] alpha;
    private int[][] lightAlpha;

    private static Set<Integer> lightSources = new HashSet<>();
    private static Set<Integer> nonIlluminable = new HashSet<>();
    private static Set<Integer> collisionTiles = new HashSet<>();

    private ArrayList<Point> lightSourcePositions = new ArrayList<>();
    private ArrayList<Integer> lightSourceStatus = new ArrayList<>();

    // On top of the tiles are dark images (black images of varying opacity) and
    // light images (yellow images of varying opacity) to represent lighting
    private BufferedImage[] darkImages = new BufferedImage[MAX_ALPHA];
    private BufferedImage[] lightImages = new BufferedImage[MAX_ALPHA];

    // Lighting logic: black tiles are drawn over all tiles to create darkness and
    // yellow tiles are drawn over lighter tiles to create warm lighting

    private final int NUM_COLS = 63;
    private final int NUM_ROWS = 43;
    private final int NUM_TILES = 134;

    // Tile class to store image and collision boolean of each tile
    public class Tile {

        public BufferedImage image;
        public boolean collision;
    }

    public TileManager(InteractivePanel gamePanel, PlayerMovable player) {

        this.gamePanel = gamePanel;
        this.player = player;

        tile = new Tile[NUM_TILES + 1];
        map = new int[NUM_ROWS][NUM_COLS];
        alpha = new int[NUM_ROWS][NUM_COLS];
        lightAlpha = new int[NUM_ROWS][NUM_COLS];
        tileHitboxes = new Hitbox[NUM_ROWS][NUM_COLS];

        // These are the integer codes of all the tiles that have special attributes
        lightSources.addAll(Arrays.asList(new Integer[] { 114 }));
        nonIlluminable.addAll(Arrays.asList(new Integer[] { 105, 84, 85, 95, 98, 132 }));
        collisionTiles
                .addAll(Arrays.asList(new Integer[] { 105, 84, 85, 95, 98, 132, 0, 96, 103, 108, 111, 112, 113, 114, 7,
                        91, 94, 106, 107, 133, 134, 130 }));

        getTileImage();
        loadMap("maps/base-map2.csv");
        generateHitboxes();
        loadObjects();
        getLighting(map);
    }

    // Load all the objects
    private void loadObjects() {

        Chest.loadChests();
        InteractiveEnemy.loadEnemies();
        Vent.loadVents();
        OrbStand.loadOrbStands();
        Chest.loadChestStandImage();
    }

    // Get lighting by setting the entire map to dark by default, and the light
    // source squares to bright by default
    private void getLighting(int[][] map) {

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

            // Get the darkness and brightness of the squares surrounding a light source
            getSurroundingAlpha(position, variation);
        }

        // Make dark/light images of every single possible opacity
        for (int alpha = 0; alpha < MAX_ALPHA; alpha++) {

            darkImages[alpha] = createImage(alpha, 0, 0, 10);
            lightImages[alpha] = createImage(alpha, 80, 80, 0);
        }
    }

    // Set the aplha values of the tiles surrounding the light source
    private void getSurroundingAlpha(Point start, double variation) {

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

    // Get the neighboring points of a point
    private ArrayList<Point> getNeighbors(Point current) {

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

    // Fill the tile array with real tiles
    private void getTileImage() {

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

    // Make a hitbox for each tile to detect collision
    private void generateHitboxes() {

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

    // Loads map from .csv file. Each tile is represented with an integer code.
    private void loadMap(String file) {

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
        }
    }

    // Creates an image of any colour.
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

    // Draw everything
    public void draw(Graphics2D graphic) {

        drawTiles(graphic);
        drawObjects(graphic);
    }

    // Draw the base tiles
    private void drawTiles(Graphics2D graphic) {

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

    // Draw all the map's objects
    private void drawObjects(Graphics2D g) {

        Chest.drawChestStand(g, player);

        for (Chest chest : Chest.chests) {

            chest.draw(g, player, gamePanel);
        }

        for (Vent vent : Vent.vents) {

            vent.draw(g, player, gamePanel);
        }

        for (OrbStand stand : OrbStand.stands) {

            stand.draw(g, player);
        }
    }

    // Draw dark and light tiles over all tiles to create lighting effect
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

    public static Set<Integer> getCollisionTiles() {

        return collisionTiles;
    }

    public static Tile[] getTiles() {

        return tile;
    }

    public static Hitbox[][] getTileHitboxes() {

        return tileHitboxes;
    }

    public static int[][] getMap() {

        return map;
    }
}
