package it.polimi.ingsw.network.servertoclient.renderable.requests;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;
import it.polimi.ingsw.network.servertoclient.renderable.ConfirmationMessageType;
import it.polimi.ingsw.network.servertoclient.renderable.PrivateRenderable;

/**
 * Generic confirmation message sent to client when the received action is legitimate
 */
public class ConfirmationMessage extends PrivateRenderable {
    private final ConfirmationMessageType confirmationMessage;

    public ConfirmationMessage(ConfirmationMessageType confirmationMessageType) {
        super(null);
        this.confirmationMessage = confirmationMessageType;
    }


    /**
     * This method returns an Enum value which represents what type of graphical element is contained in the Renderable
     *
     * @param dumbModel
     * @return OnScreenElement enum value representing the payload in the Renderable
     */
    @Override
    public OnScreenElement getOnScreenElement(DumbModel dumbModel) {
        return OnScreenElement.DONT_RENDER;
    }

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     *
     * @param dumbModel the dumb model in which the updates will be stored
     */
    @Override
    public void update(DumbModel dumbModel) {
        //no updates needed
    }

    /**
     * This method is used by the UI to display an element part of the model on the user's screen
     *
     * @param ui the ui which calls the method
     */
    @Override
    public void render(UI ui) {
        switch (confirmationMessage){
            case ACTIVATE_LEADER_CARD -> ui.renderActivateLeaderCardConfirmation();
            case ACTIVATE_PRODUCTION -> ui.renderActivateProductionConfirmation();
            case BUY_DEVELOPMENT_CARD -> ui.renderBuyDevelopmentCardConfirmation();
            case DISCARD_LEADER_CARD -> ui.renderDiscardLeaderCardConfirmation();
            case END_TURN -> ui.renderEndTurnConfirmation();
            case PREGAME_LEADER_CARDS_CHOICE -> ui.renderPregameLeaderCardsChoiceConfirmation();
            case PREGAME_RESOURCE_CHOICE -> ui.renderPregameResourceChoiceConfirmation();
            case SELECT_MARBLES -> ui.renderSelectMarblesConfirmation();
            case TAKE_FROM_MARKET -> ui.renderTakeFromMarketConfirmation();
        }
    }
}
