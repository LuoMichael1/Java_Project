import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Battle extends JPanel implements ActionListener{

    private Battler player; //= new Player(); <--- player is made in the constructor
    private Battler enemy = new Enemy();

    private Battler playersArray[] = new Battler[2];

    private int round = 1;    // round starts at 1 so player goes first in any battle, if round starts as 0, the enemy will go first
    private int turn;         // player or enemys turn to act
    private int altTurn;      // the party that is not currently acting
    private boolean isWon = false;

    private int speed = 1000; // how many milliseconds before a card acts, set lower for a faster game
    // space for messages
    private JLabel messageLabel;
    private JLabel instructionLabel;
    private Timer timer;
    //private Cards[] playerSelectedCards;
    
    //
    private JPanel cardPanel = new JPanel() {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // display player's cards
            for (int i = 0; i < GamePanel.deckSize; i++) {
                player.deck[i].setX(20 + i * 106);
                player.deck[i].setY(740);
                
                // moves the currently acting card upwards to make it more visible
                if (turn == 0 && i == playersArray[turn].cardsUsed)
                    player.deck[i].setY(720);
                
                player.deck[i].myDraw(g);
                drawCardInfo(g, player.deck[i]);
            }

            // display enemy's cards
            for (int i = 0; i < GamePanel.deckSize; i++) {
                enemy.deck[i].setX(1200 + i * -106);
                enemy.deck[i].setY(100);

                // moves the currently acting card upwards to make it more visible
                if (turn == 1 && i == playersArray[turn].cardsUsed)
                    enemy.deck[i].setY(80);

                enemy.deck[i].myDraw(g);
                drawCardInfo(g, enemy.deck[i]);
            }

            // if is players turn and the card represented by i is the card currently acting, setY to 800 instead
        }

        // display health and attack
        private void drawCardInfo(Graphics g, Cards card) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Health: " + card.getHealth(), card.getX() + 10, card.getY() + 30);
            g.drawString("Attack: " + card.getAttack(), card.getX() + 10, card.getY() + 50);
        }
    };

    public Battle(Player player, Cards[] playerSelectedCards) {

        // check if cards are selected correctly
        for (Cards card : playerSelectedCards) {
            System.out.println(card.getHealth() + " " + card.getAttack());
        }

        setLayout(new BorderLayout());

        instructionLabel = new JLabel(
                "Automatically playing...");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(instructionLabel, BorderLayout.NORTH);

        // space for messages
        messageLabel = new JLabel("");
        messageLabel.setBounds(500, 500, 300, 20);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(messageLabel);

        add(cardPanel, BorderLayout.CENTER);

        // get player and cards
        this.player = player;
        //this.playerSelectedCards = playerSelectedCards;


        playersArray[0] = player;
        playersArray[1] = enemy;
        
        timer = new Timer(speed, this);
        timer.start(); 
        
        
    }


    private void performAttack(Cards attackerCard, Cards defenderCard) {

        defenderCard.setHealth(defenderCard.getHealth() - attackerCard.getAttack());
    }

    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == timer) {
        
        // if round is even, it is the player's turn, if round is odd, its the enemy's
        // turn. turn is 0 or 1 to make using an array easier
        
            round++;
            altTurn = (round+1)%2;
            if (round % 2 == 0) {
                turn = 0;
                messageLabel.setText("Player card " + player.cardsUsed + " attacks enemy card " + enemy.cardsUsed);  
                System.out.println("Playerturn");    
            }
            else {
                turn = 1;
                messageLabel.setText("Enemy card " + enemy.cardsUsed + " attacks player card " + player.cardsUsed);
                System.out.println("Enemyturn");  
            }


            System.out.println(turn);
            performAttack(playersArray[turn].deck[playersArray[turn].cardsUsed], playersArray[altTurn].deck[playersArray[altTurn].cardsUsed]);

            // moves card currently acting so the game is more clear and easy to follow
            playersArray[turn].deck[playersArray[turn].cardsUsed].setY(playersArray[turn].deck[playersArray[turn].cardsUsed].getY()+100); 

            // removes attacked card if is has no health left
            if (playersArray[altTurn].deck[playersArray[altTurn].cardsUsed].getHealth() <= 0) {
                messageLabel.setText(playersArray[altTurn] + "card defeated!");
                playersArray[altTurn].cardsUsed++;
            }

            repaint();

            if (playersArray[altTurn].cardsUsed == GamePanel.deckSize) {
                System.out.println(playersArray[altTurn] + "loses!");
                isWon = true;
            }

            if (isWon) {
                System.exit(0);
            }
        }
    }
}