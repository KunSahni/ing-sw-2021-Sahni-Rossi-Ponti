package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.server.ServerApp;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MasterOfRenaissance {

    /**
     * Method main selects CLI, GUI or Server based on the arguments provided.
     * @param args of type String[]
     */
    public static void main(String[] args){
        System.out.println(Constants.ANSI_CLEAR);
        System.out.println("Hello! Welcome to Masters of Renaissance!\nWhat do you want to launch?");
        System.out.println("0. Server\n1. Client (CLI interface)\n2. Client (GUI interface)");
        System.out.println("\n>Type the number of the desired option!");
        System.out.print(">");
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        try {
            input = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Numeric format requested, application will now close...");
            System.exit(-1);
        }
        switch (input) {
            case 0 -> ServerApp.main(null);
            case 1 -> CLI.main(null);
            case 2 -> GUI.main(null);
            default -> System.err.println("Invalid argument, please run the executable again with one of these options:\n1.server\n2.client");
        }
    }
}
