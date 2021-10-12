package com.example.effectivejava.ch11concurrency.i79avoidexcessivesync.collection;

import java.util.Set;

/**
 * A set which forwards all its method calls to another set. Subclasses should override one or more
 * methods to modify the behavior of the backing set as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 */
public abstract class ForwardingSet<E> extends ForwardingCollection<E> implements Set<E> {
    
    protected ForwardingSet() {}
    
    @Override
    protected abstract Set<E> decorated();

    @Override
    public int hashCode() {
        return decorated().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || decorated().equals(obj);
    }
    
}
