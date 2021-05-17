package it.polimi.ingsw.client;

//todo: implement pub/sub for rendering

public abstract class UI {
    private String nickname;

    public abstract void renderMessage(String message);
    public abstract void renderErrorMessage(String message);
    public abstract void renderAuthenticationRequest(String message);
    public abstract void renderCreateLobbyRequest(String message);
}
