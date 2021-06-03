package it.polimi.ingsw.client.utils.constants;

public enum Commands {
    PERSONAL_BOARD("show personal board"),
    COMMONS("show commons"),
    PRODUCE("produce"),
    BUY_DEVELOPMENT_CARD("buy"),
    TAKE_FROM_MARKET("take"),
    ACTIVATE_LEADER_CARD("activate"),
    DISCARD_LEADER_CARD("discard"),
    HELP("help"),
    QUIT("quit"),
    PICK("pick");

    private final String command;

    Commands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
