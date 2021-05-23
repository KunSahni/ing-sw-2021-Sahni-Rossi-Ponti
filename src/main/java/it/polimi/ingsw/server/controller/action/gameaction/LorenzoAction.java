package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsDeck;
import it.polimi.ingsw.server.model.personalboard.SinglePlayerFaithTrack;

import java.util.Arrays;

public class LorenzoAction extends GameAction {
    public LorenzoAction(Game game) {
        super(game);
    }

    @Override
    public GameAction execute() {
        switch (game.getActionTokenDeck().pop()) {
            case MOVE_BY_TWO -> moveBlackCross(2);
            case MOVE_AND_SHUFFLE -> {
                moveBlackCross(1);
                game.getActionTokenDeck().reset();
            }
            case REMOVE_BLUE -> game.getDevelopmentCardsBoard().discardTwo(Color.BLUE);
            case REMOVE_GREEN -> game.getDevelopmentCardsBoard().discardTwo(Color.GREEN);
            case REMOVE_PURPLE -> game.getDevelopmentCardsBoard().discardTwo(Color.PURPLE);
            case REMOVE_YELLOW -> game.getDevelopmentCardsBoard().discardTwo(Color.YELLOW);
        }
        GameAction consequentAction = null;
        // Checks if any of the 4 colors has no cards left on the
        // DevelopmentCardsBoard
        if (Arrays.stream(Color.values()).anyMatch(color ->
                        Arrays.stream(game.getDevelopmentCardsBoard().peekBoard())
                                .flatMap(Arrays::stream)
                                .map(DevelopmentCardsDeck::peek)
                                .noneMatch(card -> card.getColor().equals(color))
                // Checks if the Black cross has reached the last tile on the Faith Track
        ) || ((SinglePlayerFaithTrack) game.getPlayerList().get(0).getPersonalBoard().getFaithTrack())
                .getBlackCrossPosition() == 24) {
            game.end();
        } else consequentAction = new StartNextTurnAction(game);
        return consequentAction;
    }

    private void moveBlackCross(int steps) {
        SinglePlayerFaithTrack faithTrack = (SinglePlayerFaithTrack) game.getPlayerList().get(0)
                .getPersonalBoard().getFaithTrack();
        for (int i = 0; i < steps; i++) {
            faithTrack.moveBlackCross();
            if (faithTrack.checkVaticanReport(faithTrack.getBlackCrossPosition())) {
                faithTrack.flipPopesFavor(faithTrack.getBlackCrossPosition() / 8);
            }
        }
    }
}
