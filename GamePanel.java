// java game Michael 
// main panel

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GamePanel extends JPanel implements MouseMotionListener, MouseListener, ActionListener {

    private int x, y;
    private int cardsInDeck = 8;
    public static int deckSize = 8;
    // private int cardx, cardy;
    private Cards selected = null;
    // private Cards deck[] = new Cards[cardsInDeck];
    // private ImageIcon test = new ImageIcon("card.png");
    private int offsetX, offsetY;

    // initialize player
    private Player player;

    // initialize button
    private JButton battleButton;

    private JLabel[] cardBoxes = new JLabel[deckSize + 1];
    private Cards[] selectedCards = new Cards[8];
    private int cardsSelected = 0;

    private JLabel instructionLabel;

    public GamePanel() {
        this.setLayout(new BorderLayout());

        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        instructionLabel = new JLabel(
                "This is a card-based auto battler. Drag exactly " + deckSize
                        + " cards into the boxes to take into battle, then press Start Battle");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(instructionLabel, BorderLayout.NORTH);

        // cardx = 0;
        // cardy = 0;

        // create the players deck
        player = new Player();

        for (int i = 0; i < deckSize + 1; i++) {
            cardBoxes[i] = new JLabel();
            cardBoxes[i].setBounds(50 + i * 110, 350, 100, 200);
            cardBoxes[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.add(cardBoxes[i]);
        }

        // create the battle button
        battleButton = new JButton("Start Battle");
        battleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == battleButton) {
                    if (cardsSelected == deckSize) {

                        removeAll();
                        revalidate();
                        repaint();

                        Battle battle = new Battle(player, selectedCards);
                        add(battle, BorderLayout.CENTER);
                        revalidate();
                        repaint();
                    } else {
                        System.out.println("Please select exactly " + deckSize + " cards for the battle.");
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
            boolean putInBox = false;
            for (int i = 0; i < deckSize; i++) {
                if (e.getX() >= cardBoxes[i].getX() && e.getX() <= cardBoxes[i].getX() + cardBoxes[i].getWidth()
                        && e.getY() >= cardBoxes[i].getY()
                        && e.getY() <= cardBoxes[i].getY() + cardBoxes[i].getHeight()) {

                    if (selectedCards[i] != null) {

                        selectedCards[i].setX(selectedCards[i].getOriginalX());
                        selectedCards[i].setY(selectedCards[i].getOriginalY());

                        removeCard(selectedCards[i]);
                    }

                    // remove card from current position in selection if applicable to support
                    // reordering
                    if (selected.getSelectionIndex() != -1)
                        removeCard(selected);

                    // put the card in the box
                    selected.setX(cardBoxes[i].getX());
                    selected.setY(cardBoxes[i].getY());
                    selected.setSelectionIndex(i);
                    selectedCards[i] = selected;
                    cardsSelected++;

                    putInBox = true;
                }
            }
            if (!putInBox) { // remove selected card from selectedCards

                if (selected.getSelectionIndex() != -1)
                    removeCard(selected);

                selected.setX(selected.getOriginalX());
                selected.setY(selected.getOriginalY());
            }

            selected = null;
            repaint();
        }
        System.out.println("Update");
        for (Cards card : selectedCards) {

            if (card != null)
                System.out.println(card.getHealth() + " " + card.getAttack());
        }
    }

    public void removeCard(Cards card) {

        selectedCards[card.getSelectionIndex()] = null;
        card.setSelectionIndex(-1);
        cardsSelected--;
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