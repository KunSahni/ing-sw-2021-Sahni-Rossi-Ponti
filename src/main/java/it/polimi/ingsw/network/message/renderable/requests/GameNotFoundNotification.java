package it.polimi.ingsw.network.message.renderable.requests;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;

/**
 * This class represents a notification which is sent to the user when he tries to reconnect to a game
 * and that exact game is already finished, so the user can't be reconnected to it.
 */
public class GameNotFoundNotification extends Notification{

    public GameNotFoundNotification() {
        super("We're sorry, but the requested game is finished, you can either join a new game or quit the app.");
    }

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     * @param dumbModel the dumb model in which the updates will be stored
     */
    @Override
    public void update(DumbModel dumbModel) {
        dumbModel.updateGameID(-1);
        dumbModel.updateGameSize(0);
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
        super.render(ui);
    }
}
