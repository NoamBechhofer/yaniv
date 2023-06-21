package com.noambechhofer.yaniv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.stream.Stream;

import com.noambechhofer.yaniv.players.Player;

class PlayerList implements Publisher<Player>, Iterable<Player> {
    private List<Subscriber<? super Player>> subscribers;
    private List<Player> players;

    private int maxPlayers;

    public PlayerList(int maxPlayers) {
        this.subscribers = new ArrayList<>();
        this.players = new ArrayList<>();

        this.maxPlayers = maxPlayers;
    }

    public Stream<Player> stream() {
        return players.stream();
    }

    public int indexOf(Player p) {
        return players.indexOf(p);
    }

    public int numPlayers() {
        return players.size();
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
        if (players.size() >= maxPlayers) {
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
     * 
     * @param p the Player to remove
     * @return {@code true} if the Player was removed, {@code false} if the Player
     *         was not in the game
     */
    /*
     * Justification for suppressing S2250:
     * there are never that many players that there would be a significant
     * performance hit when using the O(n) remove method
     */
    @SuppressWarnings("java:S2250")
    public boolean remove(Player p) {
        if (!players.remove(p)) {
            return false;
        }

        for (Subscriber<? super Player> s : subscribers) {
            s.onNext(p);
        }
        p.onGameLoss();

        return true;
    }

    public Player get(int index) {
        return players.get(index);
    }

    /**
     * Returns the next player in the list after the given player
     * 
     * @param player the player to get the next player of
     * @return the next player in the list
     */
    public Player playerAfter(Player player) {
        int index = players.indexOf(player);
        index = (index + 1) % players.size();
        return players.get(index);
    }
}