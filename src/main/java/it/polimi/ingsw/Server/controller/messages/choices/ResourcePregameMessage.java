package it.polimi.ingsw.server.controller.messages.choices;

/**
 * This class contains the amount of resources which the Player needs to pick at the beginning of the game
 */
public class ResourcePregameMessage implements Message{
    private final int numberOfResources;

    public ResourcePregameMessage(int numberOfResources) {
        this.numberOfResources = numberOfResources;
    }

    public int getNumberOfResources() {
        return numberOfResources;
    }
}
