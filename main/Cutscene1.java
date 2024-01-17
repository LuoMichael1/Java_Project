package main;
// This is the first cutscene in the game. This scene has a car and some dialogue

// Mostly by Alec

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import card_game.DeckBuildPanel;
import tile_game.InteractivePanel;

public class Cutscene1 extends JPanel implements KeyListener, MouseListener, ComponentListener {

    private int characterX = 0;
    private BufferedImage farBackground, background1, background2, foreground, blackBar;
    private int background1X, background2X, foregroundX;

    private static final int BACKGROUND_1_SLOW = 8; // the higher the value, the slower the scroll in comparison to the
    // character speed
    private static final int BACKGROUND_2_SLOW = 4; // ex. backgound 2 (far background) scrolls twice as fast as
                                                    // background 1
    // and 4 times slower than foreground
    private static final int FOREGROUND_SLOW = 1;
    private static final int CHARACTER_SPEED = 70; // change this to change overall scroll speed

    private BufferedImage[] carSprites = new BufferedImage[4];
    private int spriteIndex = 0;
    private int frameCounter = 0;
    private Dialogue dialogue;

    // fade logic: a black rectangle of varying opacity is drawn over the screen
    // every frame. Depending on the values of fadingIn/fadingOut, the rectangle
    // changes in transparency, and the opacity value is represented by fadeAlpha
    // where the higher the alpha, the greater the opacity (with a maximum of 255
    // and an minimum of 0)

    private int fadeAlpha = 255;
    private boolean fadingIn = true;
    private boolean fadingOut = false;

    private JPanel controls = new JPanel(new BorderLayout());
    private JLabel skipText = new JLabel("Press X to skip");

