package it.polimi.ingsw.network.clienttoserver.messages;

/**
 * This class represents a message sent by the client to authenticate himself with the server
 */
public class AuthenticationMessage implements Message{
    private final String nickname;
    private final int requestedGameID;

    public AuthenticationMessage(String nickname, int requestedGameID) {
        this.nickname = nickname;
        this.requestedGameID = requestedGameID;
    }

    public String getNickname() {
        return nickname;
    }

    public int getRequestedGameID() {
        return requestedGameID;
    }
}
