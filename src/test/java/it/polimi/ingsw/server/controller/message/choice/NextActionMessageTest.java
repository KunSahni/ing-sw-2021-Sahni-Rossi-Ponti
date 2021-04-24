package it.polimi.ingsw.server.controller.message.choice;

import it.polimi.ingsw.server.controller.message.choice.NextActionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.server.controller.message.action.Actions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NextActionMessageTest {
    NextActionMessage nextActionMessage;
    List<Actions> nextActions;

    @BeforeEach
    void init(){
        nextActions = List.of(Actions.ACTIVATELEADERCARDACTION);
        nextActionMessage = new NextActionMessage(nextActions);
    }
    @Test
    @DisplayName("Next actions have been set correctly")
    void SetNextActionTest(){
        assertEquals(nextActions, nextActionMessage.getNextActions());
    }
}
