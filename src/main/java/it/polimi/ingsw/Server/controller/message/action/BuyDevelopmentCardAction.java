package it.polimi.ingsw.server.controller.message.action;

import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.controller.gamepackage.Turn;
import it.polimi.ingsw.server.model.utils.Resource;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

import java.util.Map;

/**
 * This class represents the action of buying a DevelopmentCard
 */
public class BuyDevelopmentCardAction implements Forwardable {
    private final PersonalBoard board;
    private final DevelopmentCardsBoard developmentCardsBoard;
    private final Level level;
    private final Color color;
    private final int position;
    private final Map<String, Map<Resource, Integer>> discardedResources;

    /**
     * @param turn the turn in which this action is happening
     * @param level the level of the chosen LeaderCard
     * @param color the color of the chosen LeaderCard
     * @param position the positon on the PersonalBoard where the DevelopmentCard will be placed
     * @param discardedResources the resources discarded in order to buy the DevelopmentCard
     */
    public BuyDevelopmentCardAction(Turn turn, Level level, Color color, int position, Map<String, Map<Resource, Integer>> discardedResources) {
        board = turn.getPlayer().getPersonalBoard();
        developmentCardsBoard = turn.getGame().getDevelopmentCardsBoard();
        this.level = level;
        this.color = color;
        this.position = position;
        this.discardedResources = discardedResources;
    }

    @Override
    public void forward() {
        buyDevelopmentCard();
    }

    /*
    todo: this should be moved to View (maybe ModelView?)
    public List<DevelopmentCard> affordableCards() {
        return Arrays.stream(developmentCardsBoard.peekBoard()).filter(
                developmentCards -> Arrays.stream(developmentCards).filter(
                        developmentCard -> board.hasResources(developmentCard.getCost())
                )
        ).collect(Collectors.toList());
    }
    */

    /**
     * This method manages the transaction for buying  a DevelopmentCard
     */
    private void buyDevelopmentCard() {
        board.discardResources(discardedResources.get("depots"), discardedResources.get("strongbox"));
        board.placeDevelopmentCard(developmentCardsBoard.pick(level, color), position);
    }

}