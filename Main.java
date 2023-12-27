// java game Michael 
// frame

import javax.swing.*;
import java.awt.*;
import java.io.File;

class Main {

    // Fonts
    public static Font Lexend12 = loadFont("fonts/lexend/static/Lexend-Regular.ttf", 12);
    public static Font Lexend18 = loadFont("fonts/lexend/static/Lexend-Regular.ttf", 18);

    public static void main(String[] args) {

        JFrame f = new JFrame("hi");

        JPanel p = new JPanel(new CardLayout());
        MainMenu p1 = new MainMenu();
        Game p2 = new Game();
        GamePanel p3 = new GamePanel();

        f.add(p, BorderLayout.CENTER);
        p.add(p3, "Menu");
        p.add(p2, "Game");
        p.add(p1, "CardGame");

        f.setVisible(true);
        f.setSize(1280, 720);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CardLayout cardLayout = (CardLayout) p.getLayout();
        cardLayout.show(p, "Menu");

    }

    public static Font loadFont(String path, float size) {
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
        } catch (Exception e) {
            System.out.println("Couldn't get font file");
        }
        return font;
    }
}
