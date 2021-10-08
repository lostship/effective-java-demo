package com.example.effectivejava.ch7lambdaandstream.i47stream;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 生成一个给定列表的所有<strong>相邻的</strong>子列表
 */
public class ContiguousSubList {
    public static void main(String[] args) {
        List<Integer> src = List.of(1, 2, 3);

        // 迭代实现（没有打印空集）
        for (int start = 0; start < src.size(); start++) {
            for (int end = start + 1; end <= src.size(); end++) {
                System.out.println(src.subList(start, end));
            }
        }

        // Stream实现
        ContiguousSubList.of(src).forEach(System.out::println);
    }

    public static <E> Stream<List<E>> of(List<E> list) {
        return Stream.concat(Stream.of(Collections.emptyList()),
                prefixes(list).flatMap(ContiguousSubList::suffixes));
    }

    private static <E> Stream<List<E>> prefixes(List<E> list) {
        return IntStream.rangeClosed(1, list.size())
                .mapToObj(end -> list.subList(0, end));
    }

    private static <E> Stream<List<E>> suffixes(List<E> list) {
        return IntStream.range(0, list.size())
                .mapToObj(start -> list.subList(start, list.size()));
    }
}
