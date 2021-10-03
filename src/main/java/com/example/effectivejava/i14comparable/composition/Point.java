package com.example.effectivejava.i14comparable.composition;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        Point p = (Point) obj;
        return p.x == x && p.y == y;
    }

    @Override
    public int compareTo(Point o) {
        return (x * x + y * y) - (o.x * o.x + o.y * o.y);
    }

    @Override
    public String toString() {
        return String.format("Point[%d, %d]", x, y);
    }
}
