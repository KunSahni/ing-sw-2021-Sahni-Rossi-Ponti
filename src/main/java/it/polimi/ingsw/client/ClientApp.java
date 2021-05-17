package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.server.Server;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) {
        CLI cli = new CLI();
        Client client = new Client(cli);
        cli.setClient(client);
        client.run();
    }
}