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

    private int FPS = 60;              // frames per second
    private int framesPerTurn = 60;    // set lower for faster game
    private int frameCounter = 0;
    private int framesForCardUp = 5;  // how many frames for the card up animation
    private final int CARDY = 500;
    private int cardUpY = CARDY;

    // space for messages
    private JLabel messageLabel;
    private JLabel instructionLabel;
    private Timer timer;
    private ImageIcon playerSprite = new ImageIcon("images/player.png");
    private ImageIcon enemySprite = new ImageIcon("images/enemy.png");
    // private Cards[] playerSelectedCards;

    private JPanel cardPanel = new JPanel() {

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // enables antialiasing on the font which makes it look way better
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // draw the characters
            //g.drawImage(playerSprite.getImage(), 100, 120, null);
            player.myDraw(g);
            g.drawImage(enemySprite.getImage(), 900, 120, null);

            // healthbars
            g.drawRect(35, 60, 251, 25);
            g.drawRect(1000, 60, 251, 25);
            g.setColor(Color.red);
            g.fillRect(36, 61, player.getHealth() / (player.getMaxHealth() / 250), 24);
            g.fillRect(1001, 61, enemy.getHealth() / (enemy.getMaxHealth() / 250), 24);

            g.setColor(Color.black);
            g.setFont(Main.Lexend18);
            g.drawString("" + player.getHealth() + "/" + player.getMaxHealth(), 40, 80);
            g.drawString("" + enemy.getHealth() + "/" + enemy.getMaxHealth(), 1005, 80);

            // ambrosia stat
            g.drawString("Ambrosia: " + player.getAmbrosia(), 40, 120);
            g.drawString("Ambrosia: " + enemy.getAmbrosia(), 1005, 120);

            // Vulnerable Stacks
            if (player.getVulnerableStacks() > 0)
                g.drawString("Vulnerable: x" + player.getVulnerableStacks(), 20, 150);
            if (enemy.getVulnerableStacks() > 0)
                g.drawString("Vulnerable: x" + enemy.getVulnerableStacks(), 1055, 150);

            // display player's cards
            for (int i = GamePanel.deckSize-1; i >= 0; i=i-1) {
                player.hand[i].setX(5 + i * 60);
                player.hand[i].setY(CARDY);

                // moves the currently acting card upwards 20px to make it more visible
                if (turn == 0 && i == (round - 1) / 2 % 8)
                    player.hand[i].setY(cardUpY);

                player.hand[i].myDraw(g);
                // drawCardInfo(g, player.hand[i]);
            }

            // display enemy's cards
            for (int i = GamePanel.deckSize-1; i >= 0; i=i-1) {
                enemy.hand[i].setX(1140 + i * -60);
                enemy.hand[i].setY(CARDY);

                //System.out.println("round: " + round);

                // moves the currently acting card upwards 20px to make it more visible
                if (turn == 1 && i == ((round - 1) / 2 - 1) % 8)
                    enemy.hand[i].setY(cardUpY);

                enemy.hand[i].myDraw(g);
                // drawCardInfo(g, enemy.hand[i]);
            }

            // if is players turn and the card represented by i is the card currently
            // acting, setY to 800 instead
        }

        // display health and attack
        /*
         * private void drawCardInfo(Graphics g, Cards card) {
         * g.setColor(Color.BLACK);
         * g.setFont(new Font("Arial", Font.BOLD, 14));
         * g.drawString("Health: " + card.getHealth(), card.getX() + 10, card.getY() +
         * 30);
         * g.drawString("Attack: " + card.getAttack(), card.getX() + 10, card.getY() +
         * 50);
         * }
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

        timer = new Timer(1000/FPS, this);
        timer.start();

    }

    private void performAttack(Cards attackerCard, Battler defender) {
        // gets ambrosia from card
        playersArray[turn].setAmbrosia(attackerCard.getAmbrosia());

        // checks if the character has enough ambrosia to use this card
        if (attackerCard.getAmbrosiaCost() <= playersArray[turn].getAmbrosia()) {

            playersArray[turn].setAmbrosia(-1 * (attackerCard.getAmbrosiaCost()));
            playersArray[altTurn].setVulnerableStacks(attackerCard.getVulnerableStacks());

            // deal damage
            if (playersArray[altTurn].getVulnerableStacks() > 0)
                defender.setHealth(defender.getHealth() - (20 * (attackerCard.getAttack())));
            else
                defender.setHealth(defender.getHealth() - (10 * (attackerCard.getAttack())));

        }

    }

    public void actionPerformed(ActionEvent e) {
    
        if (e.getSource() == timer) {
            frameCounter++;

            // frame 0 - 5
            if (frameCounter <= framesForCardUp) {
                cardUpY = cardUpY-(75/framesForCardUp);
                System.out.println("Testing pint 1"+cardUpY);
            }

            if (frameCounter == 10)
                player.attackAnim();

            repaint();
            
            // frame 60
            if (frameCounter == framesPerTurn) {
                frameCounter = 0;
                cardUpY = CARDY;
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

                reduceStacks(playersArray[turn]);

                performAttack(playersArray[turn].hand[(round - 1) / 2 % 8], playersArray[altTurn]);

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

    private void reduceStacks(Battler target) {
        if (target.getVulnerableStacks() > 0) {
            target.setVulnerableStacks(-1);
        }
    }

}