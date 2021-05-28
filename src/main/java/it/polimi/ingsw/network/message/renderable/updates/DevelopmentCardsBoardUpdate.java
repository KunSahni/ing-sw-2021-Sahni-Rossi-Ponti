package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCardsBoard;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsDeck;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class contains an updated version of DevelopmentCardsBoard which will be saved in the local DumbModel
 */
public class DevelopmentCardsBoardUpdate extends BroadcastRenderable {
    private final DumbDevelopmentCard[][] updatedDevelopmentCardsBoard;

    public DevelopmentCardsBoardUpdate(DevelopmentCardsBoard updatedDevelopmentCardsBoard) {
        this.updatedDevelopmentCardsBoard = new DumbDevelopmentCard[3][4];
        IntStream.range(0,3).forEach(
                i->IntStream.range(0,4).forEach(
                        j-> this.updatedDevelopmentCardsBoard[i][j] = updatedDevelopmentCardsBoard.peekBoard()[i][j].peek()==null? null : new DumbDevelopmentCard(updatedDevelopmentCardsBoard.peekBoard()[i][j].peek())
                )
        );
    }

    @Override
    public void render(UI ui) {
        ui.updateDevelopmentCardsBoard(updatedDevelopmentCardsBoard);
    }
}
