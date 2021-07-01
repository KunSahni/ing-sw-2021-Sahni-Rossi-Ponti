package it.polimi.ingsw.client.utils.constants;

/**
 * This class contains a set of commands which can be selected by the user when playing on the CLI
 */
public enum Commands {
    PERSONAL_BOARD("show personal board", Constants.SHOW_PERSONAL_BOARD_HELP),
    COMMONS("show commons", Constants.SHOW_COMMONS_HELP),
    PRODUCE("produce", Constants.PRODUCE_HELP),
    BUY_DEVELOPMENT_CARD("buy", Constants.BUY_HELP),
    TAKE_FROM_MARKET("take", Constants.TAKE_FROM_MARKET_HELP),
    ACTIVATE_LEADER_CARD("activate", Constants.ACTIVATE_HELP),
    DISCARD_LEADER_CARD("discard", Constants.DISCARD_HELP),
    HELP("help", Constants.HELP),
    QUIT("quit", Constants.QUIT_HELP),
    PICK_RESOURCES("pick resources", Constants.PICK_RESOURCES_HELP),
    PICK_TEMP_MARBLES("pick marbles", Constants.PICK_MARBLES_HELP),
    PICK_LEADER_CARDS("pick leader cards", Constants.PICK_LEADER_CARDS_HELP),
    LEGEND("show legend", Constants.LEGEND),
    END_TURN("end turn", Constants.END_TURN_HELP);

    private final String command;
    private final String help;

    Commands(String command, String help) {
        this.command = command;
        this.help = help;
    }

    public String getCommand() {
        return command;
    }

    public String getHelp() {
        return help;
    }
}
