package com.example.effectivejava.ch3commonmethod.i14comparable;

import java.util.Comparator;

public class ValueCompareTest {
    /**
     * 错误：
     * 千万不要这样比较两个数值，因为如果o1.hashCode是一个正数，
     * o2.hashCode是一个负数，最终结果很可能造成整数溢出，返回错误的结果。
     */
    static Comparator<Object> badOrder = new Comparator<>() {
        @Override
        public int compare(Object o1, Object o2) {
            return o1.hashCode() - o2.hashCode();
        }
    };

    /**
     * 正确方式1：
     * 应该使用静态方法Integer.compare
     */
    static Comparator<Object> hashOrder1 = new Comparator<>() {
        @Override
        public int compare(Object o1, Object o2) {
            return Integer.compare(o1.hashCode(), o2.hashCode());
        }
    };

    /**
     * 正确方式2：
     * 或者使用Comparator构造方法
     */
    static Comparator<Object> hashOrder2 = Comparator.comparingInt(Object::hashCode);
}
