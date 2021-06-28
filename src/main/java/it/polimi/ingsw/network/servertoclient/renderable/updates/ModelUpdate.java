package it.polimi.ingsw.network.servertoclient.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;
import it.polimi.ingsw.network.servertoclient.renderable.PrivateRenderable;
import it.polimi.ingsw.server.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 *  This class contains an updated version of the Game which will be saved in the local DumbModel
 */
public class ModelUpdate extends PrivateRenderable {
    GameStateUpdate gameStateUpdate;
    DevelopmentCardsBoardUpdate  developmentCardsBoardUpdate;
    MarketUpdate marketUpdate;
    LeaderCardsPrivateUpdate leaderCardsPrivateUpdate;
    PlayerPrivateUpdate playerPrivateUpdate;
    ArrayList<LeaderCardsBroadcastUpdate> leaderCardsBroadcastUpdates;
    ArrayList<DevelopmentCardSlotUpdate> developmentCardSlotUpdates;
    ArrayList<FaithTrackUpdate> faithTrackUpdates;
    ArrayList<DepotsUpdate> depotsUpdates;
    ArrayList<StrongboxUpdate> strongboxUpdates;
    ArrayList<PlayerBroadcastUpdate> playerBroadcastUpdates;
    int size;

    /**
     * @param nickname the nickname of the player who will receive this update
     * @param game the game from which the model will be extracted
     */
    public ModelUpdate(String nickname, Game game) {
        super(nickname);
        size = game.size();
        gameStateUpdate = new GameStateUpdate(game.getCurrentState());
        developmentCardsBoardUpdate = new DevelopmentCardsBoardUpdate(game.getDevelopmentCardsBoard());
        marketUpdate = new MarketUpdate(game.getMarket());

        playerPrivateUpdate = new PlayerPrivateUpdate(game.getPlayer(nickname));
        leaderCardsPrivateUpdate = new LeaderCardsPrivateUpdate(nickname, game.getPlayer(nickname).getPersonalBoard().getLeaderCards());

        leaderCardsBroadcastUpdates = game
                .getPlayerList()
                .stream()
                .filter(
                    player -> !player.getNickname().equals(nickname)
                ).map(
                    player -> new LeaderCardsBroadcastUpdate(player.getNickname(), player.getPersonalBoard().getLeaderCards())
                ).collect(
                        Collectors.toCollection(ArrayList::new)
                );

        developmentCardSlotUpdates = game.getPlayerList()
                .stream()
                .map(
                        player -> new ArrayList<>(
                                player
                                        .getPersonalBoard()
                                        .getDevelopmentCardSlots()
                                        .stream()
                                        .map(
                                                developmentCardSlot -> new DevelopmentCardSlotUpdate(player.getNickname(), developmentCardSlot)
                                        ).collect(Collectors.toList())
                        )
                ).flatMap(List::stream).collect(Collectors.toCollection(ArrayList::new));

        faithTrackUpdates = game
                .getPlayerList()
                .stream()
                .map(
                        player -> new FaithTrackUpdate(player.getNickname(), player.getPersonalBoard().getFaithTrack())
                ).collect(
                        Collectors.toCollection(ArrayList::new)
                );

        depotsUpdates = game
                .getPlayerList()
                .stream()
                .map(
                        player -> new DepotsUpdate(player.getNickname(), player.getPersonalBoard().getWarehouseDepots())
                ).collect(
                        Collectors.toCollection(ArrayList::new)
                );

        strongboxUpdates = game
                .getPlayerList()
                .stream()
                .map(
                        player -> new StrongboxUpdate(player.getNickname(), player.getPersonalBoard().getStrongbox())
                ).collect(
                        Collectors.toCollection(ArrayList::new)
                );

        playerBroadcastUpdates = game
                .getPlayerList()
                .stream()
                .filter(
                        player -> !player.getNickname().equals(nickname)
                ).map(
                        PlayerBroadcastUpdate::new
                ).collect(
                        Collectors.toCollection(ArrayList::new)
                );

    }

    /**
     * This method returns an Enum value which represents what type of graphical element is contained in the Renderable
     * @return OnScreenElement enum value representing the payload in the Renderable
     */
    @Override
    public OnScreenElement getOnScreenElement(DumbModel dumbModel){
        return OnScreenElement.FORCE_DISPLAY;
    }

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     * @param dumbModel the dumb model in which the updates will be stored
     */
    @Override
    public void update(DumbModel dumbModel){
        dumbModel.updateGameSize(size);
        developmentCardsBoardUpdate.update(dumbModel);
        marketUpdate.update(dumbModel);
        playerPrivateUpdate.update(dumbModel);
        leaderCardsPrivateUpdate.update(dumbModel);

        playerBroadcastUpdates.forEach(
                playerBroadcastUpdate -> playerBroadcastUpdate.update(dumbModel)
        );
        leaderCardsBroadcastUpdates.forEach(
                leaderCardsBroadcastUpdate -> leaderCardsBroadcastUpdate.update(dumbModel)
        );
        developmentCardSlotUpdates.forEach(
                developmentCardSlotUpdate -> developmentCardSlotUpdate.update(dumbModel)
        );
        faithTrackUpdates.forEach(
                faithTrackUpdate -> faithTrackUpdate.update(dumbModel)
        );
        depotsUpdates.forEach(
                leaderCardsBroadcastUpdate -> leaderCardsBroadcastUpdate.update(dumbModel)
        );
        strongboxUpdates.forEach(
                strongboxUpdate -> strongboxUpdate.update(dumbModel)
        );
        gameStateUpdate.update(dumbModel);
    }

    /**
     * This method is used by the UI to display an element part of the model on the user's screen
     * @param ui the ui which calls the method
     */
    @Override
    public void render(UI ui) {
        ui.renderModelUpdate();
    }
}
