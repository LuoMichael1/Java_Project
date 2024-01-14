package main;
// java game 

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.io.File;

// Frame, cardlayout, and some fonts
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import card_game.DeckBuildPanel;
import tile_game.InteractivePanel;
import tile_game.Settings;

public class Main {

    // Window size
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    static CardLayout cardLayout;
    static JPanel p = new JPanel(new CardLayout());

    public static void main(String[] args) {
        // JPanel p;
        ImageIcon test = new ImageIcon("images/favicon.png");

        JFrame f = new JFrame("BEFALL");
        f.setIconImage(test.getImage());

        MainMenu mainMenu = new MainMenu();
        Cutscene1 cutscene1 = new Cutscene1();
        //InteractivePanel p3 = new InteractivePanel();
        //InteractivePanel ptest = new InteractivePanel();
        //DeckBuildPanel p4 = new DeckBuildPanel();
        Settings setting = new Settings();

        f.add(p, BorderLayout.CENTER);
        addCard(mainMenu, "Menu");
        addCard(setting, "Settings");
        addCard(cutscene1, "Cutscene1");
        
        //addCard(p3, "Map");
        //addCard(p4, "CardGame");
        
        //removeCard(p3);

        //addCard(ptest, "Map");
        // idk why but keylistener does not work unless I do this
        //f.addKeyListener(p2);
        //f.addKeyListener(p3.getKeyHandler());

        f.setVisible(true);
        f.setSize(WIDTH, HEIGHT);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        // f.setFocusable(false);

        cardLayout = (CardLayout) p.getLayout();
        showCard("Menu");

    }

    static public void nextCard() {
        cardLayout.next(p);
    }

    static public void showCard(String name) {
        cardLayout.show(p, name);
    }

    static public void addCard(JPanel jPanel, String name) {
        p.add(jPanel, name);
    }
    static public void removeCard(JPanel jPanel) {
        p.remove(jPanel);
    }

    // method to make fonts
    public static Font loadFont(String path, float size) {
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
        } catch (Exception e) {
            System.out.println("Couldn't get font");
        }
        return font;
    }

    // Fonts
    public static final Font Lexend12 = loadFont("fonts/lexend/static/Lexend-Regular.ttf", 12);
    public static final Font Lexend18 = loadFont("fonts/lexend/static/Lexend-Regular.ttf", 18);
    public static final Font Lexend24 = loadFont("fonts/lexend/static/Lexend-Regular.ttf", 24);
    public static final Font Lexend30 = loadFont("fonts/lexend/static/Lexend-Regular.ttf", 30);
    public static final Font Lexend60 = loadFont("fonts/lexend/static/Lexend-Regular.ttf", 60);
    public static final Font Lexend180 = loadFont("fonts/lexend/static/Lexend-Regular.ttf", 180);

    public static final Font QuinqueFive = loadFont("QuinqueFive_Font_1_1/QuinqueFive.ttf", 18);
}
