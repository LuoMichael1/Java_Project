import java.util.*;
import javax.swing.*;
import java.awt.*;

// This is the abstract class that encompasses both player and enemy
public abstract class Battler {

    public int cardsUsed = 0;

    public ArrayList<Cards> deck = new ArrayList<>();
    public Cards[] hand = new Cards[10];

    private String name;
    // players stats
    private int health = 1000;
    private int maxHealth = 1000;
    private int ambrosia = 0;
    private int shield = 0;
    private int vulnerableStacks = 0;

    public Battler(String name) {
        this.name = name;
        // create the players deck
        for (int i = 0; i < 10; i++) {
            deck.add(new Cards(i * 120 + 40, 420, 70, 30));
            System.out.println("hello");
            System.out.println(i);
        }
    }

    // Setters -----------------------------------
    public void setShield(int shield) {
        this.shield = this.shield + shield;
    }

    public void setAmbrosia(int ambrosia) {
        this.ambrosia = this.ambrosia + ambrosia;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setVulnerableStacks(int vulnerableStacks) {
        this.vulnerableStacks = this.vulnerableStacks + vulnerableStacks;
    }

    // Getters -----------------------------------
    public int getShield() {
        return shield;
    }

    public int getAmbrosia() {
        return ambrosia;
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

    public String toString() {
        return name;
    }

    abstract public void myDraw(Graphics g);
    abstract public void attackAnim(int frame);
    abstract public void attackAnimStop(int frame);
    
}