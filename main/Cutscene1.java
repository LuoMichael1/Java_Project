package main;
// this is the first cutscene in the game. This scene has a car and some dialoge

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
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

public class Cutscene1 extends JPanel implements KeyListener, MouseListener {
    private int characterX = 0;
    private BufferedImage farBackground, background1, background2, foreground, blackBar;
    private BufferedImage[] carSprites = new BufferedImage[4];
    private int spriteIndex = 0;
    private long lastFrameTime;
    private int frameCounter = 0; // Add a frame counter
    private Dialogue dialogue;

    private int fadeAlpha = 255;
    private boolean fadingIn = true;
    private boolean fadingOut = false;

    private JPanel controls = new JPanel(new BorderLayout());
    private JLabel skipText = new JLabel("Press X to skip");

    public Cutscene1() {
        this.setFocusable(true);

        this.addComponentListener(new ComponentListener() {
            public void componentShown(ComponentEvent e) {
                requestFocusInWindow();
            }
            public void componentResized(ComponentEvent e) {
            }
            public void componentMoved(ComponentEvent e) {
            }
            public void componentHidden(ComponentEvent e) {
            }
        });

        // this.requestFocusInWindow();
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

        dialogue = new Dialogue();
        this.addKeyListener(this);
        this.addMouseListener(this);

        this.setLayout(new BorderLayout());
        controls.setOpaque(false);
        this.add(controls, BorderLayout.SOUTH);

        skipText.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 1000));
        skipText.setFont(FontFactory.loadFont("fonts/lexend/static/Lexend-Regular.ttf", 16));
        skipText.setForeground(new Color(200, 200, 200));
        controls.add(skipText);

        Timer timer = new Timer(1000 / 60, e -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastFrameTime >= 1000 / 60) {
                characterX += 70;
                frameCounter++; // Increment the frame counter each frame
                if (frameCounter % 30 == 0) { // Only change the sprite index every 30 frames
                    spriteIndex = (spriteIndex + 1) % carSprites.length;
                }
                if (dialogue.index < dialogue.dialogues.size() - 1)
                    dialogue.update();
                repaint();
                lastFrameTime = currentTime;
            }
        });
        timer.start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();

        int bg1x = -characterX / 8 % w;
        int bg2x = -characterX / 4 % w;
        int fgx = -characterX % w;

        final int BAR_HEIGHT = getHeight() / 8;

        g.drawImage(farBackground, 0, 0, w, h, null);

        g.drawImage(background1, bg1x, 0, w, h, null);
        g.drawImage(background1, bg1x + w, 0, w, h, null);

        g.drawImage(background2, bg2x, 0, w, h, null);
        g.drawImage(background2, bg2x + w, 0, w, h, null);

        // Calculate the y-coordinate based on a sine function
        int characterY = (int) ((h - (h / 3)) + Math.sin(characterX / 100.0));
        g.drawImage(carSprites[spriteIndex], w / 3, characterY, w / 3, h / 4, null);

        g.drawImage(foreground, fgx, -200, w / 2, h + (h / 3), null);
        g.drawImage(foreground, fgx + w, -200, w / 2, h + (h / 3), null);

        g.drawImage(blackBar, 0, 0, w, BAR_HEIGHT, null);
        g.drawImage(blackBar, 0, h - BAR_HEIGHT, w, BAR_HEIGHT, null);

        if (dialogue.index < dialogue.dialogues.size())
            dialogue.draw(g);

        if (fadingIn) {
            fadeAlpha -= 255 / 60; // Decrease alpha over 60 frames for fade-out
            if (fadeAlpha < 0) {
                fadeAlpha = 0;
                fadingIn = false;
            }
        } else if (fadingOut) {
            fadeAlpha += 255 / 60; // Decrease alpha over 60 frames for fade-in
            if (fadeAlpha > 255) {
                startTutorial();
                fadeAlpha = 255;
            }
        }

        g.setColor(new Color(0, 0, 0, fadeAlpha));
        g.fillRect(0, 0, w, h);
    }

    // add(panel, BorderLayout.CENTER);
    // setSize(1280, 720);
    // setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    private void startTutorial() {
        // switches to the next card in the layout
        this.setFocusable(false);
        Main.showCard("Map");
    }

    // public static void main(String[] args) {
    // SwingUtilities.invokeLater(() -> {
    // Cutscene1 game = new Cutscene1();
    // game.setVisible(true);
    // });
    // }

    class Dialogue {
        private ArrayList<String[]> dialogues = new ArrayList<>();
        private BufferedImage enemyFile;
        private int displayImageIndex;
        private int tutorialStartIndex;
        private int x = 1280 / 4;
        private int y = 720 / 2;
        private int index = 0;
        private int alpha = 200;
        private int textIndex = 0;

        Dialogue() {
            try (Scanner scanner = new Scanner(new File("dialogue/car-ride.txt"))) {
                int lineCount = 0;
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (!line.equals("")) {
                        if (line.equals("displayImage()")) {
                            enemyFile = ImageIO.read(new File("images/enemy.png"));
                            dialogues.add(new String[] { "Function", "displayImage()" });
                            displayImageIndex = lineCount;
                        } else if (line.equals("startTutorial()")) {
                            dialogues.add(new String[] { "Function", "startTutorial()" });
                            tutorialStartIndex = lineCount;
                        } else {
                            String[] parts = line.split(": ", 2);
                            dialogues.add(parts);
                        }
                        lineCount++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void draw(Graphics g) {

            if (index == displayImageIndex) {
                g.drawImage(enemyFile, 1280 / 4, 720 / 4, null);
                return;
            } else if (index == tutorialStartIndex) {
                return;
            }

            g.setColor(new Color(0, 0, 0, alpha));
            g.fillRect(x, y, 200, 100);
            g.setColor(Color.WHITE);
            g.drawString(dialogues.get(index)[0], x + 10, y + 30);

            String[] words = dialogues.get(index)[1].substring(0, textIndex).split(" ");
            String displayText = "";

            int lineCount = 0;

            for (String word : words) {
                if (lineCount + word.length() <= 25) {
                    displayText += word + " ";
                    lineCount += word.length();
                } else {
                    displayText += "\n" + word + " ";
                    lineCount = word.length();
                }
            }

            String lines[] = displayText.split("\n");

            for (int i = 0; i < lines.length; i++)
                g.drawString(lines[i], x + 10, y + 50 + i * 15);
        }

        void next() {
            index = Math.min(dialogues.size(), index + 1);
            x = (x == 1280 / 4 && index != displayImageIndex) ? (int) (1280 * 0.6) : 1280 / 4;
            textIndex = 0;
        }

        void update() {
            if (textIndex < dialogues.get(index)[1].length()) {
                textIndex++;
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        dialogue.next();
        if (dialogue.index == dialogue.tutorialStartIndex) {
            fadingOut = true;
        } else if (dialogue.index == dialogue.dialogues.size()) {
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
        if (e.getKeyCode() == 88)
            startTutorial();
        // spacebar works like the mouse
        if (e.getKeyCode() == 32) {
            dialogue.next();
            if (dialogue.index == dialogue.tutorialStartIndex) {
                fadingOut = true;
            } else if (dialogue.index == dialogue.dialogues.size()) {
                fadingOut = true;
            }
        }
    }
}
