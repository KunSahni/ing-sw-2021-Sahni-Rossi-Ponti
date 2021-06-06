package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.leadercard.StoreLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This is a dumber version of a regular PersonalBoard,
 * this class contains data contained in a PersonalBoard but has none of its logic.
 * It also contains some of the data contained in a Player.
 */
public class DumbPersonalBoard {
    private final String nickname;
    private final int position;
    private final DumbFaithTrack faithTrack;
    private final DumbResourceManager depots;
    private final DumbResourceManager strongbox;
    private List<DumbLeaderCard> leaderCards;
    private final List<DumbDevelopmentCardSlot> developmentCardSlots;
    private boolean turnStatus;
    private boolean connectionStatus;

    public DumbPersonalBoard(String nickname, int position, boolean isSinglePlayerGame) {
        super();
        this.nickname = nickname;
        this.position = position;
        this.faithTrack = isSinglePlayerGame? new DumbSinglePlayerFaithTrack() : new DumbFaithTrack();
        this.depots = new DumbResourceManager();
        this.strongbox = new DumbResourceManager();
        this.developmentCardSlots = new ArrayList<>();
        this.developmentCardSlots.add(new DumbDevelopmentCardSlot(1));
        this.developmentCardSlots.add(new DumbDevelopmentCardSlot(2));
        this.developmentCardSlots.add(new DumbDevelopmentCardSlot(3));
    }

    public void updateLeaderCards(List<DumbLeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public void updateTurnStatus(boolean turn) {
        turnStatus = turn;
    }

    public void updateConnectionStatus(boolean connected) {
        connectionStatus = connected;
    }

    public String getNickname() {
        return nickname;
    }

    public int getPosition() {
        return position;
    }

    public DumbFaithTrack getFaithTrack() {
        return faithTrack;
    }

    public DumbResourceManager getDepots() {
        return depots;
    }

    public DumbResourceManager getStrongbox() {
        return strongbox;
    }

    public List<DumbLeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public List<DumbDevelopmentCardSlot> getDevelopmentCardSlots() {
        return developmentCardSlots;
    }

    public boolean getTurnStatus() {
        return turnStatus;
    }

    public boolean getConnectionStatus() {
        return connectionStatus;
    }

    /**
     * Checks if depots + store leader cards can contain the passed map of resources without the need of discarding any resource
     * @param resources the resources which user wants to store
     * @return true if resources can be placed, false otherwise
     */
    public boolean depotsCanContain(Map<Resource, Integer> resources) {
        Map<Resource, Integer> resourcesToStore = new HashMap<>(resources);
        // Verify effects of StoreLeaderCards
        leaderCards.stream()
                .filter(card -> card.isActive()
                        && card.getAbility().equals(LeaderCardAbility.STORE))
                .map(card -> (DumbStoreLeaderCard) card)
                .forEach(card -> {
                    // If there are some resources that can be stored in StoreLeaderCards
                    if (resourcesToStore.get(card.getStoredType()) != null) {
                        // Count as if the LeaderCards will store them (remove them
                        // from ToStore map)
                        resourcesToStore.put(
                                card.getStoredType(),
                                Math.max(
                                        0,
                                        resourcesToStore.get(card.getStoredType())
                                                - (2 - card.getResourceCount())
                                )
                        );
                    }
                });
        // Clean the resulting map before merging to current WarehouseDepots
        Map<Resource, Integer> remainingResources = resourcesToStore.entrySet().stream()
                .filter(entry -> entry.getValue() != 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        depots.getStoredResources()
                .forEach((depotsKey, depotsValue) -> remainingResources.compute(
                        depotsKey,
                        (k, v) -> (v == null) ? depotsValue : v + depotsValue
                ));
        // remainingResources map now contains the stored resources and the WarehouseDepots
        // resources.
        return remainingResources.keySet().size() <= 3
                && remainingResources.values().stream().allMatch(value -> value <= 3)
                && remainingResources.entrySet().stream()
                .filter(entry -> entry.getValue() == 3).count() <= 1
                && remainingResources.entrySet().stream()
                .filter(entry -> entry.getValue() >= 2).count() <= 2;
    }


    /**
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string color of a leader Card with the top left corner in position x,y
     */
    public String formatPrintableStringAt(int x, int y) {
        StringBuilder printableString = new StringBuilder(Constants.ANSI_CLEAR);
        if(turnStatus)
            printableString.append("\033[1;1H" + Constants.ANSI_ORANGE + "Nickname:" + nickname + "(in turn)" + Constants.ANSI_RESET + " \033[1;117Hposition:" + position + "");
        else
            printableString.append("\033[1;1HNickname:" + nickname + " \033[1;117Hposition:" + position + "");
        printableString.append(faithTrack.formatPrintableStringAt(2, 1));
        printableString.append(depots.formatPrintableStringAtAsDepots(8, 1));
        printableString.append(strongbox.formatPrintableStringAtAsStrongbox(10, 22));
        if(leaderCards.get(0)!=null)
            printableString.append(leaderCards.get(0).formatPrintableStringAt(8, 31));
        if(leaderCards.get(1)!=null)
            printableString.append(leaderCards.get(1).formatPrintableStringAt(8, 48));

        printableString.append(developmentCardSlots.get(0).formatPrintableStringAt(8, 52));
        printableString.append(developmentCardSlots.get(1).formatPrintableStringAt(8, 69));
        printableString.append(developmentCardSlots.get(2).formatPrintableStringAt(8, 86));

        return printableString.toString();
    }
}
