package it.polimi.ingsw.client;

import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.network.servertoclient.renderable.Renderable;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
import it.polimi.ingsw.server.model.market.MarketMarble;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Flow.Subscriber;

public interface UI extends Subscriber<Renderable> {

    void renderModelUpdate();

    void renderPersonalBoard(String nickname);

    void renderCommons();

    //Management of confirmation messages
    void renderActivateLeaderCardConfirmation();

    void renderActivateProductionConfirmation();

    void renderBuyDevelopmentCardConfirmation();

    void renderDiscardLeaderCardConfirmation();

    void renderEndTurnConfirmation();

    void renderPregameLeaderCardsChoiceConfirmation();

    void renderPregameResourceChoiceConfirmation();

    void renderSelectMarblesConfirmation();

    void renderTakeFromMarketConfirmation();

    void renderActionToken(ActionToken actionToken);

    void renderGameOutcome(int finalScore);

    void renderGameOutcome(TreeMap<Integer, String> finalScores);

    void renderLeaderCardsChoice(List<DumbLeaderCard> leaderCards);

    void renderTempMarblesChoice(Map<MarketMarble, Integer> updateMarbles);

    void renderResourcePregameChoice();

    void renderErrorMessage(String message);

    void renderAuthenticationRequest(String message);

    void renderCreateLobbyRequest(String message);

    void renderGameNotFoundNotification(String message);

    void renderGameStartedNotification(String message);

    void renderJoinedLobbyNotification(String message);

    void renderWaitingForPlayersNotification(String message);

    void renderWrongNicknameNotification(String message);

    void renderNicknameAlreadyInUseNotification(String message);

    void renderServerOffline();
}
