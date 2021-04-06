package it.polimi.ingsw.server.controller.messages.actions;

import it.polimi.ingsw.server.controller.gamepackage.ProductionCombo;
import it.polimi.ingsw.server.model.ProduceLeaderCard;
import it.polimi.ingsw.server.model.personalboardpackage.DefaultSlot;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * This class represents the action of activating productions chosen by a Player
 */
public class ActivateProductionAction implements Forwardable {
    private final PersonalBoard board;
    private final ProductionCombo productionCombo;

    /**
     * @param board the Player's PersonalBoard
     * @param productionCombo a combination of LeaderCards and DevelopmentSlots where the Player want to activate the production
     */
    public ActivateProductionAction(PersonalBoard board, ProductionCombo productionCombo) {
        this.board = board;
        this.productionCombo = productionCombo;
    }

    @Override
    public void forward() {
        activateProduction();
    }

    /*
    todo: move this to view (maybe ModelView?)
    public ProductionCombo possibleProductions() {
        //This list contains all the DevelopmentSlots from which the Player can produce
        List<DevelopmentSlot> possibleSlots = board.getDevelopmentSlots().stream().filter(
                developmentSlot -> developmentSlot instanceof DevelopmentCardSlot
        ).filter(
                developmentSlot -> board.hasResources(developmentSlot.getInputResources())
        );

        //Check if the Player can afford to produce from DefaultSlot
        possibleSlots.add(board.getDevelopmentSlots().stream().filter(
                developmentSlot -> developmentSlot instanceof DefaultSlot && board.getResourcesCount() > 2
        ));

        //This list contains all the LeaderCards from which the Player can produce
        List<ProduceLeaderCard> possibleLeaderCards = board.getLeaderCards().stream().filter(
                leaderCard -> leaderCard.getAbility().equals(LeaderCardAbility.PRODUCE)
        ).filter(
                leaderCard -> board.hasResource(leaderCard.getInputResource())
        );

        return new ProductionCombo(possibleSlots, possibleLeaderCards);
    }
    */

    /**
     * This method manages the production from different sources like DevelopmentCards, LeaderCards and DefaultSlot
     */
    private void activateProduction() {
        //First the required resources are discarded
        board.discardResources(productionCombo.getDiscardedResources().get("depots"), productionCombo.getDiscardedResources().get("strongbox"));

        //Extracts the productions from DevelopmentCardSlots
        List<ProductionOutput> developmentCardsOutput = Optional.ofNullable(productionCombo.getDevelopmentCardSlots()).ifPresent(
                developmentCardSlots -> developmentCardSlots.stream().map(
                        developmentCardSlot -> developmentCardSlot.produce()
                ).collect(Collectors.toList())
        );

        //Manage DevelopmentCardSlots
        elaborateProductionOutputs.accept(developmentCardsOutput);

        //Extracts the production from DefaultSlot
        ProductionOutput defaultSlotOutput = Optional.ofNullable(productionCombo.getDefaultSlotOutput()).ifPresent(
                resource -> board.getDevelopmentSlots().stream()
                        .filter(slot -> slot instanceof DefaultSlot)
                        .map(
                                slot -> ((DefaultSlot) slot).produce(productionCombo.getDefaultSlotOutput())
                        )
        );

        //Manage defaultSlotOutput
        elaborateProductionOutput.accept(defaultSlotOutput);

        //Extracts the productions from ProduceLeaderCards and ConvertLeaderCards
        List<ProductionOutput> leaderCardsOutput = Optional.ofNullable(productionCombo.getLeaderCards()).ifPresent(
                leaderCards -> leaderCards.stream().map(
                        leaderCard -> ((ProduceLeaderCard) leaderCard).produce(productionCombo.getLeaderCardOutputs().get(leaderCard))
                ).collect(Collectors.toList())
        );

        //Manage leaderCardsOutput
        elaborateProductionOutputs.accept(leaderCardsOutput);
    }

    /**
     * This Consumer manages a List of ProductionOutput where each item contains a map of produced resources and FaithPoints.
     * Saves all the resources in the strongbox and moves the faith marker accordingly.
     */
    private Consumer<List<ProductionOutput>> elaborateProductionOutputs = productionOutputs ->  productionOutputs.forEach(
            productionOutput -> {
                board.getFaithTrack().moveMarker(productionOutput.getFaithIncrement());
                board.getStrongbox().storeResources(productionOutput.getResources());
            }
    );

    /**
     * This Consumer manages a single ProductionOutput which contains a map of produced resources and FaithPoints.
     * Saves all the resources in the strongbox and moves the faith marker accordingly.
     */
    private Consumer<ProductionOutput> elaborateProductionOutput = productionOutput ->  {
                board.getFaithTrack().moveMarker(productionOutput.getFaithIncrement());
                board.getStrongbox().storeResources(productionOutput.getResources());
            };

}