package com.example.effectivejava.ch3commonmethod.i13clone;

import java.util.Arrays;

public class HashTable implements Cloneable {
    private Entry[] buckets;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public HashTable() {
        buckets = new Entry[DEFAULT_INITIAL_CAPACITY];
    }

    /**
     * 总结
     * 
     * clone方法还具有很多缺陷，例如想要在clone后修复final域就无法做到（比如本例中的buckets是final，就无法深拷贝修复了）
     * 因为Cloneable的复杂性，不应该再使用这个接口。
     * 
     * 这条规则的例外时候数组，最好利用clone方法复制数组。
     * 
     * 更好的方法是使用转换构造器（conversion constructor）和转换工厂（conversion factory）。
     * 这种方式甚至可以携带一个参数，参数类型是该类所实现的接口。
     * 例如所有通用集合都提供了一个转换构造器，参数类型是Collection或者Map，允许客户端选择
     * 拷贝的实现类型，而不是强迫使用原始的实现类型。
     * 例如有一个HashSet，希望把它拷贝成一个TreeSet，clone方法无法做到，但是使用
     * 转换构造器就很容易实现：new TreeSet<>(s)。
     */
    public HashTable(HashTable hashtable) {
        // copy
    }

    public static HashTable newInstance(HashTable hashtable) {
        return new HashTable(hashtable);
    }

    public static class Entry {
        private final Object key;
        private Object value;
        private Entry next;

        public Entry(Object key, Object value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        /**
         * 在我们实现的Hashtable内部，buckets是包含深层结构的可变对象。
         * 每个bucket都指向链表的第一项，但是每个桶实际存储的是一个链表，
         * 这整个数据结构都是Hashtable内部需要使用和负责维护的。
         * 
         * 不同于Stack的例子，原始Stack和克隆Stack使用的elements是两个不同的数组对象，
         * 克隆后数组中的元素引用的是相同的对象。但是这些元素对象的状态是不需要Stack使用和负责维护的。
         * 两个Stack对象中获取的元素指向的本就是同一个对象，从其中一个Stack获取并修改了元素状态，
         * 另一个Stack对象获取相同下标元素，对象状态一直，这符合程序的期望。
         * elements才是Stack需要维护的对象，修改一个Stack中elements某个下标引用一个新的对象，
         * 另一个Stack中相同下标位置还是引用的原有对象，不受影响，这符合程序的期望。
         * 
         * 但是在我们实现的Hashtable例子中如果仅对buckets使用浅拷贝，
         * 得到的克隆Hashtable与原始Hashtable引用同一个entry对象，
         * 修改克隆Hashtable中某个桶中某个entry的next，原始Hashtable中相应的桶的链表也发生变化了，
         * 但是链表的数据在不同的Hashtable对象中应该是独立维护的，
         * 因为它本身就是Hashtable功能实现和要维护的部分。
         * 
         * 因此对于这种包含深层结构的可变对象，必须提供一个深拷贝方法。
         */
        public Entry deepCopy() {
            Entry result = new Entry(key, value, next);
            Entry p = result;
            while (p.next != null) {
                p.next = new Entry(p.next.key, p.next.value, p.next.next);
                p = p.next;
            }
            return result;
        }

        /**
         * 这里使用递归不是很好，拷贝每一个元素都要分配栈空间，如果数组过长可能造成栈溢出
         * 
         * 这里可以使用迭代（iteration）代替递归（recursive）
         */
        public Entry badDeepCopy() {
            return new Entry(key, value,
                    next == null ? null : next.deepCopy());
        }
    }

    /**
     * 可以不抛出异常，返回类型和当前类相同
     * 
     * 注意clone方法也不应该在构造过程中调用可以被重写的方法（非private或非final）。
     * 因为不知道子类具体会执行什么操作，可能会造成克隆结果数据不一致。
     */
    @Override
    protected HashTable clone() {
        try {
            HashTable result = (HashTable) super.clone();
            result.buckets = new Entry[buckets.length];
            for (int i = 0; i < buckets.length; i++) {
                if (buckets[i] != null) {
                    result.buckets[i] = buckets[i].deepCopy();
                }
            }
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static void main(String[] args) {
        int[][] arr1 = { { 1 }, { 2 }, { 3 } };
        System.out.println(Arrays.toString(Arrays.copyOf(arr1, 5)));
        System.out.println(Arrays.toString(arr1.clone()));
    }
}
