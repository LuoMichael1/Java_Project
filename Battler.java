// This is the abstract class that encompasses both player and enemy
public abstract class Battler {

    public int cardsUsed;

    protected Cards deck[] = new Cards[8];

    // players stats
    private int health = 0;
    private int ambrosia = 0;
    private int shield = 0;

    public Battler() {

        // create the players deck
        for (int i = 0; i < 8; i++) {
            deck[i] = new Cards(i * 150 + 20, 600);
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
    public int getShield() {
        return shield;
    }
    public int getAmbrosia() {
        return ambrosia;
    }
    public int getHealth() {
        return health;
    }

}