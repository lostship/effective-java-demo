package com.example.effectivejava.ch5generic.i28genericlistorarray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntFunction;

/**
 * 泛型 - 列表和数组
 * 
 * 数组是协变的（convariant，如果Sub是Super的子类，Sub[]就是Super[]的子类），可具体化的（reifiable）；
 * 泛型是不可变的（invariant），不可具体化的（non-reifiable，运行时会被擦除，运行时表示法包含的信息比编译时表示法包含的信息更少）。
 * 
 * 因此数组和泛型通常不能很好地混合使用，如果混合使用时得到编译错误或警告，第一反应就应该是使用列表代替数组。
 */
public class ArrayCollection {

    public static class ChooserGenericArrayImpl<E> {
        private final E[] choiceArray;
        private static final int DEFAULT_INITIAL_CAPACITY = 16;

        /**
         * 在编写用数组支持的泛型时，都会遇到数组实例化的问题。一般有两种方法解决：
         * 
         * 1. 创建一个Object数组，强转成泛型数组。
         * 采用这种方式需要自己确保unchecked的转化那不会危机程序的类型安全性，
         * 数组必须保存在一个私有域中，永远不会返回到客户端
         * （否则客户端用Object[]接收数组后，就能向其中存放任何类型的数据，因为数组的componentType本身确实是Object类型的）。
         * 
         * 2. 将数组类型从E[]改为Object[]，{@link ChooserObjectArrayImpl}。
         * 
         * 第一种方法可读性更强，而且不需要在每次读取数组元素时都进行转换，因此第一种方法优先。
         * 但是第一种方法会导致堆污染（heap pollution），详见i32：数组的运行时类型与它的编译时类型不匹配（除非E正好是Object）。
         * 虽然堆污染在这种情况下并没有什么危害。
         */
        @SuppressWarnings("unchecked")
        public ChooserGenericArrayImpl(Collection<? extends E> choices) {
            choiceArray = (E[]) choices.toArray(); // 此处的强转是假象，泛型擦除后T[]就是Object[]
        }

        /**
         * 实例化数组不是很方便，需要客户端传递额外的构造函数
         */
        public ChooserGenericArrayImpl(IntFunction<E[]> constr) {
            choiceArray = constr.apply(DEFAULT_INITIAL_CAPACITY);
        }

        public E choose() {
            Random rnd = ThreadLocalRandom.current();
            return choiceArray[rnd.nextInt(choiceArray.length)];
        }
    }

    /**
     * 错误的实现，在类型参数有限定时，类型变量擦除后会替换成第一个限定类型，
     * 本例中构造方法实际上相当于将Object[]强转成Comparable[]，因此运行时会报ClassCastException。
     * 这种情况只能将E[]改为Object[]，或者使用列表代替数组。
     */
    public static class FailChooser<E extends Comparable<? super E>> {
        private final E[] choiceArray;

        /**
         * 运行时报错，泛型擦除后无法将Object[]转换成Comparable[]
         */
        public FailChooser(Collection<? extends E> choices) {
            @SuppressWarnings("unchecked")
            E[] ta = (E[]) choices.toArray();
            choiceArray = ta;
        }

        public E choose() {
            Random rnd = ThreadLocalRandom.current();
            return choiceArray[rnd.nextInt(choiceArray.length)];
        }
    }

    /**
     * 采用Object[]数组的实现方式
     */
    public static class ChooserObjectArrayImpl<E> {
        private final Object[] choiceArray;
        private static final int DEFAULT_INITIAL_CAPACITY = 16;

        public ChooserObjectArrayImpl(Collection<? extends E> choices) {
            choiceArray = choices.toArray();
        }

        public ChooserObjectArrayImpl() {
            choiceArray = new Object[DEFAULT_INITIAL_CAPACITY];
        }

        public E choose() {
            Random rnd = ThreadLocalRandom.current();
            @SuppressWarnings("unchecked")
            E result = (E) choiceArray[rnd.nextInt(choiceArray.length)];
            return result;
        }
    }

    /**
     * 使用List代替数组的实现，确保不会有ClassCastException
     */
    public static class ChooserListImpl<E> {
        private final List<E> choiceList;
        private static final int DEFAULT_INITIAL_CAPACITY = 16;

        public ChooserListImpl(Collection<? extends E> choices) {
            choiceList = new ArrayList<>(choices);
        }

        public ChooserListImpl() {
            choiceList = new ArrayList<>(DEFAULT_INITIAL_CAPACITY);
        }

        public E choose() {
            Random rnd = ThreadLocalRandom.current();
            return choiceList.get(rnd.nextInt(choiceList.size()));
        }
    }

}
