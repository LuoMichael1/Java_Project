import java.awt.*;
import java.util.Arrays;
import java.io.*;
import javax.imageio.ImageIO;

public class TileManager {

    InteractivePanel gamePanel;
    Tile[] tile;
    String[][] map;

    int C = 34;
    int R = 49;

    public TileManager(InteractivePanel gamePanel) {

        this.gamePanel = gamePanel;

        tile = new Tile[120];
        map = new String[R][C];

        getTileImage();
        loadMap("maps/map1.txt");
    }

    public void getTileImage() {

        try {

            for (int i = 0; i < 120; i++) {

                tile[i] = new Tile();
                tile[i].image = ImageIO
                        .read(getClass().getResourceAsStream("tile/tile" + String.format("%03d", i) + ".png"));
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
        int h = gamePanel.WINDOW_HEIGHT;
        int w = gamePanel.WINDOW_WIDTH;
        int t = Math.min(h / R, w / C);

        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                graphic.drawImage(tile[Integer.parseInt(map[i][j])].image,
                        j * t,
                        i * t,
                        t,
                        t, null);
            }
        }
    }
}
