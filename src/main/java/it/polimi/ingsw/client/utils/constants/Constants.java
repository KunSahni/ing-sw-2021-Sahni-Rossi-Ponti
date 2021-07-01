package it.polimi.ingsw.client.utils.constants;

/**
 * This class contains a set of constants (messages, ascii escape codes, ascii arts, etc.)
 * used to make a nicer CLI
 */
public final class Constants {
    public final static String ANSI_RESET = "\u001B[0m";
    public final static String ANSI_RED = "\u001b[31;1m";
    public final static String ANSI_GREEN = "\u001b[32;1m";
    public final static String ANSI_YELLOW = "\u001b[33;1m";
    public final static String ANSI_BLUE = "\u001b[34;1m";
    public final static String ANSI_PURPLE = "\u001B[35;1m";
    public final static String ANSI_DARK_GREY = "\u001b[38;5;239m";
    public final static String ANSI_GREY = "\u001b[38;5;244m";
    public final static String ANSI_ORANGE = "\u001b[38;5;208m";
    public final static String ANSI_WHITE = "\u001b[37;1m";
    public final static String ANSI_BOLD = "\u001B[1m";
    public final static String RESOURCE = "\u25A0";
    public final static String MARBLE = "\u25CF";
    public final static String LEVEL = "\u25C8";
    public final static String COIN = ANSI_YELLOW + "\u25A0" + ANSI_RESET;
    public final static String STONE = ANSI_GREY + "\u25A0" + ANSI_RESET;
    public final static String SERVANT = ANSI_PURPLE + "\u25A0" + ANSI_RESET;
    public final static String SHIELD = ANSI_BLUE + "\u25A0" + ANSI_RESET;
    public final static String FAITH_POINT = ANSI_RED + ANSI_BOLD + "\u271D" + ANSI_RESET;
    public final static String FAITH_MARKER = FAITH_POINT;
    public final static String BLACK_CROSS = ANSI_WHITE + ANSI_BOLD + "\u271D" + ANSI_RESET;
    public final static String ANY_RESOURCE = ANSI_BOLD + "?" + ANSI_RESET;
    //public final static String COIN_MARBLE = ANSI_YELLOW + "\u25CF" + ANSI_RESET;
    //public final static String STONE_MARBLE = ANSI_GREY + "\u25CF" + ANSI_RESET;
    //public final static String SERVANT_MARBLE = ANSI_PURPLE + "\u25CF" + ANSI_RESET;
    //public final static String SHIELD_MARBLE = ANSI_BLUE + "\u25CF" + ANSI_RESET;
    public final static String WHITE_MARBLE = ANSI_WHITE + "\u25CF" + ANSI_RESET;
    //public final static String RED_MARBLE = ANSI_RED + "\u25CF" + ANSI_RESET;
    public final static String YELLOW_LEVEL = ANSI_YELLOW + "\u25C8" + ANSI_RESET;
    public final static String BLUE_LEVEL = ANSI_BLUE + "\u25C8" + ANSI_RESET;
    public final static String PURPLE_LEVEL = ANSI_PURPLE + "\u25C8" + ANSI_RESET;
    public final static String GREEN_LEVEL = ANSI_GREEN + "\u25C8" + ANSI_RESET;
    public final static String GREEN_TICK = ANSI_GREEN + "\u2713" + ANSI_RESET;
    public final static String RED_CROSS = ANSI_RED + "\u2A2F" + ANSI_RESET;
    public static final String ANSI_CLEAR = "\033[2J";
    public static final String INPUT_MESSAGE = "\n" + ANSI_BLUE + ">" + ANSI_RESET;
    public static final String MASTER_OF_RENAISSANCE =
            """
                    \033[1;35H█████████████████████████████████████████████████████████████████████
                    \033[2;35H█████████▄─▀█▀─▄██▀▄─██─▄▄▄▄█─▄─▄─█▄─▄▄─█▄─▄▄▀███─▄▄─█▄─▄▄─██████████
                    \033[3;35H██████████─█▄█─███─▀─██▄▄▄▄─███─████─▄█▀██─▄─▄███─██─██─▄████████████
                    \033[4;35H█████████▄▄▄█▄▄▄█▄▄█▄▄█▄▄▄▄▄██▄▄▄██▄▄▄▄▄█▄▄█▄▄███▄▄▄▄█▄▄▄████████████
                    \033[5;35H█████████████████████████████████████████████████████████████████████
                    \033[6;35H█▄─▄▄▀█▄─▄▄─█▄─▀█▄─▄██▀▄─██▄─▄█─▄▄▄▄█─▄▄▄▄██▀▄─██▄─▀█▄─▄█─▄▄▄─█▄─▄▄─█
                    \033[7;35H██─▄─▄██─▄█▀██─█▄▀─███─▀─███─██▄▄▄▄─█▄▄▄▄─██─▀─███─█▄▀─██─███▀██─▄█▀█
                    \033[8;35H█▄▄█▄▄█▄▄▄▄▄█▄▄▄██▄▄█▄▄█▄▄█▄▄▄█▄▄▄▄▄█▄▄▄▄▄█▄▄█▄▄█▄▄▄██▄▄█▄▄▄▄▄█▄▄▄▄▄█
                    \033[9;35H█████████████████████████████████████████████████████████████████████""";

