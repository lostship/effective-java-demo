package com.example.effectivejava.ch4classandinterface.i21interface;

import java.util.Objects;

/**
 * 承诺实现一个所有方法线程安全的Collection
 * 
 * 但是如果Collection增加非线程安全的缺省方法，
 * SynchronizedCollection的实现者不知道这一变化，没有重写相应方法的话，
 * 就引入了非线程安全的方法，破坏了承诺。
 * 
 * 在创建接口的时候，利用缺省接口提供标准的方法实现是非常方便的，简化了实现接口的工作。
 * 但是在修改接口时，除非特殊需要，建议尽量避免利用缺省方法在接口上添加新方法，如果这样做需要慎重考虑是否会破坏已有实现类。
 * 
 * 在编写接口、类的可重写成员时都要非常慎重的考虑，在未来这些成员是否会被修改、删除，或者增加新的成员，这些修改是否会破坏既有实现类。
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
