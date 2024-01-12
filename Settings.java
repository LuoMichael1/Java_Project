//setting go here

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class Settings extends JPanel implements ActionListener{
    int num_of_rows = 10;
    JPanel[] panelArray = new JPanel[num_of_rows];
    ImageIcon buttonOnIcon = new ImageIcon("images/buttonOff.png");
    ImageIcon buttonOffIcon = new ImageIcon("images/buttonOn.png");
    JButton bt1 = new JButton(buttonOnIcon);
    
    Music music;
    boolean isMusicPlaying = false;

    JButton[] buttonArray = {bt1 , new JButton(buttonOffIcon), new JButton(buttonOffIcon), new JButton(buttonOffIcon), new JButton(buttonOffIcon), new JButton(buttonOffIcon), new JButton(buttonOffIcon), new JButton(buttonOffIcon), new JButton(buttonOffIcon)};
    String[] descriptons = { "Background Music", "", "", "", "", "", "", "", "", ""};

    ImageIcon CloseIcon = new ImageIcon("images/CloseButton.png");
    JButton CloseButton = new JButton(CloseIcon);
    
    

    public Settings() {
        
        // start back ground music 
        // placed music here because the music setting are also here
        try {
            music = new Music("music/bg.wav", -1);
            isMusicPlaying = true;
        }
        catch (Exception e) {
            System.out.println(e);
        }

        this.setLayout(new BorderLayout());
        this.setBackground(new Color(10,10,10) );
        
        // Placing some empty panels so that the center panel has some space and doesn't touch the window edges
        JPanel paddingW = new JPanel();
        JPanel paddingE = new JPanel();
        JPanel paddingN = new JPanel(new BorderLayout());
        JPanel paddingS = new JPanel();
        paddingW.setOpaque(false);
        paddingE.setOpaque(false);
        paddingN.setOpaque(false);
        paddingS.setOpaque(false);
        paddingW.setPreferredSize(new Dimension(30,700));
        paddingE.setPreferredSize(new Dimension(30,700));
        paddingN.setPreferredSize(new Dimension(1200,60));
        paddingS.setPreferredSize(new Dimension(1200,30));
        this.add(paddingN, BorderLayout.NORTH);
        this.add(paddingW, BorderLayout.WEST);
        this.add(paddingS, BorderLayout.SOUTH);
        this.add(paddingE, BorderLayout.EAST);

        // creating the main panel that all the setting are added to
        JPanel container = new JPanel(new GridLayout(num_of_rows, 1, 0,0));
        container.setOpaque(false);
        this.add(container, BorderLayout.CENTER);

        // adds the SETTING text at the top of the menu
        JLabel title = new JLabel("  SETTINGS");
        title.setFont(Main.Lexend24);
        title.setForeground(Color.WHITE);
        paddingN.add(title, BorderLayout.WEST);


        for (int i = 0; i < num_of_rows-1; i++) {
            //panelArray[i] = new JPanel(new BorderLayout());
            //panelArray[i].setOpaque(false);
            buttonArray[i].addActionListener(this);
            buttonArray[i].setContentAreaFilled(false);
            buttonArray[i].setBorderPainted(false);
            buttonArray[i].setFocusPainted(false);
            panelArray[i] = createToggle(descriptons[i], buttonArray[i]);
            container.add(panelArray[i]);
        }
        /* 
        bt1 = 
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

*/      
        // sends you back to the main menu
        CloseButton.setContentAreaFilled(false);
        CloseButton.setBorderPainted(false);
        CloseButton.setFocusPainted(false);
        CloseButton.addActionListener(this);
        // puts the close button at the bottom
        panelArray[num_of_rows-1] = new JPanel(new BorderLayout());
        panelArray[num_of_rows-1].setOpaque(false);
        panelArray[num_of_rows-1].add(CloseButton,BorderLayout.CENTER);
        container.add(panelArray[num_of_rows-1]);

    }

 
  
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonArray[0]) {
            if (isMusicPlaying) {
                music.stop();
                isMusicPlaying = false;
                bt1.setIcon(buttonOffIcon);
            }
            else {
                music.start();
                isMusicPlaying = true;
                bt1.setIcon(buttonOnIcon);
            }
        }
        if (e.getSource() == CloseButton) {
            Main.showCard("Menu");
        }
    }

    private JPanel createToggle(String text, JButton button) {
        JPanel TogglePanel = new JPanel(new BorderLayout(10,0));
        JLabel description = new JLabel(text);

        TogglePanel.setOpaque(false);
        description.setFont(Main.Lexend18);
        description.setForeground(Color.WHITE);

        TogglePanel.add(button,BorderLayout.EAST);
        TogglePanel.add(description,BorderLayout.WEST);

        description.setBorder(BorderFactory.createEmptyBorder(5,12,5,5));
        TogglePanel.setBorder(BorderFactory.createLineBorder(new Color(100,100,100)));

        return TogglePanel;
    }



}
