// java game Michael 
// main panel

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements MouseMotionListener, MouseListener, ActionListener {

    private int x, y;
    private int cardsInDeck = 8;
    // private int cardx, cardy;
    private Cards selected = null;
    // private Cards deck[] = new Cards[cardsInDeck];
    // private ImageIcon test = new ImageIcon("card.png");
    private int offsetX, offsetY;

    // initialize player
    private Player player;

    // initialize button
    private JButton battleButton;

    public GamePanel() {
        this.setLayout(new BorderLayout());

        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        // cardx = 0;
        // cardy = 0;

        // create the players deck
        player = new Player();

        // create the battle button
        battleButton = new JButton("Start Battle");
        battleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // create a new instance of Battle class here
                Battle battle = new Battle(player);
                add(battle, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });

        // add the battle button to the panel
        this.add(battleButton, BorderLayout.SOUTH);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawOval(x - 20, y - 20, 40, 40);
        /*
         * test.paintIcon(this, g, cardx - 60, cardy - 100);
         * deck[1].myDraw(g);
         */
        for (int i = 0; i < 8; i++) {
            player.deck[i].myDraw(g);
        }
    }

    public void mousePressed(MouseEvent e) {

        for (int i = 0; i < cardsInDeck; i++) {

            if (player.deck[i].isInside(e.getX(), e.getY())) {

                selected = player.deck[i];

                // set offsets
                offsetX = e.getX() - selected.getX();
                offsetY = e.getY() - selected.getY();

                break;
            }
        }
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

        selected = null;
    }

    public void mouseDragged(MouseEvent e) {

        if (selected != null) {

            x = e.getX();
            y = e.getY();

            selected.setX(x - offsetX);
            selected.setY(y - offsetY);
        }
        // only if the mouse is on top of the card then the card moves
        /*
         * if (cardx - 80 < e.getX() && cardy - 120 < e.getY() && cardx + 150 > e.getX()
         * && cardy + 220 > e.getY()) {
         * cardx = e.getX();
         * cardy = e.getY(); * }
         * 
         * for (int i = 0; i < deck.length; i++) {
         * if (deck[i].getX() - 80 < e.getX() && deck[i].getY() - 120 < e.getY() &&
         * deck[i].getX() + 150 > e.getX()
         * && deck[i].getY() + 220 > e.getY()) {
         * deck[i].setX(e.getX());
         * deck[i].setY(e.getY());
         * 
         * }
         * repaint();
         * }
         */
        repaint();
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

    public void actionPerformed(ActionEvent e) {

    }
}
