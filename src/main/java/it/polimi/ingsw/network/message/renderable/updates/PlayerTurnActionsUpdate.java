package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;
import it.polimi.ingsw.server.model.utils.Actions;

import java.util.List;

/**
 *  This class contains an update regarding the valid actions requested by the client in the current turn
 */
public class PlayerTurnActionsUpdate extends PrivateRenderable {
    List<Actions> updatedTurnActions;

    public PlayerTurnActionsUpdate(String nickname, List<Actions> updatedTurnActions) {
        super(nickname);
        this.updatedTurnActions = updatedTurnActions;
    }

    @Override
    public void render(UI ui) {
        ui.updateTurnActions(updatedTurnActions);
    }
}
