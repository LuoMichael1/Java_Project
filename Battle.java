import javax.swing.*;
import java.awt.*;

public class Battle extends JPanel {

    private JLabel playerLabel;
    private JLabel opponentLabel;

    private Player player;
    private Enemy enemy;

    // space for messages
    private JLabel messageLabel;

    private JPanel cardPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Display player's cards
            for (int i = 0; i < 8; i++) {
                player.deck[i].setX(10 + i * 150);
                player.deck[i].setY(10);
                player.deck[i].myDraw(g);
                drawCardInfo(g, player.deck[i]);
            }

            // Display enemy's cards
            for (int i = 0; i < 8; i++) {
                enemy.deck[i].setX(10 + i * 150);
                enemy.deck[i].setY(150);
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

    public Battle(Player player) {
        // Set up the panel
        setLayout(new BorderLayout());

        // space for messages
        messageLabel = new JLabel("");
        add(messageLabel, BorderLayout.NORTH);

        // Create components
        playerLabel = new JLabel("Player's Pokemon");
        opponentLabel = new JLabel("Opponent's Pokemon");
        add(playerLabel, BorderLayout.WEST);
        add(opponentLabel, BorderLayout.EAST);
        add(cardPanel, BorderLayout.CENTER);

        // Get player
        this.player = player;

        // create other player's cards
        enemy = new Enemy();

        // NOT ORIGINAL, CITE CODE
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
            performAttack(player.deck[player.cardsUsed], enemy.deck[enemy.cardsUsed]);

            repaint();

            if (enemy.deck[enemy.cardsUsed].getHealth() <= 0) {

                messageLabel.setText("Enemy card defeated!");
                enemy.cardsUsed++;
            }

            if (enemy.cardsUsed == 8) {
                System.out.println("Enemy loses!");
                break;
            }

            try {
                Thread.sleep(500); // Pause for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            messageLabel.setText("Enemy card " + enemy.cardsUsed + " attacks player card " + player.cardsUsed);
            performAttack(enemy.deck[enemy.cardsUsed], player.deck[player.cardsUsed]);

            repaint();

            if (player.deck[player.cardsUsed].getHealth() <= 0) {

                messageLabel.setText("Player card defeated!");
                player.cardsUsed++;
            }

            if (player.cardsUsed == 8) {
                System.out.println("Player loses!");
                break;
            }

            try {
                Thread.sleep(500); // Pause for 1 second
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
