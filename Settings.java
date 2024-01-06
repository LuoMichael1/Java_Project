//setting go here

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class Settings extends JPanel implements ActionListener{
    int num_of_rows = 10;
    JPanel[] panelArray = new JPanel[num_of_rows];
    JButton bt1;
    Music music;

    public Settings() {
        

        // start back ground music 
        // placed music here because the music setting are also here
        try {
            music = new Music("music/bg.wav");
        }
        catch (Exception e) {
            System.out.println(e);
        }


        this.setLayout(new BorderLayout());
        this.setBackground(new Color(10,10,10) );
        this.add(new JLabel("Settings go here"), BorderLayout.NORTH);

        JPanel paddingW = new JPanel();
        paddingW.setOpaque(false);
        paddingW.setPreferredSize(new Dimension(30,700));
        JPanel paddingE = new JPanel();
        paddingE.setOpaque(false);
        paddingE.setPreferredSize(new Dimension(30,700));
        JPanel paddingN = new JPanel();
        paddingN.setOpaque(false);
        paddingN.setPreferredSize(new Dimension(1200,30));
        JPanel paddingS = new JPanel();
        paddingS.setOpaque(false);
        paddingS.setPreferredSize(new Dimension(1200,30));
        this.add(paddingN, BorderLayout.NORTH);
        this.add(paddingW, BorderLayout.WEST);
        this.add(paddingS, BorderLayout.SOUTH);
        this.add(paddingE, BorderLayout.EAST);

        JPanel container = new JPanel(new GridLayout(num_of_rows, 1, 10,10));
        container.setOpaque(false);
        this.add(container, BorderLayout.CENTER);

        for (int i = 0; i < num_of_rows; i++) {
            panelArray[i] = new JPanel(new BorderLayout());
            panelArray[i].setOpaque(false);
            container.add(panelArray[i]);
        }

        bt1 = new JButton("image placeholder");
        bt1.addActionListener(this);
        panelArray[1].add(bt1, BorderLayout.WEST);
        panelArray[1].add(new JLabel("Turn off and on background music"));
        
        panelArray[0].add(new JButton("image placeholder"),BorderLayout.WEST);
        panelArray[0].add(new JLabel("placeholder text"));
        panelArray[2].add(new JButton("image placeholder"),BorderLayout.WEST);
        panelArray[2].add(new JLabel("placeholder text"));
        panelArray[3].add(new JButton("image placeholder"),BorderLayout.WEST);
        panelArray[3].add(new JLabel("placeholder text"));
        panelArray[4].add(new JButton("image placeholder"),BorderLayout.WEST);
        panelArray[4].add(new JLabel("placeholder text"));
        panelArray[5].add(new JButton("image placeholder"),BorderLayout.WEST);
        panelArray[5].add(new JLabel("placeholder text"));
        panelArray[6].add(new JButton("image placeholder"),BorderLayout.WEST);
        panelArray[6].add(new JLabel("placeholder text"));
        panelArray[7].add(new JButton("image placeholder"),BorderLayout.WEST);
        panelArray[7].add(new JLabel("placeholder text"));
        panelArray[8].add(new JButton("image placeholder"),BorderLayout.WEST);
        panelArray[8].add(new JLabel("placeholder text"));
        panelArray[9].add(new JButton("image placeholder"),BorderLayout.WEST);
        panelArray[9].add(new JLabel("placeholder text"));



    }

 
  
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bt1) {
            music.stop();
        }
    }





}
