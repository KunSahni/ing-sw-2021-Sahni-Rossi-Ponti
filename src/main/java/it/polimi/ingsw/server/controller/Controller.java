package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.connection.Connection;
import it.polimi.ingsw.server.controller.action.Action;
import it.polimi.ingsw.server.controller.action.gameaction.*;
import it.polimi.ingsw.server.controller.action.playeraction.InvalidActionException;
import it.polimi.ingsw.server.controller.action.playeraction.PlayerAction;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.remoteview.RemoteView;

import java.util.Optional;
import java.util.concurrent.Flow.*;

public class Controller implements Subscriber<PlayerAction> {
    private final Game game;
    private RemoteView remoteView;
    private Subscription subscription;

    public Controller(Game game) {
        this.game = game;
        runGame();
    }

    public void setRemoteView(RemoteView remoteView) {
        this.remoteView = remoteView;
        game.subscribe(remoteView);
    }

    public void connectPlayer(String nickname, Connection connection) {
        remoteView.connectPlayer(nickname, connection);
        game.connect(nickname);
    }

    public void disconnectPlayer(String nickname) {
        remoteView.disconnectPlayer(nickname);
        game.disconnect(nickname);
        if (game.getPlayer(nickname).isTurn()) {
            handleGameAction(new StartNextTurnAction(game));
        }
    }

    private void runGame() {
        GameAction initiator = switch (game.getCurrentState()) {
            case NOT_STARTED -> new DealLeaderCardsAction(game);
            case PICKED_LEADER_CARDS -> new AssignInkwellAction(game);
            case PICKED_RESOURCES -> new StartGameAction(game);
            case IN_GAME, LAST_ROUND -> game.getCurrentTurnPlayer()
                    .getPerformedActions()
                    .get(game.getCurrentTurnPlayer().getPerformedActions().size() - 1)
                    .equals(ExecutedActions.TURN_ENDED_ACTION)
                    ? new StartNextTurnAction(game)
                    : null;
            case ASSIGNED_INKWELL, DEALT_LEADER_CARDS -> null;
        };
        handleGameAction(initiator);
    }

    private void handleGameAction(GameAction gameAction) {
        Action currentAction = gameAction;
        while (currentAction != null) {
            currentAction = currentAction.execute();
        }
    }

    private void handlePlayerAction(PlayerAction playerAction) {
        try {
            playerAction.runChecks();
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
