package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class LoseScreen extends JPanel implements ActionListener {
    JButton btt1;
    JButton btt2;
    JLabel message;

    public LoseScreen() {
        this.setBackground(new Color(10,10,10));
        this.setBounds(0, 0, Main.WIDTH, Main.HEIGHT);
        this.setLayout(null);

        message = new JLabel("You Lost");
        message.setBounds(300, 300, 800, 100);
        message.setForeground(Color.white);
        message.setFont(FontFactory.loadFont("QuinqueFive_Font_1_1/QuinqueFive.ttf", 70));
        this.add(message);

        btt1 = new JButton("Menu");
        btt1.setBounds(300, 500, 250, 100);
        //btt1.setBorderPainted(false);
        btt1.setContentAreaFilled(false);
        btt1.setForeground(Color.white);
        btt1.setFont(Main.Lexend60);
        btt1.addActionListener(this);
        btt1.setFocusable(false);
        this.add(btt1);

        btt2 = new JButton("Retry");
        btt2.setBounds(700, 500, 250, 100);
        //btt2.setBorderPainted(false);
        btt2.setContentAreaFilled(false);
        btt2.setForeground(Color.white);
        btt2.setFont(Main.Lexend60);
        btt2.addActionListener(this);
        btt2.setFocusable(false);
        this.add(btt2);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btt1) {
            Main.showCard("Menu");
        }
        else if (e.getSource() == btt2) {
            Cutscene1.newGame();
            Main.showCard("Map");
        }
    }
}
