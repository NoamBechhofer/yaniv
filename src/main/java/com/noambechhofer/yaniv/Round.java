package com.noambechhofer.yaniv;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.noambechhofer.yaniv.cards.Card;
import com.noambechhofer.yaniv.cards.Deck;
import com.noambechhofer.yaniv.cards.Hand;
import com.noambechhofer.yaniv.players.Player;
import com.noambechhofer.yaniv.utilities.SingletonLogger;

/**
 * A round of Yaniv. This class is responsible for managing the state of a round
 */
public final class Round {
    public static class InvalidCardSetException extends RuntimeException {
        public InvalidCardSetException() {
            super();
        }

        public InvalidCardSetException(String message) {
            super(message);
        }
    }

    public static class DrawFromMiddleOfStraightException extends RuntimeException {
        public DrawFromMiddleOfStraightException() {
            super();
        }

        public DrawFromMiddleOfStraightException(String message) {
            super(message);
        }
    }

    private static final Logger LOGGER = SingletonLogger.getLogger();

    private enum PreviousDraw {
        DECK, DISCARD
    }

    private Deck deck;
    private Deque<Set<Card>> discardPile;
    private PlayerList players;
    private Player nextPlayer;
    private Player canSlap;
    private boolean yanivCalled;
    private Player caller;
    private PreviousDraw previousDraw;

    /**
     * @param startingPlayer the first Player that moves
     * @param players        the PlayerList containing all the players.
     * @param dealer         the Dealer to be used for this round
     * @param deck           the Deck to be used for this round
     */
    public Round(Player startingPlayer, PlayerList players, Deck deck) {
        this.players = players;
        this.nextPlayer = startingPlayer;
        this.canSlap = null;
        this.discardPile = new ArrayDeque<>();
        this.deck = deck;
        this.caller = null;
    }

    /**
     * Play a round of Yaniv. The points of each player will be stored in the
     * PlayerList passed to the constructor. This method, and in fact the whole
     * class, does not take repsonsibility for removing players who pass the loss
     * threshold.
     * 
     * 
     * @return the Player who called Yaniv - regardless of whether they won or lost
     *         (got assaf'd)
     */
    public Player play() {
        while (!yanivCalled) {
            nextPlayer.doTurn(this);
            if (caller != null) {
                return caller;
            }
            nextPlayer = players.playerAfter(nextPlayer);
        }

        throw new AssertionError("Yaniv called but no winner set");
    }

    public void callYaniv(Player caller) {
        int callerTally = Dealer.tally(caller.peekHand());
        if (callerTally > YanivProperties.YANIV_VALUE) {
            throw new IllegalStateException(
                    String.format("Player %s called Yaniv with a hand value of %d (max is %d)",
                            caller.toString(), callerTally, YanivProperties.YANIV_VALUE));
        }

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(
                    String.format("Player %s called Yaniv with a hand value of %d", caller.toString(), callerTally));
        }

        this.yanivCalled = true;
        this.caller = caller;

