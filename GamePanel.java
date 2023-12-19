// java game Michael 
// main panel

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements MouseMotionListener, MouseListener, ActionListener{
    
    private int x, y;
    private int cardx, cardy;
    private Cards deck[] = new Cards[10];
    private ImageIcon test = new ImageIcon("card.png");

    public GamePanel() {
        this.setLayout(new BorderLayout());
       
        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        cardx = 0;
        cardy = 0;

        // create the players deck
        for (int i=0; i<7; i++) {
            deck[i] = new Cards(50 + i*200, 50 + i*200);
            System.out.println("hello");
            System.out.println(i);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawOval(x-20,y-20,40,40);

        test.paintIcon(this, g, cardx-60, cardy-100);
        deck[1].myDraw(g);
    } 



    public void mousePressed(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
        
    }
    public void mouseReleased(MouseEvent e) {
        
    }
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();

        // only if the mouse is on top of the card then the card moves
        if (cardx-80 < e.getX() && cardy-120 < e.getY() && cardx+150 > e.getX() && cardy+220 > e.getY()) {
            cardx = e.getX();
            cardy = e.getY();
        }

        for (int i=0; i<deck.length; i++) {
            if (deck[i].getX()-80 < e.getX() && deck[i].getY()-120 < e.getY() && deck[i].getX()+150 > e.getX() && deck[i].getY()+220 > e.getY()) {
                deck[i].setX(e.getX());
                deck[i].setY(e.getY());
                
            }
            repaint();
        }
    }

    public void mouseClicked(MouseEvent e) {
        
    }
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        repaint();
    }
    public void mouseExited(MouseEvent e) {
        
    }
    public void actionPerformed(ActionEvent e){

    }

}
