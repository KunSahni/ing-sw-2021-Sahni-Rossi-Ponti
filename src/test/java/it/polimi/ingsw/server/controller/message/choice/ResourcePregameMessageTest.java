package it.polimi.ingsw.server.controller.message.choice;

import it.polimi.ingsw.server.controller.message.choice.ResourcePregameMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourcePregameMessageTest {
    ResourcePregameMessage resourcePregameMessage;

    @BeforeEach
    void init(){
        resourcePregameMessage = new ResourcePregameMessage(3);
    }

    @Test
    @DisplayName("Number of Resources has been set")
    void setResourcePregameTest() {
        assertEquals(3, resourcePregameMessage.getNumberOfResources());
    }
}
