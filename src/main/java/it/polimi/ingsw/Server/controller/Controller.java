package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.controller.message.action.Action;
import it.polimi.ingsw.server.controller.message.action.gameaction.GameAction;
import it.polimi.ingsw.server.controller.message.action.playeraction.PlayerAction;

import java.util.Optional;
import java.util.concurrent.Flow.*;

public class Controller implements Subscriber<PlayerAction> {
    private final Game game;
    private Subscription subscription;

    public Controller(Game game) {
        this.game = game;
    }

    public void endGame() {
        // figure out how it will be called by EndTurnAction
    }

    private void handleGameAction(GameAction gameAction) {
        // decide how to handle concurrent GameActions
        Action currentAction = gameAction;
        while (currentAction != null) {
            currentAction = currentAction.execute();
        }
        // endGame call condition
    }

    private void handlePlayerAction(PlayerAction playerAction) {
        if (playerAction.isAllowed()) {
            Optional<GameAction> consequentGameAction = Optional.ofNullable(playerAction.execute());
            consequentGameAction.ifPresent(this::handleGameAction);
        } else {
            // plan how to communicate to the player that the action that was
            // sent to the serer is illegal
        }
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public void onNext(PlayerAction playerAction) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
