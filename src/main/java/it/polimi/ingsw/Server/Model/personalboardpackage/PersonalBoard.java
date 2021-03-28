package it.polimi.ingsw.server.model.personalboardpackage;

import java.util.*;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gamepackage.Player;

public class PersonalBoard implements VictoryPointsElement {
    private final List<DevelopmentSlot> developmentSlots = new ArrayList<>();
    private final List<LeaderCard> leaderCards = new ArrayList<>();
    private final Player player;
    private final WarehouseDepots warehouseDepots;
    private final Strongbox strongbox;
    private final FaithTrack faithTrack;



    public PersonalBoard(LeaderCard leaderCard1, LeaderCard leaderCard2, Player player) {
        leaderCards.add(leaderCard1);
        leaderCards.add(leaderCard2);
        this.player = player;
        warehouseDepots = new WarehouseDepots();
        strongbox = new Strongbox();
        faithTrack = new FaithTrack(player.getPosition(), this);
    }

    public List<LeaderCard> getLeaderCards() {
        return new ArrayList(leaderCards);
    }

    public Player getPlayer() {
        return player;
    }

    public List<DevelopmentSlot> getDevelopmentSlots() {
        return developmentSlots;
    }

    public WarehouseDepots getWarehouseDepots() {
        return warehouseDepots;
    }

    public Strongbox getStrongBox() {
        return strongbox;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    /**
     * Calculates the number of resources in all storages collectively
     * @return int representing the number of resources
     */
    private int getNumberOfResources() {
        int totalResources = 0;

        totalResources+=strongbox.peekResources()
                .values()
                .stream()
                .reduce(0, Integer::sum);
        totalResources+=warehouseDepots.peekResources()
                .values()
                .stream()
                .reduce(0, Integer::sum);
        totalResources+=leaderCards.stream()
                .filter(x -> x instanceof StoreLeaderCard)
                .map(x -> (StoreLeaderCard) x)
                .map(StoreLeaderCard::peekResources)
                .mapToInt(x -> x.values().iterator().next())
                .sum();
        return totalResources;
    }

    /**
     * The method calculates the sum of victory points obtained via faith track,
     * development cards, leader cards and resource amount
     * @return int representing the number of victory points
     */
    @Override
    public int getVictoryPoints() {
        int totalVictoryPoints = 0;

        totalVictoryPoints+=getNumberOfResources()/5;
        totalVictoryPoints+=faithTrack.getVictoryPoints();
        totalVictoryPoints+=leaderCards.stream()
                .mapToInt(LeaderCard::getVictoryPoints)
                .sum();
        totalVictoryPoints+=developmentSlots.stream()
                .filter(x -> x instanceof DevelopmentCardSlot)
                .map(x -> (DevelopmentCardSlot) x)
                .mapToInt(DevelopmentCardSlot::getVictoryPoints)
                .sum();

        return totalVictoryPoints;
    }

    /**
     * Delegates arrangement selection to controller which will update
     * all storage elements.
     * @param addedResources incoming resources from the market
     */
    public void storeResources(Map<Resource, Integer> addedResources) {
        // controller.dispatchStoreResources(addedResources, currentResourceArrangement)
        // currentResourceArrangement is an object of class ResourceArrangement
        // used to communicate the availability of resource slots with the controller
    }

    /**
    * @param card gets removed from the board's card list
    */
    public void discardLeaderCard(LeaderCard card) {
        leaderCards.remove(card);
    }
    /**
    * @param card has just been purchased and will be placed on the personal board
    * @param position will define which position the card will occupy
    */
    public void placeDevelopmentCard(DevelopmentCard card, int position) {

    }
}