// The screen where you can select the cards you will bring into the auto-battle as well as fuse and upgrade cards
package card_game;

import javax.swing.*;

import main.Main;
import main.Music;
import main.Settings;
import tile_game.Chest;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DeckBuildPanel extends JPanel
        implements MouseMotionListener, MouseListener, ActionListener, ComponentListener {

    // private int cardsInDeck = 8;
    public static int deckSize = 8;
    private Cards selected = null;
    private int offsetX, offsetY;

    // initialize player
    private Player player = new Player();;

    // initialize buttons
    private JButton battleButton; // start battle

    private JLabel[] cardBoxes = new JLabel[9]; // the slots that a card can be dragged to (the player's hand)
    private Cards[] handCards = new Cards[8]; // the cards actually in the card boxes

    // card columns describe the area that a card can be dropped and return to a
    // specfic part of the deck
    // for example, dropping a card in the leftmost column will put the card at the
    // leftmost place in the deck
    private int numberOfColumns = 0;
    private int indexCounter = 0; // used to finds the original index in the deck the selected card came from
    private int deckIndex = 0;

    // recycling allows the player to exchange some cards for a new card
    private int[] recyclingDimensions = { 0, 420, 150, 300 }; // x:0, y:420, width:150, height:300
    private int numRecycled = 0;

    public static int difficulty = 5; // <-- the number of cards the enemy has and maxHp = difficulty*100

    private JLabel instructionLabel;

    public DeckBuildPanel() {
        this.setLayout(new BorderLayout());
        initUserInterface();
        initButtons();
        removeGaps();
        this.addComponentListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // anti-alising on font
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // recycling box
        g.setColor(Color.RED);
        g.fillRect(recyclingDimensions[0], recyclingDimensions[1], recyclingDimensions[2], recyclingDimensions[3]);
        g.setColor(Color.BLACK);
        g.setFont(Main.Lexend60);
        g.drawString("" + numRecycled, 50, 560);

        // draw card boxes
        for (int i = 0; i < deckSize; i++) {
            g.setColor(new Color(101, 111, 249));
            g.fillRect(122 + i * 130, 162, Cards.CARDWIDTH - 4, Cards.CARDHIGHT - 4);

            g.setColor(new Color(45, 44, 60));
            g.fillRect(123 + i * 130, 163, Cards.CARDWIDTH - 6, Cards.CARDHIGHT - 6);

            g.setColor(new Color(58, 57, 74));

            // draw the number inside the box
            g.setFont(Main.Lexend60);
            g.drawString("" + (i + 1), 165 + i * 130, 295);
        }

        // draw cards
        for (Cards card : handCards) {
            if (card != null)
                card.myDraw(g);
        }
        for (Cards card : player.deck) {
            card.myDraw(g);
        }
    }

    private void initUserInterface() {

        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        instructionLabel = new JLabel(
                ("Drag cards into the boxes to take into battle, then press Start Battle")
                        .toUpperCase());
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setFont(Main.Lexend18);
        this.add(instructionLabel, BorderLayout.NORTH);

        for (int i = 0; i < deckSize + 1; i++) {
            cardBoxes[i] = new JLabel();
            cardBoxes[i].setBounds(120 + i * 130, 160, 120, 220);
        }
    }

    private void initButtons() {

        // create the battle button
        battleButton = new JButton("Start Battle");

        battleButton.addActionListener(new ActionListener() {
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
    }

    private void startBattle() {

        // resizes the nessary arrays so that the number of cards placed is the size of
        // the array

        // first moves our hand array to an arraylist to get rid of empty spaces
        ArrayList<Cards> temp = new ArrayList<Cards>();
        for (int i = 0; i < deckSize; i++) {
            if (handCards[i] != null) {
                temp.add(handCards[i]);
            }
        }
        // then moves the cards back into a properly sized array so that it can been
        // given to the battle constructor
        Cards[] temp2 = new Cards[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            temp2[i] = temp.get(i);
        }
        Battle battle = new Battle(player, temp2, difficulty, handCards);
        Main.addCard(battle, "battle");
        Main.showCard("battle");
    }

    public void mousePressed(MouseEvent e) {
        for (Cards card : player.deck) {
            indexCounter++;

            if (card != null && card.isInside(e.getX(), e.getY())) {

                deckIndex = indexCounter;

                selected = card;
                // set offsets
                offsetX = e.getX() - selected.getX();
                offsetY = e.getY() - selected.getY();
                break;
            }
        }
        indexCounter = 0;

        for (Cards card : handCards) {
            indexCounter++;

            if (card != null && card.isInside(e.getX(), e.getY())) {

                deckIndex = indexCounter;

                selected = card;
                // handCards[deckIndex-1] = null;

                // set offsets
                offsetX = e.getX() - selected.getX();
                offsetY = e.getY() - selected.getY();
                break;
            }
        }
        indexCounter = 0;
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

        if (selected != null) {
            // little bubble sound effect
            if (Settings.soundEffects) {
                Music soundeffect = new Music("music/test3.wav", 0);
                soundeffect.start();
            }

            // check if the card was placed in the recycling
            if (e.getY() >= recyclingDimensions[1] && e.getX() < recyclingDimensions[2]) {
                // System.out.println("Placed in recycling");
                numRecycled++;

                if (numRecycled >= 3) {
                    player.deck.add(new Cards(0, 420, 30, 70));
                    numRecycled = 0;
                }

                if (selected.getSelectionIndex() != -1)
                    handCards[selected.getSelectionIndex()] = null;
                else
                    player.deck.remove(selected);

                selected = null;
            } else {

                boolean putInBox = false;
                boolean leveled = false; // prevents replacing the card that leveled up with the card that was used to
                                         // merge

                for (int i = 0; i < deckSize; i++) {
                    // checks if the mouse is within the dimensions of a card box
                    if (e.getX() >= cardBoxes[i].getX() && e.getX() <= (cardBoxes[i].getX() + cardBoxes[i].getWidth())
                            && e.getY() >= cardBoxes[i].getY()
                            && e.getY() <= (cardBoxes[i].getY() + cardBoxes[i].getHeight())) {

                        // if the same card is in the box already, merge the two cards. Otherwise switch
                        // the cards
                        if (handCards[i] != null) {

                            // if two cards have the same id | checks if you are trying to place the same
                            // card in the same spot | prevents you from leveling past level 3
                            if (handCards[i].getID() == selected.getID() && !(handCards[i] == selected)
                                    && !(handCards[i].getLevel() == 2)) {
                                handCards[i].increaseLevel();

                                leveled = true;
                                if (selected.getSelectionIndex() != -1)
                                    handCards[selected.getSelectionIndex()] = null;
                                else
                                    player.deck.remove(selected);
                                selected = null;
                            } else if (handCards[i] == selected) {
                                // nothing happens if someone drops a card in the spot it is already in

                            } else {
                                // if the selected card is in the hand already
                                if (selected.getSelectionIndex() != -1) {

                                    player.deck.add(handCards[i]);

                                    int temp = selected.getSelectionIndex();

                                    handCards[temp] = player.deck.get(player.deck.size() - 1);
                                    handCards[temp].setSelectionIndex(temp);
                                    handCards[temp].setX(cardBoxes[temp].getX());
                                    handCards[temp].setY(cardBoxes[temp].getY());

                                    player.deck.remove(player.deck.size() - 1);

                                }
                                // if the selected card comes from the deck
                                else {
                                    handCards[i].setX(handCards[i].getOriginalX());
                                    handCards[i].setY(handCards[i].getOriginalY());

                                    removeCard(handCards[i], deckIndex);
                                }

                            }
                        } else if (handCards[i] == null) {
                            if (selected.getSelectionIndex() != -1) {
                                Cards tempcard = selected;
                                handCards[selected.getSelectionIndex()] = null;

                                selected = tempcard;
                                // cardsSelected--;
                            }
                        }
                        // remove card from current position in selection if applicable (As long as the
                        // selected card is currently in the hand) to support reordering
                        // basically, you can move cards in the hand to different positions in the hand
                        // if (selected.getSelectionIndex() != -1) {
                        //
                        // removeCard(selected, getCardColumns(e.getX(), e.getY()));
                        // }

                        if (!leveled) {
                            // put the card in the box
                            selected.setX(cardBoxes[i].getX());
                            selected.setY(cardBoxes[i].getY());
                            selected.setSelectionIndex(i);

                            handCards[i] = selected;
                        }

                        // remove the card from the deck
                        player.deck.remove(selected);
                        putInBox = true;
                    }
                }
                // I think this is for if the card selected is from the selectedCards hand and
                // is dragged off into the deck
                if (!putInBox) { // remove selected card from selectedCards

                    // removes card from hand only if the card is in the hand (as opposed to being
                    // in the deck which would cause an error)
                    if (selected.getSelectionIndex() != -1) {
                        removeCard(selected, getCardColumns(e.getX(), e.getY()));
                    }

                    selected.setX(selected.getOriginalX());
                    selected.setY(selected.getOriginalY());
                }
            }

            removeGaps();
            selected = null;
            repaint();
        }
    }

    // remove gaps between cards in the deck as cards are removed. Tries to center
    // the cards
    public void removeGaps() {

        // gets the center of the screen
        int deckX = (Main.WIDTH) / 2;

        // adjusts the start accounting for each of the cards in the deck
        for (int i = 0; i < player.deck.size(); i++)
            // subtract 60 because that is half of the width of a card which is what we want
            // because we want to center them
            deckX = deckX - 60;

        // puts each card after one another
        for (Cards card : player.deck) {
            card.setX(deckX);
            deckX += 120;
        }
    }

    public void removeCard(Cards card, int index) {
        handCards[card.getSelectionIndex()] = null;

        card.setSelectionIndex(-1);

        // add card back to deck
        if (index == player.deck.size()) {
            System.out.println("Placing card at: " + index);
            player.deck.add(card);
        } else {
            System.out.println("Placing card at: " + index);
            player.deck.add(index, card);
        }
    }

    public void mouseDragged(MouseEvent e) {
        // moves the card that was clicked on
        if (selected != null) {
            selected.setX(e.getX() - offsetX);
            selected.setY(e.getY() - offsetY);
        }
        repaint();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
    }

    public int getCardColumns(int x, int y) {
        int index = 0;
        numberOfColumns = player.deck.size() + 1;
        int widthOfColumns = numberOfColumns * 120;

        // if there is only one column, it should take up the entire screen
        if (numberOfColumns == 1) {
            widthOfColumns = Main.WIDTH;
            index = 0;
        }
        // two columns
        else if (numberOfColumns == 2) {
            widthOfColumns = Main.WIDTH / 2;
            if (x < widthOfColumns) {
                index = 0;
            } else
                index = 1;
        }
        // if number of columns is greater than 2
        else {
            // widthOfColumns would be more acurately called width of all center columns, each center column is the width of a card, just offset by half a card
            widthOfColumns = (numberOfColumns - 2) * 120;
            int widthOfSideColumns = (Main.WIDTH - widthOfColumns) / 2;

            if (x < widthOfSideColumns) {
                index = 0;
            } else if (x < widthOfColumns + widthOfSideColumns) {
                // System.out.println("between 1 and end");
                for (int i = 0; i < numberOfColumns - 2; i++) {
                    if (x > widthOfSideColumns + (i) * 120) {
                        index = i + 1;

                    } else
                        break;
                }
            } else {
                index = player.deck.size();
            }
        }
        return index;
    }

    public JLabel[] getCardBoxes() {
        return cardBoxes;
    }

    public Cards[] getHandCards() {
        return handCards;
    }

    public void componentResized(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
        for (int i = 0; i < Chest.getGiveCards(); i++) {
            player.deck.add(new Cards(0, 420, 30, 70));
        }
        removeGaps();
        Chest.setGiveCards(0);
        repaint();
    }

    public void componentHidden(ComponentEvent e) {
    }
}