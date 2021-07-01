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
    public static final String HELP = "HELP MANUAL\n" +
            "show commons\n" +
            "\tdisplays game commons, which includes development cards board and market\n" +
            "\tsyntax: show commons\n" +
            "\n" +
            "show personal board\n" +
            "\tdisplays a player's personal board\n" +
            "\tsyntax: show personal board i\n" +
            "\targuments:\n" +
            "\t\ti\tposition of the player\n" +
            "\n" +
            "activate\n" +
            "\tactivates one of your leader cards\n" +
            "\tsyntax: activate i\n" +
            "\targuments:\n" +
            "\t\ti\tindex of leader card (1 for first LC, 2 for second)\n" +
            "\n" +
            "discard\n" +
            "\tdiscards one of your leader cards\n" +
            "\tsyntax: activate i\n" +
            "\targuments:\n" +
            "\t\ti\tindex of leader card (1 for first LC, 2 for second)\n" +
            "\n" +
            "produce\n" +
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
            "\t\t\tdiscards 3 coins and 2 shields from depots and discards 2 shields from strongbox\n" +
            "\n" +
            "buy\n" +
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
            "\t\t\t\t\t\t\t\t\tfrom depots and 2 stones from strongbox\n" +
            "\n" +
            "take\n" +
            "\ttake resources from market\n" +
            "\tsyntax: take place i\n" +
            "\targuments:\n" +
            "\t\tplace\trow if you want to take from a row\n" +
            "\t\t\tcolumn if you want to take from a column\n" +
            "\t\ti\tnumber of row or column\n" +
            "\n" +
            "pick resources\n" +
            "\tpick resources at the beginning of the game\n" +
            "\tsyntax: pick resources i res\n" +
            "\targuments:\n" +
            "\t\ti\tquantity of res\n" +
            "\t\tres\tname of picked resource\n" +
            "\t\t\n" +
            "\t\tYou can add more resources in the end, each separated by space\n" +
            "\t\te.g: pick resources shield coin\t\t-> pick a shield and a coin\n" +
            "\n" +
            "pick leader cards\n" +
            "\tpick leader cards at the beginning of the game\n" +
            "\tsyntax: pick leader cards i j\n" +
            "\targuments:\n" +
            "\t\ti\tindex of first picked leader card\n" +
            "\t\tj\tindex of second picked leader card\n" +
            "\n" +
            "pick marbles\n" +
            "\tpick marbles from the ones picked from market. The selected marbles will be converted to resources and stored in warehouse depots\n" +
            "\tsyntax: pick marbles color\n" +
            "\targuments:\n" +
            "\t\tcolor\tcolor of marble which you want to convert in resource\n" +
            "\n" +
            "\tTo select multiple marbles just write color of each selected marble in the end\n" +
            "\te.g: pick marbles yellow yellow blue\t-> picks 2 yellow marbles and 1 blue marble\n" +
            "\n" +
            "quit\n" +
            "\tquit game\n" +
            "\tsyntax: quit";
}
