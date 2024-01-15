package tile_game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import javax.imageio.ImageIO;

import main.Main;

public class InteractiveEnemy extends Entity {

    private BufferedImage image;

    private static final int InteractiveEnemy_WIDTH = 2;
    private static final int InteractiveEnemy_HEIGHT = 2;

    public static ArrayList<InteractiveEnemy> InteractiveEnemies = new ArrayList<>();

    private int frameCounter = 0;
    private Point current = new Point(this.x / 1, this.y / 1);;

    private ArrayList<Point> path = new ArrayList<>();

    private Set<Integer> collisionTiles = new HashSet<>();

    private int C = 63;
    private int R = 43;
    private String[][] map;

    private ArrayList<Point> triggerTiles;
    private boolean chasing = false;
    private boolean awayFromHome = false;
    private boolean goingHome = false;

    private int initalX;
    private int initalY;

    private boolean inBattle = false;

    public void loadImages(String type) {

        try {

            switch (type) {
                case "tusk":
                    image = ImageIO.read(getClass().getResourceAsStream("objects/tusk-zombie.png"));
                    break;
                case "tiny":
                    image = ImageIO.read(getClass().getResourceAsStream("objects/tiny-zombie.png"));
                    break;
                case "blob":
                    image = ImageIO.read(getClass().getResourceAsStream("objects/blob-zombie.png"));
                    break;
                case "hugeGreen":
                    image = ImageIO.read(getClass().getResourceAsStream("objects/huge-green-zombie.png"));
                    break;
                default:
                    System.out.println("No image found");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public InteractiveEnemy(int x, int y, InteractivePanel gamePanel, String enemyType, ArrayList<Point> triggerTiles) {

        this.x = x * InteractivePanel.getTileSize();
        this.y = y * InteractivePanel.getTileSize();

        initalX = this.x;
        initalY = this.y;

        this.triggerTiles = triggerTiles;

        collisionTiles
                .addAll(Arrays.asList(new Integer[] { 105, 84, 85, 95, 98, 132, 0, 96, 103, 108, 111, 112, 113, 114, 7,
                        91, 94, 106, 107, 133, 134, 130 }));

        loadImages(enemyType);
        loadMap("maps/base-map2.csv");

        hitbox = new Hitbox(y + InteractivePanel.getTileSize(),
                x + InteractivePanel.getTileSize() * InteractiveEnemy_WIDTH / 4,
                InteractivePanel.getTileSize(),
                InteractivePanel.getTileSize());
    }

    public void loadMap(String file) {

        map = new String[R][C];

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(file)));

            for (int i = 0; i < R; i++) {
                map[i] = reader.readLine().split(",");
                System.out.println(Arrays.toString(map[i]));
            }

        } catch (Exception e) {
            System.out.println(":P");
            e.printStackTrace();
            ;
        }
    }

    public void chasingUpdate(PlayerMovable player, InteractivePanel gamePanel) {

        if (hitbox.centerX / InteractivePanel.getTileSize() == initalX / InteractivePanel.getTileSize()
                && hitbox.centerY / InteractivePanel.getTileSize() == initalY / InteractivePanel.getTileSize()
                && !chasing) {

            System.out.println(
                    "arrived home at " + initalX / InteractivePanel.getTileSize() + ", "
                            + initalY / InteractivePanel.getTileSize());
            chasing = false;
            goingHome = false;
            awayFromHome = false;
        }

        if (frameCounter % 30 == 0) {

            if (goingHome) {

                path = calculatePath(gamePanel, initalX, initalY);
            } else {

                path = calculatePath(gamePanel, player.hitbox.centerX, player.hitbox.centerY);
            }
            // Reset counter
            frameCounter = 0;
        }

        if (!path.isEmpty()) {

            if ((path.size() > 8 || player.inVent) && !goingHome) {
                chasing = false;
                goingHome = true;
                return;
            } else {
                chasing = true;
                goingHome = false;
            }

            Point nextStep = path.get(0);
            int dx = nextStep.x - hitbox.centerX / InteractivePanel.getTileSize();
            int dy = nextStep.y - hitbox.centerY / InteractivePanel.getTileSize();

            int speed = 4;

            this.x += dx * speed;
            this.y += dy * speed;

            System.out.println("x: " + dx);
            System.out.println("y: " + dy);

            if (hitbox.centerX / InteractivePanel.getTileSize() == nextStep.x
                    && hitbox.centerY / InteractivePanel.getTileSize() == nextStep.y) {
                path.remove(0);
            }
        }
        // Increment counter
        frameCounter++;
    }