        boolean assaf = false;
        for (Player other : players) {
            if (other == caller) {
                continue;
            }

            Hand h = other.removeHand();
            int otherTally = Dealer.tally(h);
            if (otherTally <= callerTally) {
                other.onAssafOther();
                assaf = true;
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(String.format("Player %s assaf'd player %s with hand value %d",
                            caller.toString(), other.toString(), otherTally));
                }
            }
            other.addPoints(otherTally);
            this.deck.add(h);
        }

        if (assaf) {
            caller.addPoints(YanivProperties.ASSAF_PENALTY + callerTally);
            caller.onAssaf();
        } else {
            caller.onYaniv();
        }

        if (deck.size() != Deck.DECK_SIZE) {
            throw new AssertionError("Deck size is not " + Deck.DECK_SIZE);
        }
    }

    /**
     * Query whether a specified player may slap down their cards at this moment
     * 
     * @param p the player to query
     * @return {@code true} if the player may slap down a card, {@code false}
     *         otherwise
     */
    public boolean canSlap(Player p) {
        return p == canSlap;
    }

    /**
     * Attempt to slap down a card.
     * 
     * @param p the player attempting to slap
     * @param c the card to slap down
     * @return {@code true} if the slap was successful, {@code false} otherwise
     * @throws IllegalArgumentException if the discard was a straight, or the rank
     *                                  does not match the rank(s) of the discard
     * @throws IllegalStateException    if the player drew from the discard
     *                                  pile instead of the deck
     */
    public boolean slap(Player p, Card c) {
        if (p != canSlap) {
            return false;
        }

        if (this.previousDraw == PreviousDraw.DISCARD) {
            throw new IllegalStateException("Cannot slap a card drawn from the discard pile");
        }

        Set<Card> top = discardPile.peek();
        if (top.size() > 1 && Card.isStraight(top)) {
            throw new IllegalArgumentException("Cannot slap a straight");
        }

        for (Card card : top) {
            if (card.isJoker()) {
                continue;
            }
            if (card.rank() != c.rank()) {
                throw new IllegalArgumentException("Cannot slap a card of rank " + c.rank() + " on a card of rank "
                        + card.rank());
            }
        }

        boolean added = top.add(c);
        if (!added) {
            throw new AssertionError();
        }

        return true;
    }

    /**
     * Draw a card from the deck after discarding a valid set of cards.
     * 
     * @param s the set of cards to discard
     * @return the card drawn from the deck
     * @throws InvalidCardSetException if the set of cards is not valid
     */
    public Card drawFromDeck(Set<Card> s) {
        if (!Dealer.isValidCardSet(s)) {
            throw new InvalidCardSetException(s + " is not valid a valid set");
        }
        this.canSlap = nextPlayer; // at this point, nextPlayer is the Player calling this method
        this.discardPile.push(s);
        this.previousDraw = PreviousDraw.DECK;

        if (deck.isEmpty()) {
            while (discardPile.size() > 1) {
                discardPile.removeLast().forEach(deck::add);
            }
            deck.shuffle();
        }

        return deck.drawTopCard();
    }

    /**
     * Draw a card from the discard pile after discarding a valid set of cards.
     * 
     * @param c the card to draw from the discard pile
     * @param s the set of cards to discard
     * @return the card drawn from the discard pile
     * 
     * @throws InvalidCardSetException           if the set of cards is not valid
     * @throws IllegalArgumentException          if the card is not in the last
     *                                           discarded set (the set on top of
     *                                           the discard pile)
     * @throws DrawFromMiddleOfStraightException if the card is drawn from the
     *                                           middle of a straight
     */
    public Card drawFromDiscard(Card c, Set<Card> s) {
        if (!Dealer.isValidCardSet(s)) {
            throw new InvalidCardSetException(s + " is not a valid set");
        }
        this.canSlap = nextPlayer; // at this point, nextPlayer is the Player calling this method
        this.previousDraw = PreviousDraw.DISCARD;

        Set<Card> topOfDiscardPile = discardPile.pop();
        if (!topOfDiscardPile.contains(c)) {
            throw new IllegalArgumentException(c + " is not in the top of the discard pile");
        }

        topOfDiscardPile.remove(c);
        /*
         * In order to draw from a straight on the discard pile, the card being drawn
         * must be on either end of the straight, not the middle. In other words, the
         * top of the discard pile must still constitute a straight after the card has
         * been removed. We use the most lenient definition of a straight here, meaning
         * that the resultant straight can be any length (even 0). Notice that a joker
         * will be succesfully removed if it fits on either end of the straight (e.g., 7
         * - 8 - 9 - joker ). If it is integral to the middle of a straight (e.g 7 -
         * joker - 9), it may not be removed.
         * 
         * In all then, we can merely verify that the remaining cards are simply a valid
         * set.
         * If there are no cards remaining, then the top of the discard pile was just
         * one card. If there are cards remaining, then they must have either been a
         * straight or a flush. In either case, the remaining cards should still be a
         * valid set - unless the player attempted to draw from the middle of a
         * straight.
         */
        if (!(topOfDiscardPile.isEmpty() || Dealer.isValidCardSet(topOfDiscardPile))) {
            topOfDiscardPile.add(c);
            discardPile.push(topOfDiscardPile);
            throw new DrawFromMiddleOfStraightException("Cannot draw from the middle of a straight");
        }
        discardPile.push(topOfDiscardPile);
        discardPile.push(s);
        return c;
    }
}
