package card_game;

import javax.swing.*;

import main.Main;

import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class Player extends Battler {

    private ImageIcon playerSprite = new ImageIcon("images/player3.png");
    private Scanner filesc;
    private String SpriteArray[] = new String[50]; // 50 just to be on the safe side
    private String SpriteAnimArray[] = new String[50]; // 50 just to be on the safe side
    private String tmpArray[]; // holds the array created by spliting one line of the cut array
    private String tmpArray2[]; // holds the array created by spliting one line of the animation array
    private boolean attackAnimtest = false;

    // where the characters image is drawn
    private int x = 160;
    private int y = 200;

    private int lineCounter = 0;
    private int yoffset = 0; // used to draw the status effect icons at different Y values
    private int animXOffSet = 0;
    private int animYOffSet = 0;

    public Player() {
        super("Player");
        createSprite();
    }

    // creating a sprite
    public void createSprite() {
        // read the file to get how to divide the sprite map
        try {
            // get card details from file, first 4 numbers in each line are how to cut, last
            // two are where to place
            filesc = new Scanner(new File("images/spriteCut.txt"));

            while (filesc.hasNextLine()) {
                SpriteArray[lineCounter] = (filesc.nextLine());
                lineCounter++;
            }
            // get how to animate the images
            filesc = new Scanner(new File("images/spirteAnim1.txt"));
            lineCounter = 0;
            while (filesc.hasNextLine()) {
                SpriteAnimArray[lineCounter] = (filesc.nextLine());
                lineCounter++;
            }
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    // this gets called every frame during the auto-battling segment (Battle.java)
    public void drawSprite(Graphics g) {

        tmpArray2 = SpriteAnimArray[(super.getCounter() / 2) % (lineCounter - 1)].split(" ");

        // iterate through each part of the sprite
        for (int i = 0; i < 4; i++) {
            tmpArray = SpriteArray[i].split(" ");

            // sprite
            int posX = Integer.parseInt(tmpArray[0]);
            int posY = Integer.parseInt(tmpArray[1]);
            int iwidth = Integer.parseInt(tmpArray[2]);
            int iheight = Integer.parseInt(tmpArray[3]);
            int offsetX = Integer.parseInt(tmpArray[4]);
            int offsetY = Integer.parseInt(tmpArray[5]);

            // animation
            int isVisible = Integer.parseInt(tmpArray2[i * 3]);
            animXOffSet = Integer.parseInt(tmpArray2[1 + (i * 3)]);
            animYOffSet = Integer.parseInt(tmpArray2[2 + (i * 3)]);

            if (isVisible == 1 || attackAnimtest) {
                g.clipRect(x + posX + offsetX + animXOffSet, y + posY + offsetY + animYOffSet, iwidth, iheight);
                g.drawImage(playerSprite.getImage(), x + offsetX + animXOffSet, y + offsetY + animYOffSet, null);
                g.setClip(null);
            }
        }
    }

    public void drawStatus(Graphics g) {
        // show health
        g.setColor(Color.black);
        g.drawRect(35, 100, 250 + 1, 24 + 1);
        g.setColor(Color.gray);
        g.fillRect(36, 100 + 1, 250, 24);
        g.setColor(Color.red);
        g.fillRect(36, 100 + 1, (int) (super.getHealth() * 1.0 / (super.getMaxHealth()) * 250), 24);

        g.setColor(Color.black);
        g.setFont(Main.Lexend18);
        g.drawString("" + super.getHealth() + "/" + super.getMaxHealth(), 40, 100 + 20);

        // shield stat
        g.setColor(Color.white);
        g.setFont(Main.Lexend12);
        if (super.getShield() > 0) {
            g.drawImage(super.getStatusImage()[0].getImage(), 300, 100, null);
            g.drawString("" + super.getShield(), 300, 100);
        }

        // show other stats
        for (int i = 1; i < super.getStatusNum().length; i++) {
            if (super.getStatusNum()[i] > 0) {
                yoffset = yoffset + 80;
                g.drawImage(super.getStatusImage()[i].getImage(), 22, 100 + yoffset, null);
                
                g.setColor(new Color(10,10,10));
                g.fillRoundRect(15, 147 + yoffset, 65, 20, 10, 10);

                g.setColor(Color.white);
                g.setFont(Main.Lexend12);
                g.drawString(super.getStatusName()[i], 20, 160 + yoffset);
                g.setFont(Main.Lexend18);
                g.drawString("" + super.getStatusNum()[i], 25, 105 + yoffset);
            }
        }
        yoffset = 0;
    }

    public void attackAnim(int frame) {
        attackAnimtest = true;
    }

    public void attackAnimStop(int frame) {
        attackAnimtest = false;
    }

}
