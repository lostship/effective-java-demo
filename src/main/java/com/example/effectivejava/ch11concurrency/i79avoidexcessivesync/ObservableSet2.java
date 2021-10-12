package com.example.effectivejava.ch11concurrency.i79avoidexcessivesync;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import com.example.effectivejava.ch11concurrency.i79avoidexcessivesync.collection.ForwardingSet;

/**
 * <p>
 * 使用{@link java.util.concurrent.CopyOnWriteArrayList}实现。
 * 
 * <p>
 * 该类是一个ArrayList的变体，实现了线程安全，所有mutatior操作都对底层数组生成一个新的拷贝，在拷贝上进行修改，之后将拷贝数组对象赋值给列表底层数组引用。
 * 这样导致修改操作的开销很大，但是非常适合遍历多、修改少的应用情况。
 * 
 * <p>
 * 因为CopyOnWriteArrayList是线程安全的，所以移除了所有操作访问obs列表时显式的同步语句。
 */
public class ObservableSet2<E> extends ForwardingSet<E> {

    private final Set<E> s;
    private final List<SetObserver<E>> obs = new CopyOnWriteArrayList<>();

    public static <E> ObservableSet2<E> of(Set<E> s) {
        return new ObservableSet2<>(s);
    }

    private ObservableSet2(Set<E> s) {
        this.s = s;
    }

    @Override
    protected Set<E> decorated() {
        return s;
    }

    public boolean addObserver(SetObserver<E> ob) {
        return obs.add(ob);
    }

    public boolean removeObserver(SetObserver<E> ob) {
        return obs.remove(ob);
    }

    private void notifyElementAdded(E e) {
        for (SetObserver<E> ob : obs) {
            ob.added(this, e);
        }
    }

    @FunctionalInterface
    public interface SetObserver<E> {

        void added(ObservableSet2<E> set, E element);

    }

    @Override
    public boolean add(E e) {
        boolean added = super.add(e);
        if (added) {
            notifyElementAdded(e);
        }
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E e : c) {
            result |= add(e);
        }
        return result;
    }

}
