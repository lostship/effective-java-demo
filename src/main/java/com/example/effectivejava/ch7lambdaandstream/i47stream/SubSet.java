package com.example.effectivejava.ch7lambdaandstream.i47stream;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 子集遍历
 */
public class SubSet {
    public static void main(String[] args) {
        List<Integer> elements = List.of(1, 2, 3);
        List<List<Integer>> powerSet = powerSets(elements);
        powerSet.forEach(System.out::println);
    }

    /**
     * 幂集 - 给定集合的所有可能子集
     * 
     * @param <E>      元素类型
     * @param elements 给定集合
     * @return 所有可能子集列表
     */
    public static <E> List<List<E>> powerSets(List<E> elements) {
        List<List<E>> result = new ArrayList<>();
        pick(elements, result, new LinkedList<>(), 0);
        return result;
    }

    /**
     * 幂集 - 给定集合的所有可能子集
     * 
     * @param <E>      元素类型
     * @param elements 给定集合
     * @param result   最终结果，所有可能子集列表
     * @param temp     迭代中选取元素临时列表
     * @param index    迭代元素下标
     */
    private static <E> void pick(final List<E> elements,
            final List<List<E>> result,
            final LinkedList<E> temp,
            final int index) {
        result.add(new ArrayList<>(temp));

        for (int i = index; i < elements.size(); i++) {
            temp.add(elements.get(i));
            pick(elements, result, temp, i + 1);
            temp.removeLast();
        }
    }

    /**
     * 给定集合长度为n的所有可能子集
     * 
     * @param <E>      元素类型
     * @param elements 给定集合
     * @param n        子集元素个数
     * @return 所有可能子集列表
     */
    public static <E> List<List<E>> subSets(final List<E> elements, final int n) {
        List<List<E>> result = new ArrayList<>();
        pick(elements, n, result, new LinkedList<>(), 0);
        return result;
    }

    /**
     * 给定集合长度为n的所有可能子集
     * 
     * @param <E>      元素类型
     * @param elements 给定集合
     * @param n        子集元素个数
     * @param result   最终结果，所有可能子集列表
     * @param temp     迭代中选取元素临时列表
     * @param index    迭代元素下标
     */
    private static <E> void pick(final List<E> elements,
            final int n,
            final List<List<E>> result,
            final LinkedList<E> temp,
            final int index) {
        if (temp.size() == n) {
            result.add(new ArrayList<>(temp));
            return;
        }

        if (index == elements.size()) {
            return; // index限制正确（max）不会调用到
        }

        for (int i = index, max = elements.size() - (n - temp.size()); i <= max; i++) {
            temp.add(elements.get(i));
            pick(elements, n, result, temp, i + 1);
            temp.removeLast();
        }
    }
}
