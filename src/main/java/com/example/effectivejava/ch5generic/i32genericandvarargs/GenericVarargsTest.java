package com.example.effectivejava.ch5generic.i32genericandvarargs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 谨慎同时使用泛型和可变参数数组
 * 
 * 泛型可变参数在实践中用处很大，因此设计者选择容忍它和不允许实例化泛型数组规则的矛盾。但是开发者要确保类型安全性。
 *
 * 泛型可变参数方法在下列条件下是安全的：
 * 1.方法没有在泛型可变参数数组中保存任何值；
 * 2.方法没有对不被信任的代码开放该数组（有两个例外，将数组传递给另一个@SafeVarargs注解的可变参数方法，将数组传给只计算数组内容部分函数的非可变参数方法）。
 * 以上两个条件只要有任何一条被破坏，就要立即修正它。
 * 
 * SafeVarargs注解只能用在不可被重写的方法上（static或final或private等），因为它不能确保重写的方法也是安全的。
 */
public class GenericVarargsTest {
    /* 以下为正例 */

    /**
     * 安全使用泛型可变参数的示例
     */
    @SafeVarargs
    public static <T> List<T> flatten(List<? extends T>... lists) {
        List<T> result = new ArrayList<>();
        for (List<? extends T> list : lists) {
            result.addAll(list);
        }
        return result;
    }

    /**
     * 使用集合代替可变参数数组的实现方式，优点是编译器能够确保类型安全性，缺点是客户端调用比较繁琐，运行速度相对慢一些。
     * {@code flatten(List.of(l1, l2, l3...))}
     * 
     * 这种方式也适用于无法编写出安全的泛型可变参数方法的情况。比如{@code dangerousToArray}就可以改为一个返回集合的方法。
     */
    public static <T> List<T> flatten(List<List<? extends T>> lists) {
        List<T> result = new ArrayList<>();
        for (List<? extends T> list : lists) {
            result.addAll(list);
        }
        return result;
    }

    /* 以下为反例 */

    public static <T> void dangerous1(T... ta) {
        System.out.println(ta.getClass());
        Object[] oa = ta;
        oa[0] = new Object(); // ArrayStoreException，数组的实际类型是由传到方法的参数的编译时类型决定的
    }

    public static void dangerous2(List<String>... lists) {
        Object[] objs = lists;
        objs[0] = List.of(1, 2, 3);
        System.out.println(lists[0].get(0)); // ClassCastException
    }

    /**
     * 看似安全，实际上会将堆污染传递到调用堆栈上。{@link #pickTwo(Object, Object, Object)}
     */
    public static <T> T[] dangerousToArray(T... ta) {
        System.out.println(ta.getClass());
        return ta;
    }

    /**
     * 这个方法本身没有危险，但是它调用了带有泛型可变参数的方法。
     * 这种情况下，因为泛型擦除，dangerousToArray返回的将是一个Object[]，客户单在使用String[]接收返回值时，编译器插入的隐藏类型转换将失败。
     */
    public static <T> T[] pickTwo(T a, T b, T c) {
        switch (ThreadLocalRandom.current().nextInt(3)) {
            case 0:
                return dangerousToArray(a, b);
            case 1:
                return dangerousToArray(b, c);
            case 2:
                return dangerousToArray(c, a);
            default:
                throw new AssertionError();
        }
    }

    public static void testDangerousToArray() {
        String[] strs1 = dangerousToArray("1", "2"); // 运行正常
        String[] strs2 = pickTwo("1", "2", "3"); // ClassCastException
    }
}
