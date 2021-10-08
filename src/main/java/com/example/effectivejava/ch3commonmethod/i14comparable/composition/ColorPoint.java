package com.example.effectivejava.ch3commonmethod.i14comparable.composition;

import java.awt.Color;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class ColorPoint implements Comparable<ColorPoint> {
    private final Point point;
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        point = new Point(x, y);
        this.color = color;
    }

    /**
     * 返回point视图（view）
     */
    public Point asPoint() {
        return point;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ColorPoint)) {
            return false;
        }

        ColorPoint cp = (ColorPoint) obj;
        return cp.point.equals(point) && cp.color.equals(color);
    }

    @Override
    public String toString() {
        return String.format("ColorPoint[%s, %s]", point, Integer.toHexString(color.getRGB()));
    }

    @Override
    public int compareTo(ColorPoint o) {
        int result = point.compareTo(o.point);

        if (result == 0) {
            result = Integer.compare(color.getRGB(), o.color.getRGB());
        }

        return result;
    }

    /**
     * 与i10equals中介绍的一样，这是面向对象语言中关于等价关系的一个基本问题：
     * 我们无法在扩展可实例化的类的同时，既增加新的值组件，同时又保留实现Comparable接口的compareTo约定，除非愿意放弃面向对象的抽象所带来的优势。
     */
    public static void main(String[] args) {
        ColorPoint p1 = new ColorPoint(1, 1, Color.RED);
        ColorPoint p2 = new ColorPoint(1, 2, Color.RED);
        ColorPoint p3 = new ColorPoint(1, 2, Color.BLUE);

        Set<ColorPoint> set = new TreeSet<>();
        Collections.addAll(set, p1, p2, p3);

        System.out.println(set);
    }
}
