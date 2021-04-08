package it.polimi.ingsw.server.model;

import java.util.Map;

public class LeaderCardRequirements {
    private Map<Color, Integer> colorIntegerRequirement;
    private Map<Color, Level> colorLevelRequirement;
    private Map<Resource, Integer> resourceIntegerRequirement;

    public void setColorIntegerRequirement(Map<Color, Integer> colorIntegerRequirement) {
        this.colorIntegerRequirement = colorIntegerRequirement;
    }

    public void setColorLevelRequirement(Map<Color, Level> colorLevelRequirement) {
        this.colorLevelRequirement = colorLevelRequirement;
    }

    public void setResourceIntegerRequirement(Map<Resource, Integer> resourceIntegerRequirement) {
        this.resourceIntegerRequirement = resourceIntegerRequirement;
    }

    public Map<Color, Integer> getColorIntegerRequirement() {
        return colorIntegerRequirement;
    }

    public Map<Color, Level> getColorLevelRequirement() {
        return colorLevelRequirement;
    }

    public Map<Resource, Integer> getResourceIntegerRequirement() {
        return resourceIntegerRequirement;
    }
}
