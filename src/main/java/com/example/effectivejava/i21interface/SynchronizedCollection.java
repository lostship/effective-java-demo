package com.example.effectivejava.i21interface;

import java.util.Objects;

/**
 * 承诺实现一个所有方法线程安全的Collection
 * 
 * 但是如果Collection增加非线程安全的缺省方法，
 * SynchronizedCollection的实现者不知道这一变化，没有重写相应方法的话，
 * 就引入了非线程安全的方法，破坏了承诺。
 */
public class SynchronizedCollection<E> implements Collection<E> {
    private final Collection<E> c;
    private final Object mutex;

    public static <E> SynchronizedCollection<E> synchronizedCollection(Collection<E> c) {
        return new SynchronizedCollection<>(c);
    }

    public SynchronizedCollection(Collection<E> c) {
        this.c = Objects.requireNonNull(c);
        mutex = this;
    }

    private Collection<E> decorated() {
        return c;
    }

    @Override
    public void add(E e) {
        synchronized (mutex) {
            decorated().add(e);
        }
    }

    @Override
    public E get(int i) {
        synchronized (mutex) {
            return decorated().get(i);
        }
    }

    @Override
    public int size() {
        synchronized (mutex) {
            return decorated().size();
        }
    }

    @Override
    public void remove(int i) {
        synchronized (mutex) {
            decorated().remove(i);
        }
    }
}
