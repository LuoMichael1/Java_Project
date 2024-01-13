package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MenuButtons extends JPanel implements ActionListener {
    //ImageIcon settingsIcon = new ImageIcon("images/Settings.png");
    
    private boolean seenScene1 = false;

    JButton start = new JButton("START");
    JButton tutorial = new JButton("TUTORIAL");
    JButton back = new JButton("BACK");

    public MenuButtons() {
        // this.setO;
        this.setOpaque(false);
        this.setBounds(0, 0, Main.WIDTH, Main.HEIGHT);
        this.setLayout(null);
        this.setFocusable(false);

        start.setBounds(120, Main.HEIGHT+200, 400, 100);
        //start.setBorderPainted(false);
        //start.setContentAreaFilled(false);
        start.setFont(Main.Lexend60);
        start.addActionListener(this);
        this.add(start);

        tutorial.setBounds(120, Main.HEIGHT+350, 400, 100);
        //tutorial.setBorderPainted(false);
        //tutorial.setContentAreaFilled(false);
        tutorial.setFont(Main.Lexend60);
        tutorial.addActionListener(this);
        this.add(tutorial);

        back.setBounds(120, Main.HEIGHT+500, 400, 100);
        //back.setBorderPainted(false);
        //back.setContentAreaFilled(false);
        back.setFont(Main.Lexend60);
        back.addActionListener(this);
        this.add(back);

        /*
         * btt2 = new JButton("START");
         * btt2.setFont(Main.Lexend60);
         * btt2.setForeground(Color.white);
         * btt2.setBounds(Main.WIDTH-320, Main.HEIGHT-245, 300, 100);
         * //btt1.setBorderPainted(false);
         * btt2.setContentAreaFilled(false);
         * btt2.addActionListener(this);
         * this.add(btt2);
         */

    }
    public void move(double time) {
        start.setBounds(120, (int)(Main.HEIGHT-easing(time,Main.HEIGHT-200)), 400, 100);
        tutorial.setBounds(120, (int)(Main.HEIGHT-easing(time,Main.HEIGHT-350)), 400, 100);
        back.setBounds(120, (int)(Main.HEIGHT-easing(time,Main.HEIGHT-500)), 400, 100);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            switchCard();
        }
        // if (e.getSource() == btt2) {
        // //start
        // Main.showCard("Cutscene1");
        // }
    }

    private int easing(double time, int max) {
        //System.out.println((int)(max*(Math.pow(time,4) * Math.pow(time - 2, 4))));
        return (int)(max*(Math.pow(time,2) * Math.pow(time - 2, 2)));
    }

    private void switchCard() {
        this.setFocusable(false);
        if (!seenScene1) {
            Main.nextCard();
            seenScene1 = true;
        } else
            Main.showCard("CardGame");
    }

}
