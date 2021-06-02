package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.leadercard.*;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.utils.ExecutedActions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  This class contains an update regarding the client who receives it
 */
public class PlayerPrivateUpdate extends PrivateRenderable {
    private final int position;
    private final boolean updatedTurnStatus;
    private final boolean updatedConnectionStatus;
    private final List<DumbLeaderCard> tempLeaderCards;
    private final List<ExecutedActions> performedActions;
    private final Map<MarketMarble, Integer> tempMarbles;

    public PlayerPrivateUpdate(Player updatedPlayer) {
        super(updatedPlayer.getNickname());
        this.position = updatedPlayer.getPosition();
        this.updatedTurnStatus = updatedPlayer.isTurn();
        this.updatedConnectionStatus = updatedPlayer.isConnected();
        this.tempLeaderCards = updatedPlayer.getTempLeaderCards().stream().map(LeaderCard::convertToDumb)
                .collect(Collectors.toList());
        this.performedActions = updatedPlayer.getPerformedActions();
        this.tempMarbles = updatedPlayer.getTempMarbles();
    }

    /**
     * This method returns an Enum value which represents what type of graphical element is contained in the Renderable
     * @return OnScreenElement enum value representing the payload in the Renderable
     */
    @Override
    public OnScreenElement getOnScreenElement(DumbModel dumbModel){
        if((tempMarbles != null && tempMarbles.size()>0) || (tempLeaderCards != null && tempLeaderCards.size()>0))
            return OnScreenElement.FORCE_DISPLAY;
        else
            return OnScreenElement.valueOf(dumbModel.getPersonalBoard(getNickname()).getPosition());

    }

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     * @param dumbModel the dumb model in which the updates will be stored
     */
    @Override
    public void update(DumbModel dumbModel){
        dumbModel.updatePersonalBoard(getNickname(), position, updatedTurnStatus, updatedConnectionStatus);
        dumbModel.updateTurnActions(performedActions);
    }

    /**
     * This method is used by the UI to display an element part of the model on the user's screen
     * @param ui the ui which calls the method
     */
    @Override
    public void render(UI ui) {
        if(tempMarbles != null && tempMarbles.size()>0)
            ui.renderTempMarblesChoice(tempMarbles);
        else if(tempLeaderCards != null && tempLeaderCards.size()>0)
            ui.renderLeaderCardsChoice(tempLeaderCards);
    }
}
