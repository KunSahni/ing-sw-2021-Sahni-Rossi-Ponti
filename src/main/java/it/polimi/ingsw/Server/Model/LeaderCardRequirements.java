package it.polimi.ingsw.server.model;

import java.util.HashMap;
import java.util.Map;

public class LeaderCardRequirements {
    public static class LevelQuantityPair {
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
    }

    private Map<Color, LevelQuantityPair> requiredDevelopmentCards;
    private Map<Resource, Integer> requiredResources;

    public Map<Color, LevelQuantityPair> getRequiredDevelopmentCards() {
        return requiredDevelopmentCards;
    }

    public void setDevelopmentCardsRequirement(Color color, Level level, int quantity) {
        this.requiredDevelopmentCards = new HashMap<>() {{
            put(color, new LevelQuantityPair(level, quantity));
        }};
    }

    public void setDevelopmentCardsRequirement(Color color, int quantity) {
        this.requiredDevelopmentCards = new HashMap<>() {{
            put(color, new LevelQuantityPair(Level.LEVEL1, quantity));
        }};
    }

    public Map<Resource, Integer> getRequiredResources() {
        return requiredResources;
    }

    public void setRequiredResources(Map<Resource, Integer> requiredResources) {
        this.requiredResources = requiredResources;
    }
}
