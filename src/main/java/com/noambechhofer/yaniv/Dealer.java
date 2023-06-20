package com.noambechhofer.yaniv;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/** The Dealer manages and runs the game. */
public class Dealer {

    /**
     * The minimum number of consecutive cards that is counted as a straight.
     */
    private static final int MINIMUM_STRAIGHT_LENGTH = 3;
    /** players in the game. */
    private List<Player> players;
    /** single deck of cards */
    private Deck deck;
    // Each discard is a set. That way we can access any of the cards from the
    // previous discard.
    private Deque<Set<Card>> discardPile;

    private boolean yaniv;
    private boolean canSlap;

    public Dealer() {
        this.players = new LinkedList<>();

        this.deck = new Deck();
        this.deck.shuffle();

        this.discardPile = new ArrayDeque<>();

        this.yaniv = false;
    }

    // TODO: add method for player to quit

    /**
     * Play a game of Yaniv.
     * 
     * @param numPlayers the number of players in the game
     */
    public void play(int numPlayers) {
        if (numPlayers < YanivProperties.MIN_PLAYERS || numPlayers > YanivProperties.MAX_PLAYERS) {
            throw new RuntimeException("invalid number of players " + numPlayers + ". Must be >= "
                    + YanivProperties.MIN_PLAYERS + " and <= " + YanivProperties.MAX_PLAYERS);
        }

        // TODO: replace with listener
        while (this.players.size() < numPlayers) {
            ;
        }

        while (players.size() > 1) {
            this.deck.shuffle();

            for (int i = 0; i < YanivProperties.NUM_STARTING_CARDS; i++) {
                for (Player p : players) {
                    p.dealToHand(this.deck.removeTopCard());
                }
            }

            Iterator<Player> itr = this.players.iterator();
            Player curr = null;

            while (!this.yaniv) {
                if (!itr.hasNext()) {
                    itr = this.players.iterator();
                }

                this.canSlap = true;
                curr = itr.next();
                curr.doTurn(); // ! side effect: doTurn() sets canSlap to false. This is accomplished within
                               // ! drawFromDiscard() and drawFromDeck(), which are the only two options for a
                               // ! Player to call within their doTurn().
            }
            this.yaniv = false;
            for (Player p : players) {
                if (p != curr) {
                    p.endRound();
                }
            }

            boolean atLeastOneLeft = false;
            for (Player p : players) {
                if (p.getScore() < YanivProperties.LOSING_THRESHOLD) {
                    atLeastOneLeft = true;
                }
            }
            // may happen if all players go above the losing threshold in the round:
            if (!atLeastOneLeft) {
                Player winner = players.get(0);
                for (int i = 1; i < players.size(); i++) {
                    Player tmp = players.get(i);
                    if (tmp.getScore() < winner.getScore()) {
                        winner = tmp;
                        for (Player p : players) {
                            if (p != tmp)
                                players.remove(p);
                        }
                        continue;
                    }
                }
            } else {
                for (Player p : players) {
                    if (p.getScore() > YanivProperties.LOSING_THRESHOLD) {
                        Player next = null;
                        if (p == curr) {
                            if (itr.hasNext()) {
                                next = itr.next();
                            } else {
                                next = players.get(0);
                            }
                        } else {
                            next = curr;
                        }
                        players.remove(p);

                        // We've modified players so we need a new iterator
                        itr = players.iterator();
                        while (next != itr.next()) {
                            ;
                        }
                    }
                }
            }
        }

    }

    /**
     * Call Yaniv. The calling Player must be sure to tally their points properly
     * after making this call, similar to as if their {@link Player#endRound()}
     * method had been called.
     * 
     * @param hand the calling Player's hand
     * @return {@code true} if the call is successful, {@code false} if the player
     *         is "assaf"ed
     */
    public boolean callYaniv(Collection<Card> hand) {
        this.yaniv = true;
        for (Player p : this.players) {
            if (tally(p.peekHand()) <= tally(hand)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verify that the given set is valid under yaniv rules. Always returns false
     * for {@link Set} of size 0.
     * 
     * @param cards set of cards to be checked
     * @return {@code true} if the given set is valid under yaniv rules.
     */
    public static boolean isValid(Set<Card> cards) {
        if (cards.size() == 0) {
            return false;
        }
        if (cards.size() == 1) {
            return true;
        }

        boolean sameRank = Card.sameRank(cards);

        boolean size = cards.size() >= MINIMUM_STRAIGHT_LENGTH;
        boolean suit = Card.sameSuit(cards);
        boolean straight = Card.isStraight(cards);

        return sameRank || (size && suit && straight);
    }

    /**
     * Look at, but do not remove, the top {@link Set} of {@link Card}s on the
     * discard pile.
     * 
     * @return the top {@link Set} of {@link Card}s on the discard pile.
     */
    public Set<Card> peekDiscardPile() {
        return this.discardPile.peek();
    }

    // TODO: enforce rules about ends of a straight with a checked exception
    /**
     * Draw from the discard pile.
     * 
     * @param c     the Card to be removed. Must be part of the previous player's
     *              discard.
     * @param cards valid set of cards to discard
     * @return the requested Card
     */
    public Card drawFromDiscard(Card c, Set<Card> cards) {
        if (cards.isEmpty() || !isValid(cards)) {
            throw new AssertionError();
        }

        this.canSlap = false;

        boolean removed = this.discardPile.peek().remove(c);
        if (!removed) {
            throw new AssertionError();
        }

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
        if (cards.isEmpty() || !isValid(cards)) {
            throw new AssertionError("Players should not have the ability to discard an invalid set of cards.");
        }

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
        if (!canSlap) {
            return false;
        }

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

        for (Card c : cards) {
            tally += c.yanivValue();
        }

        return tally;
    }
}

class PlayerList implements Publisher<Player>, Iterable<Player> {
    private List<Subscriber<? super Player>> subscribers;
    private List<Player> players;

    private Dealer dealer;

    public PlayerList(Dealer dealer) {
        this.subscribers = new ArrayList<>();
        this.players = new ArrayList<>();

        this.dealer = dealer;
    }

    public int size() {
        return players.size();
    }

    /**
     * Returns the Dealer associated with this PlayerList
     * 
     * @return the Dealer associated with this PlayerList
     */
    public Dealer getDealer() {
        return dealer;
    }

    /**
     * Called by {@code p} to join the game. Returns {@code true} if the player
     * joined successfully
     * 
     * @param p the {@link Player} that wishes to join
     * @return {@code true} if the player joined successfully, {@code false}
     *         otherwise (too many players, etc.)
     */
    public boolean join(Player p) {
        if (players.size() >= YanivProperties.MAX_PLAYERS) {
            return false;
        }

        boolean added = players.add(p);
        if (!added) {
            return false;
        }

        for (Subscriber<? super Player> s : subscribers) {
            s.onNext(p);
        }

        return true;
    }

    /**
     * The contract of subscribing is that onNext will be called alternately when a
     * Player joins or is removed from the game.
     */
    @Override
    public void subscribe(Subscriber<? super Player> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public Iterator<Player> iterator() {
        return players.iterator();
    }

    /**
     * Remove the given Player from the game
     */
    public boolean remove(Player p) {
        if (!players.remove(p)) {
            return false;
        }

        for (Subscriber<? super Player> s : subscribers) {
            s.onNext(p);
        }
        return true;
    }

    public Player get(int index) {
        return players.get(index);
    }
}