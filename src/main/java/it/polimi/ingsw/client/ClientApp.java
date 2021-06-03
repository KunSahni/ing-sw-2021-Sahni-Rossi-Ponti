package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.gui.GUI;

public class ClientApp {
    public static void main(String[] args) {
        // CLI cli = new CLI(args[0], Integer.parseInt(args[1]));

        GUI.main(null);
    }
}