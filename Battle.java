import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Battle extends JPanel {

    private Player player;
    private Enemy enemy;

    // space for messages
    private JLabel messageLabel;
    private JLabel instructionLabel;

    private ArrayList<Cards> playerSelectedCards;

    private JPanel cardPanel = new JPanel() {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // display player's cards
            for (int i = 0; i < 4; i++) {
                playerSelectedCards.get(i).setX(20 + i * 100);
                playerSelectedCards.get(i).setY(750);
                playerSelectedCards.get(i).myDraw(g);
                drawCardInfo(g, playerSelectedCards.get(i));
            }

            // display enemy's cards
            for (int i = 0; i < 4; i++) {
                enemy.deck[i].setX(1200 + i * -100);
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

    public Battle(Player player, ArrayList<Cards> playerSelectedCards) {

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

            // 1 second pause
            try {
                Thread.sleep(1000);
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
        System.exit(0);
    }

    private void performAttack(Cards attackerCard, Cards defenderCard) {

        defenderCard.setHealth(defenderCard.getHealth() - attackerCard.getAttack());
    }
}