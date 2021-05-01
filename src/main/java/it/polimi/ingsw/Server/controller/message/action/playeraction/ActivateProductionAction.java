package it.polimi.ingsw.server.controller.message.action.playeraction;

import it.polimi.ingsw.server.model.utils.ProductionCombo;
import it.polimi.ingsw.server.controller.message.action.Action;
import it.polimi.ingsw.server.model.leadercard.ProduceLeaderCard;
import it.polimi.ingsw.server.model.utils.ProductionOutput;
import it.polimi.ingsw.server.model.personalboardpackage.DefaultSlot;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * This class represents the action of activating productions chosen by a Player
 */
public class ActivateProductionAction implements Action {
    private PersonalBoard board;
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
    public void execute() {
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
        Optional.ofNullable(productionCombo.getDiscardedResourcesFromStrongbox()).ifPresent(
                resourceIntegerMap -> board.discardFromStrongbox(resourceIntegerMap)
        );
        Optional.ofNullable(productionCombo.getDiscardedResourcesFromDepots()).ifPresent(
                resourceIntegerMap -> board.discardFromDepots(resourceIntegerMap)
        );

        //Extracts the productions from DevelopmentCardSlots
        List<ProductionOutput> developmentCardsOutput = new ArrayList<>();
        Optional.ofNullable(productionCombo.getDevelopmentCardSlots()).ifPresent(
                developmentCardSlots -> developmentCardSlots.forEach(
                        developmentCardSlot -> developmentCardsOutput.add(developmentCardSlot.produce())
                )
        );

        //Manage DevelopmentCardSlots
        Optional.ofNullable(developmentCardsOutput).ifPresent(
                outputList -> elaborateProductionOutputs.accept(outputList)
        );

        //Extracts the production from DefaultSlot
        ProductionOutput defaultSlotOutput = null;
        if(productionCombo.getDefaultSlotOutput() != null)
            defaultSlotOutput = DefaultSlot.produce(productionCombo.getDefaultSlotOutput());

        //Manage defaultSlotOutput
        Optional.ofNullable(defaultSlotOutput).ifPresent(
                output -> elaborateProductionOutput.accept(output)
        );

        //Extracts the productions from ProduceLeaderCards and ConvertLeaderCards
        List<ProductionOutput> leaderCardsOutput = new ArrayList<>();

        Optional.ofNullable(productionCombo.getLeaderCardOutputs()).ifPresent(
                $ -> Optional.ofNullable(productionCombo.getLeaderCards()).ifPresent(
                    leaderCards -> leaderCards.forEach(
                            leaderCard -> leaderCardsOutput.add(((ProduceLeaderCard) leaderCard).produce(productionCombo.getLeaderCardOutputs().get(leaderCard)))
                    )
            )
        );

        //Manage leaderCardsOutput
        Optional.ofNullable(leaderCardsOutput).ifPresent(
                outputList -> elaborateProductionOutputs.accept(outputList)
        );
    }

    /**
     * This Consumer manages a single ProductionOutput which contains a map of produced resources and FaithPoints.
     * Saves all the resources in the strongbox and moves the faith marker accordingly.
     */
    private final Consumer<ProductionOutput> elaborateProductionOutput = productionOutput ->  {
                board.getFaithTrack().moveMarker(productionOutput.getFaithIncrement());
                board.storeInStrongbox(productionOutput.getResources());
            };

    /**
     * This Consumer manages a List of ProductionOutput where each item contains a map of produced resources and FaithPoints.
     * Saves all the resources in the strongbox and moves the faith marker accordingly.
     */
    private final Consumer<List<ProductionOutput>> elaborateProductionOutputs = productionOutputs ->  productionOutputs.forEach(
            elaborateProductionOutput
    );

}