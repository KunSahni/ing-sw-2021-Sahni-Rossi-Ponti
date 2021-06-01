package it.polimi.ingsw.network.message.renderable;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;

/**
 * Error message created server-side and transmitted client-side
 * via network
 */
public class ErrorMessage extends PrivateRenderable{
    private final String message;

    public ErrorMessage(String nickname, String message){
        super(nickname);
        this.message = message;
    }

    public ErrorMessage(String message){
        super(null);
        this.message = message;
    }

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     * @param dumbModel the dumb model in which the updates will be stored
     */
    @Override
    public void update(DumbModel dumbModel) {
        //empty because messages don't need to update anything in the dumb model
    }

    /**
     * This method is used by the UI to display an element part of the model on the user's screen
     * @param ui the ui which calls the method
     */
    @Override
    public void render(UI ui) {
        ui.renderErrorMessage(message);
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
