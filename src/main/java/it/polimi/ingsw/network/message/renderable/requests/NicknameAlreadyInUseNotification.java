package it.polimi.ingsw.network.message.renderable.requests;

import it.polimi.ingsw.client.UI;

public class NicknameAlreadyInUseNotification extends Notification{
    public NicknameAlreadyInUseNotification(String message) {
        super("The nickname you have selected is already in use.");
    }

    @Override
    public void render(UI ui) {
        ui.renderNicknameAlreadyInUseNotification(message);
    }
}
