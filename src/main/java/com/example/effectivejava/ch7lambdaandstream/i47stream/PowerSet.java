package com.example.effectivejava.ch7lambdaandstream.i47stream;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Returns the power set of an input set as custom collection
 * 返回给定集合的幂集
 * 可以用幂集中每个元素的索引作为位向量，在索引中排第n位，表示源集合中第n位元素存在或者不存在。
 *
 * 详细解释：
 * 对于n个元素的集合，它的幂集有2<sup>n</sup>个。
 * 将幂集元素映射到下标0至2<sup>n</sup>-1，转换成二进制后2<sup>n</sup>-1总共n位，和元素数量相等。
 * 所以可以用每个下标二进制的非0位，来表示下标对应子集包含的元素。
 * 这样处理大集合时，可以巧妙地节省内存空间。
 * 
 * 客户端可能需要对返回结果进行迭代或者当做Stream处理，尽量兼顾两者。
 * 1.如果可以返回集合，就返回集合。
 * - 如果集合中已经有元素，或者序列中的元素数量很少，足以创建一个新的集合，就返回一个标准的集合。
 * - 否则就要考虑实现一个定制的集合，比如幂集这种情况。
 * 2.如果无法返回集合，就返回Stream或者Iterable，感觉哪一种更自然即可。
 */
public class PowerSet {
    public static void main(String[] args) {
        Collection<Set<Integer>> sets = PowerSet.of(Set.of(1, 2, 3));
        for (Set<Integer> set : sets) {
            System.out.println(set);
        }
    }

    public static final <E> Collection<Set<E>> of(Set<E> s) {
        // 注意这里先进行保护性拷贝，再对拷贝之后的对象进行参数的有效性检查
        // 避免原始对象在参数检查到拷贝期间被其它线程改变（TOCTOU攻击，Time-Of-Check/Time-Of-Use）
        List<E> src = new ArrayList<>(s);
        if (src.size() > 30) {
            // Collection的size最大只能支持Integer.MAX_VALUE = 2 power 31 - 1
            // 这正是用Collection而不是Stream或Iterable作为返回类型的缺点
            throw new IllegalArgumentException("Set too big " + s);
        }

        return new AbstractList<Set<E>>() {
            @Override
            public int size() {
                return 1 << src.size(); // 2 to power srcSize
            }

            @Override
            public boolean contains(Object o) {
                return o instanceof Set && src.containsAll((Set<?>) o);
            }

            @Override
            public Set<E> get(int index) {
                Set<E> result = new HashSet<>();
                for (int i = 0; index > 0; i++, index >>= 1) {
                    if ((index & 1) == 1) {
                        result.add(src.get(i));
                    }
                }
                return result;
            }
        };
    }
}
