package card_game;

// This is the abstract class that encompasses both player and enemy

import java.util.*;
import javax.swing.*;
import java.awt.*;

public abstract class Battler {

    public int cardsUsed = 0;

    public ArrayList<Cards> deck = new ArrayList<Cards>(); // the cards that can be put into the hand
    public Cards[] hand = new Cards[8]; // the cards that will be used in battle

    private ImageIcon energyIcon = new ImageIcon("images/shieldIcon.png");
    private ImageIcon shieldIcon = new ImageIcon("images/shieldIcon.png");
    private ImageIcon vulnerableIcon = new ImageIcon("images/vulnerableIcon.png");
    private ImageIcon strengthenIcon = new ImageIcon("images/strenghtIcon.png");
    private ImageIcon healingIcon = new ImageIcon("images/healingIcon.png");
    private ImageIcon bleedIcon = new ImageIcon("images/bleedIcon.png");

    private String name;
    // players stats
    private int health = 500;
    private int maxHealth = 500;
    private int energy = 0;
    private int shield = 0;
    private int vulnerableStacks = 0;
    private int strengthenStacks = 0;
    private int healingStacks = 0;
    private int bleedStacks = 0;
    private int counter = 0;

    private int statusNum[] = {shield, energy, vulnerableStacks, strengthenStacks, healingStacks, bleedStacks};
    private String statusName[] = { "Shield", "Energy", "Vulnerable", "Strength", "Healing", "Bleed"};
    private ImageIcon statusImage[] = { shieldIcon, energyIcon, vulnerableIcon, strengthenIcon, healingIcon, bleedIcon };

    private ArrayList<Integer> showDamage = new ArrayList<Integer>(); // currently unused

    public Battler(String name) {
        this.name = name;
        // create the players deck
        for (int i = 0; i < 5; i++) {
            deck.add(new Cards(i * 120 + 40, 420, 70, 30));
            // System.out.println(deck);
            // System.out.println(i);
        }
    }

    // Setters -----------------------------------
    public void setShield(int shield) {
        statusNum[0] = statusNum[0] + shield;
    }

    public void setEnergy(int energy) {
        statusNum[1] = statusNum[1] + energy;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setVulnerableStacks(int vulnerableStacks) {
        statusNum[2] = statusNum[2] + vulnerableStacks;
    }

    public void setStrengthenStacks(int strengthenStacks) {
        statusNum[3] = statusNum[3] + strengthenStacks;
    }

    public void setHealingStacks(int healingStacks) {
        statusNum[4] = statusNum[4] + healingStacks;
    }

    public void setBleedStacks(int bleedStacks) {
        statusNum[5] = statusNum[5] + bleedStacks;
    }

    public void increaseCounter() {
        counter++;
    }

    public void clearStatus() {
        for (int i = 0; i < statusNum.length; i++) {
            statusNum[i] = 0;
        }
    }

    // Getters -----------------------------------
    public int getShield() {
        return statusNum[0];
    }

    public int getEnergy() {
        return statusNum[1];
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getVulnerableStacks() {
        return statusNum[2];
    }

    public int getStrengthenStacks() {
        return statusNum[3];
    }

    public int getHealingStacks() {
        return statusNum[4];
    }
    public int getBleedStacks() {
        return statusNum[5];
    }

    public int getCounter() {
        return counter;
    }

    public int[] getStatusNum() {
        return statusNum;
    }

    public String[] getStatusName() {
        return statusName;
    }

    public ImageIcon[] getStatusImage() {
        return statusImage;
    }

    public String toString() {
        return name;
    }

    public void addShowDamage(int damage) {
        showDamage.add(damage);
    }

    public ArrayList<Integer> getShowDamage() {
        return showDamage;
    }

    abstract public void drawSprite(Graphics g);

    abstract public void drawStatus(Graphics g);

    abstract public void attackAnim(int frame);

    abstract public void attackAnimStop(int frame);

}