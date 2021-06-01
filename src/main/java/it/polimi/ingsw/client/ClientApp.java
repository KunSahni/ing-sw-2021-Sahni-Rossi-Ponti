package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLI;

public class ClientApp {
    public static void main(String[] args) {
        CLI cli = new CLI(args[0], Integer.parseInt(args[1]));
    }
}