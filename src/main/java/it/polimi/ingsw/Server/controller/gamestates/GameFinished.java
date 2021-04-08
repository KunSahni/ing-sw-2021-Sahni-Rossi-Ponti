package it.polimi.ingsw.server.controller.gamestates;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.model.Player;

import java.util.*;
import java.util.stream.Collectors;


public class GameFinished extends AbstractGameState {
    public GameFinished(Game game) {
        super(game);
    }

    /**
     * Executes end game logic: victory points calculation handling
     */
    @Override
    public void run() {
        Map<Player, Integer> playersPoints = collectVictoryPoints();
        Map<Player, Integer> sortedPlayersPoints = sortLeaderboard(playersPoints);
        setOutcome(sortedPlayersPoints);
    }

    /**
     * Creates a map with player:victoryPoints for sorting purposes
     */
    private Map<Player, Integer> collectVictoryPoints() {
        Map<Player, Integer> playersPoints = new HashMap<>();
        game.getPlayers().forEach(p -> playersPoints.put(p, p.getPersonalBoard().getVictoryPoints()));
        return playersPoints;
    }

    /**
     * Returns a map which represents the descending sorted equivalent of the given map
     */
    private Map<Player, Integer> sortLeaderboard(Map<Player, Integer> playersPoints) {
        return playersPoints.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    /**
     * Updates the model, sets each player's finishing ranking and victory points
     */
    private void setOutcome(Map<Player, Integer> sortedPlayersPoints) {
        int index = 0;
        for (Map.Entry<Player, Integer> entry : sortedPlayersPoints.entrySet()) {
            entry.getKey().setRank(index + 1);
            entry.getKey().setVictoryPoints(entry.getValue());
            index++;
        }
    }
}