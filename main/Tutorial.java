package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Tutorial extends JPanel implements ActionListener {

    ImageIcon CloseIcon = new ImageIcon("images/CloseButton.png");
    JButton closeButton = new JButton(CloseIcon);
    Scanner filesc;

    ArrayList<JLabel> text = new ArrayList<JLabel>();
    JPanel textPanel; 

    public Tutorial() {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(10, 10, 10));

        try {
            filesc = new Scanner(new File("main/tutorial.txt"));

            while (filesc.hasNextLine()) {
                
                
                JLabel line = new JLabel("" + filesc.nextLine());
                line.setFont(Main.Lexend12);
                line.setForeground(Color.WHITE);
                text.add(line);
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

        textPanel = new JPanel(new GridLayout(text.size(),1));
        textPanel.setOpaque(false);
        textPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        this.add(textPanel, BorderLayout.CENTER);

        for (int i = 0; i < text.size(); i++) {
            textPanel.add(text.get(i));
        }


        // sends you back to the main menu
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(this);
        this.add(closeButton, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            Main.showCard("Menu");
        }
    }
}
