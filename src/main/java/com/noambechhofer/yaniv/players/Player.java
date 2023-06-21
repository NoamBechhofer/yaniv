package com.noambechhofer.yaniv.players;

import java.util.Collection;

import com.noambechhofer.yaniv.Dealer;
import com.noambechhofer.yaniv.Round;
import com.noambechhofer.yaniv.cards.Card;
import com.noambechhofer.yaniv.cards.Hand;

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
     * {@link Round#drawFromDiscard(Card, java.util.Set)},
     * {@link Round#drawFromDeck(java.util.Set)}, or {@link Round#callYaniv()}.
     * 
     * @param round the {@link Round} instance representing the round this Player is
     *              playing
     */
    void doTurn(Round round);

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
     * Called when this player wins an entire game
     */
    void onGameWin();

    /**
     * Called when this player loses an entire game
     */
    void onGameLoss();

    /**
     * Called when this player calls Yaniv and is not assaf'ed
     */
    void onYaniv();

    /**
     * Called when this player calls Yaniv and is assaf'ed
     */
    void onAssaf();

    /**
     * Called when this player assaf's another player
     */
    void onAssafOther();

    /**
     * Returns this player's point total
     * 
     * @return this player's point total
     */
    int getPoints();

    /**
     * Add points to this player's point total.
     * If the player hits exactly a multiple of 50, their point total is cut in
     * half.
     * 
     * @param points points to add
     * @return true if the player's point total was cut in half
     */
    boolean addPoints(int points);
}
