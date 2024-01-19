// This class represents a point containing an x and y coordinate with helpful variables / methods. 
// By Alec

package tile_game;

class Point {

    int x, y; // Represent posision
    int f, g, h; // Represent distance cost values for pathfinding
    Point parent; // Store parent node for pathfinding

    Point(int x, int y) {

        this.x = x;
        this.y = y;
    }

    // Check if location of two points is equal
    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        Point point = (Point) o;
        return x == point.x && y == point.y;
    }
}