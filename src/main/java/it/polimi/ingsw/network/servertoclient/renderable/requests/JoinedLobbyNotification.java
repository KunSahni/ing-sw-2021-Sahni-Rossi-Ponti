package it.polimi.ingsw.network.servertoclient.renderable.requests;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;

/**
 * This class represents a notification sent when the client is added to a lobby.
 */
public class JoinedLobbyNotification extends Notification {
    private final int gameID;
    private final int size;

    public JoinedLobbyNotification(int gameID, int size) {
        super("You've been added to a lobby!");
        this.gameID = gameID;
        this.size = size;
    }

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     * @param dumbModel the dumb model in which the updates will be stored
     */
    @Override
    public void update(DumbModel dumbModel) {
        dumbModel.updateGameID(gameID);
        dumbModel.updateGameSize(size);
    }

    /**
     * This method returns an Enum value which represents what type of graphical element is contained in the Renderable
     * @return OnScreenElement enum value representing the payload in the Renderable
     */
    @Override
    public OnScreenElement getOnScreenElement(DumbModel dumbModel) {
        return OnScreenElement.FORCE_DISPLAY;
    }

    /**
     * This method is used by the UI to display an element part of the model on the user's screen
     * @param ui the ui which calls the method
     */
    @Override
    public void render(UI ui) {
        ui.renderJoinedLobbyNotification(message);
    }
}
