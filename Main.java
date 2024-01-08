// java game 
// Frame, cardlayout, and some fonts

import javax.swing.*;
import java.awt.*;
import java.io.File;

class Main {

    // Window size
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;   
    
    static CardLayout cardLayout;
    static JPanel p = new JPanel(new CardLayout());

    public static void main(String[] args) {
        //JPanel p;
        ImageIcon test = new ImageIcon("images/favicon.png");

        JFrame f = new JFrame("BEFALL");
        f.setIconImage(test.getImage());

        
        MainMenu p1 = new MainMenu();
        Cutscene1 p2 = new Cutscene1();
        InteractivePanel p3 = new InteractivePanel();
        DeckBuildPanel p4 = new DeckBuildPanel();
        Settings setting = new Settings();
        
        f.add(p, BorderLayout.CENTER);
        addCard(p1, "Menu");
        addCard(p2, "Cutscene1");
        addCard(p3, "Map");
        addCard(setting, "Settings");
        addCard(p4, "CardGame");
        
        // idk why but keylistener does not work unless I do this
        f.addKeyListener(p2);
        f.addKeyListener(p3.keyHandler);

        f.setVisible(true);
        f.setSize(WIDTH, HEIGHT);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        //f.setFocusable(false);

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

}
