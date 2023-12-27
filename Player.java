import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class Player extends Battler {

    private ImageIcon playerSprite = new ImageIcon("images/player2.png");    // just for fun
    private Scanner filesc;
    private String SpriteArray[] = new String[50];  // 50 just to be on the safe side
    private String SpriteAnimArray[] = new String[50];  // 50 just to be on the safe side
    private String tmpArray[];
    private String tmpArray2[];

    private int x = 130;
    private int y = 120;
    private int count = 0;


    public Player() {
        
        super("Player");
        createSprite();
    }

    // creating a sprite
    public void createSprite() {
        // read the file to get how to divide the sprite map
        try {
            // get card details from file, first 4 numbers in each line are how to cut, last two are where to place
            filesc = new Scanner (new File("images/spriteCut.txt"));
            
            while (filesc.hasNextLine()) {                                    
                SpriteArray[count] = (filesc.nextLine()); 
                count++;	
            }	
            // get how to animate the images
            filesc = new Scanner (new File("images/spirteAnim1.txt"));
            count=0;
            while (filesc.hasNextLine()) {                                    
                SpriteAnimArray[count] = (filesc.nextLine()); 
                count++;	
            }	
        }
        catch(Exception e) {
            System.out.print(e);
        }



    }

    public void myDraw(Graphics g) {

        for (int i=0; i < 4; i++) {
            //System.out.println("uwu");
            tmpArray = SpriteArray[i].split(" ");
            tmpArray2 = SpriteAnimArray[i].split(" ");

            int posX = Integer.parseInt(tmpArray[0]);
            int posY = Integer.parseInt(tmpArray[1]);
            int iwidth = Integer.parseInt(tmpArray[2]);
            int iheight = Integer.parseInt(tmpArray[3]);
            int offsetX = Integer.parseInt(tmpArray[4]);
            int offsetY = Integer.parseInt(tmpArray[5]);

            int isVisible = Integer.parseInt(tmpArray2[1]);
            //System.out.println("uwu");
            if (isVisible == 1) {
                g.clipRect(x+posX+offsetX, y+posY+offsetY, iwidth, iheight);
                g.drawImage(playerSprite.getImage(), x+offsetX, y+offsetY, null);
                g.setClip(null);
            }
        }
    }


    public void attackAnim() {
        
    }

}
