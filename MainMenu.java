import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    private static final int LAYER_SPEED_PLANETS = 2;
    private static final int LAYER_SPEED_STARS = 1;

    private static final int IMAGE_WIDTH = 1280;
    private static final int IMAGE_HEIGHT = 720;

    private int layer1X1_planets = 0;
    private int layer2X1_planets = IMAGE_WIDTH;

    private int layer1X2_planets = 0;
    private int layer2X2_planets = IMAGE_WIDTH;

    private int layer1X1_stars = 0;
    private int layer2X1_stars = IMAGE_WIDTH;

    private int layer1X2_stars = 0;
    private int layer2X2_stars = IMAGE_WIDTH;

    public MainMenu() {
        setTitle("Main Menu");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1280, 720));

        // Add a background image to the background layer
        JLabel backgroundImage = createBackgroundLabel("menu/parallax-space-backgound.png", IMAGE_WIDTH, IMAGE_HEIGHT);
        layeredPane.add(backgroundImage, Integer.valueOf(-1)); // Set to -1 for the background layer
        setLayerBounds(layeredPane, backgroundImage, 0, 0, -1);

        // add black bars
        JLabel bar1 = createBackgroundLabel("menu/black-bar.png", IMAGE_WIDTH, IMAGE_HEIGHT / 6);
        JLabel bar2 = createBackgroundLabel("menu/black-bar.png", IMAGE_WIDTH, IMAGE_HEIGHT / 6);

        layeredPane.add(bar1, 1);
        layeredPane.add(bar2, 1);

        setLayerBounds(layeredPane, bar1, 0, 0,
                100);
        setLayerBounds(layeredPane, bar2, 0, 564, 100); // later update magic values

        JLabel background1_1_planets = createBackgroundLabel("menu/parallax-space-far-planets.png", IMAGE_WIDTH, 720);
        JLabel background2_1_planets = createBackgroundLabel("menu/parallax-space-far-planets.png", IMAGE_WIDTH, 720);

        JLabel background1_1_stars = createBackgroundLabel("menu/parallax-space-stars.png", IMAGE_WIDTH, 720);
        JLabel background2_1_stars = createBackgroundLabel("menu/parallax-space-stars.png", IMAGE_WIDTH, 720);

        // Add parallax layers to the background layer
        layeredPane.add(background1_1_planets, Integer.valueOf(0));
        layeredPane.add(background2_1_planets, Integer.valueOf(0));
        layeredPane.add(background1_1_stars, Integer.valueOf(0));
        layeredPane.add(background2_1_stars, Integer.valueOf(0));

        setLayerBounds(layeredPane, background1_1_planets, 0, 0, JLayeredPane.DEFAULT_LAYER);
        setLayerBounds(layeredPane, background2_1_planets, IMAGE_WIDTH, 0, JLayeredPane.DEFAULT_LAYER);
        setLayerBounds(layeredPane, background1_1_stars, 0, 0, JLayeredPane.DEFAULT_LAYER);
        setLayerBounds(layeredPane, background2_1_stars, IMAGE_WIDTH, 0, JLayeredPane.DEFAULT_LAYER);

        Timer timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update parallax layers for planets
                layer1X1_planets -= LAYER_SPEED_PLANETS;
                layer2X1_planets -= LAYER_SPEED_PLANETS;

                layer1X2_planets -= LAYER_SPEED_PLANETS;
                layer2X2_planets -= LAYER_SPEED_PLANETS;

                // Update parallax layers for stars
                layer1X1_stars -= LAYER_SPEED_STARS;
                layer2X1_stars -= LAYER_SPEED_STARS;

                layer1X2_stars -= LAYER_SPEED_STARS;
                layer2X2_stars -= LAYER_SPEED_STARS;

                // Check if the images are off-screen, and reset their positions for planets
                if (layer1X1_planets <= -IMAGE_WIDTH) {
                    layer1X1_planets = IMAGE_WIDTH;
                }
                if (layer2X1_planets <= -IMAGE_WIDTH) {
                    layer2X1_planets = IMAGE_WIDTH;
                }

                setLayerBounds(layeredPane, background1_1_planets, layer1X1_planets, 0, JLayeredPane.DEFAULT_LAYER);
                setLayerBounds(layeredPane, background2_1_planets, layer2X1_planets, 0, JLayeredPane.DEFAULT_LAYER);
                setLayerBounds(layeredPane, background1_1_stars, layer1X1_stars, 0, JLayeredPane.DEFAULT_LAYER);
                setLayerBounds(layeredPane, background2_1_stars, layer2X1_stars, 0, JLayeredPane.DEFAULT_LAYER);
                setLayerBounds(layeredPane, background1_1_planets, layer1X2_planets, 0, JLayeredPane.DEFAULT_LAYER);
                setLayerBounds(layeredPane, background2_1_planets, layer2X2_planets, 0, JLayeredPane.DEFAULT_LAYER);
                setLayerBounds(layeredPane, background1_1_stars, layer1X2_stars, 0, JLayeredPane.DEFAULT_LAYER);
                setLayerBounds(layeredPane, background2_1_stars, layer2X2_stars, 0, JLayeredPane.DEFAULT_LAYER);
            }
        });

        timer.start();

        setContentPane(layeredPane);
    }

    private JLabel createBackgroundLabel(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(image);
        JLabel label = new JLabel(scaledIcon);
        label.setSize(width, height);
        return label;
    }

    private void setLayerBounds(JLayeredPane layeredPane, Component component, int x, int y, int layer) {
        component.setBounds(x, y, component.getWidth(), component.getHeight());
        layeredPane.setLayer(component, layer);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }
}