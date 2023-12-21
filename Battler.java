// This is the abstract class that encompasses both player and enemy
public abstract class Battler {

    public int cardsUsed;

    protected Cards deck[] = new Cards[8];

    public Battler() {

        // create the players deck
        for (int i = 0; i < 8; i++) {
            deck[i] = new Cards(i * 150 + 20, 600);
            System.out.println("hello");
            System.out.println(i);
        }
    }
}