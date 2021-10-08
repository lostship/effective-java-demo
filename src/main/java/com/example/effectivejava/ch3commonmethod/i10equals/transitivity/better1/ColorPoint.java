package com.example.effectivejava.ch3commonmethod.i10equals.transitivity.better1;

import java.awt.Color;
import java.util.Objects;
import java.util.Set;

public class ColorPoint extends Point {
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        return super.equals(obj)
                && Objects.equals(((ColorPoint) obj).color, color);
    }

    /**
     * 虽然通过getClass代替instanceof，确保了equals的约定，但是破坏了里式替换原则（Listov）
     */
    public static void main(String[] args) {
        ColorPoint p1 = new ColorPoint(1, 1, Color.RED);
        Point p2 = new Point(1, 1);
        ColorPoint p3 = new ColorPoint(1, 1, Color.RED);
        System.out.println(p1.equals(p2));
        System.out.println(p2.equals(p3));
        System.out.println(p1.equals(p3));

        Set<Point> points = Set.of(p2);
        System.out.println(points.contains(p1));
    }
}
