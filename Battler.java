import java.util.*;
import javax.swing.*;
import java.awt.*;

// This is the abstract class that encompasses both player and enemy
public abstract class Battler {

    public int cardsUsed = 0;

    public ArrayList<Cards> deck = new ArrayList<Cards>();
    public Cards[] hand = new Cards[10];

    private ImageIcon energyIcon = new ImageIcon("images/shieldIcon.png");
    private ImageIcon shieldIcon = new ImageIcon("images/shieldIcon.png");
    private ImageIcon vulnerableIcon = new ImageIcon("images/vulnerableIcon.png");
    private ImageIcon strengthenIcon = new ImageIcon("images/strenghtIcon.png");

    private String name;
    // players stats
    private int health = 500;
    private int maxHealth = 500;
    private int energy = 0;
    private int shield = 0;
    private int vulnerableStacks = 0;
    private int strengthenStacks = 0;
    private int counter = 0;

    private int statusNum[] = {shield, energy, vulnerableStacks, strengthenStacks};
    private String statusName[] = {"Shield", "Energy", "Vulnerable", "Strength"};
    private ImageIcon statusImage[] = {shieldIcon, energyIcon, vulnerableIcon, strengthenIcon};
    
    private ArrayList<Integer> showDamage = new ArrayList<Integer>();   // currently unused

    public Battler(String name) {
        this.name = name;
        // create the players deck
        for (int i = 0; i < 10; i++) {
            deck.add(new Cards(i * 120 + 40, 420, 70, 30));
            //System.out.println(deck);
            //System.out.println(i);
        }
    }

    // Setters -----------------------------------
    public void setShield(int shield) {
        this.shield = this.shield + shield;
        statusNum[0] = this.shield;
    }
    public void setEnergy(int energy) {
        this.energy = this.energy + energy;
        statusNum[1] = this.energy;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }
    public void setVulnerableStacks(int vulnerableStacks) {
        this.vulnerableStacks = this.vulnerableStacks + vulnerableStacks;
        statusNum[2] = this.vulnerableStacks;
    }
    public void setStrengthenStacks(int strengthenStacks) {
        this.strengthenStacks = this.strengthenStacks + strengthenStacks;
        statusNum[3] = this.strengthenStacks;
    }
    public void increaseCounter() {
        counter++;
    }
    // Getters -----------------------------------
    public int getShield() {
        return shield;
    }
    public int getEnergy() {
        return energy;
    }
    public int getHealth() {
        return health;
    }
    public int getMaxHealth() {
        return maxHealth;
    }
    public int getVulnerableStacks() {
        return vulnerableStacks;
    }
    public int getStrengthenStacks() {
        return strengthenStacks;
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