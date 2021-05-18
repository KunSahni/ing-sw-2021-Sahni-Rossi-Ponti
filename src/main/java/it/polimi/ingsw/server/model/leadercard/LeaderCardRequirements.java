package it.polimi.ingsw.server.model.leadercard;

import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.utils.Resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Data structure created to store and parse the set of resources required to activate a
 * LeaderCard
 */
public class LeaderCardRequirements implements Serializable {
    public static class LevelQuantityPair implements Serializable{
        private final Level level;
        private final int quantity;

        public LevelQuantityPair(Level level, int quantity) {
            this.level = level;
            this.quantity = quantity;
        }

        public Level getLevel() {
            return level;
        }

        public int getQuantity() {
            return quantity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LevelQuantityPair that = (LevelQuantityPair) o;
            return quantity == that.quantity && level == that.level;
        }
    }
    private Map<Color, LevelQuantityPair> requiredDevelopmentCards;
    private Map<Resource, Integer> requiredResources;

    /**
     * Constructor for a LeaderCardRequirements
     * @param requiredDevelopmentCards requirement map or null
     * @param requiredResources requirement map or null
     */
    public LeaderCardRequirements(Map<Color, LevelQuantityPair> requiredDevelopmentCards,
                                  Map<Resource, Integer> requiredResources) {
        if (Optional.ofNullable(requiredDevelopmentCards).isPresent()) {
            this.requiredDevelopmentCards = mapDeepCopy(requiredDevelopmentCards);
        }
        if (Optional.ofNullable(requiredResources).isPresent()) {
            this.requiredResources = new HashMap<>(requiredResources);
        }
    }

    /**
     * Returns a map with entries which specify Color, Level and quantity
     * of required DevelopmentCards. If the LeaderCard does not have any
     * DevelopmentCard-related requirements then the method returns null.
     * @return null or a Map of Colors and Level-quantity pairs.
     */
    public Map<Color, LevelQuantityPair> getRequiredDevelopmentCards() {
        if (Optional.ofNullable(requiredDevelopmentCards).isPresent()) {
            return mapDeepCopy(requiredDevelopmentCards);
        } else {
            return null;
        }
    }

    /**
     * Returns a map with Resource:quantity entries that represents the amount
     * of Resources needed for activation. If the LeaderCard does not have any
     * resource-related requirements then the method returns null.
     * @return null or a Map of Resource-quantity pairs.
     */
    public Map<Resource, Integer> getRequiredResources() {
        if (Optional.ofNullable(requiredResources).isPresent()) {
            return new HashMap<>(requiredResources);
        } else {
            return null;
        }
    }

    /**
     * Creates a deep copy of the given Color, LevelQuantityPair Map
     * @param givenMap initial Color, LevelQuantityPair map
     * @return result of the deep copy
     */
    private Map<Color, LevelQuantityPair> mapDeepCopy(Map<Color, LevelQuantityPair> givenMap) {
        Map<Color, LevelQuantityPair> copyMap = new HashMap<>(givenMap);
        givenMap.forEach((key, value) -> copyMap.put(
                key, new LevelQuantityPair(value.getLevel(), value.getQuantity())
        ));
        return copyMap;
    }
}
