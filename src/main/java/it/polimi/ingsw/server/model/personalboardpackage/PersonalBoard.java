package it.polimi.ingsw.server.model.personalboardpackage;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;
import it.polimi.ingsw.server.model.leadercard.StoreLeaderCard;
import it.polimi.ingsw.server.model.utils.ChangesHandler;
import it.polimi.ingsw.server.model.utils.Resource;
import it.polimi.ingsw.server.model.utils.ResourceManager;
import it.polimi.ingsw.server.model.utils.VictoryPointsElement;

public class PersonalBoard implements VictoryPointsElement {
    private final ChangesHandler changesHandler;
    private final List<DevelopmentCardSlot> developmentCardSlots;
    private List<LeaderCard> leaderCards;
    private final FaithTrack faithTrack;
    private final ResourceManager warehouseDepots, strongbox;
    private final String nickname;

    public PersonalBoard(ChangesHandler changesHandler, String nickname) throws FileNotFoundException {
        this.changesHandler = changesHandler;
        this.nickname = nickname;
        this.leaderCards = changesHandler.readPlayerLeaderCards(nickname);
        this.developmentCardSlots = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            developmentCardSlots.add(changesHandler.readDevelopmentCardSlot(nickname, i));
        }
        this.faithTrack = changesHandler.readFaithTrack(nickname);
        this.warehouseDepots = changesHandler.readWarehouseDepots(nickname);
        this.strongbox = changesHandler.readStrongbox(nickname);
    }

    public List<LeaderCard> getLeaderCards() {
        return new ArrayList<>(leaderCards);
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

    public ResourceManager getWarehouseDepots() {
        return warehouseDepots;
    }

    public int getResourceCount() {
        return getTotalResources().values().stream().reduce(0, Integer::sum);
    }

    public void setLeaderCards(List<LeaderCard> leaderCards) {
        this.leaderCards = new ArrayList<>(leaderCards);
        changesHandler.writePlayerLeaderCards(nickname, leaderCards);
    }

    /**
     * Checks if the given requirements, needed to activate a leader card, are fulfilled
     */
    public boolean containsLeaderCardRequirements(LeaderCardRequirements requirements) {
        if (Optional.ofNullable(requirements.getRequiredResources()).isPresent()) {
            return containsResources(requirements.getRequiredResources());
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

//    /**
//     * @return shallow copy of all currently stored resources by the player
//     */
//    public Map<Resource, Integer> getResources() {
//        return new HashMap<>(proxyStorage.getStoredResources());
//    }

    /**
     * Returns true if the resources listed in parameter are available
     */
    public boolean containsResources(Map<Resource, Integer> requirement) {
        Map<Resource, Integer> totalResourcesMap = getTotalResources();
        requirement.forEach((resource, quantity) -> totalResourcesMap.compute(resource,
                (k, v) -> (v == null) ? -quantity : v - quantity));
        return totalResourcesMap.values().stream().noneMatch(v -> v < 0);
    }

    public boolean depotsContainResources(Map<Resource, Integer> resources) {
        Map<Resource, Integer> allResourcesInDepots =
                new HashMap<>(warehouseDepots.getStoredResources());
        leaderCards.stream()
                .filter(card -> card.isActive() && card.getAbility().equals(LeaderCardAbility.STORE))
                .map(card -> (StoreLeaderCard) card)
                .forEach(card -> allResourcesInDepots.compute(
                        card.getStoredType(),
                        (k, v) -> (v == null) ? card.getResourceCount() :
                                v + card.getResourceCount()
                ));
        resources.forEach((key, value) ->
                allResourcesInDepots.compute(key, (k, v) ->
                        (v == null) ? -value : v - value)
        );
        return allResourcesInDepots.values().stream().allMatch(val -> val >= 0);
    }

    public boolean strongboxContainsResources(Map<Resource, Integer> resource) {
        Map<Resource, Integer> resourcesInStrongbox = new HashMap<>(strongbox.getStoredResources());
        resource.forEach((key, value) ->
                resourcesInStrongbox.compute(key, (k, v) -> (v == null) ? -value : v - value)
        );
        return resourcesInStrongbox.values().stream().allMatch(val -> val >= 0);
    }

    public boolean depotsCanContain(Map<Resource, Integer> resources) {
        Map<Resource, Integer> resourcesToStore = new HashMap<>(resources);
        // Verify effects of StoreLeaderCards
        leaderCards.stream()
                .filter(card -> card.isActive()
                        && card.getAbility().equals(LeaderCardAbility.STORE))
                .map(card -> (StoreLeaderCard) card)
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
        warehouseDepots.getStoredResources()
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
     * Discards resources from depots
     *
     * @param resources map of resources that will get discarded
     */
    public void discardFromDepots(Map<Resource, Integer> resources) {
        Map<Resource, Integer> discardMapWarehouse =
                warehouseDepots.getStoredResources().keySet().stream()
                        .filter(resources::containsKey)
                        .collect(Collectors.toMap(
                                k -> k,
                                k -> Integer.min(resources.get(k),
                                        warehouseDepots.getStoredResources().get(k))));
        warehouseDepots.discardResources(discardMapWarehouse);
        leaderCards.stream()
                .filter(x -> (x instanceof StoreLeaderCard) && x.isActive())
                .map(leaderCard -> (StoreLeaderCard) leaderCard)
                .forEach(storeLeaderCard -> storeLeaderCard.discardResources(
                        new HashMap<>() {{
                            put(
                                    storeLeaderCard.getStoredType(),
                                    resources.get(storeLeaderCard.getStoredType()) -
                                            Optional.ofNullable(discardMapWarehouse.get(storeLeaderCard.getStoredType())).orElse(0)
                            );
                        }}
                ));
        changesHandler.writePlayerLeaderCards(nickname, leaderCards);
        changesHandler.writeWarehouseDepots(nickname, warehouseDepots);
    }

    /**
     * Discards resources from the strongbox
     *
     * @param resources map of resources that will get discarded
     */
    public void discardFromStrongbox(Map<Resource, Integer> resources) {
        strongbox.discardResources(resources);
        changesHandler.writeStrongbox(nickname, strongbox);
    }

    /**
     * @return total victory points achieved on the board
     */
    @Override
    public int getVictoryPoints() {
        int totalVictoryPoints = 0;
        totalVictoryPoints += getResourceCount() / 5;
        totalVictoryPoints += faithTrack.getVictoryPoints();
        totalVictoryPoints += leaderCards.stream()
                .mapToInt(LeaderCard::getVictoryPoints)
                .sum();
        totalVictoryPoints += developmentCardSlots.stream()
                .mapToInt(DevelopmentCardSlot::getVictoryPoints)
                .sum();
        return totalVictoryPoints;
    }

    /**
     * Stores the given resources in the Warehouse Depots and, if possible, in Storage
     * Leader Cards
     *
     * @param resources map containing the resources to add
     */
    public void storeInDepots(Map<Resource, Integer> resources) {
        Map<Resource, Integer> storeInLeaderCards = leaderCards.stream()
                .filter(x -> (x instanceof StoreLeaderCard) && x.isActive())
                .map(x -> (StoreLeaderCard) x)
                .filter(storeLeaderCard -> storeLeaderCard.getResourceCount() < 2 && resources.containsKey(storeLeaderCard.getStoredType()))
                .collect(Collectors.toMap(
                        StoreLeaderCard::getStoredType,
                        storeLeaderCard -> Integer.min(
                                2 - storeLeaderCard.getResourceCount(),
                                resources.get(storeLeaderCard.getStoredType())
                        )
                ));
        leaderCards.stream()
                .filter(leaderCard -> (leaderCard instanceof StoreLeaderCard) && leaderCard.isActive())
                .map(leaderCard -> (StoreLeaderCard) leaderCard)
                .filter(storeLeaderCard -> storeInLeaderCards.containsKey(storeLeaderCard.getStoredType()))
                .forEach(storeLeaderCard -> storeLeaderCard.storeResources(
                        new HashMap<>() {{
                            put(storeLeaderCard.getStoredType(),
                                    storeInLeaderCards.get(storeLeaderCard.getStoredType()));
                        }}
                ));
        storeInLeaderCards.forEach((k, v) -> resources.put(k, resources.get(k) - v));
        warehouseDepots.storeResources(resources);
        changesHandler.writePlayerLeaderCards(nickname, leaderCards);
        changesHandler.writeWarehouseDepots(nickname, warehouseDepots);
    }

    /**
     * Stores the given resources in the Strongbox
     *
     * @param resources map containing the resources to add
     */
    public void storeInStrongbox(Map<Resource, Integer> resources) {
        strongbox.storeResources(resources);
        changesHandler.writeStrongbox(nickname, strongbox);
    }

    /**
     * @param card gets removed from the board's card list
     */
    public void discardLeaderCard(LeaderCard card) {
        leaderCards.remove(card);
        changesHandler.writePlayerLeaderCards(nickname, leaderCards);
    }

    /**
     * Activates the LeaderCard which matches the one passed
     * as parameter.
     *
     * @param targetCard card which will get activated.
     */
    public void activateLeaderCard(LeaderCard targetCard) {
        leaderCards.stream()
                .filter(card -> card.equals(targetCard))
                .forEach(LeaderCard::activate);
        changesHandler.writePlayerLeaderCards(nickname, leaderCards);
    }

    public void placeDevelopmentCard(DevelopmentCard card, int slotIndex) {
        developmentCardSlots.get(slotIndex - 1).placeCard(card);
    }

    /**
     * Returns the number of purchased development cards
     */
    public int getDevelopmentCardsCount() {
        return developmentCardSlots.stream()
                .mapToInt(DevelopmentCardSlot::getCardsNumber)
                .sum();
    }

    private Map<Resource, Integer> getTotalResources() {
        Map<Resource, Integer> totalResourcesMap = new HashMap<>();
        List<Map<Resource, Integer>> storages = new ArrayList<>();
        storages.add(strongbox.getStoredResources());
        storages.add(warehouseDepots.getStoredResources());
        leaderCards.stream()
                .filter(card -> card.isActive()
                        && card.getAbility().equals(LeaderCardAbility.STORE))
                .map(card -> (StoreLeaderCard) card)
                .filter(card -> card.getResourceCount() > 0)
                .forEach(card -> storages.add(card.getStoredResources()));
        storages.forEach(
                resourceIntegerMap -> resourceIntegerMap.forEach((resource, quantity) ->
                        totalResourcesMap.merge(resource, quantity, (k, v) -> v + quantity)
                )
        );
        return totalResourcesMap;
    }
}