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
    private Cards selected = null;
    private int offsetX, offsetY;

    // initialize player
    private Player player;

    // initialize buttons
    private JButton battleButton; // start battle
    private JButton leftButton; // scroll left
    private JButton rightButton; // scroll right

    private JLabel[] cardBoxes = new JLabel[deckSize + 1];
    private Cards[] selectedCards = new Cards[8];
    private int cardsSelected = 0;

    private JLabel instructionLabel;

    private int scrollValue = 0;

    public GamePanel() {
        this.setLayout(new BorderLayout());

        initUserInterface();
        initButtons();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawOval(x - 20, y - 20, 40, 40);

        for (Cards card : player.deck) {
            card.myDraw(g);
        }
        for (Cards card : selectedCards) {
            if (card != null)
                card.myDraw(g);
        }
    }

    private void initUserInterface() {

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
        // deck logic: card from deck remains in deck until put in selectedCards. Then x
        // values of all remaining cards are updated to remove gaps

        for (int i = 0; i < deckSize + 1; i++) {
            cardBoxes[i] = new JLabel();
            cardBoxes[i].setBounds(200 + i * 110, 350, 100, 200);
            cardBoxes[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.add(cardBoxes[i]);
        }
    }

    private void initButtons() {

        // create the battle button
        battleButton = new JButton("Start Battle");
        battleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startBattle();
            }
        });

        // add the battle button to the panel

        this.add(battleButton, BorderLayout.SOUTH);

        // Create the left button
        leftButton = new JButton("<");
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("left button");
                scroll(50);
            }
        });
        this.add(leftButton, BorderLayout.WEST);

        // Create the right button
        rightButton = new JButton(">");
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("right button");
                scroll(-50);
            }
        });
        this.add(rightButton, BorderLayout.EAST);
    }

    public void scroll(int scrollValue) {

        for (Cards card : player.deck) {

            card.setX(card.getX() + scrollValue);
        }
        repaint();
        this.scrollValue += scrollValue;
    }

    private void startBattle() {

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

    public void mousePressed(MouseEvent e) {

        for (Cards card : player.deck) {

            if (card != null && card.isInside(e.getX(), e.getY())) {

                selected = card;

                // set offsets
                offsetX = e.getX() - selected.getX();
                offsetY = e.getY() - selected.getY();

                break;
            }
        }
        for (Cards card : selectedCards) {

            if (card != null && card.isInside(e.getX(), e.getY())) {

                selected = card;

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

                    // if there is already a card in the box, remove the card
                    if (selectedCards[i] != null) {

                        selectedCards[i].setX(selectedCards[i].getOriginalX());
                        selectedCards[i].setY(selectedCards[i].getOriginalY());

                        removeCard(selectedCards[i]);
                    }

                    // remove card from current position in selection if applicable to support
                    // reordering
                    if (selected.getSelectionIndex() != -1) {
                        removeCard(selected);
                    }
                    // put the card in the box
                    selected.setX(cardBoxes[i].getX());
                    selected.setY(cardBoxes[i].getY());
                    selected.setSelectionIndex(i);
                    selectedCards[i] = selected;
                    cardsSelected++;
                    // remove the card from the deck
                    player.deck.remove(selected);
                    removeGaps();

                    putInBox = true;
                }
            }
            if (!putInBox) { // remove selected card from selectedCards

                if (selected.getSelectionIndex() != -1)
                    removeCard(selected);

                selected.setX(selected.getOriginalX());
                selected.setY(selected.getOriginalY());

                removeGaps();
            }

            selected = null;
            repaint();
        }
        System.out.println("selected");
        for (Cards card : selectedCards) {

            if (card != null)
                System.out.println(card.getHealth() + " " + card.getAttack());
        }
        System.out.println("deck");
        for (Cards card : player.deck) {

            if (card != null)
                System.out.println(card.getHealth() + " " + card.getAttack());
        }
    }

    public void removeGaps() {

        // remove gaps between cards in the deck
        int deckX = 200;
        for (Cards card : player.deck) {
            card.setX(deckX + scrollValue);
            deckX += 110;
        }
    }

    public void removeCard(Cards card) {

        selectedCards[card.getSelectionIndex()] = null;
        card.setSelectionIndex(-1);
        cardsSelected--;
        // add card back to deck
        player.deck.add(card);
    }

    public void mouseDragged(MouseEvent e) {

        if (selected != null) {

            selected.setX(e.getX() - offsetX);
            selected.setY(e.getY() - offsetY);
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