    public static final String WRONG_COMMAND = "Command doesn't exist, type help for help";
    public static final String WRONG_COMMAND_ARGS = "Command wrongly used, possible error in argument typing, type help for help";
    public static final String INVALID_ARGS = "Command was typed properly, but either it can't be executed right now or command arguments are invalid, type help for help";

    public static final String OFFLINE_MESSAGE = "Connection with server was dropped";

    public static final String QUIT_HELP = "quit\n" +
            "\tquit game\n" +
            "\tsyntax: quit";
    public static final String END_TURN_HELP = "end turn\n" +
            "\tends current turn\n" +
            "\tsyntax: end turn";
    public static final String SHOW_LEGEND_HELP = "show legend\n" +
            "\tdisplays legend of symbols used in the game\n" +
            "\tsyntax: show legend";
    public static final String PICK_MARBLES_HELP = "pick marbles\n" +
            "\tpick marbles from the ones picked from market. The selected marbles will be converted to resources and stored in warehouse depots\n" +
            "\tsyntax: pick marbles color\n" +
            "\targuments:\n" +
            "\t\tcolor\tcolor of marble which you want to convert in resource\n" +
            "\n" +
            "\tTo select multiple marbles just write color of each selected marble in the end\n" +
            "\te.g: pick marbles yellow yellow blue\t-> picks 2 yellow marbles and 1 blue marble\n";
    public static final String PICK_LEADER_CARDS_HELP = "pick leader cards\n" +
            "\tpick leader cards at the beginning of the game\n" +
            "\tsyntax: pick leader cards i j\n" +
            "\targuments:\n" +
            "\t\ti\tindex of first picked leader card\n" +
            "\t\tj\tindex of second picked leader card\n";
    public static final String PICK_RESOURCES_HELP = "pick resources\n" +
            "\tpick resources at the beginning of the game\n" +
            "\tsyntax: pick resources i res\n" +
            "\targuments:\n" +
            "\t\ti\tquantity of res\n" +
            "\t\tres\tname of picked resource\n" +
            "\t\t\n" +
            "\t\tYou can add more resources in the end, each separated by space\n" +
            "\t\te.g: pick resources shield coin\t\t-> pick a shield and a coin\n";
    public static final String TAKE_FROM_MARKET_HELP = "take\n" +
            "\ttake resources from market\n" +
            "\tsyntax: take place i\n" +
            "\targuments:\n" +
            "\t\tplace\trow if you want to take from a row\n" +
            "\t\t\tcolumn if you want to take from a column\n" +
            "\t\ti\tnumber of row or column\n";
    public static final String BUY_HELP = "buy\n" +
            "\tbuys a development card from development cards board\n" +
            "\tsyntax: buy level color slotIndex [-depots i res] [-strongbox i res]\n" +
            "\targuments:\n" +
            "\t\tlevel\t\tlevel of selected development card\n" +
            "\t\tcolor\t\tcolor of selected development card\n" +
            "\t\tslotIndex\tindex of development card slot where you want to place the selected development card\t\n" +
            "\toptions:\n" +
            "\t\t-depots i res\t\tif you want to discard resources from warehouse depots\n" +
            "\t\t\t\t\twrite quantity of res instead of i\n" +
            "\t\t\t\t\twrite discarded resource name instead of res\n" +
            "\t\t\t\t\tyou can add more resources in the end, each separated by space\n" +
            "\t\t-strongbox i res\tif you want to discard resources from strongbox\n" +
            "\t\t\t\t\twrite quantity of res instead of i\n" +
            "\t\t\t\t\twrite discarded resource name instead of res\n" +
            "\t\t\t\t\tyou can add more resources in the end, each separated by space\n" +
            "\n" +
            "\t\tIn order to write a valid command at least one of the two options should be present\n" +
            "\t\te.g: buy 1 blue 2 -depots 2 coin -strongbox 2 stone\t-> buys a level 1 blue development card and places it \n" +
            "\t\t\t\t\t\t\t\t\tin the second development card slot while discarding 2 coins \n" +
            "\t\t\t\t\t\t\t\t\tfrom depots and 2 stones from strongbox\n";
    public static final String PRODUCE_HELP = "produce\n" +
            "\tactivates a production\n" +
            "\tsyntax: produce [-default res] [-leadercard i res] [-developmentcards i] [-depots i res] [-strongbox i res]\n" +
            "\toptions:\n" +
            "\t\t-default res\t\tif you want to activate default prodcution\n" +
            "\t\t\t\t\twrite wanted resource name instead of res\n" +
            "\t\t-leadercard i res\tif you want to activate leader card production\n" +
            "\t\t\t\t\twrite index of selected leader card instead of i\n" +
            "\t\t\t\t\twrite wanted resource name instead of res\n" +
            "\t\t-developmentcards i\tif you want to activate development cards production\n" +
            "\t\t\t\t\twrite indeces of development cards separated by space instead of i\n" +
            "\t\t-depots i res\t\tif you want to discard resources from warehouse depots\n" +
            "\t\t\t\t\twrite quantity of res instead of i\n" +
            "\t\t\t\t\twrite discarded resource name instead of res\n" +
            "\t\t\t\t\tyou can add more resources in the end, each separated by space\n" +
            "\t\t-strongbox i res\tif you want to discard resources from strongbox\n" +
            "\t\t\t\t\twrite quantity of res instead of i\n" +
            "\t\t\t\t\twrite discarded resource name instead of res\n" +
            "\t\t\t\t\tyou can add more resources in the end, each separated by space\n" +
            "\n" +
            "\t\tIn order to write a valid command at least one production element and one resource element should be present\n" +
            "\t\te.g: produce -default coin -leadercard 1 shield -developmentcards 2 3 -depots 3 coin 2 shield -strongbox 2 shield\n" +
            "\t\t\t-> produce a coin from default slot, a shield from leader card 1, activates second and third development cards,\n" +
            "\t\t\tdiscards 3 coins and 2 shields from depots and discards 2 shields from strongbox\n";
    public static final String DISCARD_HELP = "discard\n" +
                    "\tdiscards one of your leader cards\n" +
                    "\tsyntax: activate i\n" +
                    "\targuments:\n" +
                    "\t\ti\tindex of leader card (1 for first LC, 2 for second)\n";
    public static final String ACTIVATE_HELP = "activate\n" +
            "\tactivates one of your leader cards\n" +
            "\tsyntax: activate i\n" +
            "\targuments:\n" +
            "\t\ti\tindex of leader card (1 for first LC, 2 for second)\n";
    public static final String SHOW_PERSONAL_BOARD_HELP = "show personal board\n" +
            "\tdisplays a player's personal board\n" +
            "\tsyntax: show personal board i\n" +
            "\targuments:\n" +
            "\t\ti\tposition of the player\n";
    public static final String SHOW_COMMONS_HELP = "show commons\n" +
            "\tdisplays game commons, which includes development cards board and market\n" +
            "\tsyntax: show commons\n";

