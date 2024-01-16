package tile_game;

public class Hitbox {

    private int top, left, width, height, bot, right, centerX, centerY;

    public Hitbox(int top, int left, int width, int height) {

        update(top, left, width, height);
    }

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
