package com.noambechhofer.yaniv;

/**
 * A round of Yaniv. This class is responsible for managing the state of a round
 */
public final class Round {
    private Round() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Play a round of Yaniv. The points of each player will be stored in the
     * PlayerList passed to the constructor. This method, and in fact the whole
     * class, does not take repsonsibility for removing players who pass the loss
     * threshold.
     * 
     * @param startingPlayer the first Player that moves
     * @param players        the PlayerList containing all the players.
     * @param dealer         the Dealer to be used for this round
     * @param deck           the Deck to be used for this round
     * 
     * @return the Player who called Yaniv - regardless of whether they won or lost
     *         (got assaf'd)
     */
    // ! TODO: make sure the deck is returned fully!
    public static Player play(Player startingPlayer, PlayerList players, Dealer dealer, Deck deck) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
