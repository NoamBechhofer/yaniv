package com.noambechhofer.yaniv.cards;

import java.util.ArrayList;
import java.util.Random;

/**
 * A deck of {@link Card}s. Note that cards can be removed or added to the deck.
 */
public class Deck {

    private ArrayList<Card> cards;
    private Random rn;
    /** number of jokers in a deck */
    public static final int NUM_JOKERS = 2;
    public static final int DECK_SIZE = 54;

    /**
     * Produce a regular 54-card deck.
     */
    public Deck() {
        this.cards = new ArrayList<>();

        cards.add(new Card(Rank.JOKER, Suit.HEARTS));
        cards.add(new Card(Rank.JOKER, Suit.SPADES));

        // the first rank will be joker
        Rank[] ranks = Rank.values();
        for (Suit s : Suit.values()) {
            // skip 0 because we already populated Jokers
            for (int i = 1; i < ranks.length; i++) {
                cards.add(new Card(ranks[i], s));
            }
        }

        if (cards.size() != DECK_SIZE) {
            throw new AssertionError("Deck is not " + DECK_SIZE + " cards!");
        }
        rn = new Random();
    }

    /**
     * Returns the number of {@link Card}s in this deck.
     * 
     * @return the number of {@link Card}s in this deck
     */
    public int size() {
        return this.cards.size();
    }

    /**
     * Removes the top card of this deck.
     * 
     * @return the top card of this deck.
     */
    public Card drawTopCard() {
        return cards.remove(cards.size() - 1);
    }

    /**
     * Add a {@link Card} to the top of the deck
     * 
     * @param c the {@link Card} to be added
     */
    public void addCard(Card c) {
        this.cards.add(c);
    }

    /**
     * Perform a Durstenfeld's Fisher–Yates shuffle on this deck
     */
    public void shuffle() {
        for (int i = this.cards.size() - 1; i >= 1; i--) {
            int j = rn.nextInt(0, i + 1);

            Card tmp = this.cards.get(j);
            this.cards.set(j, this.cards.get(i));
            this.cards.set(i, tmp);
        }
    }

    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder();

        for (Card c : this.cards) {
            if (c == null) {
                throw new AssertionError("there shouldn't be any null cards in the deck");
            }
            bld.append(c.toString() + "\n");
        }

        // there's a trailing '\n'
        bld.deleteCharAt(bld.length() - 1);

        return bld.toString();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public boolean add(Card c) {
        return cards.add(c);
    }

    public void add(Hand h) {
        while (!h.isEmpty()) {
            cards.add(h.remove(h.size() - 1));
        }
    }
}
