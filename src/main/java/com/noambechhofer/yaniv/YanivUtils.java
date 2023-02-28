package com.noambechhofer.yaniv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class YanivUtils {

    // todo: change to relative path and make platform-independent ("/" vs "\\")
    public static final String RESOURCES_PATH = "C:\\Users\\noamb\\OneDrive\\Programming\\yanivMaven\\yaniv\\src\\main\\resources\\";

    // todo: replace with shell command based on platform
    public static void clearTerminal() {
        for (int i = 0; i < 100; i++)
            System.out.println("\n");
    }

    // todo: add test
    /**
     * Verify that the given set is valid under yaniv rules. Always returns false
     * for {@link Set} of size 0.
     * 
     * @param cards
     * @return
     */
    public static boolean validateSet(Set<Card> cards) {
        if (cards.size() == 0)
            return false;
        if (cards.size() == 1)
            return true;

        if (sameVal(cards) || (sameSuit(cards) && isStraight(cards)))
            return true;
        return false;
    }

    /**
     * Test whether a {@link Set} of {@link Card}s all share the same face value. A
     * set of size 0 will always return {@code false}, and a set of size 1 will
     * always return {@code true}.
     * 
     * @param cards Set of Cards to test
     * @return true if all the cards share the same face value
     */
    public static boolean sameVal(Set<Card> cards) {
        if (cards.size() == 0 || cards.size() > Suit.values().length)
            return false;

        // don't wanna start with a joker so we sort
        List<Card> cardsList = sortedSet(cards);

        Iterator<Card> itr = cardsList.iterator();
        FaceValue val = itr.next().val();

        while (itr.hasNext()) {
            Card next = itr.next();
            if (next.val() != val && !next.isJoker())
                return false;
        }

        return true;
    }

    // todo javadoc
    public static boolean sameSuit(Set<Card> cards) {
        if (cards.size() == 0 || cards.size() > FaceValue.values().length)
            return false;

        // don't wanna start with a joker so we sort
        List<Card> cardsList = sortedSet(cards);

        Iterator<Card> itr = cardsList.iterator();
        Suit suit = itr.next().suit();

        while (itr.hasNext()) {
            Card next = itr.next();
            if (next.suit() != suit && !next.isJoker())
                return false;
        }

        return true;
    }

    // todo javadoc
    public static boolean isStraight(Set<Card> cards) {
        if (cards.size() == 0 || cards.size() > FaceValue.values().length)
            return false;

        List<Card> cardsList = sortedSet(cards);

        Iterator<Card> itr = cardsList.iterator();
        Card curr = itr.next();

        while (itr.hasNext()) {
            Card next = itr.next();

            if (!curr.isJoker() && (curr.val().asInt() + 1) != next.val().asInt() && !next.isJoker())
                return false;

            curr = next;
        }
        return true;
    }

    /**
     * Convert a {@link Set} into a sorted {@link List}
     * 
     * @param <T> the class of the objects in the Set
     * @param set the set to be converted
     * @return the sorted list
     */
    public static <T extends Comparable<? super T>> List<T> sortedSet(Set<T> set) {
        List<T> tmp = new ArrayList<T>(set);

        Collections.sort(tmp);

        return tmp;
    }
}
