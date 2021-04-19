package it.polimi.ingsw.server.model.leadercard;

import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.utils.Resource;

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
    private final Map<Color, LevelQuantityPair> requiredDevelopmentCards;
    private final Map<Resource, Integer> requiredResources;

    public LeaderCardRequirements() {
        requiredDevelopmentCards = new HashMap<>();
        requiredResources = new HashMap<>();
    }

    public Map<Color, LevelQuantityPair> getRequiredDevelopmentCards() {
        return requiredDevelopmentCards;
    }

    public Map<Resource, Integer> getRequiredResources() {
        return requiredResources;
    }
}
