import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Cards implements MouseMotionListener{

    private int x, y;
    private ImageIcon cardtest;
    private JLabel cardtest2;

    public Cards(int x, int y) {
        cardtest = new ImageIcon("card.png");
        this.x = x;
        this.y = y; 
        cardtest2 = new JLabel(cardtest);
        cardtest2.setOpaque(false);
        cardtest2.addMouseMotionListener(this);
    }

    //public ImageIcon getImage() {
     //   return card;
    //}

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }

    public void myDraw(Graphics g){   
        cardtest.paintIcon(null, g, x-60, y-100);
    }

    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        System.out.println(x);
        System.out.println(y);
        
        this.myDraw(g);
    }
    public void mouseMoved(MouseEvent e) {
    }

    
}
