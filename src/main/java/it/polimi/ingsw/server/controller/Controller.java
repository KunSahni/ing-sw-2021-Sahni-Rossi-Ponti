package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.action.Action;
import it.polimi.ingsw.server.controller.action.gameaction.GameAction;
import it.polimi.ingsw.server.controller.action.playeraction.InvalidActionException;
import it.polimi.ingsw.server.controller.action.playeraction.PlayerAction;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.remoteview.RemoteView;

import java.util.Optional;
import java.util.concurrent.Flow.*;

public class Controller implements Subscriber<PlayerAction> {
    private final Game game;
    private RemoteView remoteView;
    private Subscription subscription;

    public Controller(Game game) {
        this.game = game;
    }

    public void setRemoteView(RemoteView remoteView) {
        this.remoteView = remoteView;
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
        System.out.println("Error occurred: " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Controller is not subscribed to RemoteView anymore.");
    }
}
