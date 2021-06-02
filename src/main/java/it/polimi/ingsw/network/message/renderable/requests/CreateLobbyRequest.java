package it.polimi.ingsw.network.message.renderable.requests;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;

/**
 * This class represent a request sent by the server.
 * In this request the server asks the client to select the number of players for the next game.
 */
public class CreateLobbyRequest extends PrivateRenderable {
    private final String message;

    public CreateLobbyRequest() {
        super(null);
        this.message = "How many players (1-4) will be in the next game?";
    }

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     * @param dumbModel the dumb model in which the updates will be stored
     */
    @Override
    public void update(DumbModel dumbModel) {
        //empty because requests don't need to update anything in the dumb model
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
        ui.renderCreateLobbyRequest(message);
    }
}
