package it.polimi.ingsw.network.servertoclient.renderable.requests;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;

/**
 * This class represents a notification which is sent when the lobby is completed
 * and therefore the actual game can be started.
 */
public class GameStartedNotification extends Notification{
    private final int size;

    public GameStartedNotification(int size) {
        super("The game is about to start!");
        this.size = size;
    }

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     * @param dumbModel the dumb model in which the updates will be stored
     */
    @Override
    public void update(DumbModel dumbModel) {
        dumbModel.updateGameSize(size);
    }

    @Override
    public void render(UI ui) {
        ui.renderGameStartedNotification(message);
    }
}
