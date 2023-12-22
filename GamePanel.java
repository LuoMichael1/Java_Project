// java game Michael 
// main panel

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

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

    private JLabel[] cardBoxes = new JLabel[5];
    private ArrayList<Cards> selectedCards = new ArrayList<>();

    private JLabel instructionLabel;

    public GamePanel() {
        this.setLayout(new BorderLayout());

        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        instructionLabel = new JLabel(
                "This is a card-based auto battler. Drag four cards into the boxes to take into battle, then press Start Battle");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(instructionLabel, BorderLayout.NORTH);

        // cardx = 0;
        // cardy = 0;

        // create the players deck
        player = new Player();

        for (int i = 0; i < 5; i++) {
            cardBoxes[i] = new JLabel();
            cardBoxes[i].setBounds(320 + i * 150, 350, 100, 200);
            cardBoxes[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.add(cardBoxes[i]);
        }

        // create the battle button
        battleButton = new JButton("Start Battle");
        battleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == battleButton) {
                    if (selectedCards.size() == 4) {

                        removeAll();
                        revalidate();
                        repaint();

                        Battle battle = new Battle(player, selectedCards);
                        add(battle, BorderLayout.CENTER);
                        revalidate();
                        repaint();
                    } else {
                        System.out.println("Please select exactly 4 cards for the battle.");
                    }
                }
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

        if (selected != null) {
            for (int i = 0; i < 4; i++) {
                if (e.getX() >= cardBoxes[i].getX() && e.getX() <= cardBoxes[i].getX() + cardBoxes[i].getWidth()
                        && e.getY() >= cardBoxes[i].getY()
                        && e.getY() <= cardBoxes[i].getY() + cardBoxes[i].getHeight()) {

                    // put the card in the box
                    cardBoxes[i].setIcon(new ImageIcon(selected.getImage()));
                    selectedCards.add(selected);
                }
            }
            selected = null;
            repaint();
        }
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