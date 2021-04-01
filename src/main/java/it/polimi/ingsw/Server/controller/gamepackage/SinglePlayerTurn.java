package it.polimi.ingsw.server.controller.gamepackage;

import java.util.*;

import it.polimi.ingsw.server.model.ActionToken;
import it.polimi.ingsw.server.controller.gamepackage.actions.Action;
import it.polimi.ingsw.server.controller.gamepackage.singleplayeractions.DiscardDevelopmentCardsAction;
import it.polimi.ingsw.server.controller.gamepackage.singleplayeractions.MoveBlackCrossAction;
import it.polimi.ingsw.server.controller.gamepackage.singleplayeractions.ResetAndMoveAction;
import it.polimi.ingsw.server.controller.gamepackage.singleplayerturnstates.DiscardDevelopmentCards;
import it.polimi.ingsw.server.controller.gamepackage.singleplayerturnstates.MoveBlackCross;
import it.polimi.ingsw.server.controller.gamepackage.singleplayerturnstates.ResetAndMove;
import it.polimi.ingsw.server.controller.gamepackage.turnstates.AbstractTurnState;
import it.polimi.ingsw.server.controller.gamepackage.turnstates.End;

//todo: actionToken is saved twice

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
    private AbstractTurnState pickActionToken(){
        actionToken = this.game.getActionTokenDeck().pop();

        if(actionToken.equals(ActionToken.MOVEBYTWO))
            return new MoveBlackCross();
        else if(actionToken.equals((ActionToken.MOVEANDSHUFFLE)))
            return new ResetAndMove();
        else
            return new DiscardDevelopmentCards();
    }

    //todo: manage singleplayerturnstates

    /**
     * @param nextState the state corresponding to the Action
     * @param action    the next Action that the Player wants to do
     */
    @Override
    public void setNextState(AbstractTurnState nextState, Action action) {
        if(nextState instanceof End){
            nextState = pickActionToken();
            if(nextState instanceof MoveBlackCross)
                super.setNextState(nextState, new MoveBlackCrossAction(this.actionToken));
            else if(nextState instanceof ResetAndMove)
                super.setNextState(nextState, new ResetAndMoveAction(this.actionToken));
            else
                super.setNextState(nextState, new DiscardDevelopmentCardsAction(this.actionToken));
        }

        super.setNextState(nextState, action);
    }

    /**
     * @return a list of all the possible actions that the player can choose
     */
    @Override
    public List<AbstractTurnState> availableNextStates() {
        return super.availableNextStates();
    }
}