package com.noambechhofer.yaniv;

import java.util.Collection;
import java.util.Set;

import com.noambechhofer.yaniv.cards.Card;

/** The Dealer manages and runs the game. */
public final class Dealer {

    private Dealer() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Verify that the given set is valid under yaniv rules. Always returns false
     * for {@link Set} of size 0.
     * 
     * @param cards set of cards to be checked
     * @return {@code true} if the given set is valid under yaniv rules.
     */
    public static boolean isValidCardSet(Set<Card> cards) {
        if (cards.isEmpty()) {
            return false;
        }
        if (cards.size() == 1) {
            return true;
        }

        boolean sameRank = Card.sameRank(cards);

        boolean size = cards.size() >= YanivProperties.MINIMUM_STRAIGHT_LENGTH;
        boolean suit = Card.sameSuit(cards);
        boolean straight = Card.isStraight(cards);

        return sameRank || (size && suit && straight);
    }

    /**
     * Tally a {@link Collection} of Cards according to Yaniv rules laid out in the
     * documentation of {@link Card#yanivValue()}
     * 
     * @param cards the cards to be tallied
     * @return the tally
     */
    public static int tally(Collection<Card> cards) {
        return cards.stream().mapToInt(Card::yanivValue).sum();
    }
}