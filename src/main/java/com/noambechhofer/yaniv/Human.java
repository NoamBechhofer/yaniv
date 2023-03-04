package com.noambechhofer.yaniv;

import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Human implements Player {

    /** The game instance to tie this Human to */
    private Game game;
    /** the hand */
    private Set<Card> hand;

    public Human(Game game) throws PlayerRefusalException {
        this.game = game;
        this.hand = game.initializePlayer(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Card> hand() {
        LockableHashSet<Card> tmp = new LockableHashSet<>(this.hand);

        tmp.lock();

        return tmp;
    }

    // todo
    /**
     * {@inheritDoc}
     */
    @Override
    public void doTurn() { }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Your hand:\n");

        List<Card> handList = SetSorter.sort(this.hand);

        for (Card c : handList)
            sb.append(String.format("%d. %s\t", handList.indexOf(c) + 1, c.toString()));

        sb.append(String.format("\n Your points: %d", this.game.getPoints(this)));

        return sb.toString();
    }

    // todo: replace with shell command based on platform
    public static void clearTerminal() {
        System.out.println(System.lineSeparator().repeat(50));
    }
}
