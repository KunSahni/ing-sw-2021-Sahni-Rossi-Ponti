package it.polimi.ingsw.client.utils.constants;

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
    public final static String COIN_MARBLE = ANSI_YELLOW + "\u25CF" + ANSI_RESET;
    public final static String STONE_MARBLE = ANSI_GREY + "\u25CF" + ANSI_RESET;
    public final static String SERVANT_MARBLE = ANSI_PURPLE + "\u25CF" + ANSI_RESET;
    public final static String SHIELD_MARBLE = ANSI_BLUE + "\u25CF" + ANSI_RESET;
    public final static String WHITE_MARBLE = ANSI_WHITE + "\u25CF" + ANSI_RESET;
    public final static String RED_MARBLE = ANSI_RED + "\u25CF" + ANSI_RESET;
    public final static String YELLOW_LEVEL = ANSI_YELLOW + "\u25C8" + ANSI_RESET;
    public final static String BLUE_LEVEL = ANSI_BLUE + "\u25C8" + ANSI_RESET;
    public final static String PURPLE_LEVEL = ANSI_PURPLE + "\u25C8" + ANSI_RESET;
    public final static String GREEN_LEVEL = ANSI_GREEN + "\u25C8" + ANSI_RESET;
    public static final String ANSI_CLEAR = "\033[2J";
    public static final String CMD_MESSAGE = "\033[35;1H\u001b[44;1mInsert command: ";
}
