import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Battle extends JPanel implements ActionListener {

    private Battler player; // = new Player(); <--- player is made in the constructor
    private Battler enemy = new Enemy();

    private Battler playersArray[] = new Battler[2];

    private int round = 0;  // player goes first in any battle
    private int turn = 5;   // is either 0 or 1 to signify if it is the player or enemys turn to act
    private int altTurn;    // the party that is not currently acting
    private boolean isWon = false;  

    // would probably be more acurate to call these ticks rather than frames
    private int FPS = 60;              // frames per second (use this to change speed cuz some animations are tied to stuff) 
    private int framesPerTurn = 60;     
    private int frameCounter = 0;     
    private int framesForCardUp = 5;   // how many frames for the card up animation

    private final int CARDY = 500;     // the Y level the cards are drawn at
    private int cardUpY = CARDY;       // intializing the card up offset

    static final int HEALTHBAR_Y = 60; // the Y level the healthbar is drawn at

    private int damage = 0;

    // space for messages
    private JLabel messageLabel;
    private JLabel instructionLabel;

    private Timer timer;
    
    // images
    private ImageIcon vulnerableIcon = new ImageIcon("images/VulnerableIcon.png");
    private ImageIcon strenghtIcon = new ImageIcon("images/strenghtIcon.png");
    private ImageIcon enemySprite = new ImageIcon("images/enemy.png");
    private ImageIcon shieldIcon = new ImageIcon("images/shieldIcon.png");

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
        messageLabel.setBounds(580, 60, 300, 20);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(messageLabel);

        //add(cardPanel, BorderLayout.CENTER);

        // get player and cards
        this.player = player;
        // this.playerSelectedCards = playerSelectedCards;

        // create player array 
        playersArray[0] = player;
        playersArray[1] = enemy;

        // setup timer
        timer = new Timer(1000/FPS, this);
        timer.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // enables antialiasing on the font which makes it look way better
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // draw the characters
        //g.drawImage(playerSprite.getImage(), 100, 120, null);
        player.myDraw(g);
        g.drawImage(enemySprite.getImage(), 800, 140, null);

        // healthbars
        g.drawRect(35, HEALTHBAR_Y, 251, 25);
        g.drawRect(1000, HEALTHBAR_Y, 251, 25);
        g.setColor(Color.red);
        g.fillRect(36, HEALTHBAR_Y+1, player.getHealth() / (player.getMaxHealth() / 250), 24);
        g.fillRect(1001, HEALTHBAR_Y+1, enemy.getHealth() / (enemy.getMaxHealth() / 250), 24);

        g.setColor(Color.black);
        g.setFont(Main.Lexend18);
        g.drawString("" + player.getHealth() + "/" + player.getMaxHealth(), 40, HEALTHBAR_Y+20);
        g.drawString("" + enemy.getHealth() + "/" + enemy.getMaxHealth(), 1005, HEALTHBAR_Y+20);

        // ambrosia stat
        g.drawString("Ambrosia: " + player.getAmbrosia(), 40, 120);
        g.drawString("Ambrosia: " + enemy.getAmbrosia(), 1005, 120);

        // shield stat
        g.setFont(Main.Lexend12);
        if (player.getShield() > 0) {
            g.drawImage(shieldIcon.getImage(), 300, HEALTHBAR_Y, null);
            g.drawString(""+player.getShield(), 300, HEALTHBAR_Y);
        }
        if (enemy.getShield() > 0) {
            g.drawImage(shieldIcon.getImage(), 930, HEALTHBAR_Y, null);
            g.drawString(""+enemy.getShield(), 930, HEALTHBAR_Y);
        }
        
        // Vulnerable Stacks
        if (player.getVulnerableStacks() > 0) {
            g.drawImage(vulnerableIcon.getImage(), 22, 200, null);
            g.setFont(Main.Lexend12);
            g.drawString("Vulnerable", 20, 260);
            g.setFont(Main.Lexend18);
            g.drawString("" + player.getVulnerableStacks(), 25, 205);
        }
        if (enemy.getVulnerableStacks() > 0) {
            g.drawImage(vulnerableIcon.getImage(), 1190, 200, null);
            g.setFont(Main.Lexend12);
            g.drawString("Vulnerable", 1180, 260);
            g.setFont(Main.Lexend18);
            g.drawString("" + enemy.getVulnerableStacks(), 1190, 205);
        }
        // Strenghten Stacks
        if (player.getStrengthenStacks() > 0) {
            g.drawImage(strenghtIcon.getImage(), 22, 240, null);
            g.setFont(Main.Lexend12);
            g.drawString("Strenght", 20, 300);
            g.setFont(Main.Lexend18);
            g.drawString("" + player.getStrengthenStacks(), 25, 245);
        }
        if (enemy.getStrengthenStacks() > 0) {
            g.drawImage(strenghtIcon.getImage(), 1190, 240, null);
            g.setFont(Main.Lexend12);
            g.drawString("Strenght", 1180, 300);
            g.setFont(Main.Lexend18);
            g.drawString("" + enemy.getStrengthenStacks(), 1190, 245);
        }

        if (damage > 0) {
            g.setColor(Color.red);
            g.setFont(Main.Lexend18);
            if (turn == 0)
                g.drawString("-" + damage, 880, HEALTHBAR_Y+10);
            else 
                g.drawString("-" + damage, 350, HEALTHBAR_Y+10);
        }

        // display player's cards
        for (int i = GamePanel.deckSize-1; i >= 0; i=i-1) {
            player.hand[i].setX(5 + i * 62);
            player.hand[i].setY(CARDY);

            // moves the currently acting card upwards 20px to make it more visible
            if (turn == 0 && i == (round - 1) / 2 % 8)
                player.hand[i].setY(cardUpY);

            player.hand[i].myDraw(g);
            // drawCardInfo(g, player.hand[i]);
        }

        // display enemy's cards
        for (int i = GamePanel.deckSize-1; i >= 0; i=i-1) {
            enemy.hand[i].setX(1140 + i * -62);
            enemy.hand[i].setY(CARDY);

            //System.out.println("round: " + round);

            // moves the currently acting card upwards 20px to make it more visible
            if (turn == 1 && i == ((round - 1) / 2) % 8)
                enemy.hand[i].setY(cardUpY);

            enemy.hand[i].myDraw(g);
            // drawCardInfo(g, enemy.hand[i]);
        }
    }

    

    private void performAttack(Cards attackerCard, Battler defender) {
        // gets ambrosia from card
        playersArray[turn].setAmbrosia(attackerCard.getAmbrosia());

        // checks if the character has enough ambrosia to use this card
        if (attackerCard.getAmbrosiaCost() <= playersArray[turn].getAmbrosia()) {

            playersArray[turn].setAmbrosia(-1 * (attackerCard.getAmbrosiaCost()));
            playersArray[turn].setShield(attackerCard.getShield()*10);
            playersArray[altTurn].setVulnerableStacks(attackerCard.getVulnerableStacks());
            playersArray[turn].setStrengthenStacks(attackerCard.getStrengthenStacks());

            // deal damage -----------------------------------
            damage = (attackerCard.getAttack()+playersArray[turn].getStrengthenStacks()) * 10;

            if (playersArray[altTurn].getVulnerableStacks() > 0)
                damage = damage*2;

            if (damage > 0) {

                // first deals damage to any shield
                if (defender.getShield() >= damage) {
                    defender.setShield(-damage);
                    damage = 0;
                    
                } else if (defender.getShield() < damage) {
                    int tempShield = defender.getShield();
                    damage = damage-tempShield;
                    defender.setShield(-tempShield);
                }   

                defender.setHealth(defender.getHealth() - damage);
                playersArray[turn].attackAnim(1);

                // prevents health from going below 0
                if (defender.getHealth() < 0)
                    defender.setHealth(0);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
    
        if (e.getSource() == timer) {
            frameCounter++;
            
            // first frame of turn
            if (frameCounter == 1) {
                altTurn = (round + 1) % 2;
                if (round % 2 == 0) {
                    turn = 0;
                    messageLabel.setText("Player attacks");
                } else {
                    turn = 1;
                    messageLabel.setText("Enemy attacks");
                }
                round++;
            }

            // frame 0 - 5
            if (frameCounter <= framesForCardUp)
                cardUpY = cardUpY-(100/framesForCardUp);

            if (frameCounter == 20)
                performAttack(playersArray[turn].hand[(round - 1) / 2 % 8], playersArray[altTurn]);
                
            if (frameCounter == 40)
                player.attackAnimStop(1);

            repaint();
            // frame 55 - 60
            if (frameCounter >= 55)
                cardUpY = cardUpY+(100/framesForCardUp);

            // frame 60
            if (frameCounter == framesPerTurn) {
                frameCounter = 0;
                cardUpY = CARDY;
            // if round is even, it is the player's turn, if round is odd, its the enemy's
            // turn. turn is 0 or 1 to make using an array easier

                
                damage = 0;

                reduceStacks(playersArray[turn]);

                

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