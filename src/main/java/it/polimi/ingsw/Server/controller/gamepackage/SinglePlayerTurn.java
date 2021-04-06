package it.polimi.ingsw.server.controller.gamepackage;

import it.polimi.ingsw.server.controller.messages.actions.Forwardable;
import it.polimi.ingsw.server.model.ActionToken;
import it.polimi.ingsw.server.controller.messages.actions.DiscardDevelopmentCardsAction;
import it.polimi.ingsw.server.controller.messages.actions.MoveBlackCrossAction;
import it.polimi.ingsw.server.controller.messages.actions.ResetAndMoveAction;
import it.polimi.ingsw.server.controller.messages.actions.EndAction;

/**
 * This class represents a single Turn in a single player game
 */
public class SinglePlayerTurn extends Turn {
    private ActionToken actionToken;

    public SinglePlayerTurn(Game game, Player player) {
        super(game, player);
    }

    /**
     * @return picks an ActionToken from the ActionTokenDeck and returns its corresponding AbstractTurnState
     */
    private Forwardable pickActionToken(){
        actionToken = this.getGame().getActionTokenDeck().pop();

        if(actionToken.equals(ActionToken.MOVEBYTWO))
            return new MoveBlackCrossAction(this);
        else if(actionToken.equals((ActionToken.MOVEANDSHUFFLE)))
            return new ResetAndMoveAction(this);
        else
            return new DiscardDevelopmentCardsAction(this, actionToken);
    }

    /**
     * @param nextAction    the next Action that the Player wants to do
     */
    @Override
    public void nextAction(Forwardable nextAction) {
        if(nextAction instanceof EndAction && this instanceof SinglePlayerTurn){    //todo: ha senso il secondo controllo?
            nextAction = pickActionToken();
            super.nextAction(nextAction);
        }
        if(nextAction instanceof MoveBlackCrossAction || nextAction instanceof ResetAndMoveAction || nextAction instanceof DiscardDevelopmentCardsAction)
            super.nextAction(new EndAction());

        super.nextAction(nextAction);
    }
}