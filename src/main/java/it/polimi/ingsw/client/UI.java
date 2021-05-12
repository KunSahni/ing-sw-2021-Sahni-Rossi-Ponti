package it.polimi.ingsw.client;

import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.personalboardpackage.FavorStatus;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//todo: StoreLeaderCard related methods are not implemented yet
//todo: implement pub/sub for rendering

public abstract class UI {
    private DumbModel dumbModel;

    public abstract void renderPersonalBoard(DumbPersonalBoard personalBoard);
    public abstract void renderDevelopmentCardsBoard(DumbDevelopmentCardsBoard developmentCardsBoard);
    public abstract void renderActionTokenDeck(DumbActionTokenDeck actionTokenDeck);
    public abstract void renderLeaderCardsChoice(List<DumbLeaderCard> leaderCards);
    public abstract void renderResourcePregameChoice(int numberOfResources);
    public abstract void renderMarket(DumbMarket market);
    public abstract void renderMessage(String message);
    public abstract void renderErrorMessage(String message);
    public abstract void renderAuthenticationRequest(String message);
    public abstract void renderCreateLobbyRequest(String message);

    /**
     * This method creates a new DumbModel based on the passed parameters
     * @param gameId the gameId to which the client is connected
     * @param size the size of the game to which the client is connected
     */
    public void createModelView(int gameId, int size) {
        dumbModel = new DumbModel(gameId, size);
    }

    /**
     * This method creates a new PersonalBoard inside the DumbModel base on the passed parameters
     * @param nickname the nickname of the player that needs to be added
     * @param position the position of the player on the "table"
     */
    public void addPersonalBoard(String nickname, int position){
        dumbModel.addPersonalBoard(nickname, position);
    }

    /**
     * This method is called every time the client receives an update regarding DumbDevelopmentCardsBoard.
     * @param updatedDevelopmentCardsBoard an updated version of the game's DumbDevelopmentCardsBoard
     */
    public void updateDevelopmentCardsBoard(DumbDevelopmentCard[] updatedDevelopmentCardsBoard){
        dumbModel.getDevelopmentCardsBoard().updateBoard(updatedDevelopmentCardsBoard);
    }

    /**
     * This method is called every time the client receives an update regarding DumbActionTokenDeck.
     * @param updatedActionTokenDeck an updated version of the game's DumbActionTokenDeck
     */
    public void updateActionTokenDeck(List<ActionToken> updatedActionTokenDeck){
        dumbModel.getActionTokenDeck().updateActionTokens(updatedActionTokenDeck);
    }

    /**
     * This method is called every time the client receives an update regarding DumbMarket.
     * @param updatedMarket an updated version of the game's DumbMarket
     */
    public void updateMarket(MarketMarble[] updatedMarket){
        dumbModel.getMarket().updateMarket(updatedMarket);
    }

    /**
     * This method is called every time the client receives an update regarding a player's faithTrackPosition.
     * @param nickname the nickname of the player whose position will be updated
     * @param updatedFaithTrackPosition the updated position of his faithMarker
     */
    public void updateFaithTrackPosition(String nickname, int updatedFaithTrackPosition){
        getPersonalBoard(nickname).ifPresent(
                dumbPersonalBoard -> dumbPersonalBoard.getFaithTrack().updateFaithMarkerPosition(updatedFaithTrackPosition)
        );
    }

    /**
     * This method is called every time the client receives an update regarding a player's popesFavors.
     * @param nickname the nickname of the player whose position will be updated
     * @param updatedPopesFavors the updated list of popesFavors of the player
     */
    public void updatePopesFavors(String nickname, List<FavorStatus> updatedPopesFavors){
        getPersonalBoard(nickname).ifPresent(
                dumbPersonalBoard -> dumbPersonalBoard.getFaithTrack().updatePopesFavors(updatedPopesFavors)
        );
    }

    //todo: implement
    public void updateTempMarbles(List<MarketMarble> updateMarbles){}

    /**
     * This method is called every time the client receives an update regarding a player's leaderCards.
     * @param nickname the nickname of the player whose leaderCards will be updated
     * @param updatedLeaderCards the updated list of leaderCards held by the player
     */
    public void updateLeaderCards(String nickname, List<DumbLeaderCard> updatedLeaderCards){
        getPersonalBoard(nickname).ifPresent(
                dumbPersonalBoard -> dumbPersonalBoard.updateLeaderCards(updatedLeaderCards)
        );
    }

    /**
     * This method is called every time the client receives an update regarding a player's turnStatus.
     * @param nickname the nickname of the player whose turnStatus will be updated
     * @param updatedTurnStatus true if the player is starting a turn, false if he's finishing it
     */
    public void updateTurnStatus(String nickname, boolean updatedTurnStatus){
        getPersonalBoard(nickname).ifPresent(
                dumbPersonalBoard -> dumbPersonalBoard.updateTurnStatus(updatedTurnStatus)
        );
    }

    /**
     * This method is called every time the client receives an update regarding a player's connectionStatus.
     * @param nickname the nickname of the player whose connectionStatus will be updated
     * @param updatedConnectionStatus true if the player is connected to the server, false if he's disconnected from the server
     */
    public void updateConnectionStatus(String nickname, boolean updatedConnectionStatus){
        getPersonalBoard(nickname).ifPresent(
                dumbPersonalBoard -> dumbPersonalBoard.updateConnectionStatus(updatedConnectionStatus)
        );
    }

    /**
     * This method is called every time the client receives an update regarding a player's DumbDevelopmentCardSlot.
     * @param nickname the nickname of the player whose DumbDevelopmentCardSlot will be updated
     * @param position the position of the DumbDevelopmentCardSlot which needs to be updated
     * @param updatedDevelopmentCardSlot an updated list of DumbDevelopmentCard contained in the selected DumbDevelopmentCardSlot
     */
    public void updateDevelopmentCardSlot(String nickname, int position, List<DumbDevelopmentCard> updatedDevelopmentCardSlot){
        getPersonalBoard(nickname).ifPresent(
                dumbPersonalBoard -> dumbPersonalBoard.getDevelopmentCardSlots().get(position).updateDevelopmentCards(updatedDevelopmentCardSlot)
        );
    }

    /**
     * This method is called every time the client receives an update regarding a player's depots.
     * @param nickname the nickname of the player whose depots will be updated
     * @param updatedDepots an updated Map of Resources contained in the player's depots
     */
    public void updateDepots(String nickname, Map<Resource, Integer> updatedDepots){
        getPersonalBoard(nickname).ifPresent(
                dumbPersonalBoard -> dumbPersonalBoard.getDepots().updateStoredResources(updatedDepots)
        );
    }


    /**
     * This method is called every time the client receives an update regarding a player's strongbox.
     * @param nickname the nickname of the player whose strongbox will be updated
     * @param updatedStrongbox an updated Map of Resources contained in the player's strongbox
     */
    public void updateStrongbox(String nickname, Map<Resource, Integer> updatedStrongbox){
        getPersonalBoard(nickname).ifPresent(
                dumbPersonalBoard -> dumbPersonalBoard.getStrongbox().updateStoredResources(updatedStrongbox)
        );
    }

    /**
     * @param nickname a player whose DumbPersonalBoard need to be retrieved
     * @return the DumbPersonalBoard corresponding to the player with the nickname given as a parameter
     */
    public Optional<DumbPersonalBoard> getPersonalBoard(String nickname){
        return dumbModel.getPersonalBoards()
                .stream()
                .filter(
                        dumbPersonalBoard -> dumbPersonalBoard.getNickname().equals(nickname)
                ).findFirst();
    }

}
