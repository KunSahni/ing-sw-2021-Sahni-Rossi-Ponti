package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.leadercard.*;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.utils.Actions;

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
    private final List<Actions> performedActions;
    private final Map<MarketMarble, Integer> tempMarbles;

    public PlayerPrivateUpdate(Player updatedPlayer) {
        super(updatedPlayer.getNickname());
        this.position = updatedPlayer.getPosition();
        this.updatedTurnStatus = updatedPlayer.isPlayersTurn();
        this.updatedConnectionStatus = updatedPlayer.isConnected();
        this.tempLeaderCards = updatedPlayer.getTempLeaderCards().stream().map(LeaderCard::convertToDumb)
                .collect(Collectors.toList());
        this.performedActions = updatedPlayer.getPerformedActions();
        this.tempMarbles = updatedPlayer.getTempMarbles();
    }

    @Override
    public void render(UI ui) {
        ui.updatePersonalBoard(getNickname(), position, updatedTurnStatus, updatedConnectionStatus);
        ui.updateTurnActions(performedActions);
        if(tempMarbles != null && tempMarbles.size()>0)
            ui.renderTempMarbles(tempMarbles);
        else if(tempLeaderCards != null && tempLeaderCards.size()>0)
            ui.renderLeaderCardsChoice(tempLeaderCards);
    }
}
