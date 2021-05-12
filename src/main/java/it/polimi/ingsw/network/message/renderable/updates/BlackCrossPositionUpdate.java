package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;

/**
 * This class contains an update regarding the black cross's position on the faith track
 */
public class BlackCrossPositionUpdate extends PrivateRenderable {
    private final int updatedBlackCrossPosition;

    public BlackCrossPositionUpdate(int updatedBlackCrossPosition) {
        this.updatedBlackCrossPosition = updatedBlackCrossPosition;
    }

    @Override
    public void render(UI ui) {
        ui.updateBlackCrossPosition(updatedBlackCrossPosition);
    }
}
