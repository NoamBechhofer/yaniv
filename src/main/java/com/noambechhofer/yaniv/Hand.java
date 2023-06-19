package com.noambechhofer.yaniv;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A Player's hand. This class is a wrapper around a {@link java.util.Set} of
 * {@link Card}s.
 */
public class Hand implements List<Card> {
    public static final int MAX_HAND_SIZE = YanivProperties.NUM_STARTING_CARDS;

    private List<Card> cards;

    public void sortHand() {
        Collections.sort(cards);
    }

    @Override
    public int size() {
        return cards.size();
    }

    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return cards.contains(o);
    }

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }

    @Override
    public Object[] toArray() {
        return cards.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return cards.toArray(a);
    }

    @Override
    @SuppressWarnings("java:S1192")
    public boolean add(Card e) {
        if (cards.size() >= MAX_HAND_SIZE) {
            throw new AssertionError("Hand size would exceed " + MAX_HAND_SIZE);
        }
        return cards.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return cards.remove(o);

    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return cards.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Card> c) {
        if (cards.size() + c.size() > MAX_HAND_SIZE) {
            throw new AssertionError("Hand size would exceed " + MAX_HAND_SIZE);
        }
        return cards.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Card> c) {
        if (cards.size() + c.size() > MAX_HAND_SIZE) {
            throw new AssertionError("Hand size would exceed " + MAX_HAND_SIZE);
        }
        return cards.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return cards.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return cards.retainAll(c);
    }

    @Override
    public void clear() {
        cards.clear();
    }

    @Override
    public Card get(int index) {
        return cards.get(index);
    }

    @Override
    public Card set(int index, Card element) {
        return cards.set(index, element);
    }

    @Override
    public void add(int index, Card element) {
        if (cards.size() >= MAX_HAND_SIZE) {
            throw new AssertionError("Hand size would exceed " + MAX_HAND_SIZE);
        }
        cards.add(index, element);
    }

    @Override
    public Card remove(int index) {
        return cards.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        int index = cards.indexOf(o);
        if (index != lastIndexOf(o)) {
            throw new AssertionError("Hand contains duplicate cards");
        }
        return index;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = cards.lastIndexOf(o);
        if (index != cards.indexOf(o)) {
            throw new AssertionError("Hand contains duplicate cards");
        }
        return index;
    }

    @Override
    public ListIterator<Card> listIterator() {
        return cards.listIterator();
    }

    @Override
    public ListIterator<Card> listIterator(int index) {
        return cards.listIterator(index);
    }

    @Override
    public List<Card> subList(int fromIndex, int toIndex) {
        return cards.subList(fromIndex, toIndex);
    }
}
