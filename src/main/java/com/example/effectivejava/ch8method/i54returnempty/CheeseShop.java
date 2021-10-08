package com.example.effectivejava.ch8method.i54returnempty;

import java.util.ArrayList;
import java.util.List;

public class CheeseShop {
    public enum Cheese {
        STILTON,
        PARMESAN
    }

    private final List<Cheese> cheesesInStock = new ArrayList<>();

    /**
     * 正确返回集合的方式，包含返回空集合的可能
     * 
     * 慎重优化：
     * 不要在这里纠结相比于返回null，这样是否增加创建空集合或者数组的性能开销。
     * 在这个级别上担心性能问题是不明智的，除非分析表明这个方法正是造成性能问题的真正源头，
     * 任何优化都需要经过测试，对比前后性能差异，才能证明真的有价值引入这个优化。
     */
    public List<Cheese> getCheeses() {
        return new ArrayList<>(cheesesInStock);
    }

    /**
     * 不要返回null，增加了客户端的处理负担
     */
    public List<Cheese> badGetCheeses() {
        return cheesesInStock.isEmpty() ? null : new ArrayList<>(cheesesInStock);
    }

    /**
     * 正确返回数组的方式，包含返回空数组的可能
     */
    public Cheese[] toArray() {
        return cheesesInStock.toArray(new Cheese[0]);
    }

    public Cheese[] badToArray1() {
        return cheesesInStock.isEmpty() ? null : cheesesInStock.toArray(new Cheese[0]);
    }

    /**
     * TODO 遗留问题：
     * 不要通过预先分配传入toArray的数组来提升性能，研究表明这样会适得其反
     */
    public Cheese[] badToArray2() {
        // Don't do this - preallocating the array harms performance!
        return cheesesInStock.toArray(new Cheese[cheesesInStock.size()]);
    }

    public static void main(String[] args) {
        CheeseShop shop = new CheeseShop();

        // 客户端必须额外处理null，非常繁琐
        // 如果漏写就可能运行出错，而且可能运行很久遇到null时才能发现
        List<Cheese> cheeses = shop.badGetCheeses();
        if (cheeses != null) {
            if (cheeses.contains(Cheese.STILTON)) {
                // omit
            }
        }
    }
}
