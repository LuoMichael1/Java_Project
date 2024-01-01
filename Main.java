// java game Michael 
// frame

import javax.swing.*;
import java.awt.*;
import java.io.File;

class Main {

    // Size
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;    

    public static void main(String[] args) {

        ImageIcon test = new ImageIcon("images/favicon.png");

        JFrame f = new JFrame("BEFALL");
        f.setIconImage(test.getImage());

        JPanel p = new JPanel(new CardLayout());
        MainMenu p1 = new MainMenu();
        Cutscene1 p2 = new Cutscene1();
        DeckBuildPanel p3 = new DeckBuildPanel();

        f.add(p, BorderLayout.CENTER);
        p.add(p1, "Menu");
        p.add(p2, "Cutscene1");
        p.add(p3, "CardGame");
        
        // idk why but keylistener does not work unless I add this
        f.addKeyListener(p2);

        f.setVisible(true);
        f.setSize(WIDTH, HEIGHT);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);

        CardLayout cardLayout = (CardLayout) p.getLayout();
        cardLayout.show(p, "Menu");

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
    public static final Font Lexend30 = loadFont("fonts/lexend/static/Lexend-Regular.ttf", 30);
    public static final Font Lexend60 = loadFont("fonts/lexend/static/Lexend-Regular.ttf", 60);


}
