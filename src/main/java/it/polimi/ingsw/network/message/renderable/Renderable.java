package it.polimi.ingsw.network.message.renderable;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;

import java.io.Serializable;

/**
 * Wrapper class for all model items that are meant to
 * be rendered via GUI / CLI .
 */
public interface Renderable extends Serializable {
    /**
     * This method returns an Enum value which represents what type of graphical element is contained in the Renderable
     * @return OnScreenElement enum value representing the payload in the Renderable
     */
    OnScreenElement getOnScreenElement(DumbModel dumbModel);

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     * @param dumbModel the dumb model in which the updates will be stored
     */
    void update(DumbModel dumbModel);

    /**
     * This method is used by the UI to display an element part of the model on the user's screen
     * @param ui the ui which calls the method
     */
    void render(UI ui);
}
