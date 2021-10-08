package com.example.effectivejava.ch5generic.i29generictype;

import java.util.Arrays;
import java.util.EmptyStackException;

import com.example.effectivejava.ch4classandinterface.i21interface.Collection;
import com.example.effectivejava.ch5generic.i28genericlistorarray.ArrayCollection.ChooserObjectArrayImpl;

/**
 * 将i7的Stack泛型化（generify），同时保持已有的非参数化类型版本的客户端代码的移植兼容性（migration compatible）。
 * 
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
public class Stack<E> {
    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public Stack() {
        elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public E pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        E result = elements[--size];
        elements[size] = null; // eliminate obsolete reference
        return result;
    }

    /**
     * @param src 有限定通配符类型（bounded wildcard type）
     */
    public void pushAll(Iterable<? extends E> src) {
        for (E e : src) {
            push(e);
        }
    }

    /**
     * @param dst 有限定通配符类型（bounded wildcard type）
     */
    public void popAll(Collection<? super E> dst) {
        while (!isEmpty()) {
            dst.add(pop());
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }

    public static void main(String[] args) {
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < 5; i++) {
            stack.push("" + (char) ('a' + i));
        }
        while (!stack.isEmpty()) {
            System.out.println(stack.pop().toUpperCase());
        }
    }
}
