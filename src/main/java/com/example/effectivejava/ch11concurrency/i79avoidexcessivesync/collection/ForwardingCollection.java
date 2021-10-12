package com.example.effectivejava.ch11concurrency.i79avoidexcessivesync.collection;

import java.util.Collection;
import java.util.Iterator;

/**
 * A collection which forwards all its method calls to another collection. Subclasses should
 * override one or more methods to modify the behavior of the backing collection as desired per the
 * <a href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 */
public abstract class ForwardingCollection<E> extends ForwardingObject implements Collection<E> {
    
    protected ForwardingCollection() {}
    
    protected abstract Collection<E> decorated();

    @Override
    public int size() {
        return decorated().size();
    }

    @Override
    public boolean isEmpty() {
        return decorated().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return decorated().contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return decorated().iterator();
    }

    @Override
    public Object[] toArray() {
        return decorated().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return decorated().toArray(a);
    }

    @Override
    public boolean add(E e) {
        return decorated().add(e);
    }

    @Override
    public boolean remove(Object o) {
        return decorated().remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return decorated().contains(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return decorated().addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return decorated().retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return decorated().removeAll(c);
    }

    @Override
    public void clear() {
        decorated().clear();
    }
    
}
