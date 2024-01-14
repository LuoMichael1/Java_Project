package main;
// main menu screen

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
//import java.awt.event.ActionListener;

public class MainMenu extends JPanel implements MouseListener, KeyListener {

    private static final int LAYER_SPEED_PLANETS = 2;
    private static final int LAYER_SPEED_STARS = 1;

    // private static final int IMAGE_WIDTH = 1280;
    // private static final int IMAGE_HEIGHT = 720;

    // private int layer1X1_planets = 0;
    // private int layer2X1_planets = Main.WIDTH;

    // private int layer1X2_planets = 0;
    // private int layer2X2_planets = Main.WIDTH;

    private int layer1X1_stars = 0;
    private int layer2X1_stars = Main.WIDTH;

    private int layer1X2_stars = 0;
    private int layer2X2_stars = Main.WIDTH;

    private boolean seenScene1 = false; // prevents the cutscene from being played more than once

    private double size = 0; // the size of the planets (the formula is their normal size multiplied by this)
    private JLabel message;
    private int rgbvalue = 255;
    private int alpha = 255;
    private boolean brighten = false;

    public static boolean isTitle = true;
    private int titleY = 600;
    private int switchanimationtime = 40;
    public static double count = 0;

    private MenuButtons menuButtons = new MenuButtons();


