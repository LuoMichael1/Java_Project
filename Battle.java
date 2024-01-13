// The actual card based auto-battle

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Battle extends JPanel implements ActionListener {

    private Battler player; // player is made in the constructor
    private Battler enemy;
     

    private Battler playersArray[] = new Battler[2];

    private int round = 0; // player goes first in any battle
    private int turn = 5; // is either 0 or 1 to signify if it is the player or enemys turn to act
    private int altTurn; // the party that is not currently acting
    private boolean isWon = false;

    // would probably be more acurate to call these ticks rather than frames
    private int FPS = 60; // frames per second (use this to change speed cuz some animations are tied to
                          // stuff)
    private int framesPerTurn = 60;
    private int frameCounter = 0;
    private int framesForCardUp = 5; // how many frames for the card up animation

    private final int CARDY = 520; // the Y level the cards are drawn at
    private int cardUpY = CARDY; // intializing the card up offset

    static final int HEALTHBAR_Y = 100; // the Y level the healthbar is drawn at
    static final int HEALTHBAR_WIDTH = 250;
    static final int HEALTHBAR_HEIGHT = 24;

    private int damage = 0;
    private int shieldDamage = 0;
    private ArrayList<Integer> showDamage = new ArrayList<Integer>();
    private int xPos = 0; // for the damage messages
    // space for messages
    // private JLabel messageLabel;
    private JLabel instructionLabel;

    private JButton doubleSpeed;
    private boolean doubleTime = false;
    private int culler = 0; // when not in double speed, discards half of the timer events
    private Timer timer;

    // images
    private ImageIcon background = new ImageIcon("images/background.jpg");
    private ImageIcon backgroundOverlay = new ImageIcon("images/backgroundoverlay.png");
    // private ImageIcon vulnerableIcon = new
    // ImageIcon("images/VulnerableIcon.png");
    // private ImageIcon strenghtIcon = new ImageIcon("images/strenghtIcon.png");
    // private ImageIcon shieldIcon = new ImageIcon("images/shieldIcon.png");
    Cards[] originalHand; // this is soley here because I need to know where the cards where in the array so that I can move them back after battle

    // ------------------------------------------------------------------------------------------

    public Battle(Player player, Cards[] playerSelectedCards, int difficulty, Cards[] originalHand) {

        // put playerSelectedCards into player.hand
        for (int i = 0; i < playerSelectedCards.length; i++) {
            player.hand[i] = playerSelectedCards[i];
        }
        this.originalHand = originalHand;

        setLayout(new BorderLayout());

        JPanel topUIWrapper = new JPanel(new BorderLayout());
        topUIWrapper.setBackground(new Color(40,40,40,200));
        this.add(topUIWrapper, BorderLayout.NORTH);

        instructionLabel = new JLabel(
                "Automatically playing...");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setFont(FontFactory.loadFont("fonts/lexend/static/Lexend-Regular.ttf",16));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));
        topUIWrapper.add(instructionLabel, BorderLayout.WEST);

        doubleSpeed = new JButton(">>");
        // doubleSpeed.setHorizontalAlignment(SwingConstants.RIGHT);
        doubleSpeed.addActionListener(this);
        topUIWrapper.add(doubleSpeed, BorderLayout.EAST);

        // space for messages
        // messageLabel = new JLabel("");
        // this.add(messageLabel);
        // messageLabel.setBounds(580, 60, 300, 20);
        // messageLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // add(cardPanel, BorderLayout.CENTER);

        // get player and cards
        this.player = player;
        // this.playerSelectedCards = playerSelectedCards;

        // create player array
        playersArray[0] = player;
        
        enemy = new Enemy(difficulty);
        playersArray[1] = enemy;

        // setup timer
        timer = new Timer(500 / FPS, this);
        timer.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // enables antialiasing on the font which makes it look way better
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.drawImage(background.getImage(), 0, 0, null);
        g.drawImage(backgroundOverlay.getImage(), 0, 0, null);

        for (Battler battler : playersArray) {
            battler.drawSprite(g);
            battler.drawStatus(g);
        }

        // if (damage > 0) {
        // g.setColor(Color.red);
        // g.setFont(Main.Lexend18);
        // if (turn == 0)
        // g.drawString("-" + damage, 880, HEALTHBAR_Y+10);
        // else
        // g.drawString("-" + damage, 350, HEALTHBAR_Y+10);
        // }
        // System.out.println(showDamage);
        g.setColor(Color.red);
        g.setFont(Main.Lexend30);
        for (int i = 0; i < showDamage.size() - 1; i = i + 2) {
            if (turn == 0)
                xPos = 860;
            else
                xPos = 350;

            if (showDamage.get(i + 1) <= 100)
                g.drawString("-" + showDamage.get(i), xPos, 60 + showDamage.get(i + 1));

            showDamage.set(i + 1, showDamage.get(i + 1) - 1);

            if (showDamage.get(i + 1) == 0) {
                showDamage.remove(i);
                showDamage.remove(i);
            }
        }

        // display player's cards
        for (int i = player.hand.length - 1; i >= 0; i--) {
            if (player.hand[i] != null) {
                player.hand[i].setX(5 + i * 62);
                player.hand[i].setY(CARDY);
                
            // moves the currently acting card upwards 20px to make it more visible
            if (turn == 0 && i == (round - 1) / 2 % 8)
                player.hand[i].setY(cardUpY);

            player.hand[i].myDraw(g);
            }
            // drawCardInfo(g, player.hand[i]);
        }

        // display enemy's cards
        for (int i = DeckBuildPanel.deckSize - 1; i >= 0; i--) {
            if (enemy.hand[i] != null) {
                enemy.hand[i].setX(1140 + i * -62);
                enemy.hand[i].setY(CARDY);

                // System.out.println("round: " + round);

                // moves the currently acting card upwards 20px to make it more visible
                if (turn == 1 && i == ((round - 1) / 2) % 8)
                    enemy.hand[i].setY(cardUpY);

                enemy.hand[i].myDraw(g);
            }
            // drawCardInfo(g, enemy.hand[i]);
        }
    }

    private void performAttack(Cards attackerCard, Battler defender) {
        // gets ambrosia from card
        playersArray[turn].setEnergy(attackerCard.getEnergy());

        // checks if the character has enough ambrosia to use this card
        if (attackerCard.getEnergyCost() <= playersArray[turn].getEnergy()) {

            playersArray[turn].setEnergy(-1 * (attackerCard.getEnergyCost()));
            playersArray[turn].setShield(attackerCard.getShield() * 10);
            playersArray[altTurn].setVulnerableStacks(attackerCard.getVulnerableStacks());
            playersArray[turn].setStrengthenStacks(attackerCard.getStrengthenStacks());

            // deal damage -----------------------------------
            damage = (attackerCard.getAttack() + playersArray[turn].getStrengthenStacks()) * 10;

            if (playersArray[altTurn].getVulnerableStacks() > 0)
                damage = damage * 2;
            int orignalDamage = damage;
            if (damage > 0) {

                for (int i = 0; i < attackerCard.getMultiHit(); i++) {
                    damage = orignalDamage;

                    showDamage.add(damage);
                    showDamage.add(100 + (i * 10)); // decides how long the damage message persists for
                    // showDamage.add(shieldDamage);

                    // first deals damage to any shield
                    if (defender.getShield() >= damage) {
                        shieldDamage = damage;
                        damage = 0;
                    } else if (defender.getShield() < damage) {
                        shieldDamage = defender.getShield();
                        damage = damage - shieldDamage;
                        // defender.setShield(-shieldDamage);
                    }

                    defender.setShield(-shieldDamage);

                    defender.setHealth(defender.getHealth() - damage);
                    playersArray[turn].attackAnim(1);

                    // prevents health from going below 0
                    if (defender.getHealth() < 0)
                        defender.setHealth(0);
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == timer && !isWon) {
            if (!doubleTime) {
                culler++;
            }
            if (culler % 2 == 0) {
                frameCounter++;

            
                // first frame of turn
                if (frameCounter == 1) {
                    altTurn = (round + 1) % 2;
                    if (round % 2 == 0) {
                        turn = 0;
                        // messageLabel.setText("Player attacks");
                    } else {
                        turn = 1;
                        // messageLabel.setText("Enemy attacks");
                    }
                    round++;

                    // skips turn if you dont have a card in that slot
                    if (playersArray[turn].hand[(round - 1) / 2 % 8] == null)
                        frameCounter = framesPerTurn;
                }

                // frame 0 - 5
                if (frameCounter <= framesForCardUp)
                    // moves the acting card up
                    cardUpY = cardUpY - (150 / framesForCardUp);

                if (frameCounter == 20) {
                    performAttack(playersArray[turn].hand[(round - 1) / 2 % 8], playersArray[altTurn]);
                }

                if (frameCounter == 40)
                    player.attackAnimStop(1);

                player.increaseCounter();

                repaint();
                // frame 55 - 60
                if (frameCounter >= 55)
                    // moves the acting card back down
                    cardUpY = cardUpY + (150 / framesForCardUp);

                // frame 60
                if (frameCounter == framesPerTurn) {
                    frameCounter = 0;
                    cardUpY = CARDY;
                    // if round is even, it is the player's turn, if round is odd, its the enemy's
                    // turn. turn is 0 or 1 to make using an array easier

                    damage = 0;

                    reduceStacks(playersArray[turn]);
                    showDamage.clear();

                    if (playersArray[altTurn].getHealth() <= 0) {
                        System.out.println(playersArray[altTurn] + "loses!");
                        isWon = true;

                        

                        // resets the location of the cards so they appear in the right spot in the next
                        // battle
                        /*
                        for (int i = 0; i < player.hand.length; i++) {
                            if (player.hand[i] != null) {
                                player.hand[i].setX(i * 130 + 120);
                                player.hand[i].setY(260);
                            }
                        }
                        */

                        // resets the location of the cards so they appear in the right spot in the next
                        // battle
                        int tempCardx = 0;
                        int tempCounter = 0;
                        for (int i = 0; i < player.hand.length; i++) {
                            tempCardx = i * 130 + 120;
                            if (originalHand[i] != null) {
                                
                                for (int j = tempCounter; j < player.hand.length; j++) {
                                    tempCounter++;
                                    if (player.hand[j] != null) {
                                        
                                        player.hand[j].setX(tempCardx);
                                        player.hand[j].setY(160);
                                        break;
                                    }
                                }
                            }
                        }
                        
                        // give some extra cards as a reward
                        if (player.deck.size() <= 7) {
                            for (int i = 0; i < 3; i++) {
                                player.deck.add(new Cards(i * 120 + 200, 420, 60, 40));
                            }
                        } else {
                            for (int i = player.deck.size(); i < 10; i++) {
                                player.deck.add(new Cards(i * 120 + 200, 420, 60, 40));
                            }
                        }

                        // centers the cards in deck ---------------------
                        // gets the center of the screen
                        int deckX = (Main.WIDTH) / 2;

                        // adjusts the starting x of the cards accounting for each of the cards in the
                        // deck
                        for (int i = 0; i < player.deck.size(); i++)
                            deckX = deckX - 60;

                        // puts each card after one another
                        for (Cards card : player.deck) {
                            card.setX(deckX);
                            deckX += 120;
                        }

                        // reset the enemy ----------------------------
                        if (DeckBuildPanel.difficulty == 5) {
                            DeckBuildPanel.difficulty = 6;
                        }
                        else if (DeckBuildPanel.difficulty == 6) { 
                            DeckBuildPanel.difficulty = 8;
                        }
                        // resets health
                        //for (Battler battler : playersArray) {
                        //    battler.setMaxHealth(DeckBuildPanel.difficulty*100);
                        //    battler.setHealth(battler.getMaxHealth());
                        // }
                        player.setMaxHealth(DeckBuildPanel.difficulty*100);
                        player.setHealth(player.getMaxHealth());
                        player.clearStatus();
                        Enemy.maxHp = (DeckBuildPanel.difficulty*100);
                   
                        // the the player loses, they get sent to the menu screen, if they win, they get
                        // sent back to the map
                        if (playersArray[altTurn] == player)
                            Main.showCard("Menu");
                        else {
                            Main.showCard("Map");

                            // removes the enemy that we defeated
                            for (int i = 0; i < InteractiveEnemy.InteractiveEnemies.size(); i++) {
                                if (InteractiveEnemy.InteractiveEnemies.get(i).isInBattle())
                                    InteractiveEnemy.InteractiveEnemies.remove(i);
                            }  
                        }
                        InteractiveEnemy.setInBattle(false);
                        
                        // InteractiveEnemy.inBattle = false;
                    }
                }
            }
        } else if (e.getSource() == doubleSpeed) {
            if (!doubleTime) {
                doubleTime = true;
                culler = 0;
            } else {
                doubleTime = false;
            }
        }
    }

    private void reduceStacks(Battler target) {
        if (target.getVulnerableStacks() > 0) {
            target.setVulnerableStacks(-1);
        }
    }

}
