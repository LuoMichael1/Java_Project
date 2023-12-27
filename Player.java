import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class Player extends Battler {

    private ImageIcon SaberSprite = new ImageIcon("images/SaberSprite.png");    // just for fun
    private Scanner filesc;
    private String tempSpriteArray[] = new String[50];  // 50 just to be on the safe side
    private String tmpArray[];

    private int x = 100;
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
            // get card details from file
            filesc = new Scanner (new File("images/saber.txt"));
            
            while (filesc.hasNextLine()) {                                    
                tempSpriteArray[count] = (filesc.nextLine()); 
                count++;	
            }	
        }
        catch(Exception e) {
            System.out.print(e);
        }



    }

    public void myDraw(Graphics g) {

        for (int i=0; i < 2; i++) {
            System.out.println("uwu");
            tmpArray = tempSpriteArray[i].split(" ");

            int temp1 = Integer.parseInt(tmpArray[0]);
            int temp2 = Integer.parseInt(tmpArray[1]);
            int temp3 = Integer.parseInt(tmpArray[2]);
            int temp4 = Integer.parseInt(tmpArray[3]);

            System.out.println("uwu");
            g.clipRect(x+temp1, y+temp2, temp3, temp4);
            g.drawImage(SaberSprite.getImage(), x, y, null);
            g.setClip(null);
        }
    }

}
