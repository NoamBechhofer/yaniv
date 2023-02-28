package com.noambechhofer.yaniv;

import java.util.Set;

public interface Player {
    /**
     * Check this Player's hand. Should only be called on yaniv/assaf checks. It
     * would be good practice to return an immutable list as a safety guard.
     * 
     * @return this Player's hand
     */
    public Set<Card> hand();

    /**
     * Make a move.
     */
    public void doTurn();
}
