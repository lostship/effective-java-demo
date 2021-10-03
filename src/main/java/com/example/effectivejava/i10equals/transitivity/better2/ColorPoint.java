package com.example.effectivejava.i10equals.transitivity.better2;

import java.awt.Color;
import java.util.Set;

public class ColorPoint {
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

    /**
     * 这是面向对象语言中关于等价关系的一个基本问题。
     * 我们无法在扩展可实例化的类的同时，既增加新的值组件，同时又保留equals约定，除非愿意放弃面向对象的抽象所带来的优势。
     * （这个问题同样存在于实现Comparable接口的compareTo约定）
     * 
     * 虽然没有一种令人满意的办法可以既扩展不可实例化的类，又增加值组件，但还是有一种不错的权宜之计：
     * 遵从第18条“组合优先于继承”的建议。
     * 我们不再让ColorPoint扩展Point，而是在ColorPoint中加入一个私有的Point域以及一个公有的视图（view）方法（详见第6条），此方法返回一个与该有色点处在相同位置的普通Point对象。
     * 
     * 这些问题只会出现在超类是可实例化的情况下，如果扩展的是不可实例化的超类（abstract）就不会有这些违反equals约定的问题。
     */
    public static void main(String[] args) {
        ColorPoint p1 = new ColorPoint(1, 1, Color.RED);
        Point p2 = new Point(1, 1);
        ColorPoint p3 = new ColorPoint(1, 1, Color.RED);
        System.out.println(p1.equals(p2));
        System.out.println(p2.equals(p3));
        System.out.println(p1.equals(p3));

        Set<Point> points = Set.of(p2);
        System.out.println(points.contains(p1.asPoint()));
    }
}
