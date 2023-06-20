package com.noambechhofer.yaniv;

import java.util.Collection;

/**
 * a yaniv Player. This interface constitutes any Player's contract with the
 * {@link Dealer}.
 */
public interface Player {
    /**
     * Used by the Dealer to signal that the round has ended. The Player should
     * {@link Dealer#tally(Collection)} their points and add their score. Will not
     * be sent to the player who called Yaniv.
     */
    void endRound();

    /**
     * Return this Player's hand to the Dealer
     * 
     * @return this Player's hand
     */
    Hand removeHand();

    /**
     * Check this Player's hand but do not remove it.
     * 
     * @return this Player's hand
     */
    Hand peekHand();

    /**
     * Make a move. The contract is that the Player will call
     * {@link Dealer#drawFromDiscard(Card, java.util.Set)} or
     * {@link Dealer#drawFromDeck(java.util.Set)}
     */
    void doTurn();

    /**
     * Returns this Player's points
     * 
     * @return this Player's points
     */
    int getScore();

    /**
     * Dealer calls this to deal a Card to this Player's hand
     * 
     * @param c card to be added
     */
    void dealToHand(Card c);

    /**
     * Called when this player wins
     */
    void onWin();

    /**
     * Called when this player loses
     */
    void onLoss();
}
