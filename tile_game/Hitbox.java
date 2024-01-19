// This class represents hitboxes as rectangles and contains all associated methods and attributes.
// By Alec

package tile_game;

public class Hitbox {

    // CenterX and centerY represent the center coordinates of each hitbox. This is
    // to ensure accuracy when evaluating what tile a hitbox is on.
    private int top, left, width, height, bot, right, centerX, centerY;

    public Hitbox(int top, int left, int width, int height) {

        update(top, left, width, height);
    }

    // Update hitbox with updated values
    public void update(int top, int left, int width, int height) {

        this.top = top;
        this.left = left;
        this.width = width;
        this.height = height;

        bot = top + height;
        right = left + width;
        centerX = left + width / 2;
        centerY = top + height / 2;
    }

    // Return true if two rectangular hitboxes intersect
    public boolean intersects(Hitbox other) {

        if (other.bot < top || other.top > bot) {

            return false;
        } else if (other.left > right || other.right < left) {

            return false;
        }
        return true;
    }

    public int getTop() {

        return top;
    }

    public int getLeft() {

        return left;
    }

    public int getWidth() {

        return width;
    }

    public int getHeight() {

        return height;
    }

    public int getBot() {

        return bot;
    }

    public int getRight() {

        return right;
    }

    public int getCenterX() {

        return centerX;
    }

    public int getCenterY() {

        return centerY;
    }
}
