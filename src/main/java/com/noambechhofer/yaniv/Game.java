package com.noambechhofer.yaniv;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Game {

    // we register players by giving them their starting hand
    private Set<Player> players;
    private Deck deck;
    private Stack<Set<Card>> discardPile; // Each discard is a set. That way we can access any of the cards from the
                                          // previous discard.
                                          
    private boolean canSlap;

    public Game() {
        this.players = new LockableHashSet<>();

        this.deck = new Deck();
        this.deck.shuffle();

        this.discardPile = new Stack<>();
    }

    /**
     * Verify that the given set is valid under yaniv rules. Always returns false
     * for {@link Set} of size 0.
     * 
     * @param cards set of cards to be checked
     * @return true if the given set is valid under yaniv rules.
     */
    public static boolean validate(Set<Card> cards) {
        if (cards.size() == 0)
            return false;
        if (cards.size() == 1)
            return true;

        
    
        boolean sameRank = Card.sameRank(cards);

        boolean size = cards.size() >= 3;
        boolean suit = Card.sameSuit(cards);
        boolean straight = Card.isStraight(cards);

        if (sameRank || size && suit && straight )
            return true;
        return false;
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
    public Set<Card> initializePlayer(Player p){
        assert !players.contains(p) : "duplicate player!!";

        if (players.size() >= YanivProperties.MAX_PLAYERS)
            throw new RuntimeException("too many players");

        this.players.add(p);

        Set<Card> hand = new HashSet<>();
        for (int i = 0; i < YanivProperties.NUM_STARTING_CARDS; i++) {
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
     * @param cards a valid combination of cards to discard
     * @return the top card on the deck
     */
    public Card drawFromDeck(Set<Card> cards) {
        assert (cards.size() > 0);
        assert validate(cards);

        this.discardPile.push(cards);

        return this.deck.removeTopCard();
    }

    /**
     * Attempt to slap down the {@link Card} which was just drawn. Only call if the
     * card was drawn from the deck, not the discard pile. Successful only if
     * performed before the next player's turn. Caller should only remove the Card
     * from their hand if this returns true.
     * 
     * @param c the card to slap down
     * @return true if the slap down was succesful
     */
    public boolean slapDown(Card c) {
        if (!canSlap)
            return false;

        this.discardPile.peek().add(c);
        return true;
    }

    /**
     * Tally a {@link Collection} of Cards according to Yaniv rules laid out in the
     * documentation of {@link Card#yanivValue()}
     * 
     * @param cards the cards to be tallied
     * @return the tally
     */
    public static int tally(Collection<Card> cards) {
        int tally = 0;

        for (Card c : cards)
            tally += c.yanivValue();

        return tally;
    }
}