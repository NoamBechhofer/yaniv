package com.noambechhofer.yaniv;

import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Human implements Player {

    /** The game instance to tie this Human to */
    private Game game;
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
        return this.hand;
    }

    // todo
    /**
     * {@inheritDoc}
     */
    @Override
    public void doTurn() {
        YanivUtils.clearTerminal();

        Set<Card> toDiscard = new HashSet<>();

        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {

            System.out.println(
                    "Which cards do you want to discard? Enter a number, and ctrl-d when you're finished selecting.");
            System.out.println(this);

            Card choice = null;
            try {
                choice = YanivUtils.sortedSet(this.hand).get(sc.nextInt() - 1);
            } catch (NullPointerException e) {
                System.out.println("Index out of range!");

                e.printStackTrace(System.err); // todo: log this properly
                continue;
            } catch (InputMismatchException e) {
                System.out.println("That's not a number, please try again");

                e.printStackTrace(System.err); // todo: log this properly
                continue;
            }

            boolean worked = this.hand.remove(choice) && toDiscard.add(choice);
            assert worked : choice;

            YanivUtils.clearTerminal();

            System.out.println();
            System.out.println("Selected: ");
            for (Card c : toDiscard)
                System.out.printf("%s\t", c.toString());
        }

        sc.close();
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("Your hand:\n");

        List<Card> handList = YanivUtils.sortedSet(this.hand);

        for (Card c : handList)
            sb.append(String.format("%d. %s\t", handList.indexOf(c) + 1, c.toString()));

        sb.append(String.format("\n Your points: %d", this.game.getPoints(this)));

        return sb.toString();
    }
}
