import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MenuButtons extends JPanel implements ActionListener{
    ImageIcon settingsIcon = new ImageIcon("images/Settings.png");
    JButton btt1;
    JButton btt2;

    public MenuButtons() {
        //this.setO;
        this.setOpaque(false);
        this.setBounds(0, 0, Main.WIDTH, Main.HEIGHT);
        this.setLayout(null);


        btt1 = new JButton(settingsIcon);
        btt1.setBounds(Main.WIDTH-120, Main.HEIGHT-145, 100, 100);
        btt1.setBorderPainted(false);
        btt1.setContentAreaFilled(false);
        btt1.addActionListener(this);
        this.add(btt1);

        JLabel message = new JLabel("PRESS ANY BUTTON TO START");
        message.setFont(Main.Lexend12);
        message.setForeground(Color.white);
        message.setBounds(Main.WIDTH/2-(100), Main.HEIGHT-70, 200, 10);
        //message
        this.add(message);
        /* 
        btt2 = new JButton("START");
        btt2.setFont(Main.Lexend60);
        btt2.setForeground(Color.white);
        btt2.setBounds(Main.WIDTH-320, Main.HEIGHT-245, 300, 100);
        //btt1.setBorderPainted(false);
        btt2.setContentAreaFilled(false);
        btt2.addActionListener(this);
        this.add(btt2);
        */

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btt1) {
            Main.showCard("Settings");
        }
        //if (e.getSource() == btt2) {
        //    //start
        //    Main.showCard("Cutscene1");
        //}
    }
}