    public Cutscene1() {
        this.setFocusable(true);

        this.addComponentListener(this);

        loadImages();

        dialogue = new Dialogue();
        this.addKeyListener(this);
        this.addMouseListener(this);

        this.setLayout(new BorderLayout());
        controls.setOpaque(false);
        this.add(controls, BorderLayout.SOUTH);

        skipText.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 1000));
        skipText.setFont(FontFactory.loadFont("fonts/lexend/static/Lexend-Regular.ttf", 16));
        skipText.setForeground(new Color(200, 200, 200)); // this is a light gray colour.
        controls.add(skipText);

        Timer timer = new Timer(1000 / Main.FPS, new ActionListener() { // 1000 is milliseconds per second; 1000 /
                                                                        // Main.FPS is a delay that aims for 60 FPS

            @Override
            public void actionPerformed(ActionEvent e) {

                characterX += CHARACTER_SPEED;
                frameCounter++;

                if (frameCounter % 30 == 0) { // Only change the sprite every 30 frames
                    spriteIndex = (spriteIndex + 1) % carSprites.length;
                }

                if (dialogue.index < dialogue.dialogues.size())
                    dialogue.update();

                repaint();
            }
        });
        timer.start();
    }

    private void loadImages() {

        try {

            farBackground = ImageIO.read(new File("environment/far-buildings.png"));
            background1 = ImageIO.read(new File("environment/back-buildings.png"));
            background2 = ImageIO.read(new File("environment/highway.png"));

            for (int i = 0; i < 4; i++) {
                carSprites[i] = ImageIO.read(new File("running/car-running" + (i + 2) + ".png"));
            }

            foreground = ImageIO.read(new File("environment/palm-tree.png"));
            blackBar = ImageIO.read(new File("environment/black-bar.png"));

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    protected void paintComponent(Graphics g) {

        // parallax logic: background images continuously scroll left to create an
        // illusion of movement to the right. foreground images scroll faster than
        // background images to create an illusion of depth.

        // draw two instances of each image. once the left
        // image of the two identical images has gone completely offscreen, the
        // x coordinate resets to 0 to create a seamless scrolling effect.
        // (x coord of right img resets to 0 + w = w)

        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();

        background1X -= CHARACTER_SPEED / BACKGROUND_1_SLOW;
        background2X -= CHARACTER_SPEED / BACKGROUND_2_SLOW;
        foregroundX -= CHARACTER_SPEED / FOREGROUND_SLOW;

        background1X = (background1X <= -w) ? 0 : background1X;
        background2X = (background2X <= -w) ? 0 : background2X;
        foregroundX = (foregroundX <= -w) ? 0 : foregroundX;

        final int BAR_HEIGHT = getHeight() / 8;

        g.drawImage(farBackground, 0, 0, w, h, null);

        g.drawImage(background1, background1X, 0, w, h, null);
        g.drawImage(background1, background1X + w, 0, w, h, null);

        g.drawImage(background2, background2X, 0, w, h, null);
        g.drawImage(background2, background2X + w, 0, w, h, null);

        // calculate the y-coordinate based on a sine function. (h - (h / 3)) is the
        // regular y value and Math.sin(characterX / 100.0) fluctuates between negative
        // and positive
        int characterY = (int) ((h - (h / 3)) + Math.sin(characterX));
        g.drawImage(carSprites[spriteIndex], w / 3, characterY, w / 3, h / 4, null);

        g.drawImage(foreground, foregroundX, -200, w / 2, h + (h / 3), null);
        g.drawImage(foreground, foregroundX + w, -200, w / 2, h + (h / 3), null);

        g.drawImage(blackBar, 0, 0, w, BAR_HEIGHT, null);
        g.drawImage(blackBar, 0, h - BAR_HEIGHT, w, BAR_HEIGHT, null);

        if (dialogue.index < dialogue.dialogues.size())
            dialogue.draw(g);

        if (fadingIn) {

            fadeAlpha -= 255 / Main.FPS; // Decrease alpha over 60 frames for fade-out

            if (fadeAlpha < 0) {

                fadeAlpha = 0;
                fadingIn = false;
            }
        } else if (fadingOut) {

            fadeAlpha += 255 / Main.FPS; // Decrease alpha over 60 frames for fade-in

            if (fadeAlpha > 255) {

                startTutorial();
                fadeAlpha = 255;
            }
        }

        g.setColor(new Color(0, 0, 0, fadeAlpha));
        g.fillRect(0, 0, w, h);
    }

    private void startTutorial() {
        // switches to the next card in the layout
        this.setFocusable(false);

        newGame();
    }

    public static InteractivePanel p3;
    public static DeckBuildPanel p4;
    public static boolean interactivePanelBuilt = false;

    public static void newGame() {

        // create map only once to avoid resetting map each time new game is called
        if (!interactivePanelBuilt) {

            p3 = new InteractivePanel();
            Main.addCard(p3, "Map");
            interactivePanelBuilt = true;
        }

        p4 = new DeckBuildPanel();

        Main.addCard(p4, "CardGame");

        Main.showCard("Map");
    }

    public static void removeGame() {

        p3.resetPlayer();
        Main.removeCard(p4);
    }

    public class Dialogue {

        private ArrayList<String[]> dialogues = new ArrayList<>();

        private static final int BOX_WIDTH = 200;
        private static final int BOX_HEIGHT = 130;
        private static final int BOX_ALPHA = 200;
        private static final int BOX_Y = Main.HEIGHT / 2;
        private static final int BOX_X_LEFT = Main.WIDTH / 4;
        private static final int BOX_X_RIGHT = (int) (Main.WIDTH * 0.6);
        private static final int TEXT_PADDING_LEFT = 10;
        private static final int TEXT_PADDING_TOP = 30;
        private static final int MAX_CHARS_PER_LINE = 25;
        private static final int LINE_SPACING = 15;

        private int boxX = BOX_X_LEFT;
        private int index = 0;
        private int textIndex = 0;

        public Dialogue() {

            // read all the dialogue in the format of <speaker name>: <speech body>
            try (Scanner scanner = new Scanner(new File("dialogue/car-ride.txt"))) {

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (!line.equals("")) {

                        String[] parts = line.split(": ", 2);
                        dialogues.add(parts);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void draw(Graphics g) {

            g.setColor(new Color(0, 0, 0, BOX_ALPHA));
            g.fillRect(boxX, BOX_Y, BOX_WIDTH, BOX_HEIGHT);
            g.setColor(Color.WHITE);
            g.setFont(Main.Lexend12);

            // anti-aliasing drastically improves text readability
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.drawString(dialogues.get(index)[0], boxX + TEXT_PADDING_LEFT, BOX_Y + TEXT_PADDING_TOP);

            String[] words = dialogues.get(index)[1].substring(0, textIndex).split(" ");
            String displayText = "";

            int lineCount = 0;

            // wrap text
            for (String word : words) {
                if (lineCount + word.length() <= MAX_CHARS_PER_LINE) {
                    displayText += word + " ";
                    lineCount += word.length();
                } else {
                    displayText += "\n" + word + " ";
                    lineCount = word.length();
                }
            }

            String lines[] = displayText.split("\n");

            for (int i = 0; i < lines.length; i++)

                // i + 2 to create two newlines
                g.drawString(lines[i], boxX + TEXT_PADDING_LEFT, BOX_Y + TEXT_PADDING_TOP + (i + 2) * LINE_SPACING);
        }

        public void next() {
            if (index < dialogues.size()) {
                index++;
                boxX = (boxX == BOX_X_LEFT) ? BOX_X_RIGHT : BOX_X_LEFT;
                textIndex = 0;
            }
        }

        // one new character is displayed each frame to create a scrolling text effect.
        public void update() {
            if (textIndex < dialogues.get(index)[1].length()) {
                textIndex++;
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        dialogue.next();
        if (dialogue.index == dialogue.dialogues.size()) {
            fadingOut = true;
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
        // System.out.println(e);
    }

    public void keyPressed(KeyEvent e) {
        // System.out.println(e);
    }

    public void keyReleased(KeyEvent e) {
        // System.out.println(e);

        // skip cutscene if x is pressed
        if (e.getKeyCode() == KeyEvent.VK_X)
            fadingOut = true;
        ;
        // spacebar works like the mouse
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            dialogue.next();
            if (dialogue.index == dialogue.dialogues.size()) {
                fadingOut = true;
            }
        }
    }

    public void componentShown(ComponentEvent e) {
        this.requestFocusInWindow();
    }

    public void componentResized(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

}