    public static final String HELP = "HELP MANUAL\n" +
            SHOW_COMMONS_HELP + "\n" +
            SHOW_PERSONAL_BOARD_HELP + "\n" +
            ACTIVATE_HELP + "\n" +
            DISCARD_HELP + "\n" +
            PRODUCE_HELP + "\n" +
            BUY_HELP + "\n" +
            TAKE_FROM_MARKET_HELP + "\n" +
            PICK_RESOURCES_HELP + "\n" +
            PICK_LEADER_CARDS_HELP + "\n" +
            PICK_MARBLES_HELP + "\n" +
            END_TURN_HELP + "\n" +
            SHOW_LEGEND_HELP + "\n" +
            QUIT_HELP;

    public static final String LEGEND = "SYMBOLS LEGEND\n" +
            "\nRESOURCES\n" +
            "\t" + COIN + ": coin\n" +
            "\t" + SERVANT + ": servant\n" +
            "\t" + SHIELD + ": shield\n" +
            "\t" + STONE + ": stone\n" +
            "\t" + FAITH_POINT + ": faith point\n" +
            "\nMARKET MARBLES\n" +
            "\t" + ANSI_YELLOW + MARBLE + ANSI_RESET + ": yellow marble (coin)\n" +
            "\t" + ANSI_PURPLE + MARBLE + ANSI_RESET + ": purple marble (servant)\n" +
            "\t" + ANSI_BLUE + MARBLE + ANSI_RESET + ": blue marble (shield)\n" +
            "\t" + ANSI_GREY + MARBLE + ANSI_RESET + ": grey marble (stone)\n" +
            "\t" + ANSI_RED + MARBLE + ANSI_RESET + ": red marble (faith point)\n" +
            "\t" + WHITE_MARBLE + ": white marble\n" +
            "\nLEVELS\n" +
            "\t" + YELLOW_LEVEL + ": yellow level 1\n" +
            "\t" + PURPLE_LEVEL + ": purple level 1\n" +
            "\t" + BLUE_LEVEL + BLUE_LEVEL + ": blue level 2\n" +
            "\t" + GREEN_LEVEL + GREEN_LEVEL + GREEN_LEVEL + ": green level 3\n" +
            "\tLevels can be displayed both vertically and horizontally\n" +
            "\nFAITH TRACK CELL COLORS\n" +
            "\t" + ANSI_WHITE + "white" + ANSI_RESET + ": basi faith track cell\n" +
            "\t" + ANSI_ORANGE + "orange" + ANSI_RESET + ": cell inside pope's favor\n" +
            "\t" + ANSI_RED + "red" + ANSI_RESET + ": inactive pope's favor cell\n" +
            "\t" + ANSI_GREEN + "green" + ANSI_RESET + ": active pope's favor cell\n" +
            "\t" + ANSI_DARK_GREY + "grey" + ANSI_RESET + ": discarded pope's favor cell\n" +
            "\nOTHERS\n" +
            "\t" + BLACK_CROSS + ": lorenzo's faith marker (equivalent to black cross)\n" +
            "\tAll yellow numbers indicate victory points";

}
