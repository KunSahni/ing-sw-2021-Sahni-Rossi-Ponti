package it.polimi.ingsw.client.gui;

public enum FXMLResources {
    MAIN_MENU("MainMenu"),
    CONNECT_FOOTER("ConnectFooter"),
    LOADING_FOOTER("LoadingFooter"),
    NICKNAME_FOOTER("NicknameFooter"),
    PLAYER_SELECTION_FOOTER("PlayerSelectionFooter");

    private final String label;

    FXMLResources(String label) {
        this.label = label;
    }

    public String toPathString() {
        return "/fxml/" + label + ".fxml";
    }
}
