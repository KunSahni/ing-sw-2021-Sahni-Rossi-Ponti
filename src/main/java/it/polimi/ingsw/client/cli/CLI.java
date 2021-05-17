package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.messages.AuthenticationMessage;
import it.polimi.ingsw.network.message.messages.CreateLobbyMessage;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CLI extends UI {
    private Scanner in;
    private PrintWriter out;
    private Client client;

    public CLI() {
        in = new Scanner(System.in);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void renderMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void renderErrorMessage(String message) {
        System.out.println(message);

    }

    @Override
    public void renderAuthenticationRequest(String message) {
        System.out.println(message);
        String nickname = in.nextLine();
        client.sendMessage(new AuthenticationMessage(nickname, -1));
    }

    @Override
    public void renderCreateLobbyRequest(String message) {
        System.out.println(message);
        int size = in.nextInt();
        client.sendMessage(new CreateLobbyMessage(size));
    }
}