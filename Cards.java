import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Cards implements MouseMotionListener {

    private int x, y;
    private ImageIcon cardtest;
    private int health, attack;
    // private JLabel cardtest2;
    private int originalX, originalY;
    private int selectionIndex = -1;
    public static final int CARDWIDTH = 100;
    public static final int CARDHIGHT = 200;

    public Cards(int x, int y) {
        cardtest = new ImageIcon("card.png");

        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;

        health = (int) (Math.random() * 10 + 1);
        attack = (int) (Math.random() * 3 + 1);
        // cardtest2 = new JLabel(cardtest);
        // cardtest2.setOpaque(false);
        // cardtest2.addMouseMotionListener(this);
    }

    public Cards(int x, int y, int health, int attack, int originalX, int originalY, int selectionIndex) {

        cardtest = new ImageIcon("card.png");

        this.x = x;
        this.y = y;
        this.health = health;
        this.attack = attack;
        this.originalX = originalX;
        this.originalY = originalY;
        this.selectionIndex = selectionIndex;
    }

    public Cards makeCopy() {

        return new Cards(x, y, health, attack, originalX, originalY, selectionIndex);
    }

    public boolean isInside(int mx, int my) {
        return (x - 10 < mx && y - 10 < my && x + 110 > mx && y + 210 > my);
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

    public int getOriginalX() {
        return originalX;
    }

    public int getOriginalY() {
        return originalY;
    }

    public int getSelectionIndex() {
        return selectionIndex;
    }

    public void setSelectionIndex(int index) {
        selectionIndex = index;
    }

    public void myDraw(Graphics g) {

        // cardtest.paintIcon(null, g, x - 60, y - 100);
        g.drawImage(cardtest.getImage(), x, y, null);

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

    public Image getImage() {
        return cardtest.getImage();
    }

}