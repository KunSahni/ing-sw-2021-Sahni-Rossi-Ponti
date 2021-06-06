package it.polimi.ingsw.network.servertoclient.renderable.requests;

import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;
import it.polimi.ingsw.network.servertoclient.renderable.PrivateRenderable;

/**
 * This class represent a notification which will be displayed on the user's screen
 */
public abstract class Notification extends PrivateRenderable {
    public final String message;

    public Notification(String message) {
        super(null);
        this.message = message;
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
}
