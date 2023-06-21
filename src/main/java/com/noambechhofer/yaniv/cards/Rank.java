package com.noambechhofer.yaniv.cards;

/** The rank of a Card */
public enum Rank {
    ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8),
    NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), JOKER(14);

    /**
     * numerical representation of this Rank. Useful for determining straights.
     */
    private int value;


    Rank(int rank) {
        this.value = rank;
    }

    /**
     * Returns a numerical representation of this Rank. ACE is
     * 1, TWO - TEN are 2 - 10, and JACK - KING are 11 - 13, JOKER is 14.
     * 
     * @return a numerical representation of this Rank
     */
    public int asInt() {
        return this.value;
    }
}