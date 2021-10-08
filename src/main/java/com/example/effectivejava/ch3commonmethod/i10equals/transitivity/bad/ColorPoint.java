package com.example.effectivejava.ch3commonmethod.i10equals.transitivity.bad;

import java.awt.Color;
import java.util.Objects;

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

        if (!(obj instanceof Point)) {
            return false;
        }

        if (!(obj instanceof ColorPoint)) {
            return obj.equals(this);
        }

        return super.equals(obj)
                && Objects.equals(((ColorPoint) obj).color, color);
    }

    public static void main(String[] args) {
        test1();
        test2();
    }

    /**
     * 违反等价关系的传递性
     */
    private static void test1() {
        ColorPoint p1 = new ColorPoint(1, 1, Color.RED);
        Point p2 = new Point(1, 1);
        ColorPoint p3 = new ColorPoint(1, 1, Color.BLUE);
        System.out.println(p1.equals(p2));
        System.out.println(p2.equals(p3));
        System.out.println(p1.equals(p3));
    }

    /**
     * 当Point有多个子类时，甚至可能造成死循环
     */
    private static void test2() {
        ColorPoint p1 = new ColorPoint(1, 1, Color.RED);
        SmellPoint p2 = new SmellPoint(1, 1, "baking");
        System.out.println(p1.equals(p2));
    }
}
