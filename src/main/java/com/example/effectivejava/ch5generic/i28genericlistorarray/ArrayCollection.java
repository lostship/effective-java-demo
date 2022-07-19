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
 * 泛型是不变的（invariant，List<Sub>不是List<Super>的子类），
 * 不可具体化的（non-reifiable，运行时会被擦除，运行时表示法包含的信息比编译时表示法包含的信息更少）。
 * 
 * 因此数组和泛型通常不能很好地混合使用，如果混合使用时得到编译错误或警告，第一反应就应该是使用列表代替数组。
 */
public class ArrayCollection {
    
    /**
     * 泛型是invariant的，否则在运行时会产生类型转换错误
     */
    public void invariant() {
        // Won't compile!
        // List<Object> ol = new ArrayList<Long>(); // Incompatible types
        // ol.add("I don't fit in");
    }
    
    /**
     * 为什么创建泛型数组是非法的？因为它不是类型安全的。
     * 要是它合法，编译器在其他正确的程序中发生的转换就会在运行时失败，
     * 并出现ClassCastException异常。这就违背了泛型系统提供的基本保证。
     * 
     * 假设第1行是合法的，它创建了一个泛型数组。
     * 第2行创建并初始化了一个包含单个元素的List<Integer>。
     * 第3行将List<String>数组保存到一个Object数组变量中，这是合法的，因为数组是协变的。
     * 第4行将List<Integer>保存到Object 数组里唯一的元素中，这是可以的，因为泛型是通过擦除实现的：
     * List<Integer>实例的运行时类型只是List，List<String>[]实例的运行时类型则是List[]，
     * 因此这种安排不会产生 ArrayStoreException异常。
     * 但现在有麻烦了，我们将一个List<Integer>实例保存到了原本声明只包含List<String>实例的数组中。
     * 在第5行中，我们从这个数组里唯一的列表中获取了唯一的元素，编译器自动地将获取到的元素转换成String，
     * 但它是一个Integer，因此我们在运行时得到了一个ClassCastException异常。
     * 为了防止出现这种情况，第1行创建泛型数组的语句必须产生一条编译时错误。
     */
    public void whyGenericArrayCreationIsIllegal() {
        // Won't compile!
        // List<String>[] stringLists = new List<String>[l];  // (1)
        // List<Integer> intList = List.of(42);               // (2)
        // Object[] objects = stringLists;                    // (3)
        // objects[0] = intList;                              // (4)
        // String s = stringLists[0].get(0);                  // (5)
    }

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
