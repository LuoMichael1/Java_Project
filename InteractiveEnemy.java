import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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

    public void loadImages() {

        try {

            image = ImageIO.read(getClass().getResourceAsStream("objects/enemy.png"));

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public InteractiveEnemy(int x, int y, InteractivePanel gamePanel) {

        this.x = x * gamePanel.TILE_SIZE;
        this.y = y * gamePanel.TILE_SIZE;

    }

    public void update(PlayerMovable player, InteractivePanel gamePanel) {

        if (frameCounter % 60 == 0) {

            path = calculatePath(player, gamePanel);
            // Reset counter
            frameCounter = 0;
        }

        if (!path.isEmpty()) {
            Point nextStep = path.get(0);
            int dx = nextStep.x - this.x / gamePanel.TILE_SIZE;
            int dy = nextStep.y - this.y / gamePanel.TILE_SIZE;

            // Normalize the distances so that the enemy moves at a constant speed
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance != 0) { // Avoid division by zero
                dx /= distance;
                dy /= distance;
            }

            // Move the enemy towards the next step
            this.x += dx * 2;
            this.y += dy * 2;

            // If the enemy has reached the next step, remove it from the path
            if (this.x / gamePanel.TILE_SIZE == nextStep.x && this.y / gamePanel.TILE_SIZE == nextStep.y) {
                path.remove(0);
            }
        }
        // Increment counter
        frameCounter++;
    }

    ArrayList<Point> calculatePath(PlayerMovable player, InteractivePanel gamePanel) {

        // Create a list of nodes to be evaluated (openList) and a list of nodes already
        // evaluated (closedList)
        PriorityQueue<Point> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Set<Point> closedList = new HashSet<>();

        current = new Point(this.x / gamePanel.TILE_SIZE, this.y / gamePanel.TILE_SIZE);

        // Add the current enemy's position to the open list
        openList.add(current);

        // Keep looping until there are no more nodes to be evaluated
        while (!openList.isEmpty()) {
            // Find the node with the lowest f value (the estimated cost of the cheapest
            // solution through it)
            current = openList.poll();

            // If the current node is the player's position, we've found the shortest path
            if (current.equals(new Point(player.x / gamePanel.TILE_SIZE, player.y / gamePanel.TILE_SIZE))) {
                break;
            }

            // Generate all the neighboring nodes
            ArrayList<Point> neighbors = generateNeighbors(current);

            // Evaluate each neighboring node
            for (Point neighbor : neighbors) {
                // If the neighboring node is already evaluated and has a lower f value, skip it
                if (closedList.contains(neighbor) && neighbor.f <= current.f) {
                    continue;
                }

                // If the neighboring node is not yet evaluated or has a higher f value, update
                // it and add it to the open list
                if (!openList.contains(neighbor) || neighbor.f > current.f) {
                    neighbor.g = current.g + distanceBetween(current, neighbor); // The cost to reach the
                                                                                 // neighboring
                                                                                 // node from the start
                    neighbor.h = heuristic(neighbor,
                            new Point(player.x / gamePanel.TILE_SIZE, player.y / gamePanel.TILE_SIZE)); // The
                                                                                                        // estimated
                                                                                                        // cost from
                                                                                                        // the
                    // neighboring node to the goal
                    neighbor.f = neighbor.g + neighbor.h; // The estimated cost of the cheapest solution through the
                                                          // neighboring node
                    neighbor.parent = current; // The node that can be reached with the lowest cost from the
                                               // neighboring
                                               // node
                    System.out.println(
                            neighbor.x + ", " + neighbor.y + "'s parent is " + current.x + ", " + current.y);
                    System.out.println(neighbor.x + ", " + neighbor.y + "'s parent is " + neighbor.parent.x + ", "
                            + neighbor.parent.y);
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
                && !current.equals(new Point(this.x / gamePanel.TILE_SIZE, this.y / gamePanel.TILE_SIZE))) {
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
        neighbors.add(new Point(current.x, current.y - 1)); // North
        neighbors.add(new Point(current.x, current.y + 1)); // South
        neighbors.add(new Point(current.x - 1, current.y)); // West
        neighbors.add(new Point(current.x + 1, current.y)); // East

        return neighbors;
    }

    // This method returns the actual distance between two points
    int distanceBetween(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y); // Manhattan distance
    }

    // This method estimates the distance between two points
    int heuristic(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y); // Manhattan distance
    }

    public void draw(Graphics2D graphic, PlayerMovable player, InteractivePanel gamePanel) {

        graphic.drawImage(image, x - player.x + player.getDrawX(),
                y - player.y + player.getDrawY(),
                gamePanel.TILE_SIZE * InteractiveEnemy_WIDTH,
                gamePanel.TILE_SIZE * InteractiveEnemy_HEIGHT, null);
    }

    static void checkCollision(PlayerMovable player, InteractivePanel gamePanel) {

        // Get player's current tile
        int currentTileY = Math.max(0, player.hitbox.centerY / gamePanel.TILE_SIZE);
        int currentTileX = Math.max(0, player.hitbox.centerX / gamePanel.TILE_SIZE);

        for (InteractiveEnemy InteractiveEnemy : InteractiveEnemies) {

            // Get enemy's current tile
            int tileY = Math.max(0, InteractiveEnemy.y / gamePanel.TILE_SIZE);
            int tileX = Math.max(0, InteractiveEnemy.x / gamePanel.TILE_SIZE);

            if (tileY + 1 == currentTileY) {
                if (tileX == currentTileX || tileX + 1 == currentTileX) {

                    System.out.println("You opened a InteractiveEnemy");
                }
            }
        }
    }
}
