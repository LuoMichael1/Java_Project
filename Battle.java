import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Battle extends JPanel {

    private Battler player; //= new Player(); <--- player is made in the constructor
    private Battler enemy = new Enemy();

    private Battler playersArray[] = new Battler[2];

    private int round = 1;
    private int turn;        // player or enemys turn to act
    private int altTurn;     // the party that is not currently able to act
    private boolean isWon = false;

    // space for messages
    private JLabel messageLabel;
    private JLabel instructionLabel;

    //private Cards[] playerSelectedCards;

    private JPanel cardPanel = new JPanel() {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // display player's cards
            for (int i = 0; i < GamePanel.deckSize; i++) {
                player.deck[i].setX(20 + i * 106);
                player.deck[i].setY(750);
                player.deck[i].myDraw(g);
                drawCardInfo(g, player.deck[i]);
            }

            // display enemy's cards
            for (int i = 0; i < GamePanel.deckSize; i++) {
                enemy.deck[i].setX(1200 + i * -106);
                enemy.deck[i].setY(100);
                enemy.deck[i].myDraw(g);
                drawCardInfo(g, enemy.deck[i]);
            }
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
        
        // create other player's cards
        //enemy = new Enemy();

        // NOT ORIGINAL BUT NECESSARY TO WORK (CITE CODE)
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                battle();
                return null;
            }
        };
        worker.execute();
    }

    public void battle() {

        repaint();

        // fix this while true statement
        while (!isWon) {

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
            
            //playersArray[turn].cardsUsed++;
            //playersArray[altTurn].cardsUsed++;
            

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

            // 1 second pause
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        end();
    }

    public void end() {
        System.out.println("Game Over");
        
        // 3 second pause
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        System.exit(0);
    }

    private void performAttack(Cards attackerCard, Cards defenderCard) {

        defenderCard.setHealth(defenderCard.getHealth() - attackerCard.getAttack());
    }
}