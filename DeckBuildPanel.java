// The screen where you can select the cards you will bring into the auto-battle as well as fuse and upgrade cards

import javax.swing.*;
//import javax.swing.border.Border;
//import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DeckBuildPanel extends JPanel implements MouseMotionListener, MouseListener, ActionListener {

    private int x, y;
    //private int cardsInDeck = 8;
    public static int deckSize = 8;
    private Cards selected = null;
    private int offsetX, offsetY;

    // initialize player
    private Player player = new Player();;

    // initialize buttons
    private JButton battleButton; // start battle

    private JButton leftButton; // scroll left
    private JButton rightButton; // scroll right
    private int scrollValue = 0;

    private JLabel[] cardBoxes = new JLabel[9];   // the slots that a card can be dragged to (player hand)
    private Cards[] selectedCards = new Cards[8]; // the cards actually in the card boxes
    private int cardsSelected = 0;

    // card columns describe the area that a card can be dropped and return to a specfic part of the deck
    // for example, dropping a card in the leftmost column will put the card at the leftmost place in the deck
    private int cardColumnsWidth = 120;
    private int numberOfColumns = 0;
    private int indexCounter = 0;  // used to finds the original index in the deck the selected card came from 
    private int deckIndex = 0;

    private JLabel instructionLabel;

    

    public DeckBuildPanel() {
        /* 
        this.addComponentListener(new ComponentListener() {
            public void componentShown(ComponentEvent e) {
                for (int i = 0; i < player.hand.length; i++) {
                    //selectedCards[i] = player.hand[i];
                    removeGaps();
                }  
            }
            public void componentResized(ComponentEvent e) {}
            public void componentMoved(ComponentEvent e) {}
            public void componentHidden(ComponentEvent e) {}
        });
*/
        this.setLayout(new BorderLayout());
        initUserInterface();
        initButtons();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // anti-alising on font
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int counter = 1;
        //g.drawOval(x - 20, y - 20, 40, 40);

        // draw card boxes
        for (int i = 0; i < deckSize; i++) {
            g.setColor(new Color(101, 111, 249));
            g.fillRect(122 + i * 130, 162, Cards.CARDWIDTH-4, Cards.CARDHIGHT-4);

            g.setColor(new Color(45, 44, 60));
            g.fillRect(123 + i * 130, 163, Cards.CARDWIDTH-6, Cards.CARDHIGHT-6);

            g.setColor(new Color(58, 57, 74));
            
            // draw the number inside the box
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

        instructionLabel = new JLabel(("Drag exactly " + deckSize + " cards into the boxes to take into battle, then press Start Battle").toUpperCase());
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setFont(Main.Lexend18);
        this.add(instructionLabel, BorderLayout.NORTH);

        
        for (int i = 0; i < deckSize + 1; i++) {
            cardBoxes[i] = new JLabel();
            cardBoxes[i].setBounds(120 + i * 130, 160, 120, 220);
        }
    
        
        /* 
        cardx = 0;
        cardy = 0;

        create the players deck
        player = 
        deck logic: card from deck remains in deck until put in selectedCards. Then x
        values of all remaining cards are updated to remove gaps

        cardBoxes[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.add(cardBoxes[i]);
        */
        
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

        /*
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
        */
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
            Battle battle = new Battle(player, selectedCards);
            Main.addCard(battle, "battle");
            
            /* 
            Main.nextCard();
            removeAll();
            revalidate();
            repaint();

            Battle battle = new Battle(player, selectedCards);
            add(battle, BorderLayout.CENTER);
            revalidate();
            repaint();
            */
        } 
        // if you didn't fill out your deck, it will generate some random cards to use
        else {
            for (int i = 0; i < deckSize; i++) {
                if (selectedCards[i] == null) {
                    selectedCards[i] = new Cards(0,0,100,0);
                    cardsSelected++;
                }
            }
            Battle battle = new Battle(player, selectedCards);
            Main.addCard(battle, "battle");
            /* 
            removeAll();
            revalidate();
            repaint();

            Battle battle = new Battle(player, selectedCards);
            add(battle, BorderLayout.CENTER);
            revalidate();
            repaint();
        */
        }
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

        for (Cards card : selectedCards) {
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
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

        if (selected != null) {
            boolean putInBox = false;
            boolean leveled = false;  // prevents replacing the card that leveled up with the card that was used to merge

            for (int i = 0; i < deckSize; i++) {
                // checks if the mouse is within the dimensions of a card box
                if (e.getX() >= cardBoxes[i].getX() && e.getX() <= (cardBoxes[i].getX() + cardBoxes[i].getWidth()) && e.getY() >= cardBoxes[i].getY() && e.getY() <= (cardBoxes[i].getY() + cardBoxes[i].getHeight())) {
                    
                    // if the same card is in the box already, merge the two cards. Otherwise remove the card curretly in the box
                    if (selectedCards[i] != null) {
                        // if two cards have the same id | checks if you are trying to place the same card in the same spot | prevents you from leveling past level 3
                        if (selectedCards[i].getID() == selected.getID() && !(selectedCards[i] == selected) && !(selected.getLevel() > 2)) {
                            selectedCards[i].increaseLevel();
                            leveled = true;
                        }
                        else {
                            selectedCards[i].setX(selectedCards[i].getOriginalX());
                            selectedCards[i].setY(selectedCards[i].getOriginalY());

                            removeCard(selectedCards[i], deckIndex);
                        }
                    }

                    // remove card from current position in selection if applicable to support
                    // reordering
                    if (selected.getSelectionIndex() != -1) {
                        removeCard(selected, getCardColumns(e.getX(), e.getY()));
                    }
                    // put the card in the box
                    selected.setX(cardBoxes[i].getX());
                    selected.setY(cardBoxes[i].getY());
                    selected.setSelectionIndex(i);
                    
                    if (!leveled) {
                        selectedCards[i] = selected;
                        cardsSelected++;
                    }

                    //cardsSelected++;
                    // remove the card from the deck
                    player.deck.remove(selected);
                    //removeGaps();
                    System.out.println("Put in box");
                    putInBox = true;
                }
            }
            // I think this is for if the card selected is from the selectedCards hand and is dragged off into the deck
            if (!putInBox) { // remove selected card from selectedCards
                System.out.println("not put in box");
                if (selected.getSelectionIndex() != -1)
                    removeCard(selected, getCardColumns(e.getX(), e.getY()));

                selected.setX(selected.getOriginalX());
                selected.setY(selected.getOriginalY());   
            }

            removeGaps();
            selected = null;
            repaint();
        }

        // debugging code
        System.out.println("\nselected-------------------");
        for (Cards card : selectedCards) {

            if (card != null)
                System.out.println(card.getID());
            else
                System.out.println("null");
        }
        System.out.println("deck-------------------");
        for (Cards card : player.deck) {

            if (card != null)
                System.out.println(card.getID());
            else
                System.out.println("null");
        }
    }

    // remove gaps between cards in the deck as cards are removed. Tries to center the cards
    public void removeGaps() {

        // gets the center of the screen
        int deckX = (Main.WIDTH)/2;

        // adjusts the start accounting for each of the cards in the deck
        for (int i=0; i < player.deck.size(); i++)
            // subtract 60 because that is half of the width of a card which is what we want because we want to center them
            deckX = deckX - 60;
        
        // puts each card after one another
        for (Cards card : player.deck) {
            card.setX(deckX);
            deckX += 120;
        }
    }

    public void removeCard(Cards card, int index) {
        selectedCards[card.getSelectionIndex()] = null;

        card.setSelectionIndex(-1);
        cardsSelected--;

        // add card back to deck
        if (index == player.deck.size()) {
            System.out.println("Placing card at: " + index);
            player.deck.add(card);
        }
        else {
            System.out.println("Placing card at: " + index);
            player.deck.add(index, card);
        }
        //removeGaps();  <-- moved this to the end of the mouse released method
    }

    public void mouseDragged(MouseEvent e) {
        // moves the card that was clicked on
        if (selected != null) {
            selected.setX(e.getX() - offsetX);
            selected.setY(e.getY() - offsetY);
        }
        repaint();
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void actionPerformed(ActionEvent e) {}


    public int getCardColumns(int x, int y) {
        int index = 0;
        numberOfColumns = player.deck.size() + 1;
        int widthOfColumns = numberOfColumns*120;

        // if there is only one column, it should take up the entire screen
        if (numberOfColumns == 1) {
            widthOfColumns = Main.WIDTH;
            index = 0;
        }
        // two columns
        else if (numberOfColumns == 2) {
            widthOfColumns = Main.WIDTH/2;
            if (x < widthOfColumns) {
                index = 0;
            }
            else
                index = 1;
        }
        // if number of columns is greater than 2
        else {
            System.out.println("I DID A THINGY");
            widthOfColumns = (numberOfColumns-2)*120;
            System.out.println(widthOfColumns);

            int widthOfSideColumns = (Main.WIDTH-widthOfColumns)/2;
            System.out.println(widthOfSideColumns);
            if (x < widthOfSideColumns) {
                index = 0;
            }
            else if (x < widthOfColumns+widthOfSideColumns){
                System.out.println("between 1 and end");
                for (int i = 0; i < numberOfColumns-2; i++) {
                    if (x > widthOfSideColumns + (i)*120) {
                        index = i+1;
                        System.out.println("index:" + index);
                        System.out.println("I Also DID A THINGY");
                        
                    }
                    else 
                        break;
                        
                }
            }
            else {
                index = player.deck.size();
            }
        }
        System.out.println("returned index" + index);
        return index;
    }
}