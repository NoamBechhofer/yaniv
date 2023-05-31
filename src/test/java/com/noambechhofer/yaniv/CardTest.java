package com.noambechhofer.yaniv;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CardTest {
    @Test
    public void testToUnicodeCodePoint() {
        List<Card> cards = new ArrayList<>(54);

        for (Rank r : Rank.values())
            for (Suit s : Suit.values())
                try {
                    cards.add(new Card(r, s));
                } catch (RuntimeException e) {
                    continue;
                }

        int i = 1;
        for (Card c : cards)
                System.out.printf("%d: %s = %X\n", i++, c, c.toUnicodeCodePoint());
    }
}
