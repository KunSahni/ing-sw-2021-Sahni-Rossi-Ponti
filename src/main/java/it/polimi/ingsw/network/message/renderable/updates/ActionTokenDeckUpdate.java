package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
import it.polimi.ingsw.server.model.actiontoken.ActionTokenDeck;

import java.util.ArrayList;
import java.util.List;


//todo: isn't this class useless?

/**
 * This class contains an updated version of the ActionTokenDeck which will be saved in the local DumbModel
 */
public class ActionTokenDeckUpdate extends BroadcastRenderable {
    private final List<ActionToken> updatedActionTokenDeck;

    public ActionTokenDeckUpdate(ActionTokenDeck updatedActionTokenDeck) {
        this.updatedActionTokenDeck = updatedActionTokenDeck.getCurrentDeck();
    }

    /**
     * This method returns an Enum value which represents what type of graphical element is contained in the Renderable
     * @return OnScreenElement enum value representing the payload in the Renderable
     */
    @Override
    public OnScreenElement getOnScreenElement(DumbModel dumbModel){
        return null;
    }

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     * @param dumbModel the dumb model in which the updates will be stored
     */
    @Override
    public void update(DumbModel dumbModel){
        dumbModel.updateActionTokenDeck(updatedActionTokenDeck);
    }

    /**
     * This method is used by the UI to display an element part of the model on the user's screen
     * @param ui the ui which calls the method
     */
    @Override
    public void render(UI ui) {

    }
}
