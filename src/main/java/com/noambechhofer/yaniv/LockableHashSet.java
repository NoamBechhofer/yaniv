package com.noambechhofer.yaniv;

import java.util.Collection;
import java.util.HashSet;

/**
 * A HashSet which can be "switched off" to disallow further adds. Removes are
 * still allowed. Note that "switching off" is permanent.
 */
class LockableHashSet<E> extends HashSet<E> {
    private boolean on;

    /**
     * Constructs a new, empty set; the backing {@code HashMap} instance has
     * default initial capacity (16) and load factor (0.75).
     */
    public LockableHashSet() {
        super();
        this.on = true;
    }

    /**
     * Constructs a new set containing the elements in the specified
     * collection. The {@code HashMap} is created with default load factor
     * (0.75) and an initial capacity sufficient to contain the elements in
     * the specified collection.
     *
     * @param c the collection whose elements are to be placed into this set
     * @throws NullPointerException if the specified collection is null
     */
    public LockableHashSet(Collection<? extends E> c) {
        super(c);
        this.on = true;
    }

    /**
     * Constructs a new, empty set; the backing {@code HashMap} instance has
     * the specified initial capacity and the specified load factor.
     *
     * @param initialCapacity the initial capacity of the hash map
     * @param loadFactor      the load factor of the hash map
     * @throws IllegalArgumentException if the initial capacity is less
     *                                  than zero, or if the load factor is
     *                                  nonpositive
     */
    public LockableHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.on = true;
    }

    /**
     * {Constructs a new, empty set; the backing {@code HashMap} instance has
     * the specified initial capacity and default load factor (0.75).
     *
     * @param initialCapacity the initial capacity of the hash table
     * @throws IllegalArgumentException if the initial capacity is less
     *                                  than zero}
     */
    public LockableHashSet(int initialCapacity) {
        super(initialCapacity);
        this.on = true;
    }

    /**
     * Lock this set. No new elements weill be accepted, though elements may
     * be removed. This operation is irreversible.
     */
    public void lock() {
        this.on = false;
    }

    /**
     * Returns {@code true} if this set is unlocked, meaning it will accept new
     * elements.
     * 
     * @return {@code true} if this set is unlocked.
     */
    public boolean unlocked() {
        return this.on;
    }

    /**
     * Adds the specified element to this set if it is not already present and the
     * set is unlocked
     * More formally, adds the specified element {@code e} to this set if this set
     * contains no element {@code e2} such that {@code Objects.equals(e, e2)}, and
     * this set is not "switched off."
     * If this set already contains the element or is unlocked, the call
     * leaves the set unchanged and returns {@code false}.
     *
     * @param e element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     *         element and is unlocked.
     */
    @Override
    public boolean add(E e) {
        if (!this.on)
            return false;
        else
            return super.add(e);
    }
}