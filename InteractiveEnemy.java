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

public class InteractiveEnemy extends Entity {

    BufferedImage image;

    static final int InteractiveEnemy_WIDTH = 2;
    static final int InteractiveEnemy_HEIGHT = 2;

    static ArrayList<InteractiveEnemy> InteractiveEnemies = new ArrayList<>();

    int frameCounter = 0;
    Point current = new Point(this.x / 1, this.y / 1);;

    ArrayList<Point> path = new ArrayList<>();

    Set<Integer> collisionTiles = new HashSet<>();

    int C = 63;
    int R = 43;
    String[][] map;

    ArrayList<Point> triggerTiles;
    boolean chasing = false;
    boolean awayFromHome = false;
    boolean goingHome = false;

    int initalX;
    int initalY;

    static boolean inBattle = false;

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

            // Normalize the distances so that the enemy moves at a constant speed
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance != 0) { // Avoid division by zero
                dx /= distance;
                dy /= distance;
            }

            int speed = 4;

            // Move the enemy towards the next step
            this.x += dx * speed;
            this.y += dy * speed;

            System.out.println("x: " + dx);
            System.out.println("y: " + dy);

            // If the enemy has reached the next step, remove it from the path
            if (hitbox.centerX / InteractivePanel.getTileSize() == nextStep.x
                    && hitbox.centerY / InteractivePanel.getTileSize() == nextStep.y) {
                path.remove(0);
            }
        }
        // Increment counter
        frameCounter++;
    }

    ArrayList<Point> calculatePath(InteractivePanel gamePanel, int targetX, int targetY) {

        // Create a list of nodes to be evaluated (openList) and a list of nodes already
        // evaluated (closedList)
        PriorityQueue<Point> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Set<Point> closedList = new HashSet<>();

        current = new Point(hitbox.centerX / InteractivePanel.getTileSize(),
                hitbox.centerY / InteractivePanel.getTileSize());

        // Add the current enemy's position to the open list
        openList.add(current);

        // Keep looping until there are no more nodes to be evaluated
        while (!openList.isEmpty()) {
            // Find the node with the lowest f value (the estimated cost of the cheapest
            // solution through it)
            current = openList.poll();

            // If the current node is the player's position, we've found the shortest path
            if (current.equals(new Point(targetX / InteractivePanel.getTileSize(),
                    targetY / InteractivePanel.getTileSize()))) {
                break;
            }

            // Generate all the neighboring nodes
            ArrayList<Point> neighbors = generateNeighbors(current);

            // Evaluate each neighboring node
            for (Point neighbor : neighbors) {
                // If the neighboring node is already evaluated and has a lower f value, or if
                // the node is not traversable, skip it
                if (closedList.contains(neighbor) && neighbor.f <= current.f
                        || collisionTiles.contains(Integer.parseInt(map[neighbor.y][neighbor.x]))) {
                    continue;
                }

                // If the neighboring node is not yet evaluated or has a higher f value, update
                // it and add it to the open list
                if (!openList.contains(neighbor) || neighbor.f > current.f) {
                    neighbor.g = current.g + distanceBetween(current, neighbor); // The cost to reach the
                                                                                 // neighboring
                                                                                 // node from the start
                    neighbor.h = distanceBetween(neighbor,
                            new Point(targetX / InteractivePanel.getTileSize(),
                                    targetY / InteractivePanel.getTileSize())); // The
                    // estimated
                    // cost from
                    // the
                    // neighboring node to the goal
                    neighbor.f = neighbor.g + neighbor.h; // The estimated cost of the cheapest solution through the
                                                          // neighboring node
                    neighbor.parent = current; // The node that can be reached with the lowest cost from the
                                               // neighboring
                                               // node
                    // System.out.println(
                    // neighbor.x + ", " + neighbor.y + "'s parent is " + current.x + ", " +
                    // current.y);
                    // System.out.println(neighbor.x + ", " + neighbor.y + "'s parent is " +
                    // neighbor.parent.x + ", "
                    // + neighbor.parent.y);
                    openList.add(neighbor);
                }
            }

            // Add the current node to the closed list
            closedList.add(current);
        }

        // Trace back from the player's position to the enemy's position to get the
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

    // This method generates all valid neighbors of the current node
    ArrayList<Point> generateNeighbors(Point current) {
        ArrayList<Point> neighbors = new ArrayList<>();

        // Add the points to the north, south, east, and west of the current point
        // Make sure to check if these points are within the bounds of your game map
        // if (!collisionTiles.contains(Integer.parseInt(map[current.y][current.x -
        // 1])))
        neighbors.add(new Point(current.x, current.y - 1)); // North
        // if (!collisionTiles.contains(Integer.parseInt(map[current.y][current.x +
        // 1])))
        neighbors.add(new Point(current.x, current.y + 1)); // South
        // if (!collisionTiles.contains(Integer.parseInt(map[current.y -
        // 1][current.x])))
        neighbors.add(new Point(current.x - 1, current.y)); // West
        // if (!collisionTiles.contains(Integer.parseInt(map[current.y +
        // 1][current.x])))
        neighbors.add(new Point(current.x + 1, current.y)); // East

        // Diagonals
        /*
         * neighbors.add(new Point(current.x - 1, current.y - 1)); // North
         * neighbors.add(new Point(current.x + 1, current.y + 1)); // South
         * neighbors.add(new Point(current.x - 1, current.y + 1)); // West
         * neighbors.add(new Point(current.x + 1, current.y - 1));
         */
        return neighbors;
    }

    // This method returns the actual distance between two points
    int distanceBetween(Point a, Point b) {
        return (int) (Math.sqrt(Math.abs(a.x - b.x) * Math.abs(a.x - b.x) + Math.abs(a.y - b.y) * Math.abs(a.y - b.y))
                * 10);
    }

    public void update(PlayerMovable player, InteractivePanel gamePanel) {

        hitbox = new Hitbox(y + InteractivePanel.getTileSize(),
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

    public void draw(Graphics2D graphic, PlayerMovable player, InteractivePanel gamePanel) {

        graphic.drawImage(image, x - player.x + player.getDrawX(),
                y - player.y + player.getDrawY(),
                InteractivePanel.getTileSize() * InteractiveEnemy_WIDTH,
                InteractivePanel.getTileSize() * InteractiveEnemy_HEIGHT, null);
    }

    public void checkCollision(PlayerMovable player, InteractivePanel gamePanel) {

        if (!player.inVent) {

            if (hitbox.centerX / InteractivePanel.getTileSize() == player.hitbox.centerX
                    / InteractivePanel.getTileSize()
                    && hitbox.centerY / InteractivePanel.getTileSize() == player.hitbox.centerY
                            / InteractivePanel.getTileSize()) {

                System.out.println("You opened a InteractiveEnemy");

                InteractivePanel.active = false;
                Main.showCard("CardGame");
                inBattle = true;

            }
        }
    }
}
