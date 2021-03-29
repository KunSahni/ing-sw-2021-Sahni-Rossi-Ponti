package it.polimi.ingsw.server.model.gamepackage;

import java.util.*;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

/**
 * This class represents a Player
 */

public class Player {
    private String nickname;
    private final int position;
    private PersonalBoard personalBoard;
    private List<LeaderCard> leaderCards;
    private final Game game;

    /**
     * @param nickname an unique username associated to the player, can't be changed during the game
     * @param position the position on the table, so
     * @param game the Game in which the Player will be playing
     * @param leaderCards a list of four LeaderCard from where the Player will choose two
     */
    public Player(String nickname, int position, Game game, List<LeaderCard> leaderCards) {
        this.nickname = nickname;
        this.position = position;
        this.game = game;
        this.leaderCards = leaderCards;
    }

    /**
     * This method takes in input the chosen cards and creates the personalBoard accordingly
     * @param card1 the first LeaderCard chosen by the Player
     * @param card2 the second LeaderCard chosen by the Player
     */
    public void chooseLeaderCards(LeaderCard card1, LeaderCard card2){
        this.personalBoard = new PersonalBoard(card1, card2, this);
        this.leaderCards.clear();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPosition() {
        return position;
    }

    public PersonalBoard getPersonalBoard() {
        return personalBoard;
    }

    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public Game getGame() {
        return game;
    }
}