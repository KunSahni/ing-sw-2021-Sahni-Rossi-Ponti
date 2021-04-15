package it.polimi.ingsw.server.model.personalboardpackage;

import java.util.*;
import java.util.stream.Collectors;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;
import it.polimi.ingsw.server.model.leadercard.StoreLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;
import it.polimi.ingsw.server.model.utils.ResourceManager;
import it.polimi.ingsw.server.model.utils.VictoryPointsElement;

public class PersonalBoard implements VictoryPointsElement {
    private final List<DevelopmentCardSlot> developmentCardSlots;
    private final List<LeaderCard> leaderCards;
    private final Player player;
    private final FaithTrack faithTrack;
    private final ResourceManager warehouseDepots, strongbox, proxyStorage;

    public PersonalBoard(Player player) {
        this.developmentCardSlots = new ArrayList<>(Arrays.asList(
                new DevelopmentCardSlot(),
                new DevelopmentCardSlot(),
                new DevelopmentCardSlot()));
        this.leaderCards = new ArrayList<>();
        this.player = player;
        this.faithTrack = new FaithTrack(player.getPosition(), this);
        this.warehouseDepots = new ResourceManager();
        this.strongbox = new ResourceManager();
        this.proxyStorage = new ResourceManager();
    }

    public List<LeaderCard> getLeaderCards() {
        return new ArrayList<>(leaderCards);
    }

    public Player getPlayer() {
        return player;
    }

    public List<DevelopmentCardSlot> getDevelopmentCardSlots() {
        return new ArrayList<>(developmentCardSlots);
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public ResourceManager getStrongbox() {
        return strongbox;
    }

    public int getResourceCount() {
        return proxyStorage.getResourceCount();
    }

    public void setLeaderCards(List<LeaderCard> leaderCards) {
        this.leaderCards.addAll(leaderCards);
    }

    /**
     * Checks if the given requirements, needed to activate a leader card, are fulfilled
     */
    public boolean containsLeaderCardRequirements(LeaderCardRequirements requirements) {
        if(Optional.ofNullable(requirements.getRequiredResources()).isPresent()) {
            return proxyStorage.contains(requirements.getRequiredResources());
        } else {
            return requirements.getRequiredDevelopmentCards()
                    .entrySet()
                    .stream()
                    .map(
                            e -> e.getValue().getQuantity() <= developmentCardSlots.stream()
                                    .flatMap(d -> d.getDevelopmentCards().stream())
                                    .map(c -> e.getKey() == c.getColor() &&
                                            c.getLevel().compareTo(e.getValue().getLevel()) >= 0)
                                    .filter(b -> b)
                                    .count()
                    ).reduce(true, (a, b) -> a && b);
        }
    }
    /**
     * @return shallow copy of all currently stored resources by the player
     */ //TODO: evaluate if this is actually needed
    public Map<Resource, Integer> getResources() {
        return new HashMap<>(proxyStorage.getStoredResources());
    }

    /**
     * Returns true if the resources listed in parameter are available
     */
    public boolean containsResources(Map<Resource, Integer> requirement) {
        return proxyStorage.contains(requirement);
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
        totalVictoryPoints+=developmentCardSlots.stream()
                .mapToInt(DevelopmentCardSlot::getVictoryPoints)
                .sum();
        return totalVictoryPoints;
    }

    /**
     * Stores in depots and, if possible, in leader cards the addedResources map
     */
    public void storeResources(Map<Resource, Integer> addedResources) {
        proxyStorage.storeResources(addedResources);
        Map<Resource, Integer> storeInLeaderCards = leaderCards.stream()
                .filter(x -> (x instanceof StoreLeaderCard) && x.isActive())
                .map(x -> (StoreLeaderCard) x)
                .filter(x -> x.getStorageManager().getResourceCount() < 2 && addedResources.containsKey(x.getStoredType()))
                .collect(Collectors.toMap(
                        StoreLeaderCard::getStoredType,
                        x -> Integer.min(
                                2-x.getStorageManager().getResourceCount(),
                                addedResources.get(x.getStoredType())
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
        storeInLeaderCards.forEach((k, v) -> addedResources.put(k, addedResources.get(k)-v));
        warehouseDepots.storeResources(addedResources);
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
        developmentCardSlots.get(position).placeCard(card);
    }

    /**
     * Returns the number of purchased development cards
     */
    public int getDevelopmentCardsCount() {
        return developmentCardSlots.stream()
                .mapToInt(DevelopmentCardSlot::getCardsNumber)
                .sum();
    }
}