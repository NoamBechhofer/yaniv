package com.noambechhofer.yaniv;

import java.awt.Color;

/**
 * The suit of a Card
 */
enum Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES;

    /**
     * Returns {@link Color#BLACK} or {@link Color#RED}
     * 
     * @return {@link Color#BLACK} or {@link Color#RED}
     */
    public Color color() {
        if (this == CLUBS || this == SPADES) {
            return Color.BLACK;
        } else {
            return Color.RED;
        }
    }

    /**
     * Returns a numerical representation of this Suit. CLUBS are 1, DIAMONDS are 2,
     * HEARTS are 3, and SPADES are 4. Both jokers are 0.
     * 
     * @return a numerical representation of this Suit
     */
    @SuppressWarnings("java:S109") // Magic number
    public int asInt() {
        switch (this) {
            case CLUBS:
                return 1;
            case DIAMONDS:
                return 2;
            case HEARTS:
                return 3;
            case SPADES:
                return 4;
            default:
                assert false : this;
                return 0;
        }
    }
}