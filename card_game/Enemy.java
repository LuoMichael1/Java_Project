package card_game;

import java.awt.*;
import javax.swing.*;

import main.Main;

public class Enemy extends Battler {

    private int yoffset = 0;
    private ImageIcon enemySprite = new ImageIcon("images/enemy.png");
    public static int maxHp = 500;

    public Enemy(int numberOfCards) {
        super("Enemy");
        for (int i = 0; i < numberOfCards; i++) {
            hand[i] = new Cards(i * 150 + 20, 600, 70, 30);
        }

        super.setMaxHealth(maxHp);
        super.setHealth(maxHp);
    }

    public void drawSprite(Graphics g) {
        g.drawImage(enemySprite.getImage(), 800, 200, null);
    }

    public void drawStatus(Graphics g) {
        // show health
        g.setColor(Color.black);
        g.drawRect(950, 100, 250 + 1, 24 + 1);
        g.setColor(Color.gray);
        g.fillRect(950 + 1, 100 + 1, 250, 24);
        g.setColor(Color.red);
        g.fillRect(950 + 1, 100 + 1, (int) (super.getHealth() * 1.0 / (super.getMaxHealth()) * 250), 24);

        g.setColor(Color.black);
        g.setFont(Main.Lexend18);
        g.drawString("" + super.getHealth() + "/" + super.getMaxHealth(), 960, 100 + 20);

        // shield stat
        g.setColor(Color.white);
        g.setFont(Main.Lexend12);
        if (super.getShield() > 0) {
            g.drawImage(super.getStatusImage()[0].getImage(), 900, 100, null);
            g.drawString("" + super.getShield(), 900, 100);
        }

        // show other stats
        for (int i = 1; i < super.getStatusNum().length; i++) {
            if (super.getStatusNum()[i] > 0) {
                yoffset = yoffset + 80;
                g.drawImage(super.getStatusImage()[i].getImage(), 1150 + 22, 100 + yoffset, null);

                g.setColor(new Color(10,10,10));
                g.fillRoundRect(1150+15, 147 + yoffset, 65, 20, 10, 10);
                
                g.setColor(Color.white);
                g.setFont(Main.Lexend12);
                g.drawString(super.getStatusName()[i], 1150 + 20, 160 + yoffset);
                g.setFont(Main.Lexend18);
                g.drawString("" + super.getStatusNum()[i], 1150 + 25, 105 + yoffset);
            }
        }
        yoffset = 0;
    }

    @Override
    public void attackAnim(int frame) {

    }

    @Override
    public void attackAnimStop(int frame) {
    }
}
