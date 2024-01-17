// This class represents a point containing an x and y coordinate with helpful variables / methods. 
// By Alec

package tile_game;

class Point {

    int x, y;
    int f, g, h;
    Point parent;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        Point point = (Point) o;
        return x == point.x && y == point.y;
    }
}