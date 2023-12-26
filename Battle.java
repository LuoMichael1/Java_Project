import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Battle extends JPanel implements ActionListener {

    private Battler player; // = new Player(); <--- player is made in the constructor
    private Battler enemy = new Enemy();

    private Battler playersArray[] = new Battler[2];

    private int round = 1; // round starts at 1 so player goes first in any battle, if round starts as 0,
                           // the enemy will go first
    private int turn; // player or enemys turn to act
    private int altTurn; // the party that is not currently acting
    private boolean isWon = false;

    private int speed = 1000; // how many milliseconds before a card acts, set lower for a faster game
    // space for messages
    private JLabel messageLabel;
    private JLabel instructionLabel;
    private Timer timer;
    private ImageIcon playerSprite = new ImageIcon("images/player.png");
    private ImageIcon enemySprite = new ImageIcon("images/enemy.png");
    // private Cards[] playerSelectedCards;

    //
    private JPanel cardPanel = new JPanel() {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // draw the characters
            g.drawImage(playerSprite.getImage(), 100, 120, null);
            g.drawImage(enemySprite.getImage(), 900, 120, null);

            // healthbars
            g.drawRect(35,60, 251,25);
            g.drawRect(1000,60, 251,25);
            g.setColor(Color.red);
            g.fillRect(36, 61, player.getHealth()/(player.getMaxHealth()/250), 24);
            g.fillRect(1001, 61, enemy.getHealth()/(enemy.getMaxHealth()/250), 24);

            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Health: " + player.getHealth() +"/"+ player.getMaxHealth(), 40, 80);
            g.drawString("Health: " + enemy.getHealth() +"/"+ enemy.getMaxHealth(), 1005, 80);
            

            // display player's cards
            for (int i = 0; i < GamePanel.deckSize; i++) {
                player.hand[i].setX(5 + i * 70);
                player.hand[i].setY(440);

                // moves the currently acting card upwards to make it more visible
                if (turn == 0 && i == (round-1)/2 %8)
                    player.hand[i].setY(420);

                player.hand[i].myDraw(g);
                //drawCardInfo(g, player.hand[i]);
            }

            // display enemy's cards
            for (int i = 0; i < GamePanel.deckSize; i++) {
                enemy.hand[i].setX(1140 + i * -70);
                enemy.hand[i].setY(440);

                System.out.println("round: " + round);

                // moves the currently acting card upwards to make it more visible
                if (turn == 1 && i == ((round-1)/2-1) % 8)
                    enemy.hand[i].setY(420);

                enemy.hand[i].myDraw(g);
                //drawCardInfo(g, enemy.hand[i]);
            }

            // if is players turn and the card represented by i is the card currently
            // acting, setY to 800 instead
        }

        // display health and attack 
        /* 
        private void drawCardInfo(Graphics g, Cards card) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Health: " + card.getHealth(), card.getX() + 10, card.getY() + 30);
            g.drawString("Attack: " + card.getAttack(), card.getX() + 10, card.getY() + 50);
        } 
        */
    };

    public Battle(Player player, Cards[] playerSelectedCards) {

        // put playerSelectedCards into player.hand
        for (int i = 0; i < playerSelectedCards.length; i++) {
            player.hand[i] = playerSelectedCards[i];
        }

        setLayout(new BorderLayout());

        instructionLabel = new JLabel(
                "Automatically playing...");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(instructionLabel, BorderLayout.NORTH);

        // space for messages
        messageLabel = new JLabel("");
        messageLabel.setBounds(550, 200, 300, 20);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(messageLabel);

        add(cardPanel, BorderLayout.CENTER);

        // get player and cards
        this.player = player;
        // this.playerSelectedCards = playerSelectedCards;

        playersArray[0] = player;
        playersArray[1] = enemy;

        timer = new Timer(speed, this);
        timer.start();

    }

    private void performAttack(Cards attackerCard, Battler defender) {
        
        // checks if the character has enough ambrosia to use this card
        if (attackerCard.getAmbrosiaCost() <= playersArray[altTurn].getAmbrosia())
            defender.setHealth(defender.getHealth() - (10*(attackerCard.getAttack())));
    }

    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == timer) {

            // if round is even, it is the player's turn, if round is odd, its the enemy's
            // turn. turn is 0 or 1 to make using an array easier

            round++;
            altTurn = (round + 1) % 2;
            if (round % 2 == 0) {
                turn = 0;
                messageLabel.setText("Player attacks");
            } else {
                turn = 1;
                messageLabel.setText("Enemy attacks");
            }

            performAttack(playersArray[turn].hand[(round-1)/2 % 8], playersArray[altTurn]);

            repaint();

            if (playersArray[altTurn].getHealth() <= 0) {
                System.out.println(playersArray[altTurn] + "loses!");
                isWon = true;
            }

            if (isWon) {
                System.exit(0);
            }
        }
    }
}