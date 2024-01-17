package main;
// java game 

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {

    // Constants
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int FPS = 60;

    static CardLayout cardLayout;
    static JPanel p = new JPanel(new CardLayout());

    public static void main(String[] args) {
        // JPanel p;
        ImageIcon test = new ImageIcon("images/favicon.png");

        JFrame f = new JFrame("BEFALL");
        f.setIconImage(test.getImage());

        MainMenu mainMenu = new MainMenu();
        Cutscene1 cutscene1 = new Cutscene1();
        EndScreen lostScreen = new EndScreen();
        EndScreen winScreen = new EndScreen();
        Settings setting = new Settings();
        Tutorial tutorial = new Tutorial();

        lostScreen.addLoss();
        winScreen.addWin();

        f.add(p, BorderLayout.CENTER);
        addCard(mainMenu, "Menu");
        addCard(setting, "Settings");
        addCard(lostScreen, "lostScreen");
        addCard(winScreen, "winScreen");
        addCard(cutscene1, "Cutscene1");
        addCard(tutorial, "Tutorial");

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

    // Fonts
    public static final Font Lexend12 = FontFactory.loadFont("fonts/lexend/static/Lexend-Regular.ttf", 12);
    public static final Font Lexend18 = FontFactory.loadFont("fonts/lexend/static/Lexend-Regular.ttf", 18);
    public static final Font Lexend60 = FontFactory.loadFont("fonts/lexend/static/Lexend-Regular.ttf", 60);
}
