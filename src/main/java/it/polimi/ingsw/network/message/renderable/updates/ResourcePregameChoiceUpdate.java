package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;

public class ResourcePregameChoiceUpdate extends PrivateRenderable {
    private final int numberOfResources;

    public ResourcePregameChoiceUpdate(int numberOfResources) {
        this.numberOfResources = numberOfResources;
    }

    @Override
    public void render(UI ui) {
        ui.renderResourcePregameChoice(numberOfResources);
    }
}
