// main menu screen

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//import java.awt.event.ActionListener;

public class MainMenu extends JPanel implements MouseListener {

    private static final int LAYER_SPEED_PLANETS = 2;
    private static final int LAYER_SPEED_STARS = 1;

    //private static final int IMAGE_WIDTH = 1280;
    //private static final int IMAGE_HEIGHT = 720;

    //private int layer1X1_planets = 0;
    //private int layer2X1_planets = Main.WIDTH;

    //private int layer1X2_planets = 0;
    //private int layer2X2_planets = Main.WIDTH;

    private int layer1X1_stars = 0;
    private int layer2X1_stars = Main.WIDTH;

    private int layer1X2_stars = 0;
    private int layer2X2_stars = Main.WIDTH;
    
    private boolean seenScene1 = false;

    private int planetY = 0;
    private double size = 0;  // the size of the planets (the formula is their normal size multiplied by this)

    public MainMenu() {
        setBackground(Color.BLACK);
        // setTitle("Main Menu");
        // setSize(1280, 720);
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.addMouseListener(this);

        JLayeredPane layeredPane = new JLayeredPane();


        
        layeredPane.setPreferredSize(new Dimension(1280, 720));

        // Add a background image to the background layer
        //JLabel backgroundImage = createBackgroundLabel("menu/parallax-space-backgound.png", IMAGE_WIDTH, IMAGE_HEIGHT);
        //layeredPane.add(backgroundImage, Integer.valueOf(-1)); // Set to -1 for the background layer
        //setLayerBounds(layeredPane, backgroundImage, 0, 0, -1);

        // add black bars
        //JLabel bar1 = createBackgroundLabel("menu/black-bar.png", IMAGE_WIDTH, IMAGE_HEIGHT / 6);
        //JLabel bar2 = createBackgroundLabel("menu/black-bar.png", IMAGE_WIDTH, IMAGE_HEIGHT / 6);

        //layeredPane.add(bar1, 1);
        //layeredPane.add(bar2, 1);

        //setLayerBounds(layeredPane, bar1, 0, 0, 100);
        //setLayerBounds(layeredPane, bar2, 0, 564, 100); // later update magic values

        //JLabel background1_1_planets = createBackgroundLabel("menu/parallax-space-far-planets.png", Main.WIDTH, Main.HEIGHT);
        //JLabel background2_1_planets = createBackgroundLabel("menu/parallax-space-far-planets.png", Main.WIDTH, Main.HEIGHT);
        
        // create planets -----------------------------------
        JLabel[] planets = new JLabel[3];
        int[] planetsX = new int[3];
        int[] planetsY = new int[3];

        int rand = 0;
        for (int i = 0; i < planets.length; i++) {
            rand = (int)(Math.random()*2)+1;
            
            // randomly creates a size for each planet. The +0.3 prevents tiny planets
            size = Math.random() + 0.5;

            planets[i] = createBackgroundLabel("menu/parallax_planets_00" + rand + ".png", (int)(Main.WIDTH*size), (int)(Main.HEIGHT*size));
            
            planetsX[i] = (int)(Math.random()*Main.WIDTH)+1;
            planetsY[i] = (int)(Math.random()*Main.HEIGHT)+1;
            layeredPane.add(planets[i], Integer.valueOf(0));
            setLayerBounds(layeredPane, planets[i], planetsX[i], planetsY[i], JLayeredPane.DEFAULT_LAYER);

        }


        JLabel background1_1_stars = createBackgroundLabel("menu/parallax-space-stars.png", Main.WIDTH, Main.HEIGHT);
        JLabel background2_1_stars = createBackgroundLabel("menu/parallax-space-stars.png", Main.WIDTH, Main.HEIGHT);

        // Add parallax layers to the background layer
        //layeredPane.add(background1_1_planets, Integer.valueOf(0));
        //layeredPane.add(background2_1_planets, Integer.valueOf(0));
        layeredPane.add(background1_1_stars, Integer.valueOf(0));
        layeredPane.add(background2_1_stars, Integer.valueOf(0));

        //setLayerBounds(layeredPane, background1_1_planets, 0, 0, JLayeredPane.DEFAULT_LAYER);
        //setLayerBounds(layeredPane, background2_1_planets, Main.WIDTH, 0, JLayeredPane.DEFAULT_LAYER);
        setLayerBounds(layeredPane, background1_1_stars, 0, 0, JLayeredPane.DEFAULT_LAYER);
        setLayerBounds(layeredPane, background2_1_stars, Main.WIDTH, 0, JLayeredPane.DEFAULT_LAYER);

        Timer timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update parallax layers for planets
                for (int i=0; i < planetsX.length; i++) {
                    planetsX[i] -= LAYER_SPEED_PLANETS;

                    // Check if the images are off-screen, and reset their positions for planets
                    if (planetsX[i] <= -250) {
                        // randomizes their distance from one another (their x value) and their Y value
                        planetsX[i] = Main.WIDTH + (int)(Math.random()*1300);
                        planetsY[i] = (int)(Math.random()*Main.HEIGHT)+1;
                    }
                }
                //layer1X1_planets -= LAYER_SPEED_PLANETS;
                //layer2X1_planets -= LAYER_SPEED_PLANETS;

                //layer1X2_planets -= LAYER_SPEED_PLANETS;
                //layer2X2_planets -= LAYER_SPEED_PLANETS;

                // Update parallax layers for stars
                layer1X1_stars -= LAYER_SPEED_STARS;
                layer2X1_stars -= LAYER_SPEED_STARS;
                layer1X2_stars -= LAYER_SPEED_STARS;
                layer2X2_stars -= LAYER_SPEED_STARS;
                // Check if the images are off-screen, and reset their positions for stars
                if (layer1X1_stars <= -Main.WIDTH) {
                        layer1X1_stars = Main.WIDTH;
                }
                if (layer2X1_stars <= -Main.WIDTH) {
                        layer2X1_stars = Main.WIDTH;
                }
                if (layer1X2_stars <= -Main.WIDTH) {
                        layer1X2_stars = Main.WIDTH;
                }
                if (layer2X2_stars <= -Main.WIDTH) {
                        layer2X2_stars = Main.WIDTH;
                }
                
                
                //if (layer2X1_planets <= -Main.WIDTH) {
                //    layer2X1_planets = Main.WIDTH;
                //}
                
                for (int i = 0; i < planets.length; i++) {
                    //countY = (int)(Math.random()*500)+1;
                    setLayerBounds(layeredPane, planets[i], planetsX[i], planetsY[i], JLayeredPane.DEFAULT_LAYER);
                }
                //setLayerBounds(layeredPane, background2_1_planets, layer2X1_planets, 0, JLayeredPane.DEFAULT_LAYER);
                setLayerBounds(layeredPane, background1_1_stars, layer1X1_stars, 0, JLayeredPane.DEFAULT_LAYER);
                setLayerBounds(layeredPane, background2_1_stars, layer2X1_stars, 0, JLayeredPane.DEFAULT_LAYER);
                
                //setLayerBounds(layeredPane, background1_1_planets, layer1X2_planets, 0, JLayeredPane.DEFAULT_LAYER);
                //setLayerBounds(layeredPane, background2_1_planets, layer2X2_planets, 0, JLayeredPane.DEFAULT_LAYER);
                setLayerBounds(layeredPane, background1_1_stars, layer1X2_stars, 0, JLayeredPane.DEFAULT_LAYER);
                setLayerBounds(layeredPane, background2_1_stars, layer2X2_stars, 0, JLayeredPane.DEFAULT_LAYER);
            }
        });

        timer.start();
        this.add(layeredPane, BorderLayout.CENTER);
        //this.setVisible(true);
        // setContentPane(layeredPane);
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

    //public static void main(String[] args) {
    //    SwingUtilities.invokeLater(new Runnable() {
    //        @Override
    //        public void run() {
    //            new MainMenu().setVisible(true);
    //        }
    //    });
    //}

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        CardLayout cardLayout = (CardLayout) getParent().getLayout();
        // switches to the next card in the layout
        if (!seenScene1) {
            cardLayout.next(getParent());
            seenScene1 = true;
        }
        else
            cardLayout.show(getParent(), "CardGame");
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        
        g.setColor(Color.white);
        g.setFont(Main.Lexend160);
        g.drawString("BEFALL", 70, 600);
    }

    public int random(int start, int end) {
        return (int)(Math.random()*(end-start))+start;
    }
}