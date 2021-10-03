package com.example.effectivejava.i21interface;

import java.util.function.Predicate;

public interface Collection<E> {
    void add(E e);

    E get(int i);

    int size();

    void remove(int i);

    /**
     * 接口增加或者修改缺省方法，也有可能对实现类造成破坏。
     * 
     * 比如此处增加了一个非线程安全的removeIf方法，
     * SynchronizedCollection的实现者不知道这一变化，并没有重写removeIf方法，
     * 就破坏了SynchronizedCollection类所有方法线程安全的承诺。
     */
    default void removeIf(Predicate<E> filter) {
        for (int i = 0, size = size(); i < size; i++) {
            if (filter.test(get(i))) {
                remove(i);
            }
        }
    }
}
