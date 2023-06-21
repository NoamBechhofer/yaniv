package com.noambechhofer.yaniv;

import java.util.List;
import java.util.Optional;

import com.noambechhofer.yaniv.cards.Deck;
import com.noambechhofer.yaniv.players.Player;
import com.noambechhofer.yaniv.utilities.AbstractSubscriber;

/**
 * A game of Yaniv (consists of multiple rounds. For a single round, see
 * {@link Round})
 */
public class Game implements AbstractSubscriber<Player> {
    private PlayerList players;
    private int numPlayers;

    public Game(int numPlayers) {
        this.numPlayers = numPlayers;
        this.players = new PlayerList(numPlayers);
        players.subscribe(this);
    }

    @Override
    public void onNext(Player item) {
        if (players.numPlayers() >= this.numPlayers) {
            Player winner = play(players);
            winner.onGameWin();
        }
    }

    /**
     * Play a game of Yaniv.
     * 
     * @param players the players in the game
     * 
     * @return the winner of the game
     */
    public Player play(PlayerList players) {
        Deck deck = new Deck();

        Player firstPlayerForNextRound = players.get(0);

        while (players.numPlayers() > 1) {
            Round round = new Round(firstPlayerForNextRound, players, deck);
            firstPlayerForNextRound = round.play();

            List<Player> losers = players.stream()
                    .filter((Player p) -> p.getScore() >= YanivProperties.LOSING_THRESHOLD)
                    .toList();
            if (losers.isEmpty()) {
                continue;
            }

            /*
             * it is definitely possible for all players to be over the threshold: say there
             * are two players. Both are a couple points below the threshold. Player A calls
             * yaniv, but gets assaf'd by player B. Now player B is just past the threshold,
             * while player A is >30 points over it. Player B wins, but all players are
             * above the threshold...
             */
            if (losers.size() == players.numPlayers()) {
                Optional<Player> ret = players.stream().min((Player p1, Player p2) -> p1.getScore() - p2.getScore());
                if (ret.isEmpty()) {
                    throw new AssertionError();
                }
                return ret.get();
            }
            /*
             * Otherwise, we're safe to remove losers without worrying about removing all
             * the players.
             */
            for (Player loser : losers) {
                players.remove(loser);
            }

        }
        if (players.numPlayers() != 1) {
            throw new AssertionError();
        }
        return players.get(0);
    }

}
