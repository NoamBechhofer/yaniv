package com.noambechhofer.yaniv;

import java.util.Collection;

public interface Player {
    /**
     * Used by the Dealer to signal that the round has ended. The Player should
     * {@link Game#tally(Collection)} their points and add their score. Will not be
     * sent to the player who called Yaniv.
     */
    public void endRound();

    /**
     * Return this Player's hand to the Dealer
     * 
     * @return this Player's hand
     */
    public Collection<Card> removeHand();

    /**
     * Check this Player's hand but do not remove it. It would be good practice to
     * return an immutable {@link Collection}.
     * 
     * @return this Player's hand
     */
    public Collection<Card> peekHand();

    /**
     * Make a move. The contract is that the Player will call drawFromDiscard() or
     * drawFromDeck()
     */
    public void doTurn();

    /**
     * Returns this Player's points
     * 
     * @return this Player's points
     */
    public int points();

    /**
     * Dealer calls this to deal a Card to this Player's hand
     * 
     * @param c card to be added
     */
    public void dealToHand(Card c);
}
