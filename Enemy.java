import java.awt.*;
import javax.swing.*;

public class Enemy extends Battler {

    private ImageIcon enemySprite = new ImageIcon("images/enemy.png");

    public Enemy() {

        super("Enemy");

        for (int i = 0; i < GamePanel.deckSize; i++) {
            hand[i] = new Cards(i * 150 + 20, 600, 70, 30);
        }
    }

    @Override
    public void drawSprite(Graphics g) {
        g.drawImage(enemySprite.getImage(), 800, 200, null);
    }

    public void drawStatus(Graphics g) {
       
    }

    @Override
    public void attackAnim(int frame) {

    }

    @Override
    public void attackAnimStop(int frame) {
    }
}
