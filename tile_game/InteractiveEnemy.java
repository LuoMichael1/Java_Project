// This class contains all methods and attributes of the enemies on the map.
// By Alec

package tile_game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
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

    private static final String ENEMY_COORDINATES_PATH = "tile_game/maps/enemy-coordinates.csv";

    // This public list contains all the enemies.
    public static ArrayList<InteractiveEnemy> enemies = new ArrayList<>();

    private int frameCounter = 0;
    private Point current = new Point(this.x / 1, this.y / 1);;

    private ArrayList<Point> path = new ArrayList<>();

    // Enemy behaviour depends on the state described by these variables.
    // If none are true, enemy is idle
    // If a trigger tile is stepped on, enemy starts chasing
    // If chasing, get shortest path to player every 30 frames and chase player
    // If chasing but path gets too long, start going home
    // Continue determining path while away from home
    // If going home but distance to player decreases again, start chasing again
    // Become idle again once home is reached

    private ArrayList<Point> triggerTiles;
    private boolean chasing = false;
    private boolean goingHome = false;

    // Default position
    private int initalX;
    private int initalY;

    private boolean inBattle = false;

    public static void loadEnemies() {

        // reads in enemies in format:
        // x,y,type,triggertile1X,triggertile1Y,triggertile2X,triggertile2Y
        // any amount of trigger tiles can be added

        try {

            String line;
            BufferedReader reader = new BufferedReader(new FileReader(ENEMY_COORDINATES_PATH));

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                String type = parts[2];

                ArrayList<Point> triggers = new ArrayList<>();
                for (int j = 3; j < parts.length; j += 2) {

                    triggers.add(new Point(Integer.parseInt(parts[j]), Integer.parseInt(parts[j + 1])));
                }

                InteractiveEnemy enemy = new InteractiveEnemy(x, y, type, triggers);
                InteractiveEnemy.enemies.add(enemy);
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadImages(String type) {

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
                default:
                    System.out.println("No image found");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private InteractiveEnemy(int x, int y, String enemyType, ArrayList<Point> triggerTiles) {

        // Get actual x and y
        this.x = x * InteractivePanel.getTileSize();
        this.y = y * InteractivePanel.getTileSize();

        initalX = this.x;
        initalY = this.y;

        this.triggerTiles = triggerTiles;

        hitbox = new Hitbox(y + InteractivePanel.getTileSize(),
                x + InteractivePanel.getTileSize() * InteractiveEnemy_WIDTH / 4,
                InteractivePanel.getTileSize(),
                InteractivePanel.getTileSize());

        loadImages(enemyType);
    }

    private void chasingUpdate(PlayerMovable player, InteractivePanel gamePanel) {

        // Check if arrived at home
        if (hitbox.getCenterX() / InteractivePanel.getTileSize() == initalX / InteractivePanel.getTileSize()
                && hitbox.getCenterY() / InteractivePanel.getTileSize() == initalY / InteractivePanel.getTileSize()
                && !chasing) {

            System.out.println(
                    "arrived home at " + initalX / InteractivePanel.getTileSize() + ", "
                            + initalY / InteractivePanel.getTileSize());
            chasing = false;
            goingHome = false;
            return;
        }

        // Calculate path every 30 frames while chasing
        if (frameCounter % 30 == 0) {

            if (goingHome) {

                path = calculatePath(gamePanel, initalX, initalY);
            } else {

                path = calculatePath(gamePanel, player.hitbox.getCenterX(), player.hitbox.getCenterY());
            }
            // Reset counter
            frameCounter = 0;
        }

        // Follow the path as long as there is one
        if (!path.isEmpty()) {

            // Start going home if path is too long
            if ((path.size() > 8 || player.isInVent()) && !goingHome) {
                chasing = false;
                goingHome = true;
                return;

                // Chase otherwise
            } else {
                chasing = true;
                goingHome = false;
            }

            // Move towards next step
            Point nextStep = path.get(0);
            int dx = nextStep.x - hitbox.getCenterX() / InteractivePanel.getTileSize();
            int dy = nextStep.y - hitbox.getCenterY() / InteractivePanel.getTileSize();

            int speed = 4;

            this.x += dx * speed;
            this.y += dy * speed;

            if (hitbox.getCenterX() / InteractivePanel.getTileSize() == nextStep.x
                    && hitbox.getCenterY() / InteractivePanel.getTileSize() == nextStep.y) {
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
    private ArrayList<Point> calculatePath(InteractivePanel gamePanel, int targetX, int targetY) {

        // evaluate nodes in openlist and skip nodes in closedList (already evaluated)
        PriorityQueue<Point> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.f)); // prioritize nodes with
                                                                                                // smallest f value
        Set<Point> closedList = new HashSet<>();

        // get target coordinates as a point for comparision
        Point target = new Point(targetX / InteractivePanel.getTileSize(),
                targetY / InteractivePanel.getTileSize());

        // add the starting position to openList
        current = new Point(hitbox.getCenterX() / InteractivePanel.getTileSize(),
                hitbox.getCenterY() / InteractivePanel.getTileSize());
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
                        || TileManager.getTiles()[TileManager.getMap()[neighbor.y][neighbor.x]].collision) {
                    continue;
                }

                if (!openList.contains(neighbor) || neighbor.f > current.f) {

                    neighbor.g = current.g + distanceBetween(current, neighbor); // g cost is the estimated distance
                                                                                 // from the initial position
                    neighbor.h = distanceBetween(neighbor, target); // h cost is the estimated distance from the
                                                                    // position to the target
                    neighbor.f = neighbor.g + neighbor.h; // f cost is the sum of g and h costs
                    neighbor.parent = current; // set parent node to find path later
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
                        new Point(hitbox.getCenterX() / InteractivePanel.getTileSize(),
                                hitbox.getCenterY() / InteractivePanel.getTileSize()))) {

            path.add(0, current);
            current = current.parent;
        }

        return path;
    }

    private ArrayList<Point> generateNeighbors(Point current) {

        ArrayList<Point> neighbors = new ArrayList<>();

        neighbors.add(new Point(current.x, current.y - 1));
        neighbors.add(new Point(current.x, current.y + 1));
        neighbors.add(new Point(current.x - 1, current.y));
        neighbors.add(new Point(current.x + 1, current.y));

        return neighbors;
    }

    private int distanceBetween(Point a, Point b) {
        return (int) (Math.sqrt(Math.abs(a.x - b.x) * Math.abs(a.x - b.x) + Math.abs(a.y - b.y) * Math.abs(a.y - b.y))
                * 10);
    }

    public void update(PlayerMovable player, InteractivePanel gamePanel) {

        hitbox.update(y + InteractivePanel.getTileSize(),
                x + InteractivePanel.getTileSize() * InteractiveEnemy_WIDTH / 4,
                InteractivePanel.getTileSize(),
                InteractivePanel.getTileSize());

        // Start battle if collision with player
        checkCollision(player, gamePanel);

        // Start chasing if triggered
        for (Point triggerTile : triggerTiles) {

            if (player.getCurrentTileX() == triggerTile.x && player.getCurrentTileY() == triggerTile.y
                    && !player.isInVent() && !chasing && !goingHome) {
                System.out.println("trigger entered");
                chasing = true;
                goingHome = false;
            }
        }

        // Calculate path as long as enemy is not at home
        if (chasing || goingHome) {

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

        if (!player.isInVent() && !inBattle) {

            if (hitbox.getCenterX() / InteractivePanel.getTileSize() == player.hitbox.getCenterX()
                    / InteractivePanel.getTileSize()
                    && hitbox.getCenterY() / InteractivePanel.getTileSize() == player.hitbox.getCenterY()
                            / InteractivePanel.getTileSize()) {

                System.out.println("Start battle");
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
