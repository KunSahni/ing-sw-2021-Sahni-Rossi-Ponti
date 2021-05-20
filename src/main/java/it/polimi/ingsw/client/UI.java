package it.polimi.ingsw.client;

import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.personalboard.FavorStatus;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//todo: implement pub/sub for rendering

public abstract class UI {
    private DumbModel dumbModel;
    private String nickname;
    private ArrayList<ExecutedActions> turnActions;

    public abstract void renderPersonalBoard(DumbPersonalBoard personalBoard);
    public abstract void renderDevelopmentCardsBoard(DumbDevelopmentCardsBoard developmentCardsBoard);
    public abstract void renderActionTokenDeck(DumbActionTokenDeck actionTokenDeck);
    public abstract void renderMarket(DumbMarket market);
    public abstract void renderLeaderCardsChoice(List<DumbLeaderCard> leaderCards);
    public abstract void renderTempMarbles(Map<MarketMarble, Integer> updateMarbles);
    public abstract void renderResourcePregameChoice(int numberOfResources);
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
    private void addPersonalBoard(String nickname, int position){
        dumbModel.addPersonalBoard(nickname, position);
    }

    /**
     * This method creates a new PersonalBoard inside the DumbModel base on the passed parameters
     * @param nickname the nickname of the player that needs to be added
     * @param position the position of the player on the "table"
     * @param updatedTurnStatus true if the player is starting a turn, false if he's finishing it
     * @param updatedConnectionStatus true if the player is connected to the server, false if he's disconnected from the server
     */
    public void updatePersonalBoard(String nickname, int position, boolean updatedTurnStatus, boolean updatedConnectionStatus){
        getPersonalBoard(nickname).ifPresentOrElse(
                dumbPersonalBoard -> {
                    dumbPersonalBoard.updateConnectionStatus(updatedConnectionStatus);
                    dumbPersonalBoard.updateTurnStatus(updatedTurnStatus);
                },
                ()->{
                    addPersonalBoard(nickname, position);
                    updatePersonalBoard(nickname, position, updatedTurnStatus, updatedConnectionStatus);
                }
        );
    }

    /**
     * This method is called every time the client receives an update regarding DumbDevelopmentCardsBoard.
     * @param updatedDevelopmentCardsBoard an updated version of the game's DumbDevelopmentCardsBoard
     */
    public void updateDevelopmentCardsBoard(DumbDevelopmentCard[][] updatedDevelopmentCardsBoard){
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
    public void updateMarket(MarketMarble[][] updatedMarket, MarketMarble updatedExtraMarble){
        dumbModel.getMarket().updateMarket(updatedMarket);
        dumbModel.getMarket().updateExtraMarble(updatedExtraMarble);
    }

    /**
     * This method is called every time the client receives an update regarding a player's faithTrackPosition.
     * @param nickname the nickname of the player whose position will be updated
     * @param updatedFaithTrackPosition the updated position of his faithMarker
     * @param updatedPopesFavors the updated list of popesFavors of the player
     */
    public void updateFaithTrack(String nickname, int updatedFaithTrackPosition, List<FavorStatus> updatedPopesFavors){
        getPersonalBoard(nickname).ifPresent(
                dumbPersonalBoard -> {
                    dumbPersonalBoard.getFaithTrack().updateFaithMarkerPosition(updatedFaithTrackPosition);
                    dumbPersonalBoard.getFaithTrack().updatePopesFavors(updatedPopesFavors);
                }
        );
    }

    /**
     * This method is called every time the client receives an update regarding the faith track in a single player game
     * @param updatedFaithTrackPosition the updated position of his faithMarker
     * @param updatedPopesFavors the updated list of popesFavors of the player
     * @param updatedBlackCrossPosition the updated position of the black cross on the faith track
     */
    public void updateSinglePlayerFaithTrack(int updatedFaithTrackPosition, List<FavorStatus> updatedPopesFavors, int updatedBlackCrossPosition){
        if(dumbModel.getSize()==1) {
            updateFaithTrack(nickname, updatedFaithTrackPosition, updatedPopesFavors);
            getPersonalBoard(nickname).ifPresent(
                    dumbPersonalBoard -> ((DumbSinglePlayerFaithTrack) dumbPersonalBoard.getFaithTrack()).updateBlackCrossPosition(updatedBlackCrossPosition)
            );
        }
    }

    /**
     * This method is called every time the client receives an update regarding hiw own leaderCards.
     * @param updatedLeaderCards the updated list of leaderCards held by the player
     */
    public void updateLeaderCards(List<DumbLeaderCard> updatedLeaderCards){
        getPersonalBoard(nickname).ifPresent(
                dumbPersonalBoard -> dumbPersonalBoard.updateLeaderCards(updatedLeaderCards)
        );
    }

    /**
     * This method is called every time the client receives an update regarding another player's leaderCards.
     * @param nickname the nickname of the player whose leaderCards will be updated
     * @param updatedLeaderCards the updated list of leaderCards held by the player
     */
    public void updateLeaderCards(String nickname, List<DumbLeaderCard> updatedLeaderCards){
        getPersonalBoard(nickname).ifPresent(
                dumbPersonalBoard -> dumbPersonalBoard.updateLeaderCards(updatedLeaderCards)
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
                dumbPersonalBoard -> dumbPersonalBoard.getDevelopmentCardSlots().get(position-1).updateDevelopmentCards(updatedDevelopmentCardSlot)
        );
    }

    /**
     * This method is called every time the client receives an update regarding a player's store leader card
     * @param nickname the nickname of the player whose store leader card will be updated
     * @param leaderCard the store leader card whose storage changed
     */
    public void updateStoreLeaderCardStorage(String nickname, DumbStoreLeaderCard leaderCard){
        getPersonalBoard(nickname).flatMap(dumbPersonalBoard -> dumbPersonalBoard.getLeaderCards()
                .stream()
                .filter(
                        dumbLeaderCard -> dumbLeaderCard instanceof DumbStoreLeaderCard && dumbLeaderCard.equals(leaderCard)
                ).findFirst()).ifPresent(dumbLeaderCard -> ((DumbStoreLeaderCard) dumbLeaderCard).updateStoredResources(leaderCard.getStorage().getStoredResources()));
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
     * This method is called every time the client receives an update regarding the state of a game
     * @param updatedGameState the updated state of a game
     */
    public void updateGameState(GameState updatedGameState) {
        dumbModel.updateGameState(updatedGameState);
        if(updatedGameState.equals(GameState.ASSIGNED_INKWELL))
            getPersonalBoard(nickname).ifPresent(
                    dumbPersonalBoard -> {
                        if(dumbPersonalBoard.getDepots().getResourceCount() != dumbPersonalBoard.getPosition()/2)
                            renderResourcePregameChoice(dumbPersonalBoard.getPosition()/2);
                    }
            );
    }

    /**
     * This method is called every time the client receives an update regarding the actions he has performed so far
     * @param updatedTurnActions a list of all the valid actions requested by the client
     */
    public void updateTurnActions(List<ExecutedActions> updatedTurnActions){
        this.turnActions = new ArrayList<>(updatedTurnActions);
    }

    /**
     * @param nickname a player whose DumbPersonalBoard need to be retrieved
     * @return the DumbPersonalBoard corresponding to the player with the nickname given as a parameter
     */
    private Optional<DumbPersonalBoard> getPersonalBoard(String nickname){
        return dumbModel.getPersonalBoards()
                .stream()
                .filter(
                        dumbPersonalBoard -> dumbPersonalBoard.getNickname().equals(nickname)
                ).findFirst();
    }

}
