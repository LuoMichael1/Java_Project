import java.awt.*;
import java.util.Arrays;
import java.io.*;
import javax.imageio.ImageIO;

public class TileManager {

    InteractivePanel gamePanel;
    Tile[] tile;
    String[][] map;

    int C = 63;
    int R = 43;
    int NUM_TILES = 134;

    PlayerMovable player;

    public TileManager(InteractivePanel gamePanel, PlayerMovable player) {

        this.gamePanel = gamePanel;
        this.player = player;

        tile = new Tile[NUM_TILES + 1];
        map = new String[R][C];

        getTileImage();
        loadMap("maps/base-map2.csv");
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
                System.out.println(Arrays.toString(map[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
    }

    public void draw(Graphics2D graphic) {

        /*
         * for (int i = 0; i < gamePanel.MAX_SCREEN_ROW; i++) {
         * for (int j = 0; j < gamePanel.MAX_SCREEN_COL; j++) {
         * graphic.drawImage(tile[Integer.parseInt(map[i][j])].image, j *
         * gamePanel.TILE_SIZE,
         * i * gamePanel.TILE_SIZE,
         * gamePanel.TILE_SIZE,
         * gamePanel.TILE_SIZE, null);
         * }
         * }
         */

        // Convert the player's coordinates into the range of visible tiles
        int start_i = Math.max(0, player.y / gamePanel.TILE_SIZE);
        int end_i = Math.min(R, start_i + gamePanel.WINDOW_HEIGHT / gamePanel.TILE_SIZE + 2);

        int start_j = Math.max(0, player.x / gamePanel.TILE_SIZE);
        int end_j = Math.min(C, start_j + gamePanel.WINDOW_WIDTH / gamePanel.TILE_SIZE + 2);

        // Draw only the visible tiles
        for (int i = start_i; i < end_i; i++) {
            for (int j = start_j; j < end_j; j++) {
                graphic.drawImage(tile[Math.max(0, Integer.parseInt(map[i][j]))].image,
                        j * gamePanel.TILE_SIZE - player.x,
                        i * gamePanel.TILE_SIZE - player.y,
                        gamePanel.TILE_SIZE,
                        gamePanel.TILE_SIZE, null);
            }
        }
    }
}
