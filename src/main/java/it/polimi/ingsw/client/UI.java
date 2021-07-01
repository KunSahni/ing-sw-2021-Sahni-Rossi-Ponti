package it.polimi.ingsw.client;

import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.network.servertoclient.renderable.Renderable;
import it.polimi.ingsw.network.servertoclient.renderable.updates.MultiPlayerGameOutcomeUpdate;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
import it.polimi.ingsw.server.model.market.MarketMarble;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Flow.Subscriber;

/**
 * Interface used by GUI and CLI in order to have a more generic approach
 */
public interface UI extends Subscriber<Renderable> {

    /**
     * Renders a ModelUpdate which contains a copy of the entire model
     */
    void renderModelUpdate();

    /**
     * Renders a personal board based on the passed nickname
     * @param nickname the nickname of the player whose personal board needs to be rendered
     */
    void renderPersonalBoard(String nickname);

    /**
     * Renders game commons, which include development cards board and market
     */
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


    /**
     * Renders an action token
     * @param actionToken the action token which should be rendered
     */
    void renderActionToken(ActionToken actionToken);

    /**
     * Renders game outcome in single player game
     * @param finalScore final score of player
     */
    void renderGameOutcome(int finalScore);

    /**
     * Renders game outcome in multiplayer games
     * @param finalScores a list of tuples sorted on scores. Each tuple contains a player's nickname and its scores
     */
    void renderGameOutcome(List<MultiPlayerGameOutcomeUpdate.ScoreTuple> finalScores);

    /**
     * Renders a list of leader cards from which the user needs to choose one
     * @param leaderCards the list of leader cards which should be rendered
     */
    void renderLeaderCardsChoice(List<DumbLeaderCard> leaderCards);

    /**
     * Renders a map of market marbles from which the user needs to pick the ones he wants to convert into resources
     * @param updateMarbles a map of the market marbles which should be rendered
     */
    void renderTempMarblesChoice(Map<MarketMarble, Integer> updateMarbles);

    /**
     * Renders dialogue for resource choice in pregame based on player's position
     */
    void renderResourcePregameChoice();

    /**
     * Renders a generic error message received from the server
     * @param message a message containing an error which should be rendered
     */
    void renderErrorMessage(String message);

    /**
     * Renders an authentication request
     */
    void renderAuthenticationRequest(String message);

    /**
     * Renders a create lobby request
     */
    void renderCreateLobbyRequest(String message);


    /**
     * Renders a game not found notification
     */
    void renderGameNotFoundNotification(String message);

    /**
     * Renders a game started notification
     */
    void renderGameStartedNotification(String message);

    /**
     * Renders a joined lobby notification
     */
    void renderJoinedLobbyNotification(String message);

    /**
     * Renders a waiting for players notification
     */
    void renderWaitingForPlayersNotification(String message);

    /**
     * Renders a wrong nickname notification
     */
    void renderWrongNicknameNotification(String message);

    /**
     * Renders a nickname already in use notification
     */
    void renderNicknameAlreadyInUseNotification(String message);


    /**
     * Renders a message explaining that the server is offline
     */
    void renderServerOffline();
}
