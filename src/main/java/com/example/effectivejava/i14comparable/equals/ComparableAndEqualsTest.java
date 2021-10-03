package com.example.effectivejava.i14comparable.equals;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class ComparableAndEqualsTest {
    /**
     * 约定并没有强制compareTo方法的顺序关系与equals方法一致，
     * 但是需要了解它们不一致时可能产生的问题。
     * 
     * 例如BigDecimal的compareTo方法和equals方法不一致。
     * HashSet使用equals方法进行等同性测试，而TreeSet使用compareTo方法进行等同性测试。
     * 因此如下示例中，HashSet中最终会有两个元素，而TreeSet中只有一个元素。
     */
    public static void main(String[] args) {
        BigDecimal d1 = new BigDecimal("1.0");
        BigDecimal d2 = new BigDecimal("1.00");
        System.out.printf("%s equals %s: %b\n", d1, d2, d1.equals(d2));
        System.out.printf("%s compareTo %s: %d\n", d1, d2, d1.compareTo(d2));

        Set<BigDecimal> hashSet = new HashSet<>();
        Collections.addAll(hashSet, d1, d2);
        System.out.println("HashSet(use equals): " + hashSet);

        Set<BigDecimal> treeSet = new TreeSet<>();
        Collections.addAll(treeSet, d1, d2);
        System.out.println("TreeSet(use compareTo): " + treeSet);
    }
}
