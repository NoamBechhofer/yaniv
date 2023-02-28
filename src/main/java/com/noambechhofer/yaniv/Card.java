package com.noambechhofer.yaniv;

import java.io.File;
import java.io.IOException;

import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;

/**
 * A playing card.
 * 
 * <p>
 * Cards are immutable. Jokers not allowed.
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

    public Card(FaceValue val, Suit suit) {
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
     * Returns an image of this Card
     * 
     * @return an image of this Card
     */
    public MBFImage toImage() {
        String imagePath = YanivUtils.RESOURCES_PATH + "cards\\" + this.val + "_" + this.suit + ".png";
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
}

/**
 * The suit of a Card
 */
enum Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES;

    /**
     * Returns a numerical representation of this Suit. CLUBS are 1, DIAMONDS are 2,
     * HEARTS are 3, and SPADES are 4.
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
    ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5),
    SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
    JACK(11), QUEEN(12), KING(13);

    /**
     * numerical representation of this FaceValue. Useful for determining straights.
     */
    private int val;

    FaceValue(int val) {
        this.val = val;
    }

    /**
     * Returns a numerical representation of this FaceValue. ACE is 1, TWO - TEN are
     * 2 - 10, and JACK - KING are 11 - 13
     * 
     * @return a numerical representation of this FaceValue
     */
    public int asInt() {
        return this.val;
    }
}