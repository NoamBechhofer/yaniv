package com.noambechhofer.yaniv;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.noambechhofer.yaniv.Utilities.SetSorter;
import com.noambechhofer.yaniv.Utilities.SingletonLogger;

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

    private static final Logger LOGGER = SingletonLogger.getLogger();

    /**
     * This card's rank
     */
    private Rank rank;
    /**
     * This card's suit
     */
    private Suit suit;
    /**
     * if {@code true}, draw the face of the card. If {@code false}, draw the back
     * of the card. New instances start face down.
     */
    private boolean faceUp;
    /** This card's face */
    private Image face;
    /** This card's back */
    private Image back;

    /**
     * Instantiate a Card
     * 
     * @param rank a rank selected from the {@link Rank} enum.
     * @param suit a suit selected from the {@link Suit} enum.
     *             <p>
     *             Red Jokers should be instantiated as hearts, black jokers as
     *             spades
     * @throws RuntimeException if a joker is constructed with the wrong suit
     */
    public Card(Rank rank, Suit suit) {

        if (rank == Rank.JOKER && (suit != Suit.HEARTS && suit != Suit.SPADES)) {
            throw new CardConstructionException("bad joker construction");
        }
        this.rank = rank;
        this.suit = suit;

        String faceImagePath = YanivProperties.RESOURCES_PATH + "cards\\" + this.rank + "_" + this.suit + ".png";
        String backImagePath = YanivProperties.RESOURCES_PATH + "cards\\cardback.png";

        File faceImageFile = new File(faceImagePath);
        File backImageFile = new File(backImagePath);

        try {
            this.face = ImageIO.read(faceImageFile);
            this.back = ImageIO.read(backImageFile);
        } catch (IOException e) {
            String errMessage = "Error occured while reading image, or not able to create required ImageInputStream.";
            LOGGER.log(Level.SEVERE, errMessage, e);
            throw new CardConstructionException(errMessage + " See log for details.");
        }

        this.faceUp = false;

    }

    /**
     * Flip this card between face up and face down
     */
    public void flip() {
        faceUp = !faceUp;
    }

    /**
     * Returns the rank of this card.
     * 
     * @return the rank of this card.
     */
    public Rank rank() {
        return this.rank;
    }

    /**
     * Returns the suit of this card.
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
        assert this.rank.asInt() != 0 && this.suit().asInt() != 0;

        return this.rank.asInt() * this.suit.asInt();
    }

    /**
     * Returns {@code true} if this Card is the same as the {@code obj} argument;
     * {@code false} otherwise.
     * <p>
     * Note: this class has a natural ordering that is inconsistent with equals.
     * 
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            return this.rank == ((Card) obj).rank() && this.suit == ((Card) obj).suit();
        }
        return super.equals(obj);
    }

    public Image toImage() {
        if (faceUp) {
            return face;
        } else {
            return back;
        }
    }

    /**
     * Returns the unicode representation of this card as a hex number
     * 
     * @return the unicode representation of this card as a hex number
     */
    public int toUnicodeCodePoint() {
        if (this.isJoker()) {
            if (this.suit == Suit.HEARTS) {
                return 0x1F0BF;
            } else if (this.suit == Suit.SPADES) {
                return 0x1F0CF;
            } else {
                throw new AssertionError("Invalid Joker encoding");
            }
        } else {
            int suitPart = 0;
            switch (this.suit) {
                case CLUBS:
                    suitPart = 0x1F0D0;
                    break;
                case DIAMONDS:
                    suitPart = 0x1F0C0;
                    break;
                case HEARTS:
                    suitPart = 0x1F0B0;
                    break;
                case SPADES:
                    suitPart = 0x1F0A0;
                    break;
                default:
                    assert false : this;
            }

            int rankPart = 0;
            switch (this.rank) {
                case KING:
                case QUEEN:
                    // there's a "knight" card in the full unicode deck which needs to be skipped
                    rankPart = this.rank.asInt() + 1;
                    break;
                default:
                    rankPart = this.rank.asInt();
            }

            return suitPart | rankPart;
        }

    }

    /**
     * Returns a string representation of this Card.
     * 
     * @return a string representation of this Card.
     */
    @Override
    public String toString() {
        if (this.isJoker()) {
            if (this.suit == Suit.HEARTS) {
                return "Red Joker";
            } else if (this.suit == Suit.SPADES) {
                return "Black Joker";
            } else {
                throw new AssertionError("Invalid Joker encoding");
            }
        }
        return String.format("%s of %s", rank, suit);
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
        return this.rank.asInt() - c.rank().asInt();
    }

    /**
     * Returns the value of this Card to be used when tallying points.
     * <p>
     * The rules for this value are:
     * <ul>
     * <li>Jokers are worth 0 points</li>
     * <li>Aces are worth 1 point</li>
     * <li>2 - 10 are worth their respective ranks</li>
     * <li>royals are worth 10 points</li>
     * </ul>
     * 
     * @return the value of this Card to be used when tallying points
     * 
     * @see Dealer#tally
     */
    @SuppressWarnings("java:S109") // magic numbers are fine here
    public int yanivValue() {
        switch (this.rank) {
            case JOKER:
                return 0;
            case JACK:
            case QUEEN:
            case KING:
                return 10;
            default:
                return this.rank.asInt();
        }
    }

    /**
     * Returns true if this Card represents a Joker
     * 
     * @return true if this Card represents a Joker
     */
    public boolean isJoker() {
        return this.rank() == Rank.JOKER;
    }

    /**
     * Check whether a {@link Set} of cards constitute a straight, i.e. with Jokers
     * as wild cards, whether it is a sequence of conecutive ranks
     * 
     * @param cards set of cards to be checked
     * @return true if the set constitutes a straight
     */
    public static boolean isStraight(Set<Card> cards) {
        if (cards.isEmpty() || cards.size() > Rank.values().length + 1) {
            return false;
        }

        int numJokers = 0;
        Card blackJoker = new Card(Rank.JOKER, Suit.SPADES);
        if (cards.contains(blackJoker)) {
            numJokers++;
        }
        Card redJoker = new Card(Rank.JOKER, Suit.HEARTS);
        if (cards.contains(redJoker)) {
            numJokers++;
        }

        List<Card> cardsList = SetSorter.sort(cards);

        Iterator<Card> itr = cardsList.iterator();
        Card curr = itr.next();

        while (itr.hasNext()) {
            Card next = itr.next();

            if (!curr.isJoker() && (curr.rank().asInt() + 1) != next.rank().asInt() && !next.isJoker()
                    && numJokers-- < 1) {
                return false;
            }

            curr = next;
        }
        return true;
    }

    /**
     * Test whether a {@link Set} of {@link Card}s all share the same suit. A
     * set of size 0 will always return {@code false}, and a set of size 1 will
     * always return {@code true}.
     * 
     * @param cards Set of Cards to test
     * @return true if all the cards share the same suit.
     */
    public static boolean sameSuit(Set<Card> cards) {
        if (cards.isEmpty() || cards.size() > Rank.values().length + 1) { // + 1 for the 2nd joker
            return false;
        }

        // don't wanna start with a joker so we sort
        List<Card> cardsList = SetSorter.sort(cards);

        Iterator<Card> itr = cardsList.iterator();
        Suit suit = itr.next().suit();

        while (itr.hasNext()) {
            Card next = itr.next();
            if (next.suit() != suit && !next.isJoker()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Test whether a {@link Set} of {@link Card}s all share the same rank. A
     * set of size 0 will always return {@code false}, and a set of size 1 will
     * always return {@code true}.
     * 
     * @param cards Set of Cards to test
     * @return true if all the cards share the same face rank
     */
    public static boolean sameRank(Set<Card> cards) {
        if (cards.isEmpty() || cards.size() > Suit.values().length + Rank.NUM_JOKERS) {
            return false;
        }

        // don't wanna start with a joker so we sort
        List<Card> cardsList = SetSorter.sort(cards);

        Iterator<Card> itr = cardsList.iterator();
        Rank rank = itr.next().rank();

        while (itr.hasNext()) {
            Card next = itr.next();
            if (next.rank() != rank && !next.isJoker()) {
                return false;
            }
        }

        return true;
    }
}