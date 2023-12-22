import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Battle extends JPanel {

    private JLabel playerLabel;
    private JLabel opponentLabel;

    private Player player;
    private Enemy enemy;

    // space for messages
    private JLabel messageLabel;

    private ArrayList<Cards> playerSelectedCards;

    private JPanel cardPanel = new JPanel() {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Display player's cards
            for (int i = 0; i < 4; i++) {
                player.deck[i].setX(20 + i * 100);
                player.deck[i].setY(750);
                player.deck[i].myDraw(g);
                drawCardInfo(g, player.deck[i]);
            }

            // Display enemy's cards
            for (int i = 0; i < 4; i++) {
                enemy.deck[i].setX(1200 + i * -100);
                enemy.deck[i].setY(100);
                enemy.deck[i].myDraw(g);
                drawCardInfo(g, enemy.deck[i]);
            }
        }

        private void drawCardInfo(Graphics g, Cards card) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Health: " + card.getHealth(), card.getX() + 10, card.getY() + 30);
            g.drawString("Attack: " + card.getAttack(), card.getX() + 10, card.getY() + 50);
        }
    };

    public Battle(Player player, ArrayList<Cards> playerSelectedCards) {
        // Set up the panel
        setLayout(new BorderLayout());

        // space for messages
        messageLabel = new JLabel("");
        messageLabel.setBounds(500, 500, 300, 20); // Set absolute coordinates for the message label
        add(messageLabel);

        // Create components
        add(cardPanel, BorderLayout.CENTER);

        // Get player
        this.player = player;

        // Get selected cards
        this.playerSelectedCards = playerSelectedCards;

        // create other player's cards
        enemy = new Enemy();

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

        while (true) {

            messageLabel.setText("Player card " + player.cardsUsed + " attacks enemy card " + enemy.cardsUsed);
            performAttack(playerSelectedCards.get(player.cardsUsed), enemy.deck[enemy.cardsUsed]);

            repaint();

            if (enemy.deck[enemy.cardsUsed].getHealth() <= 0) {

                messageLabel.setText("Enemy card defeated!");
                enemy.cardsUsed++;
            }

            if (enemy.cardsUsed == 4) {
                System.out.println("Enemy loses!");
                break;
            }

            try {
                Thread.sleep(1000); // Pause for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            messageLabel.setText("Enemy card " + enemy.cardsUsed + " attacks player card " + player.cardsUsed);
            performAttack(enemy.deck[enemy.cardsUsed], playerSelectedCards.get(player.cardsUsed));

            repaint();

            if (playerSelectedCards.get(player.cardsUsed).getHealth() <= 0) {

                messageLabel.setText("Player card defeated!");
                player.cardsUsed++;
            }

            if (player.cardsUsed == 4) {
                System.out.println("Player loses!");
                break;
            }

            try {
                Thread.sleep(1000); // Pause for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        end();
    }

    public void end() {

        System.out.println("Game Over");
        System.exit(0);
    }

    private void performAttack(Cards attackerCard, Cards defenderCard) {

        defenderCard.setHealth(defenderCard.getHealth() - attackerCard.getAttack());
    }
}