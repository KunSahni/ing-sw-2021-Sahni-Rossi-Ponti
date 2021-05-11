package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.server.controller.message.action.Forwardable;
import it.polimi.ingsw.server.model.actiontoken.ActionTokenDeck;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardsDeck;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

import java.io.PrintWriter;
import java.util.Scanner;

public class CLI extends UI {
    private Scanner in;
    private PrintWriter out;
}