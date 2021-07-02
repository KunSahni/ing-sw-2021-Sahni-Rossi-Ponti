package it.polimi.ingsw.network.servertoclient.renderable.requests;

import it.polimi.ingsw.client.UI;

/**
 * This notification is sent to user when he tries to use a nickname which is already being used in the joined game/lobby
 */
public class NicknameAlreadyInUseNotification extends Notification{
    public NicknameAlreadyInUseNotification() {
        super("The nickname you have selected is already in use.");
    }

    @Override
    public void render(UI ui) {
        ui.renderNicknameAlreadyInUseNotification(message);
    }
}
