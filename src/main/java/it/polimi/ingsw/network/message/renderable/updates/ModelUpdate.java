package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;
import it.polimi.ingsw.server.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 *  This class contains an updated version of the Game which will be saved in the local DumbModel
 */
public class ModelUpdate extends PrivateRenderable {
    GameStateUpdate gameStateUpdate;
    ActionTokenDeckUpdate actionTokenDeckUpdate;
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
    boolean isSinglePlayerGame;

    /**
     * @param nickname the nickname of the player who will receive this update
     * @param game the game from which the model will be extracted
     */
    public ModelUpdate(String nickname, Game game) {
        super(nickname);
        //todo: add getSize() and getActionTokenDeck() to game
        //isSinglePlayerGame = game.getSize()==1;
        //actionTokenDeckUpdate = isSinglePlayerGame ? game.getActionTokenDeck() : null;
        gameStateUpdate = new GameStateUpdate(game);
        developmentCardsBoardUpdate = new DevelopmentCardsBoardUpdate(game.getDevelopmentCardsBoard());
        marketUpdate = new MarketUpdate(game.getMarket());
        leaderCardsPrivateUpdate = new LeaderCardsPrivateUpdate(nickname, game.getPlayer(nickname).getPersonalBoard().getLeaderCards());
        playerPrivateUpdate = new PlayerPrivateUpdate(game.getPlayer(nickname));

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

    @Override
    public void render(UI ui) {
        if(isSinglePlayerGame)
            actionTokenDeckUpdate.render(ui);
        developmentCardsBoardUpdate.render(ui);
        marketUpdate.render(ui);
        leaderCardsPrivateUpdate.render(ui);
        playerPrivateUpdate.render(ui);

        leaderCardsBroadcastUpdates.forEach(
                leaderCardsBroadcastUpdate -> leaderCardsBroadcastUpdate.render(ui)
        );
        developmentCardSlotUpdates.forEach(
                leaderCardsBroadcastUpdate -> leaderCardsBroadcastUpdate.render(ui)
        );
        faithTrackUpdates.forEach(
                leaderCardsBroadcastUpdate -> leaderCardsBroadcastUpdate.render(ui)
        );
        depotsUpdates.forEach(
                leaderCardsBroadcastUpdate -> leaderCardsBroadcastUpdate.render(ui)
        );
        strongboxUpdates.forEach(
                leaderCardsBroadcastUpdate -> leaderCardsBroadcastUpdate.render(ui)
        );
        playerBroadcastUpdates.forEach(
                leaderCardsBroadcastUpdate -> leaderCardsBroadcastUpdate.render(ui)
        );
        gameStateUpdate.render(ui);
    }
}
