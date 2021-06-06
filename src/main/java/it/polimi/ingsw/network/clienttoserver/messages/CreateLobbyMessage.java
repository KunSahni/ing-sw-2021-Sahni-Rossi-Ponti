package it.polimi.ingsw.network.clienttoserver.messages;

/**
 * This class represents a message sent by the client when the server asks the client to create a new game
 */
public class CreateLobbyMessage implements Message{
    private final int size;

    public CreateLobbyMessage(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
