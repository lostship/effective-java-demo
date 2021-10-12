package com.example.effectivejava.ch11concurrency.i79avoidexcessivesync.collection;

/**
 * An abstract base class for implementing the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>. The {@link
 * #delegate()} method must be overridden to return the instance being decorated.
 */
public abstract class ForwardingObject {

    /** Constructor for use by subclasses. */
    protected ForwardingObject() {}
    
    /**
     * Returns the backing delegate instance that methods are forwarded to. Abstract subclasses
     * generally override this method with an abstract method that has a more specific return type,
     * such as {@link ForwardingSet#delegate}. Concrete subclasses override this method to supply the
     * instance being decorated.
     */
    protected abstract Object decorated();

    @Override
    public String toString() {
        return decorated().toString();
    }
    
    /* No equals or hashCode. See class comments for details. */
    
}
