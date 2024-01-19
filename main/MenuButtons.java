package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.Color;

public class MenuButtons extends JPanel implements ActionListener {
    
    private boolean seenScene1 = false;
    JButton start;
    JButton tutorial;
    JButton exit;
    JButton back;

    public MenuButtons() {

        this.setOpaque(false);
        this.setBounds(0, 0, Main.WIDTH, Main.HEIGHT);
        this.setLayout(null);
        this.setFocusable(false);

        start = createButton("START", 120, Main.HEIGHT+150, 400, 100);
        tutorial = createButton("TUTORIAL", 120, Main.HEIGHT+270, 400, 100);
        exit = createButton("EXIT", 120, Main.HEIGHT+390, 400, 100);
        back = createButton("BACK", 120, Main.HEIGHT+510, 400, 100);

        this.add(start);
        this.add(tutorial);
        this.add(exit);
        this.add(back);
    }

    // moves the buttons into view
    public void moveIn(double time) {
        start.setBounds(120, (int)(Main.HEIGHT-easing(time,Main.HEIGHT-150)), 400, 100);
        tutorial.setBounds(120, (int)(Main.HEIGHT-easing(time,Main.HEIGHT-270)), 400, 100);
        exit.setBounds(120, (int)(Main.HEIGHT-easing(time,Main.HEIGHT-390)), 400, 100);
        back.setBounds(120, (int)(Main.HEIGHT-easing(time,Main.HEIGHT-510)), 400, 100);
    }
    // moves the buttons back out of view
    public void moveOut(double time) {
        start.setBounds(120, (int)(150-easing(time,350)), 400, 100);
        tutorial.setBounds(120, (int)(270-easing(time,470)), 400, 100);
        exit.setBounds(120, (int)(390-easing(time,590)), 400, 100);
        back.setBounds(120, (int)(510-easing(time,710)), 400, 100);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            switchCard();
        }
        else if (e.getSource() == tutorial) {
            Main.showCard("Tutorial");
        }
        else if (e.getSource() == exit) {
            System.exit(0);
        }
        else if (e.getSource() == back) {
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

    // method to set up and style the buttons
    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        
        button.setBounds(x, y, width, height);
        button.setContentAreaFilled(false);
        button.setForeground(Color.white);
        button.setFont(Main.Lexend60);
        button.addActionListener(this);
        button.setFocusable(false);

        return button;
    }

}
