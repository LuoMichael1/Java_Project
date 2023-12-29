// java game Michael 
// main panel

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

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

    private JLabel[] cardBoxes = new JLabel[9]; // the slots that a card can be dragged to
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

        // anti-alising on font
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int counter = 1;
        g.drawOval(x - 20, y - 20, 40, 40);

        // draw card boxes
        for (int i = 0; i < deckSize; i++) {
            g.setColor(new Color(101, 111, 249));
            g.fillRect(122 + i * 130, 162, Cards.CARDWIDTH-4, Cards.CARDHIGHT-4);

            g.setColor(new Color(45, 44, 60));
            g.fillRect(123 + i * 130, 163, Cards.CARDWIDTH-6, Cards.CARDHIGHT-6);

            g.setColor(new Color(58, 57, 74));
            
            
            g.setFont(Main.Lexend60);
            g.drawString(""+ counter, 165 + i * 130, 295);
            counter++;
        }


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

        instructionLabel = new JLabel((
                "Drag exactly " + deckSize
                        + " cards into the boxes to take into battle, then press Start Battle").toUpperCase());
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setFont(Main.Lexend18);
        this.add(instructionLabel, BorderLayout.NORTH);

        // cardx = 0;
        // cardy = 0;

        // create the players deck
        player = new Player();
        // deck logic: card from deck remains in deck until put in selectedCards. Then x
        // values of all remaining cards are updated to remove gaps

        for (int i = 0; i < deckSize + 1; i++) {
            cardBoxes[i] = new JLabel();
            cardBoxes[i].setBounds(120 + i * 130, 160, 120, 220);

            // I commented out these two lines and nothing broke
            //cardBoxes[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            //this.add(cardBoxes[i]);
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
        battleButton.setFont(Main.Lexend18);
        battleButton.setForeground(Color.black);
        battleButton.setBackground(Color.white);
        battleButton.setBorderPainted(false);
        battleButton.setFocusPainted(false);


        // Create the left button
        leftButton = new JButton("<");
        

        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("left button");
                scroll(50);
            }
        });
        //this.add(leftButton, BorderLayout.WEST);

        // Create the right button
        rightButton = new JButton(">");
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("right button");
                scroll(-50);
            }
        });
        //this.add(rightButton, BorderLayout.EAST);
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

    // remove gaps between cards in the deck as cards are removed. Tries to center the cards
    public void removeGaps() {

        // gets the center of the screen
        int deckX = (Main.WIDTH)/2;

        // adjusts the start accounting for each of the cards in the deck
        for (Cards card : player.deck)
            deckX = deckX - 60;

        // puts each card after one another
        for (Cards card : player.deck) {
            card.setX(deckX + scrollValue);
            deckX += 120;
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