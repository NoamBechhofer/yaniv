package com.noambechhofer.yaniv;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

public class GameTest {
    public static void main(String[] args) {
        GameTest gt = new GameTest();
        gt.testValidateSet();
    }

    @Test
    public void testValidateSet() {
        Card redJoker = new Card(Rank.JOKER, Suit.HEARTS);
        Card blackJoker = new Card(Rank.JOKER, Suit.SPADES);

        Set<Card> cards = new HashSet<>();
        for (Rank r : Rank.values()) {
            for (Suit s : Suit.values()) {
                try {
                    cards.add(new Card(r, s));
                } catch (RuntimeException e) {
                    continue;
                }
                assertTrue(Game.validate(cards));
            }
            cards = new HashSet<>();
        }

        cards.add(redJoker);
        for (Rank r : Rank.values()) {
            for (Suit s : Suit.values()) {
                try {
                    cards.add(new Card(r, s));
                } catch (RuntimeException e) {
                    continue;
                }
                assertTrue(Game.validate(cards));
            }
            cards = new HashSet<>();
        }

        cards.add(redJoker);
        cards.add(blackJoker);
        for (Rank r : Rank.values()) {
            for (Suit s : Suit.values()) {
                try {
                    cards.add(new Card(r, s));
                } catch (RuntimeException e) {
                    continue;
                }
                assertTrue(Game.validate(cards));
            }
            cards = new HashSet<>();
        }

        cards = new HashSet<>();

        cards.add(new Card(Rank.TWO, Suit.CLUBS));
        cards.add(new Card(Rank.THREE, Suit.CLUBS));

        assertFalse(Game.validate(cards));

        cards = new HashSet<>();

        for (Rank fv : Rank.values()) {
            if (fv == Rank.JOKER)
                continue;
            Card tmp = new Card(fv, Suit.CLUBS);
            cards.add(tmp);
        }
        assertTrue(Game.validate(cards));
        cards.add(redJoker);
        cards.add(blackJoker);
        assertTrue(Game.validate(cards));

        cards.remove(redJoker);
        cards.remove(blackJoker);

        for (Rank r : Rank.values()) {
            if (r == Rank.JOKER)
                continue;
            Card c = new Card(r, Suit.CLUBS);

            cards.remove(c);
            cards.add(redJoker);

            assertTrue(Game.validate(cards));

            for (Rank r1 : Rank.values()) {
                if (r1 == r || r1 == Rank.JOKER)
                    continue;
                Card c1 = new Card(r1, Suit.CLUBS);

                cards.remove(c1);
                cards.add(blackJoker);

                assertTrue(Game.validate(cards));

                cards.remove(blackJoker);
                cards.add(c1);
            }

            cards.remove(redJoker);
            cards.add(c);
        }

    }
}
