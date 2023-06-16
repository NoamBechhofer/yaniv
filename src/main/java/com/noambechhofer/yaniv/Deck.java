package com.noambechhofer.yaniv;

import java.util.Random;
import java.util.Stack;

/**
 * A deck of {@link Card}s. Note that cards can be removed or added to the deck.
 */
public class Deck {
    static {
        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (!assertsEnabled)
            throw new RuntimeException("Asserts must be enabled!!!");
    }

    Stack<Card> cards;

    /**
     * Produce a regular 54-card deck.
     */
    public Deck() {
        this.cards = new Stack<>();

        cards.push(new Card(Rank.JOKER, Suit.HEARTS));
        cards.push(new Card(Rank.JOKER, Suit.SPADES));

        // the first rank will be joker
        Rank[] ranks = Rank.values();
        for (Suit s : Suit.values()) {
            // skip 0 because we already populated Jokers
            for (int i = 1; i < ranks.length; i++) {
                cards.push(new Card(ranks[i], s));
            }
        }

        assert this.size() == 54;
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
    public Card removeTopCard() {
        return this.cards.pop();
    }

    /**
     * Add a {@link Card} to the top of the deck
     * 
     * @param c the {@link Card} to be added
     */
    public void addCard(Card c) {
        assert !this.cards.contains(c) : "duplicate card inserted!!";
        this.cards.push(c);
    }

    /**
     * Perform a Durstenfeld's Fisher–Yates shuffle on this deck
     */
    public void shuffle() {
        // https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#The_modern_algorithm
        Random rn = new Random();
        for (int i = this.cards.size() - 1; i >= 1; i--) {
            int j = rn.nextInt(0, i + 1);

            Card tmp = this.cards.get(j);
            this.cards.set(j, this.cards.get(i));
            this.cards.set(i, tmp);
        }
    }

    @Override
    public String toString() {
        String tmp = "";

        for (Card c : this.cards) {
            assert c != null : "there shouldn't be any null cards in the deck";
            tmp += c.toString() + "\n";
        }

        // remove trailing '\n'
        tmp = tmp.substring(0, tmp.length() - 1);

        return tmp;
    }
}
