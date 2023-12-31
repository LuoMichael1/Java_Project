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
    private boolean attackAnimtest = false;

    private int x = 160;
    private int y = 150;
    private int lineCounter = 0;

    private int animXOffSet = 0;
    private int animYOffSet = 0;
    //private int counter = 0;

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
                SpriteArray[lineCounter] = (filesc.nextLine()); 
                lineCounter++;	
            }	
            // get how to animate the images
            filesc = new Scanner (new File("images/spirteAnim1.txt"));
            lineCounter=0;
            while (filesc.hasNextLine()) {                                    
                SpriteAnimArray[lineCounter] = (filesc.nextLine()); 
                lineCounter++;	
            }	
        }
        catch(Exception e) {
            System.out.print(e);
        }
    }

    public void myDraw(Graphics g) {

        //animXOffSet = counter%10;
        //animYOffSet = (counter/2)%5;
        tmpArray2 = SpriteAnimArray[(super.getCounter()/2)%(lineCounter-1)].split(" ");

        

        // iterate through each part of the sprite
        for (int i=0; i < 4; i++) {
            //System.out.println("uwu");
            tmpArray = SpriteArray[i].split(" ");
            

            int posX = Integer.parseInt(tmpArray[0]);
            int posY = Integer.parseInt(tmpArray[1]);
            int iwidth = Integer.parseInt(tmpArray[2]);
            int iheight = Integer.parseInt(tmpArray[3]);
            int offsetX = Integer.parseInt(tmpArray[4]);
            int offsetY = Integer.parseInt(tmpArray[5]);

            // animation file
            int isVisible = Integer.parseInt(tmpArray2[i*3]);
            animXOffSet = Integer.parseInt(tmpArray2[1 + (i*3)]);
            animYOffSet = Integer.parseInt(tmpArray2[2 + (i*3)]);


            //int isVisible = Integer.parseInt(tmpArray2[1]);
            //System.out.println("uwu");
            if (isVisible == 1 || attackAnimtest) {
                g.clipRect(x+posX+offsetX+animXOffSet, y+posY+offsetY+animYOffSet, iwidth, iheight);
                g.drawImage(playerSprite.getImage(), x+offsetX+animXOffSet, y+offsetY+animYOffSet, null);
                g.setClip(null);
            }
        }
    }


    public void attackAnim(int frame) {
        attackAnimtest = true;
    }
    public void attackAnimStop(int frame) {
        attackAnimtest = false;
    }

}
