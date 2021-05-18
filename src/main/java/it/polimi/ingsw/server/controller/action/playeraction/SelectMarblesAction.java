package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.server.controller.action.gameaction.GameAction;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.leadercard.ConvertLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.utils.Actions;
import it.polimi.ingsw.server.model.utils.ResourceBank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SelectMarblesAction extends PlayerAction {
    private final Map<MarketMarble, Integer> selectedMarbles;

    public SelectMarblesAction(Map<MarketMarble, Integer> selectedMarbles) {
        this.selectedMarbles = selectedMarbles;
    }

    @Override
    public GameAction execute() {
        player.getPersonalBoard().storeInDepots(ResourceBank.getResourceFromMarble(selectedMarbles));
        int othersIncrement = player.getTempMarbles().values().stream().reduce(0, Integer::sum)
                - selectedMarbles.values().stream().reduce(0, Integer::sum);
        moveOtherMarkers(othersIncrement);
        player.addAction(Actions.STORED_MARKET_RESOURCES_ACTION);
        return null;
    }

    @Override
    public void runChecks() throws InvalidActionException {
        super.runChecks();
        if (!player.isValidNextAction(Actions.STORED_MARKET_RESOURCES_ACTION))
            throw new InvalidActionException("You cannot store market resources at this time.");
        if (!tempMarblesContainSelectedMarbles())
            throw new InvalidActionException("The marbles you have supplied are not an acceptable" +
                    " subset of the marbles you have taken from the market.");
        if (!player.getPersonalBoard()
                .depotsCanContain(
                        ResourceBank.getResourceFromMarble(selectedMarbles)))
            throw new InvalidActionException("Your depots cannot contain the selected resources.");
    }

    /**
     * Check if the selected marbles are a sub map of the currently stored TempMarbles,
     * also accounting for active ConvertLeaderCards
     */
    private boolean tempMarblesContainSelectedMarbles() {
        // Convert tempMarbles map to a list of marbles
        Map<MarketMarble, Integer> playerTempMarbles = player.getTempMarbles();
        playerTempMarbles.remove(MarketMarble.RED);
        List<MarketMarble> tempMarblesList = new ArrayList<>();
        playerTempMarbles.forEach((k, v) -> {
            for (int i = 0; i < v; i++) {
                tempMarblesList.add(k);
            }
        });
        // Convert selectedMarbles map to a list of marbles
        List<MarketMarble> selectedMarblesList = new ArrayList<>();
        selectedMarbles.forEach((k, v) -> {
            for (int i = 0; i < v; i++) {
                selectedMarblesList.add(k);
            }
        });
        // Create a list containing all the active ConvertLeaderCards
        List<ConvertLeaderCard> activeConvertCards = player.getPersonalBoard()
                .getLeaderCards()
                .stream()
                .filter(card -> card.getAbility().equals(LeaderCardAbility.CONVERT))
                .map(card -> (ConvertLeaderCard) card)
                .collect(Collectors.toList());
        // Subtract the tempMarbles from the selectedMarbles one by one.
        // If the resulting list is empty the selected marbles do not exceed
        // the tempMarbles.
        tempMarblesList.forEach(marble -> {
            if (!marble.equals(MarketMarble.WHITE)) {
                selectedMarblesList.remove(marble);
            } else {
                // Handle WHITE marbles in case there are some active
                // ConvertLeaderCards
                if (activeConvertCards.get(0) != null) {
                    // If the first ConvertLeaderCard does not remove
                    // any marble from the list, check if there is a
                    // second one active and use that.
                    if (!selectedMarblesList
                            .remove(activeConvertCards.get(0)
                                    .getConvertedResource()
                                    .toMarble())
                            && activeConvertCards.get(1) != null) {
                        selectedMarblesList
                                .remove(activeConvertCards.get(1)
                                        .getConvertedResource()
                                        .toMarble());
                    }
                }
            }
        });
        return tempMarblesList.size() == 0;
    }

    /**
     * This method moves all the markers and then checks if someone has passed
     * an valid Pope's place
     * @param steps faith increase for all NPCs
     */
    private void moveOtherMarkers(int steps) {
        // Move all other markers
        Stream<Player> otherPlayersStream =
                game.getPlayerList().stream().filter(npc -> npc != player);
        otherPlayersStream.forEach(
                npc -> IntStream.range(0, steps).forEach(
                       $ -> npc.getPersonalBoard().getFaithTrack().moveMarker()
                )

        );
        otherPlayersStream.forEach(
                npc -> {
                    int faithPos = npc.getPersonalBoard().getFaithTrack().getFaithMarkerPosition();
                    if (npc.getPersonalBoard().getFaithTrack().checkVaticanReport(faithPos / 8 * 8))
                        game.getPlayerList().forEach(
                                pl -> pl.getPersonalBoard().getFaithTrack().flipPopesFavor(faithPos / 8 * 8)
                        );
                }
        );
    }
}
