import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Cards implements MouseMotionListener {

    private int x, y;
    private ImageIcon cardtest;
    private int health, attack;
    // private JLabel cardtest2;

    public Cards(int x, int y) {
        cardtest = new ImageIcon("card.png");
        this.x = x;
        this.y = y;

        health = (int) (Math.random() * 10 + 1);
        attack = (int) (Math.random() * 3 + 1);
        // cardtest2 = new JLabel(cardtest);
        // cardtest2.setOpaque(false);
        // cardtest2.addMouseMotionListener(this);
    }

    public boolean isInside(int mx, int my) {
        return (x - 80 < mx && y - 120 < my && x + 150 > mx && y + 220 > my);
    }

    public void setHealth(int newHealth) {

        health = newHealth;
    }

    public int getHealth() {

        return health;
    }

    public int getAttack() {

        return attack;
    }

    // public ImageIcon getImage() {
    // return card;
    // }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void myDraw(Graphics g) {

        // check if card is being drawn
        System.out.println("Drawing card at (" + x + ", " + y + ")");

        // cardtest.paintIcon(null, g, x - 60, y - 100);
        if (cardtest.getImage() != null) {
            g.drawImage(cardtest.getImage(), x, y, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, 50, 70); // Draw a red rectangle if the image is not loaded
        }

        System.out.println(x + " " + y);
        // Draw the health and attack values
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Health: " + getHealth(), getX() + 10, getY() + 30);
        g.drawString("Attack: " + getAttack(), getX() + 10, getY() + 50);
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

}
