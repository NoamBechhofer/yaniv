package com.noambechhofer.yaniv;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.awt.Color;

import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;

/**
 * A playing card.
 * 
 * <p>
 * Red Jokers should be instantiated as hearts, black jokers as spades
 * 
 * <p>
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public class Card implements Comparable<Card> {

    /**
     * Face value.
     * 
     * See class documentation for encoding format.
     */
    private FaceValue val;
    /**
     * Suit.
     * 
     * See class documentation for encoding format.
     */
    private Suit suit;

    /**
     * Instantiate a Card
     * 
     * @param val  a face value selected from the {@link FaceValue} enum.
     * @param suit a suit selected from the {@link Suit} enum.
     *             <p>
     *             Red Jokers should be instantiated as hearts, black jokers as
     *             spades
     */
    public Card(FaceValue val, Suit suit) {
        if (val == FaceValue.JOKER && (suit != Suit.HEARTS && suit != Suit.SPADES))
            throw new RuntimeException("bad joker construction");
        this.val = val;
        this.suit = suit;
    }

    /**
     * Accessor method
     * 
     * @return the face value of this card.
     */
    public FaceValue val() {
        return this.val;
    }

    /**
     * Accessor method
     * 
     * @return the suit of this card.
     */
    public Suit suit() {
        return this.suit;
    }

    /**
     * Returns a hash code value for this card.
     * 
     * @return a hash code value for this card.
     */
    @Override
    public int hashCode() {

        int suitInt = this.suit.asInt();

        return this.val.asInt() * suitInt;
    }

    /**
     * Returns {@code true} if this Card is the same as the obj argument;
     * {@code false} otherwise.
     * <p>
     * Note: this class has a natural ordering that is inconsistent with equals.
     * 
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Card)
            return obj != null && this.val == ((Card) obj).val() && this.suit == ((Card) obj).suit();
        return super.equals(obj);
    }

    /**
     * Returns a string representation of this Card.
     * 
     * @return a string representation of this Card.
     */
    @Override
    public String toString() {
        if (this.isJoker())
            if (this.suit == Suit.HEARTS)
                return "Red Joker";
            else if (this.suit == Suit.SPADES)
                return "Black Joker";
            else
                throw new AssertionError("Invalid Joker encoding");
        return String.format("%s of %s", val, suit);
    }

    /**
     * Compares this object with the specified object for order. Returns a negative
     * integer, zero, or a positive integer as this object is less than, equal to,
     * or greater than the specified object.
     * <p>
     * Note: this class has a natural ordering that is inconsistent with equals.
     * 
     * @param c the Card to be compared.
     * 
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Card c) {
        return this.val.asInt() - c.val().asInt();
    }

    /**
     * Returns the value of this Card to be used when tallying points
     * 
     * @return the value of this Card to be used when tallying points
     */
    public int yanivValue() {
        switch (this.val) {
            case JOKER:
                return 0;
            case JACK:
            case QUEEN:
            case KING:
                return 10;
            default:
                return this.val.asInt();
        }
    }

    /**
     * Returns an image of this Card
     * Images from https://code.google.com/archive/p/vector-playing-cards/
     * 
     * @return an image of this Card
     */
    public MBFImage toImage() {
        String imagePath = YanivProperties.RESOURCES_PATH + "cards\\" + this.val + "_" + this.suit + ".png";
        File imageFile = new File(imagePath);

        MBFImage image = null;
        try {
            image = ImageUtilities.readMBF(imageFile);
        } catch (IOException e) {
            System.err.println("Could not find image at " + imagePath); // todo log properly
            e.printStackTrace(System.err);
            System.exit(1);
        }

        return image;
    }

    /**
     * Returns true if this Card represents a Joker
     * 
     * @return true if this Card represents a Joker
     */
    public boolean isJoker() {
        return this.val() == FaceValue.JOKER;
    }

    // todo javadoc
    public static boolean isStraight(Set<Card> cards) {
        if (cards.size() == 0 || cards.size() > FaceValue.values().length)
            return false;

        List<Card> cardsList = SetSorter.sort(cards);

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

    // todo javadoc
    public static boolean sameSuit(Set<Card> cards) {
        if (cards.size() == 0 || cards.size() > FaceValue.values().length)
            return false;

        // don't wanna start with a joker so we sort
        List<Card> cardsList = SetSorter.sort(cards);

        Iterator<Card> itr = cardsList.iterator();
        Suit suit = itr.next().suit();

        while (itr.hasNext()) {
            Card next = itr.next();
            if (next.suit() != suit && !next.isJoker())
                return false;
        }

        return true;
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
        List<Card> cardsList = SetSorter.sort(cards);

        Iterator<Card> itr = cardsList.iterator();
        FaceValue val = itr.next().val();

        while (itr.hasNext()) {
            Card next = itr.next();
            if (next.val() != val && !next.isJoker())
                return false;
        }

        return true;
    }
}

/**
 * The suit of a Card
 */
enum Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES;

    /**
     * Returns {@link Color.BLACK} or {@link Color.RED}
     * 
     * @return {@link Color.BLACK} or {@link Color.RED}
     */
    public Color color() {
        if (this == CLUBS || this == SPADES)
            return Color.BLACK;
        else
            return Color.RED;
    }

    /**
     * Returns a numerical representation of this Suit. CLUBS are 1, DIAMONDS are 2,
     * HEARTS are 3, and SPADES are 4. Both jokers are 0.
     * 
     * @return a numerical representation of this Suit
     */
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

/** The face value of a Card */
enum FaceValue {
    ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8),
    NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), JOKER(14);

    /**
     * numerical representation of this FaceValue. Useful for determining straights.
     */
    private int val;

    FaceValue(int val) {
        this.val = val;
    }

    /**
     * Returns a numerical representation of this FaceValue. ACE is
     * 1, TWO - TEN are 2 - 10, and JACK - KING are 11 - 13, JOKER is 14.
     * 
     * @return a numerical representation of this FaceValue
     */
    public int asInt() {
        return this.val;
    }
}