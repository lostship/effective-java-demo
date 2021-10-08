package com.example.effectivejava.ch3commonmethod.i10equals.transitivity.better1;

public class Point {
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
}