    public MainMenu() {

        // ensures the menu has focus when shown
        this.addComponentListener(new ComponentListener() {
            public void componentShown(ComponentEvent e) {
                MainMenu.this.requestFocusInWindow();
            }
            public void componentResized(ComponentEvent e) {
            }
            public void componentMoved(ComponentEvent e) {
            }
            public void componentHidden(ComponentEvent e) {
            }
        });

        setBackground(new Color(10, 10, 10));

        this.setLayout(new BorderLayout());
        this.addMouseListener(this);

        JLayeredPane layeredPane = new JLayeredPane();

        layeredPane.setPreferredSize(new Dimension(1280, 720));
        layeredPane.add(new SettingsButton(), new Integer(1));
        layeredPane.add(menuButtons, new Integer(2));

        this.setFocusable(true);
        // this.grabFocus();
        // this.requestFocusInWindow();
        this.addKeyListener(this);

        message = new JLabel("PRESS ANY BUTTON TO START");
        message.setFont(Main.Lexend12);
        message.setBounds(Main.WIDTH / 2 - (100), Main.HEIGHT - 70, 200, 10);
        // message
        this.add(message);
 
        // create planets -----------------------------------
        JLabel[] planets = new JLabel[3];
        int[] planetsX = new int[3];
        int[] planetsY = new int[3];

        int rand = 0;
        for (int i = 0; i < planets.length; i++) {
            rand = (int) (Math.random() * 2) + 1;

            // randomly creates a size for each planet. The +0.5 prevents tiny planets
            size = Math.random() + 0.5;

            planets[i] = createBackgroundLabel("menu/parallax_planets_00" + rand + ".png", (int) (Main.WIDTH * size),
                    (int) (Main.HEIGHT * size));

            planetsX[i] = (int) (Math.random() * Main.WIDTH) + 1;
            planetsY[i] = (int) (Math.random() * Main.HEIGHT) + 1;
            layeredPane.add(planets[i], JLayeredPane.DEFAULT_LAYER);
            setLayerBounds(layeredPane, planets[i], planetsX[i], planetsY[i], JLayeredPane.DEFAULT_LAYER);

        }

        JLabel background1_1_stars = createBackgroundLabel("menu/parallax-space-stars.png", Main.WIDTH, Main.HEIGHT);
        JLabel background2_1_stars = createBackgroundLabel("menu/parallax-space-stars.png", Main.WIDTH, Main.HEIGHT);

        // Add parallax layers to the background layer
        // layeredPane.add(background1_1_planets, Integer.valueOf(0));
        // layeredPane.add(background2_1_planets, Integer.valueOf(0));
        layeredPane.add(background1_1_stars, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(background2_1_stars, JLayeredPane.DEFAULT_LAYER);

        // setLayerBounds(layeredPane, background1_1_planets, 0, 0,
        // JLayeredPane.DEFAULT_LAYER);
        // setLayerBounds(layeredPane, background2_1_planets, Main.WIDTH, 0,
        // JLayeredPane.DEFAULT_LAYER);
        setLayerBounds(layeredPane, background1_1_stars, 0, 0, JLayeredPane.DEFAULT_LAYER);
        setLayerBounds(layeredPane, background2_1_stars, Main.WIDTH, 0, JLayeredPane.DEFAULT_LAYER);

        Timer timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update parallax layers for planets
                for (int i = 0; i < planetsX.length; i++) {
                    planetsX[i] -= LAYER_SPEED_PLANETS;

                    // Check if the images are off-screen, and reset their positions for planets
                    if (planetsX[i] <= -250) {
                        // randomizes their distance from one another (their x value) and their Y value
                        planetsX[i] = Main.WIDTH + (int) (Math.random() * 1300);
                        planetsY[i] = (int) (Math.random() * Main.HEIGHT) + 1;
                    }
                }
                // layer1X1_planets -= LAYER_SPEED_PLANETS;
                // layer2X1_planets -= LAYER_SPEED_PLANETS;

                // layer1X2_planets -= LAYER_SPEED_PLANETS;
                // layer2X2_planets -= LAYER_SPEED_PLANETS;

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

                // if (layer2X1_planets <= -Main.WIDTH) {
                // layer2X1_planets = Main.WIDTH;
                // }

                for (int i = 0; i < planets.length; i++) {
                    // countY = (int)(Math.random()*500)+1;
                    setLayerBounds(layeredPane, planets[i], planetsX[i], planetsY[i], JLayeredPane.DEFAULT_LAYER);
                }
                // setLayerBounds(layeredPane, background2_1_planets, layer2X1_planets, 0,
                // JLayeredPane.DEFAULT_LAYER);
                setLayerBounds(layeredPane, background1_1_stars, layer1X1_stars, 0, JLayeredPane.DEFAULT_LAYER);
                setLayerBounds(layeredPane, background2_1_stars, layer2X1_stars, 0, JLayeredPane.DEFAULT_LAYER);

                // setLayerBounds(layeredPane, background1_1_planets, layer1X2_planets, 0,
                // JLayeredPane.DEFAULT_LAYER);
                // setLayerBounds(layeredPane, background2_1_planets, layer2X2_planets, 0,
                // JLayeredPane.DEFAULT_LAYER);
                setLayerBounds(layeredPane, background1_1_stars, layer1X2_stars, 0, JLayeredPane.DEFAULT_LAYER);
                setLayerBounds(layeredPane, background2_1_stars, layer2X2_stars, 0, JLayeredPane.DEFAULT_LAYER);

                // cause the press any button to start message to shimmer
                if (rgbvalue >= 255)
                    brighten = false;
                else if (rgbvalue <= 100)
                    brighten = true;

                if (brighten)
                    rgbvalue += 2;
                else
                    rgbvalue -= 2;

                // hides the message
                if (isTitle == false && alpha > 0) {
                    alpha -= 10;
                    if (alpha < 0) 
                        alpha = 0;
                }

                else if (isTitle == true && alpha < 255) {
                    alpha += 10;
                    if (alpha < 255) 
                        alpha = 255;
                }

                message.setForeground(new Color(rgbvalue, rgbvalue, rgbvalue, alpha));

                // System.out.println("Focus: " +
                // KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
                
                // creates the animations for switching between the title and menu
                //System.out.println("Count: " + count);
                if (isTitle == false) {
                    
                    if (titleY > -100) {
                        titleY = easing(count/(switchanimationtime), -700) + 600;
                        menuButtons.moveIn((count/(switchanimationtime)));
                        count++;
                    }
                    
                }
                else if (isTitle) {
                    if (titleY != 600) {
                        titleY = (Main.HEIGHT+100) - easing(count/(switchanimationtime), Main.HEIGHT-500);
                        menuButtons.moveOut((count/(switchanimationtime)));
                        count++;
                    }
                }
            }
        });

        timer.start();
        this.add(layeredPane, BorderLayout.CENTER);
        // this.setVisible(true);
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

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (isTitle)
            swtichtoMenu();
        //else
            //switchCard();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Color.white);
        g.setFont(Main.Lexend180);
        g.drawString("BEFALL", 70, titleY);
    }

    public int random(int start, int end) {
        return (int) (Math.random() * (end - start)) + start;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (isTitle)
            swtichtoMenu();
        //else
            //switchCard();
    }


    // switches to the next card in the layout
    private void switchCard() {
        this.setFocusable(false);
        if (!seenScene1) {
            Main.showCard("Cutscene1");
            seenScene1 = true;
        } else
            Main.showCard("CardGame");
    }

    public static void swtichtoTitle() {
        isTitle = true;
        count = 0;
        //titleY = Main.HEIGHT+100;
    }
    private void swtichtoMenu() {
        isTitle = false;
        count = 0;
        //this.setFocusable(false);
    }
    public boolean isTitle() {
        return isTitle;
    }

    private int easing(double time, int max) {
        //System.out.println((int)(max*(Math.pow(time,4) * Math.pow(time - 2, 4))));
        return (int)(max*(Math.pow(time,2) * Math.pow(time - 2, 2)));
    }
}