// This is the superclass of all moving entities on the map.
// By Alec

package tile_game;

import java.awt.image.BufferedImage;

public abstract class Entity {

    protected int x, y;
    protected int speed;

    // Store the sprite images
    protected BufferedImage[] up;
    protected BufferedImage[] down;
    protected BufferedImage[] left;
    protected BufferedImage[] right;

    // Change the sprite based on thee variables
    protected String direction;
    protected int spriteCounter = 0;
    protected int spriteNum = 1;

    // Hitbox to check collision
    protected Hitbox hitbox;
}
