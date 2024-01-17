package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Tutorial extends JPanel implements ActionListener {

    ImageIcon CloseIcon = new ImageIcon("images/CloseButton.png");
    JButton closeButton = new JButton(CloseIcon);

    public Tutorial() {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(10, 10, 10));



        // sends you back to the main menu
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(this);
        this.add(closeButton);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            Main.showCard("Menu");
        }
    }
}
