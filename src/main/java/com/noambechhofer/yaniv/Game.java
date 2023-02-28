package com.noambechhofer.yaniv;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Game {

    // we register players by giving them their starting hand
    private Set<Player> players;
    private Map<Player, Integer> points;
    private Deck deck;
    private Stack<Set<Card>> discardPile; // Each discard is a set. That way we can access any of the cards from the
                                          // previous discard.

    public Game() {
        this.players = new LockableHashSet<>();
        this.points = new HashMap<>();

        this.deck = new Deck();
        this.deck.shuffle();

        this.discardPile = new Stack<>();
    }

    public Set<Card> peekDiscardPile() {
        return this.discardPile.peek();
    }

    /**
     * Call this method to receive the Player's starting hand.
     * 
     * @return this player's starting hand.
     * @throws PlayerRefusalException if there are too many players
     */
    public Set<Card> initializePlayer(Player p) throws PlayerRefusalException {
        assert !players.contains(p) : "duplicate player!!";

        if (players.size() >= YanivUtils.MAX_PLAYERS)
            throw new PlayerRefusalException("too many players");

        this.players.add(p);
        this.points.put(p, 0);

        Set<Card> hand = new HashSet<>();
        for (int i = 0; i < YanivUtils.NUM_STARTING_CARDS; i++) {
            Card tmp = this.deck.removeTopCard();
            boolean added = hand.add(tmp);
            assert added : tmp;
        }
        return hand;
    }

    /**
     * Draw from the discard pile.
     * 
     * todo: assert rules about ends of a straight
     * todo: assert valid set
     * 
     * @param c the Card to be removed. Must be part of the previous player's discard. 
     * @param cards valid set of cards to discard
     * @return the requested Card
     */
    public Card drawFromDiscard(Card c, Set<Card> cards) {
        assert cards.size() > 0;

        boolean removed = this.discardPile.peek().remove(c);
        assert removed : c;

        this.discardPile.push(cards);

        return c;
    }

    /**
     * Draw from the deck
     * 
     * todo: assert valid set
     * 
     * @param cards a valid combination of cards to discard
     * @return the top card on the deck
     */
    public Card drawFromDeck(Set<Card> cards) {
        assert (cards.size() > 0);

        this.discardPile.push(cards);

        return this.deck.removeTopCard();
    }


    // how can I accomplish this? I need to enforce that this only succeeds if the next player hasn't gone yet.
    /**
     * Attempt to slap down the {@link Card} which was just drawn. Only valid if the
     * card was drawn from the deck, not the discord pile. Successfull only if
     * performed before the next player's turn.
     * 
     * @param c the card to slap down
     * @return true if the slap down was succesful
     */
    public boolean slapDown(Card c) {
        throw new UnsupportedOperationException("Unimplemented method 'slapDown'");
    }

    public int getPoints(Player p) {
        return this.points.get(p);
    }
}

/**
 * Thrown when a player has tried to enter a game but has been refused
 */
class PlayerRefusalException extends Exception {
    public PlayerRefusalException() {
        super();
    }

    public PlayerRefusalException(String message) {
        super(message);
    }

    public PlayerRefusalException(String message, Throwable cause) {
        super(message, cause);
    }

    protected PlayerRefusalException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PlayerRefusalException(Throwable cause) {
        super(cause);
    }
}
