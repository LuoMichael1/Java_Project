import java.awt.image.BufferedImage;

public class Entity {

    public int x, y;
    public int speed;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage up3, up4, down3, down4, left3, left4, right3, right4;
    public BufferedImage[] up;
    public BufferedImage[] down;
    public BufferedImage[] left;
    public BufferedImage[] right;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Hitbox hitbox;
}
