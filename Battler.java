import java.util.*;

// This is the abstract class that encompasses both player and enemy
public abstract class Battler {

    public int cardsUsed = 0;

    public ArrayList<Cards> deck = new ArrayList<>();
    public Cards[] hand = new Cards[8];

    private String name;
    // players stats
    private int health = 1000;
    private int maxHealth = 1000;
    private int ambrosia = 0;
    private int shield = 0;

    public Battler(String name) {
        this.name = name;
        // create the players deck
        for (int i = 0; i < GamePanel.deckSize; i++) {
            deck.add(new Cards(i * 110 + 200, 420));
            System.out.println("hello");
            System.out.println(i);
        }
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public void setAmbrosia(int ambrosia) {
        this.ambrosia = ambrosia;
    }

    public void setHealth(int health) {
        this.health = health;
    }
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

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

    public String toString() {
        return name;
    }
}