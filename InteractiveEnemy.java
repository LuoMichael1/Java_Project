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

    Point triggerTile;
    boolean chasing = false;

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

    public InteractiveEnemy(int x, int y, InteractivePanel gamePanel, String enemyType, Point triggerTile) {

        this.x = x * gamePanel.TILE_SIZE;
        this.y = y * gamePanel.TILE_SIZE;

        this.triggerTile = triggerTile;

        collisionTiles
                .addAll(Arrays.asList(new Integer[] { 105, 84, 85, 95, 98, 132, 0, 96, 103, 108, 111, 112, 113, 114, 7,
                        91, 94, 106, 107, 133, 134, 130 }));

        loadImages(enemyType);
    }

    public void update(PlayerMovable player, InteractivePanel gamePanel) {

        if (triggerTile == null)
            return;

        if (player.getCurrentTileX() == triggerTile.x && player.getCurrentTileY() == triggerTile.y) {
            System.out.println("trigger entered");
            chasing = true;
        }

        if (chasing) {
            System.out.println("chasing");
            hitbox = new Hitbox(y + gamePanel.TILE_SIZE * InteractiveEnemy_HEIGHT / 4,
                    x + gamePanel.TILE_SIZE * InteractiveEnemy_WIDTH / 4,
                    gamePanel.TILE_SIZE * InteractiveEnemy_WIDTH / 2,
                    gamePanel.TILE_SIZE * InteractiveEnemy_HEIGHT / 4 * 3);

            int dx = triggerTile.x - hitbox.centerX / gamePanel.TILE_SIZE;
            int dy = triggerTile.y - hitbox.centerY / gamePanel.TILE_SIZE;

            System.out.println("x + " + dx * 5);
            System.out.println("y + " + dy * 5);

            /*
             * // Normalize the distances so that the enemy moves at a constant speed
             * double distance = Math.sqrt(dx * dx + dy * dy);
             * if (distance != 0) { // Avoid division by zero
             * dx /= distance;
             * dy /= distance;
             * }
             */

            System.out.println("x + " + dx * 5);
            System.out.println("y + " + dy * 5);

            // Move the enemy towards the next step
            this.x += dx * 2;
            this.y += dy * 2;

            // If the enemy has reached the player's location, stop moving
            if (hitbox.centerX / gamePanel.TILE_SIZE == triggerTile.x
                    && hitbox.centerY / gamePanel.TILE_SIZE == triggerTile.y) {
                chasing = false;
            }
        }
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
