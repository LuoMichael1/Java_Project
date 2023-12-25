// java game Michael 
// frame

import javax.swing.*;
import java.awt.*;

// this is a comment

class Main {
    public static void main(String[] args) {

        JFrame f = new JFrame("hi");
        
        JPanel p = new JPanel(new CardLayout());
        MainMenu p1 = new MainMenu();
        GamePanel p2 = new GamePanel();

        f.add(p, BorderLayout.CENTER);
        p.add(p1, "Menu");
        p.add(p2, "Game");

        f.setVisible(true);
        f.setSize(1300, 1000);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        CardLayout cardLayout = (CardLayout) p.getLayout();
        cardLayout.show(p, "Menu");


    }
}