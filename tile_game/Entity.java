// This is the superclass of all moving entities on the map.
// By Alec

package tile_game;

import java.awt.image.BufferedImage;

public abstract class Entity {

    protected int x, y;
    protected int speed;

    protected BufferedImage[] up;
    protected BufferedImage[] down;
    protected BufferedImage[] left;
    protected BufferedImage[] right;
    protected String direction;

    protected int spriteCounter = 0;
    protected int spriteNum = 1;

    protected Hitbox hitbox;
}
