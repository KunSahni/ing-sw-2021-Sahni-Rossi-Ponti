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
     */
    //TODO: evaluate if this is actually needed
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
     * Discards resources from depots
     * @param resources map of resources that will get discarded
     */
    public void discardFromDepots(Map<Resource, Integer> resources) {
        Map <Resource, Integer> discardMapWarehouse = warehouseDepots.getStoredResources().keySet().stream()
                .filter(resources::containsKey)
                .collect(Collectors.toMap(
                        k -> k,
                        k -> Integer.min(resources.get(k), warehouseDepots.getStoredResources().get(k))));
        warehouseDepots.discardResources(discardMapWarehouse);
        leaderCards.stream()
                .filter(x -> (x instanceof StoreLeaderCard) && x.isActive())
                .map(leaderCard -> (StoreLeaderCard) leaderCard)
                .forEach(storeLeaderCard -> storeLeaderCard.discardResources(
                        new HashMap<>() {{
                            put(
                                    storeLeaderCard.getStoredType(),
                                    resources.get(storeLeaderCard.getStoredType())-
                                            Optional.ofNullable(discardMapWarehouse.get(storeLeaderCard.getStoredType())).orElse(0)
                            );
                        }}
                ));
        proxyStorage.discardResources(resources);
    }

    /**
     * Discards resources from the strongbox
     * @param resources map of resources that will get discarded
     */
    public void discardFromStrongbox(Map<Resource, Integer> resources) {
        strongbox.discardResources(resources);
        proxyStorage.discardResources(resources);
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
     * Stores the given resources in the Warehouse Depots and, if possible, in Storage
     * Leader Cards
     * @param resources map containing the resources to add
     */
    public void storeInDepots(Map<Resource, Integer> resources) {
        proxyStorage.storeResources(resources);
        Map<Resource, Integer> storeInLeaderCards = leaderCards.stream()
                .filter(x -> (x instanceof StoreLeaderCard) && x.isActive())
                .map(x -> (StoreLeaderCard) x)
                .filter(storeLeaderCard -> storeLeaderCard.getResourceCount() < 2 && resources.containsKey(storeLeaderCard.getStoredType()))
                .collect(Collectors.toMap(
                        StoreLeaderCard::getStoredType,
                        storeLeaderCard -> Integer.min(
                                2-storeLeaderCard.getResourceCount(),
                                resources.get(storeLeaderCard.getStoredType())
                        )
                ));
        leaderCards.stream()
                .filter(leaderCard -> (leaderCard instanceof StoreLeaderCard) && leaderCard.isActive())
                .map(leaderCard -> (StoreLeaderCard) leaderCard)
                .filter(storeLeaderCard -> storeInLeaderCards.containsKey(storeLeaderCard.getStoredType()))
                .forEach(storeLeaderCard -> storeLeaderCard.storeResources(
                        new HashMap<>(){{
                            put(storeLeaderCard.getStoredType(), storeInLeaderCards.get(storeLeaderCard.getStoredType()));
                        }}
                ));
        storeInLeaderCards.forEach((k, v) -> resources.put(k, resources.get(k)-v));
        warehouseDepots.storeResources(resources);
    }

    /**
     * Stores the given resources in the Strongbox
     * @param resources map containing the resources to add
     */
    public void storeInStrongbox(Map<Resource, Integer> resources) {
        proxyStorage.storeResources(resources);
        strongbox.storeResources(resources);
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