    // A* (A-star) pathfinding algorithm to generate a path between enemy's current
    // position and a target position
    // video tutorial source by Sebastian Lague (contains pseudocode):
    // https://www.youtube.com/watch?v=-L-WgKMFuhE
    ArrayList<Point> calculatePath(InteractivePanel gamePanel, int targetX, int targetY) {

        // evaluate nodes in openlist and skip nodes in closedList (already evaluated)
        PriorityQueue<Point> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.f)); // prioritize nodes with
                                                                                                // smallest f value
        Set<Point> closedList = new HashSet<>();

        // get target coordinates as a point for comparision
        Point target = new Point(targetX / InteractivePanel.getTileSize(),
                targetY / InteractivePanel.getTileSize());

        // add the starting position to openList
        current = new Point(hitbox.centerX / InteractivePanel.getTileSize(),
                hitbox.centerY / InteractivePanel.getTileSize());
        openList.add(current);

        // evaluate all nodes in openList
        while (!openList.isEmpty()) {
            // this gets the node with the smallest f value
            current = openList.poll();

            // path is complete once current tile equals target tile
            if (current.equals(target)) {
                break;
            }

            ArrayList<Point> neighbors = generateNeighbors(current);

            for (Point neighbor : neighbors) {
                // skip if the neighboring node is already evaluated and has a lower f value or
                // if
                // the node is not traversable
                if (closedList.contains(neighbor) && neighbor.f <= current.f
                        || collisionTiles.contains(Integer.parseInt(map[neighbor.y][neighbor.x]))) {
                    continue;
                }

                if (!openList.contains(neighbor) || neighbor.f > current.f) {
                    neighbor.g = current.g + distanceBetween(current, neighbor); // g cost is the estimated distance
                                                                                 // from the initial position
                    neighbor.h = distanceBetween(neighbor, target); // h cost is the estimated distance from the
                                                                    // position to the target
                    neighbor.f = neighbor.g + neighbor.h; // f cost is the sum of g and h costs
                    neighbor.parent = current; // set parent node to find path later
                    // System.out.println(
                    // neighbor.x + ", " + neighbor.y + "'s parent is " + current.x + ", " +
                    // current.y);
                    // System.out.println(neighbor.x + ", " + neighbor.y + "'s parent is " +
                    // neighbor.parent.x + ", "
                    // + neighbor.parent.y);
                    openList.add(neighbor);
                }
            }

            closedList.add(current);
        }

        // Trace back from the target position to the enemy's position to get the
        // shortest path
        ArrayList<Point> path = new ArrayList<>();
        while (current != null
                && !current.equals(
                        new Point(hitbox.centerX / InteractivePanel.getTileSize(),
                                hitbox.centerY / InteractivePanel.getTileSize()))) {
            path.add(0, current);
            current = current.parent;
        }

        return path;
    }

    ArrayList<Point> generateNeighbors(Point current) {
        ArrayList<Point> neighbors = new ArrayList<>();

        neighbors.add(new Point(current.x, current.y - 1));
        neighbors.add(new Point(current.x, current.y + 1));
        neighbors.add(new Point(current.x - 1, current.y));
        neighbors.add(new Point(current.x + 1, current.y));

        return neighbors;
    }

    public int distanceBetween(Point a, Point b) {
        return (int) (Math.sqrt(Math.abs(a.x - b.x) * Math.abs(a.x - b.x) + Math.abs(a.y - b.y) * Math.abs(a.y - b.y))
                * 10);
    }

    public void update(PlayerMovable player, InteractivePanel gamePanel) {

        hitbox.update(y + InteractivePanel.getTileSize(),
                x + InteractivePanel.getTileSize() * InteractiveEnemy_WIDTH / 4,
                InteractivePanel.getTileSize(),
                InteractivePanel.getTileSize());

        checkCollision(player, gamePanel);

        for (Point triggerTile : triggerTiles) {

            if (player.getCurrentTileX() == triggerTile.x && player.getCurrentTileY() == triggerTile.y
                    && !player.inVent && !awayFromHome) {
                System.out.println("trigger entered");
                chasing = true;
                awayFromHome = true;
                goingHome = false;
            }
        }

        if (chasing || awayFromHome) {

            chasingUpdate(player, gamePanel);
        }
    }

    public void draw(Graphics2D graphic, PlayerMovable player) {

        graphic.drawImage(image, x - player.x + player.getDrawX(),
                y - player.y + player.getDrawY(),
                InteractivePanel.getTileSize() * InteractiveEnemy_WIDTH,
                InteractivePanel.getTileSize() * InteractiveEnemy_HEIGHT, null);
    }

    public void checkCollision(PlayerMovable player, InteractivePanel gamePanel) {

        if (!player.inVent && !inBattle) {

            if (hitbox.centerX / InteractivePanel.getTileSize() == player.hitbox.centerX
                    / InteractivePanel.getTileSize()
                    && hitbox.centerY / InteractivePanel.getTileSize() == player.hitbox.centerY
                            / InteractivePanel.getTileSize()) {

                System.out.println("You opened a InteractiveEnemy");

                Main.showCard("CardGame");

                inBattle = true;

            }
        }
    }

    public boolean isInBattle() {
        return inBattle;
    }

    public void setInBattle(boolean bool) {
        inBattle = bool;
    }
}
