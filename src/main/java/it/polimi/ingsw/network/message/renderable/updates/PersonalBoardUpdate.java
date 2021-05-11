package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;

/**
 *  This class contains an updated version of a PersonalBoard which will be saved in the local DumbModel
 */
public class PersonalBoardUpdate extends BroadcastRenderable {
    private final PersonalBoard personalBoard;

    public PersonalBoardUpdate(PersonalBoard personalBoard) {
        this.personalBoard = personalBoard;
    }

    @Override
    public void render(UI ui) {
        ui.renderPersonalBoard(personalBoard);
    }
}
