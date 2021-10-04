package com.example.effectivejava.i7obsoletereference;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 * 当类自己管理内存时，就需要警惕内存泄露问题。一旦元素不再需要，就应该清空对象引用。
 */
public class MemoryLeakStack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public MemoryLeakStack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    /**
     * 元素出栈后，对象实际还被数组引用着，因此随着数组容量越来越大，大量下标大于等于size的对象不会被GC回收，造成内存泄露
     */
    @Deprecated
    public Object badPop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        return elements[--size];
    }

    /**
     * 正确的出栈方法
     */
    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        Object result = elements[--size];
        elements[size] = null; // eliminate obsolete reference
        return result;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
