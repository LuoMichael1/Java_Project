import java.awt.Graphics;

public class Enemy extends Battler {

    public Enemy() {

        super("Enemy");

        for (int i = 0; i < GamePanel.deckSize; i++) {
            hand[i] = new Cards(i * 150 + 20, 600);
        }
    }

    @Override
    public void myDraw(Graphics g) {

    }

    @Override
    public void attackAnim() {

    }
}
