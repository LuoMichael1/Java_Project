package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.Color;

public class MenuButtons extends JPanel implements ActionListener {
    
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
        start.setContentAreaFilled(false);
        start.setForeground(Color.white);
        start.setFont(Main.Lexend60);
        start.addActionListener(this);
        start.setFocusable(false);
        this.add(start);

        tutorial.setBounds(120, Main.HEIGHT+350, 400, 100);
        //tutorial.setBorderPainted(false);
        tutorial.setContentAreaFilled(false);
        tutorial.setForeground(Color.white);
        tutorial.setFont(Main.Lexend60);
        tutorial.addActionListener(this);
        tutorial.setFocusable(false);
        this.add(tutorial);

        back.setBounds(120, Main.HEIGHT+500, 400, 100);
        //back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        back.setForeground(Color.white);
        back.setFont(Main.Lexend60);
        back.addActionListener(this);
        back.setFocusable(false);
        this.add(back);


    }
    public void moveIn(double time) {
        start.setBounds(120, (int)(Main.HEIGHT-easing(time,Main.HEIGHT-200)), 400, 100);
        tutorial.setBounds(120, (int)(Main.HEIGHT-easing(time,Main.HEIGHT-350)), 400, 100);
        back.setBounds(120, (int)(Main.HEIGHT-easing(time,Main.HEIGHT-500)), 400, 100);
    }
    public void moveOut(double time) {
        start.setBounds(120, (int)(200-easing(time,400)), 400, 100);
        tutorial.setBounds(120, (int)(350-easing(time,550)), 400, 100);
        back.setBounds(120, (int)(500-easing(time,700)), 400, 100);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            switchCard();
        }
        if (e.getSource() == back) {
            MainMenu.swtichtoTitle();
        }
    }

    private int easing(double time, int max) {
        return (int)(max*(Math.pow(time,2) * Math.pow(time - 2, 2)));
    }

    private void switchCard() {
        this.setFocusable(false);
        if (!seenScene1) {
            Main.showCard("Cutscene1");
            seenScene1 = true;
        } else {
            Cutscene1.newGame();
            Main.showCard("Map");
        }
    }

}
