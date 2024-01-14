package card_game;

import javax.swing.*;

import main.Main;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Cards implements MouseMotionListener {

    private int x, y;
    private int id = 1000; // used to compare if two cards are the same for merging | format is (####);
                           // first number is level, last 3 are the numbers from the file name
    private ImageIcon cardImage;
    private ImageIcon star = new ImageIcon("images/star.png");
    private int level = 0;
    private int health = 0, attack = 0;
    private int energyCost = 0;
    private int energyGive = 0;
    private int healingStacks = 0;
    private int vulnerableStacks = 0;
    private int strengthenStacks = 0;
    private int shield = 0;
    private int multiHit = 1;
    private int originalX, originalY;
    private int selectionIndex = -1;
    private Scanner filesc;
    private int cardDataPoints = 30; // the number of different pieces of data stored in a card template
    private String cardDataArray[] = new String[cardDataPoints];
    private String cardDataSplit[];
    private int rarity = 0;
    private int rand; // index of a randomly chosen card
    private int count = 0;
    private String[] description;
    private String name;
    public static final int CARDWIDTH = 120;
    public static final int CARDHIGHT = 220;

    private int line = 0;

    public Cards(int x, int y, int common, int rare) {

        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;

        if (common + rare != 100)
            System.out.println("CHANCE DOES NOT TOTAL 100%");

        // randomly generate a card
        rarity = (int) (Math.random() * 100 + 1);
        if (rarity <= common) {
            rand = (int) (Math.random() * 7 + 1);
            rarity = 0;
            cardImage = new ImageIcon("images/card4.png");
        } else if (rarity <= common + rare) {
            rand = (int) (Math.random() * 4 + 1);
            rarity = 1;
            cardImage = new ImageIcon("images/card5.png");
        }

        id += (rarity * 100) + rand;

        try {
            // get card details from file
            filesc = new Scanner(new File("cards/card" + rarity + "0" + rand + ".txt"));

            while (filesc.hasNextLine()) {
                cardDataArray[count] = (filesc.nextLine());
                count++;
            }
        } catch (Exception e) {
            System.out.println("here" + e);
        }

        // assign the data from the file to variables in the class
        updateInfo();

        filesc.close();
    }

    /*
     * public Cards(int x, int y, int health, int attack, int originalX, int
     * originalY, int selectionIndex) {
     * 
     * cardImage = new ImageIcon("card.png");
     * 
     * this.x = x;
     * this.y = y;
     * this.health = health;
     * this.attack = attack;
     * this.originalX = originalX;
     * this.originalY = originalY;
     * this.selectionIndex = selectionIndex;
     * }
     * 
     * public Cards makeCopy() {
     * return new Cards(x, y, health, attack, originalX, originalY, selectionIndex);
     * }
     */
    public boolean isInside(int mx, int my) {
        return (x - 10 < mx && y - 10 < my && x + CARDWIDTH > mx && y + CARDHIGHT > my);
    }

    public void updateInfo() {
        // Finds the line where the data for the current level starts
        for (int i = 0; i < cardDataArray.length; i++) {
            if (("//Level" + (level + 1)).equals(cardDataArray[i]))
                line = i;
        }

        description = cardDataArray[line + 1].split(":");

        for (int i = line + 2; i < count; i++) {
            cardDataSplit = cardDataArray[i].split(" ");

            if (cardDataSplit[0].equals("name")) {
                name = (cardDataSplit[1]).toUpperCase();
            } else if (cardDataSplit[0].equals("damage")) {
                attack = Integer.parseInt(cardDataSplit[1]);
            } else if (cardDataSplit[0].equals("energyCost")) {
                energyCost = Integer.parseInt(cardDataSplit[1]);
            } else if (cardDataSplit[0].equals("energyGive")) {
                energyGive = Integer.parseInt(cardDataSplit[1]);
            } else if (cardDataSplit[0].equals("heal")) {

            } else if (cardDataSplit[0].equals("healingStacks")) {
                healingStacks = Integer.parseInt(cardDataSplit[1]);
            } else if (cardDataSplit[0].equals("addMaxHP")) {

            } else if (cardDataSplit[0].equals("addShieldHp")) {
                shield = Integer.parseInt(cardDataSplit[1]);
            } else if (cardDataSplit[0].equals("vulnerableStacks")) {
                vulnerableStacks = Integer.parseInt(cardDataSplit[1]);
            } else if (cardDataSplit[0].equals("strengthenStacks")) {
                strengthenStacks = Integer.parseInt(cardDataSplit[1]);
            } else if (cardDataSplit[0].equals("bleedStacks")) {

            } else if (cardDataSplit[0].equals("multiHit")) {
                multiHit = Integer.parseInt(cardDataSplit[1]);
            } else {
                // Assumes that if the line doesn't contain one of the above then this is the
                // end of what needs to be read
                i = count;
            }
        }

    }

    // getters -------------------------
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public int getOriginalX() {
        return originalX;
    }

    public int getOriginalY() {
        return originalY;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public int getEnergy() {
        return energyGive;
    }

    public int getHealingStacks() {
        return healingStacks;
    }

    public int getShield() {
        return shield;
    }

    public int getVulnerableStacks() {
        return vulnerableStacks;
    }

    public int getStrengthenStacks() {
        return strengthenStacks;
    }

    public int getMultiHit() {
        return multiHit;
    }

    public int getSelectionIndex() {
        return selectionIndex;
    }

    public int getLevel() {
        return level;
    }

    public int getID() {
        return id;
    }

    // setters
    public void setSelectionIndex(int index) {
        selectionIndex = index;
    }

    public void setHealth(int newHealth) {
        health = newHealth;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void increaseLevel() {
        level++;
        id += 1000;
        updateInfo();
    }

    public void myDraw(Graphics g) {
        // enables antialiasing on the font which makes it look way better
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // cardtest.paintIcon(null, g, x - 60, y - 100);
        g.drawImage(cardImage.getImage(), x, y, null);

        // Draw the health and attack values
        g.setColor(Color.WHITE);
        g.setFont(Main.Lexend18);

        // draw the name of the card
        g2d.translate(getX() + 97, getY() + 8);
        g2d.rotate(Math.toRadians(90));
        g2d.drawString(name, 0, 0);
        g2d.rotate(Math.toRadians(-90));
        g2d.translate(-(getX() + 97), -(getY() + 8));

        g.setFont(Main.Lexend12);

        // write the description of the card
        count = 120;
        for (String line : description) {
            // System.out.println("count: " + count);
            g.drawString(line, getX() + 15, getY() + count);
            count = count + 17;
        }

        if (energyCost > 0) {
            g.setColor(Color.BLACK);
            g.drawString("" + energyCost, getX() + 11, getY() + 17);

        }
        // draw the level stars
        for (int i = 0; i < level + 1; i++) {
            g.drawImage(star.getImage(), getX() + 8, getY() + 28 + i * 22, null);
        }

        // g.drawString(description[0], getX() + 15, getY() + 50);
        // g.drawString("Attack: " + getAttack(), getX() + 15, getY() + 70);
    }

    public void mouseDragged(MouseEvent e) {
        /*
         * * x = e.getX();
         * y = e.getY();
         * System.out.println(x);
         * System.out.println(y);
         * 
         * this.myDraw(g);
         */
    }

    public void mouseMoved(MouseEvent e) {
    }

    public Image getImage() {
        return cardImage.getImage();
    }
}