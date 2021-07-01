package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.connection.Connection;
import it.polimi.ingsw.network.clienttoserver.action.Action;
import it.polimi.ingsw.network.clienttoserver.action.gameaction.*;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.InvalidActionException;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.PlayerAction;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.remoteview.RemoteView;

import java.util.Optional;
import java.util.concurrent.Flow.*;

/**
 * Handles game related logic via Action legality checks and execution.
 * It's the outer most level used by the server to interact with a running game.
 */
public class Controller implements Subscriber<PlayerAction> {
    private final Game game;
    private RemoteView remoteView;
    private Subscription subscription;

    /**
     * Gets instantiated when the model abstraction (a Game Object) has already
     * been created. After this point, the game passed as parameter is running.
     * @param game Game instance associated 1:1 with the controller.
     */
    public Controller(Game game) {
        this.game = game;
        runGame();
    }

    /**
     * Handles the linking between Model and server-side View by subscribing
     * the latter to the former.
     * @param remoteView Server-side view that will received all updates from
     *                   the Model associated with the Controller's game attribute.
     */
    public void setRemoteView(RemoteView remoteView) {
        this.remoteView = remoteView;
        game.subscribe(remoteView);
    }

    /**
     * Communicates to the RemoteView and to the Model that the player with the
     * nickname passed as parameter has connected. The player will start receiving
     * Model updates.
     * @param nickname Connected Player's nickname.
     * @param connection Connection Object associated with the passed nickname's
     *                   Player. Will receive all Model updates.
     */
    public void connectPlayer(String nickname, Connection connection) {
        remoteView.connectPlayer(nickname, connection);
        game.connect(nickname);
        game.getCurrentTurnPlayer().ifPresent(player -> {
            if (!player.isConnected())
                handleGameAction(new StartNextTurnAction(game));
        });
    }

    /**
     * Communicates to the RemoteView and to the Model that the player with the
     * nickname passed as parameter has disconnected. The server will stop sending
     * Model updates to that player's Connection object.
     * @param nickname Disconnected Player's nickname.
     */
    public void disconnectPlayer(String nickname) {
        remoteView.disconnectPlayer(nickname);
        game.disconnect(nickname);
        if (game.getPlayer(nickname).isTurn()) {
            handleGameAction(new StartNextTurnAction(game));
        }
    }

    /**
     * Runs the Game associated with the Controller. Depending on the state the
     * game is in, a specific configuration will be ran.
     */
    private void runGame() {
        GameAction initiator = switch (game.getCurrentState()) {
            case NOT_STARTED -> new DealLeaderCardsAction(game);
            case PICKED_LEADER_CARDS -> new AssignInkwellAction(game);
            case PICKED_RESOURCES -> new StartGameAction(game);
            case IN_GAME, LAST_ROUND -> game.getCurrentTurnPlayer().isPresent()
                    ? game.getCurrentTurnPlayer().get().getPerformedActions().size() > 0
                    ? game.getCurrentTurnPlayer().get().getPerformedActions().get(
                    game.getCurrentTurnPlayer().get().getPerformedActions().size() - 1
            ).equals(ExecutedActions.TURN_ENDED_ACTION) ? new StartNextTurnAction(game) : null
                    : null
                    : new StartNextTurnAction(game);
            case ASSIGNED_INKWELL, DEALT_LEADER_CARDS -> null;
        };
        handleGameAction(initiator);
    }

    /**
     * Executes a GameAction and all its consequent GameActions.
     * @param gameAction GameAction to be executed.
     */
    private synchronized void handleGameAction(GameAction gameAction) {
        Action currentAction = gameAction;
        while (currentAction != null) {
            currentAction = currentAction.execute();
        }
    }

    /**
     * Runs legality checks on the passed PlayerAction. If legal, the Action is
     * executed, otherwise an error gets sent to the player.
     * @param playerAction PlayerAction which will be checked and executed.
     */
    private synchronized void handlePlayerAction(PlayerAction playerAction) {
        try {
            playerAction.runChecks();
            remoteView.sendConfirmation(playerAction.getNickname(),
                    playerAction.getConfirmationMessage());
            Optional<GameAction> consequentAction = Optional.ofNullable(playerAction.execute());
            consequentAction.ifPresent(this::handleGameAction);
        } catch (InvalidActionException e) {
            remoteView.raiseError(playerAction.getNickname(), e.getMessage());
        }
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(PlayerAction playerAction) {
        playerAction.setGame(game);
        this.subscription.request(1);
        handlePlayerAction(playerAction);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Controller is not subscribed to RemoteView anymore.");
    }
}
