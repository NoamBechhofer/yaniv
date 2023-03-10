package com.noambechhofer.yaniv;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

// TODO: rename Dealer
public class Game {

    /** players in the game. */
    private List<Player> players;
    /** single deck of cards */
    private Deck deck;
    // Each discard is a set. That way we can access any of the cards from the
    // previous discard.
    private Stack<Set<Card>> discardPile;

    private boolean yaniv;
    private boolean canSlap;

    public Game() {
        this.players = new LinkedList<>();

        this.deck = new Deck();
        this.deck.shuffle();

        this.discardPile = new Stack<>();

        this.yaniv = false;
    }

    // TODO: add method for player to quit

    /**
     * Play a game of Yaniv.
     * 
     * @param numPlayers the number of players in the game
     */
    public void play(int numPlayers) {
        if (numPlayers < YanivProperties.MIN_PLAYERS || numPlayers > YanivProperties.MAX_PLAYERS)
            throw new RuntimeException("invalid number of players " + numPlayers + ". Must be >= "
                    + YanivProperties.MIN_PLAYERS + " and <= " + YanivProperties.MAX_PLAYERS);

        // TODO: replace with listener
        while (this.players.size() < numPlayers)
            ;

        while (players.size() > 1) {
            this.deck.shuffle();

            for (int i = 0; i < YanivProperties.NUM_STARTING_CARDS; i++)
                for (Player p : players)
                    p.dealToHand(this.deck.removeTopCard());

            Iterator<Player> itr = this.players.iterator();
            Player curr = null;

            while (!this.yaniv) {
                if (!itr.hasNext())
                    itr = this.players.iterator();

                this.canSlap = true;
                curr = itr.next();
                curr.doTurn(); // ! side effect: doTurn() sets canSlap to false. This is accomplished within
                               // ! drawFromDiscard() and drawFromDeck(), which are the only two options for a
                               // ! Player to call within their doTurn().
            }
            this.yaniv = false;
            for (Player p : players)
                if (p != curr)
                    p.endRound();

            for (Player p : players)
                if (p.points() > YanivProperties.LOSING_THRESHOLD) {
                    Player next = null;
                    if (p == curr)
                        if (itr.hasNext())
                            next = itr.next();
                        else
                            next = players.get(0);
                    else
                        next = curr;
                    players.remove(p);

                    // We've modified players so we need a new iterator
                    itr = players.iterator();
                    while (next != itr.next())
                        ;
                }
        }

    }

    /**
     * Call Yaniv. The calling Player must be sure to tally their points properly
     * after making this call, similar to as if their {@link Player#endRound()}
     * method had been called.
     * 
     * @param hand the calling Player's hand
     * @return true if the call is successful, false if the player is "assaf"ed
     */
    public boolean callYaniv(Collection<Card> hand) {
        this.yaniv = true;
        for (Player p : this.players)
            if (tally(p.peekHand()) <= tally(hand))
                return false;
        return true;
    }

    /**
     * Verify that the given set is valid under yaniv rules. Always returns false
     * for {@link Set} of size 0.
     * 
     * @param cards set of cards to be checked
     * @return true if the given set is valid under yaniv rules.
     */
    public static boolean validate(Set<Card> cards) {
        if (cards.size() == 0)
            return false;
        if (cards.size() == 1)
            return true;
           
        boolean sameRank = Card.sameRank(cards);

        boolean size = cards.size() >= 3;
        boolean suit = Card.sameSuit(cards);
        boolean straight = Card.isStraight(cards);

        if (sameRank || size && suit && straight)
            return true;
        return false;
    }

    public Set<Card> peekDiscardPile() {
        return this.discardPile.peek();
    }

    /**
     * Draw from the discard pile.
     * 
     * TODO: assert rules about ends of a straight
     * 
     * @param c     the Card to be removed. Must be part of the previous player's
     *              discard.
     * @param cards valid set of cards to discard
     * @return the requested Card
     */
    public Card drawFromDiscard(Card c, Set<Card> cards) {
        assert cards.size() > 0;
        assert validate(cards);

        this.canSlap = false;

        boolean removed = this.discardPile.peek().remove(c);
        assert removed : c;

        this.discardPile.push(cards);

        return c;
    }

    /**
     * Draw from the deck
     * 
     * @param cards a valid combination of cards to discard
     * 
     * @return the top card on the deck
     */
    public Card drawFromDeck(Set<Card> cards) {
        assert cards.size() > 0;
        assert validate(cards);

        this.canSlap = false;

        this.discardPile.push(cards);

        return this.deck.removeTopCard();
    }

    /**
     * Attempt to slap down the {@link Card} which was just drawn. Only call if the
     * card was drawn from the deck, not the discard pile. Successful only if
     * performed before the next player's turn. Caller should only remove the Card
     * from their hand if this returns true.
     * 
     * @param c the card to slap down
     * @return true if the slap down was succesful
     */
    public boolean slapDown(Card c) {
        if (!canSlap)
            return false;

        this.discardPile.peek().add(c);
        return true;
    }

    /**
     * Tally a {@link Collection} of Cards according to Yaniv rules laid out in the
     * documentation of {@link Card#yanivValue()}
     * 
     * @param cards the cards to be tallied
     * @return the tally
     */
    public static int tally(Collection<Card> cards) {
        int tally = 0;

        for (Card c : cards)
            tally += c.yanivValue();

        return tally;
    }
}