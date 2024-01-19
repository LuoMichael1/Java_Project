package main;

// the button that brings up the settings which is shown on the title and menu

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SettingsButton extends JPanel implements ActionListener {
    ImageIcon settingsIcon = new ImageIcon("images/Settings.png");
    JButton btt1;

    public SettingsButton() {
        
        this.setOpaque(false);
        this.setBounds(0, 0, Main.WIDTH, Main.HEIGHT);
        this.setLayout(null);

        btt1 = new JButton(settingsIcon);
        btt1.setBounds(Main.WIDTH - 120, Main.HEIGHT - 145, 100, 100);
        btt1.setBorderPainted(false);
        btt1.setContentAreaFilled(false);
        btt1.addActionListener(this);
        this.add(btt1);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btt1) {
            Main.showCard("Settings");
        }
    }
}
