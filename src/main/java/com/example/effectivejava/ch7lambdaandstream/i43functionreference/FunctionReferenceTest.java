package com.example.effectivejava.ch7lambdaandstream.i43functionreference;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * 方法引用
 */
public class FunctionReferenceTest {
    public static void main(String[] args) {
        // 静态方法引用
        Function<String, Integer> f1 = Integer::parseInt;
        f1 = str -> Integer.parseInt(str);
        System.out.println(f1.apply("1"));

        // 有限定（bound）的实例方法引用
        // 接收对象在方法引用中指定（LocalDate.now()）
        Predicate<LocalDate> f2 = LocalDate.now()::isEqual;
        f2 = d -> LocalDate.now().isEqual(d);
        System.out.println(f2.test(LocalDate.now()));

        // 无限定（unbound）的实例方法引用
        // 接收对象在运行函数对象时，通过在该方法的声明函数前面额外添加一个参数来指定
        UnaryOperator<String> f3 = String::toUpperCase;
        f3 = str -> str.toUpperCase();
        System.out.println(f3.apply("constant"));

        // 类构造器
        Supplier<Set<String>> f4 = HashSet<String>::new;
        f4 = () -> new HashSet<>();
        System.out.println(f4.get());

        // 数组构造器
        IntFunction<int[]> f5 = int[]::new;
        f5 = len -> new int[len];
        System.out.println(f5.apply(5));
    }
}
