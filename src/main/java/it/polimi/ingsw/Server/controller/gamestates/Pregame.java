package it.polimi.ingsw.server.controller.gamestates;


import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.model.Player;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Flow.*;

public class Pregame extends AbstractGameState implements Subscriber<Object> {
    private int waitingForPlayers;
    private Subscription subscription;

    public Pregame(Game game) {
        super(game);
        waitingForPlayers = game.getSize();
    }

    /**
     * Deal 4 leader cards to each player and wait for them to
     * choose 2 out of these. Then randomly assign the inkwell
     * and all relative positions to the players. Wait for them
     * to pick the extra resources.
     */
    @Override
    public synchronized void run(){
        dealLeaderCards();
        waitForAllPlayersChoice();
        waitingForPlayers = game.getSize();
        assignInkwell();
        waitForAllPlayersChoice();
        game.setNextState(new InGame(game));

    }
    private void dealLeaderCards() {
        game.getPlayers().forEach(p -> p.drawLeaderCards(game.getLeaderCardsDeck().popFour()));
    }

    private void assignInkwell() {
        List<Player> playerList = Collections.shuffle(game.getPlayers())
        for(int i = 1; i <= game.getSize(); i++) {
            playerList.get(i).setPosition(i);
        }
        game.sortPlayers();
    }

    private void waitForAllPlayersChoice() {
        while(waitingForPlayers > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public synchronized void onNext(Object item) {
        ((Forwardable) item).forward();
        subscription.request(1);
        waitingForPlayers--;
        notifyAll();
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Error occurred: " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Pregame subscription complete.");
    }
}