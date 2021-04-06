package it.polimi.ingsw.server.model.personalboardpackage;

import java.util.*;
import java.util.stream.Collectors;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.Player;

public class PersonalBoard implements VictoryPointsElement {
    private final List<DevelopmentSlot> developmentSlots;
    private final List<LeaderCard> leaderCards;
    private final Player player;
    private final FaithTrack faithTrack;
    private final ResourceManager warehouseDepots, strongbox, proxyStorage;



    public PersonalBoard(LeaderCard leaderCard1, LeaderCard leaderCard2, Player player) {
        developmentSlots = new ArrayList<>();
        leaderCards = new ArrayList<>();
        leaderCards.add(leaderCard1);
        leaderCards.add(leaderCard2);
        this.player = player;
        faithTrack = new FaithTrack(player.getPosition(), this);
        warehouseDepots = new ResourceManager();
        strongbox = new ResourceManager();
        proxyStorage = new ResourceManager();
    }

    public List<LeaderCard> getLeaderCards() {
        return new ArrayList<>(leaderCards);
    }

    public Player getPlayer() {
        return player;
    }

    public List<DevelopmentSlot> getDevelopmentSlots() {
        return new ArrayList<>(developmentSlots);
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public ResourceManager getStrongbox() {
        return strongbox;
    }

    /**
     * @return shallow copy of all currently stored resources by the player
     */ //TODO: evaluate if this is actually needed
    public Map<Resource, Integer> getResources() {
        return new HashMap<>(proxyStorage.getStoredResources());
    }

    /**
     * @param discardFromDepots resources discarded from leader cards and depots, all Integers must be >0
     * @param discardFromStrongbox resources discarded from strongbox
     */
    public void discardResources(Map<Resource, Integer> discardFromDepots, Map<Resource, Integer> discardFromStrongbox) {
        Map <Resource, Integer> discardMapWarehouse = warehouseDepots.getStoredResources().keySet().stream()
                .filter(discardFromDepots::containsKey)
                .collect(Collectors.toMap(
                        k -> k,
                        k -> Integer.min(discardFromDepots.get(k), warehouseDepots.getStoredResources().get(k))));
        warehouseDepots.discardResources(discardMapWarehouse);
        leaderCards.stream()
                .filter(x -> (x instanceof StoreLeaderCard) && x.isActive())
                .map(x -> (StoreLeaderCard) x)
                .forEach(x -> x.getStorageManager().discardResources(
                        new HashMap<>() {{
                            put(
                                    x.getStoredType(),
                                    discardFromDepots.get(x.getStoredType())-
                                            Optional.ofNullable(discardMapWarehouse.get(x.getStoredType())).orElse(0)
                            );
                        }}
                ));
        strongbox.discardResources(discardFromStrongbox);
        proxyStorage.discardResources(discardFromDepots);
        proxyStorage.discardResources(discardFromStrongbox);
    }

    /**
     * @return total victory points achieved on the board
     */
    @Override
    public int getVictoryPoints() {
        int totalVictoryPoints = 0;
        totalVictoryPoints+=proxyStorage.getResourceCount()/5;
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
     * @param addedResources incoming resources from the market, will give discard choice to the player
     *                       and then store in the most optimal manner
     */
    public void storeResources(Map<Resource, Integer> addedResources) {
        Map<Resource, Integer> trimmedResources = controller.dispatchDiscardChoice(addedResources);
        proxyStorage.storeResources(trimmedResources);
        Map<Resource, Integer> storeInLeaderCards = leaderCards.stream()
                .filter(x -> (x instanceof StoreLeaderCard) && x.isActive())
                .map(x -> (StoreLeaderCard) x)
                .filter(x -> x.getStorageManager().getResourceCount() < 2 && trimmedResources.containsKey(x.getStoredType()))
                .collect(Collectors.toMap(
                        StoreLeaderCard::getStoredType,
                        x -> Integer.min(
                                2-x.getStorageManager().getResourceCount(),
                                trimmedResources.get(x.getStoredType())
                        )
                ));
        leaderCards.stream()
                .filter(x -> (x instanceof StoreLeaderCard) && x.isActive())
                .map(x -> (StoreLeaderCard) x)
                .filter(x -> storeInLeaderCards.containsKey(x.getStoredType()))
                .forEach(x -> x.getStorageManager().storeResources(
                        new HashMap<>(){{
                            put(x.getStoredType(), storeInLeaderCards.get(x.getStoredType()));
                        }}
                ));
        storeInLeaderCards.forEach((k, v) -> trimmedResources.put(k, trimmedResources.get(k)-v));
        warehouseDepots.storeResources(trimmedResources);
    }

    /**
    * @param card gets removed from the board's card list
    */
    public void discardLeaderCard(LeaderCard card) {
        leaderCards.remove(card);
        faithTrack.moveMarker();
    }
    /**
    * @param card has just been purchased and will be placed on the personal board
    * @param position will define which position the card will occupy
    */
    public void placeDevelopmentCard(DevelopmentCard card, int position) {
        DevelopmentCardSlot targetSlot = (DevelopmentCardSlot) developmentSlots.get(position);
        targetSlot.placeCard(card);
    }
}