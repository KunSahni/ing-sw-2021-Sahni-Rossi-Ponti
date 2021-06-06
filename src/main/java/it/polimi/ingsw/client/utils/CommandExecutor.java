package it.polimi.ingsw.client.utils;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.utils.constants.Commands;
import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.DumbProduceLeaderCard;
import it.polimi.ingsw.client.utils.exceptions.*;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.*;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.utils.ProductionCombo;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class has the goal of executing all the commands which are considered valid
 */

public class CommandExecutor {
    private final DumbModel dumbModel;
    private final InputVerifier inputVerifier;
    private ClientSocket clientSocket;

    public CommandExecutor(DumbModel dumbModel, ClientSocket clientSocket) {
        this.dumbModel = dumbModel;
        this.inputVerifier = new InputVerifier(dumbModel);
        this.clientSocket = clientSocket;
    }

    /**
     * This method has the goal of parsing a command, checking if it's well formatted and
     * eventually sending it to server if it is also a valid command
     * @param insertedCommand the command written by the user on the cli
     * @return true if command is correct
     * @throws PersonalBoardException thrown when user wants to see a valid personal board
     * @throws CommonsException thrown when user wants to see commons
     * @throws WrongCommandException thrown when the passed command isn't recognized as one
     * @throws WrongCommandArgsException thrown when the passed arguments aren't formatted properly
     * @throws InvalidArgsException thrown when the passed arguments are correctly formatted, but still not valid
     * @throws HelpException thrown when user wants to see help menu
     * @throws CommandHelpException thrown when user wants to see help menu specificly for a command
     * @throws QuitException thrown when user wants to quit from the game
     */
    public boolean executeCommand(String insertedCommand) throws
            PersonalBoardException,
            CommonsException,
            WrongCommandException,
            HelpException,
            CommandHelpException,
            QuitException,
            InvalidArgsException
    {

        insertedCommand = insertedCommand.toLowerCase();

        String finalInsertedCommand = insertedCommand.toLowerCase();

        Commands correspondentCommand = Arrays.stream(Commands.values()).filter(
                command -> finalInsertedCommand.contains(command.getCommand())
        ).findFirst().orElse(Commands.HELP);

        String[] commandArgs = finalInsertedCommand.split(" ");

        if(commandArgs[commandArgs.length-1].equals("-h"))
            throw new CommandHelpException(correspondentCommand);

        switch (correspondentCommand){
            case COMMONS -> throw new CommonsException();
            case PERSONAL_BOARD -> managePersonalBoardCommand(insertedCommand);
            case ACTIVATE_LEADER_CARD -> manageActivateCommand(insertedCommand);
            case DISCARD_LEADER_CARD -> manageDiscardCommand(insertedCommand);
            case PRODUCE -> manageProduceCommand(insertedCommand);
            case BUY_DEVELOPMENT_CARD -> manageBuyCommand(insertedCommand);
            case TAKE_FROM_MARKET -> manageTakeCommand(insertedCommand);
            case HELP -> throw new HelpException();
            case QUIT -> throw new QuitException();
            case PICK_RESOURCES -> managePickResourcesCommand(insertedCommand);
            case PICK_LEADER_CARDS -> managePickLeaderCardCommand(insertedCommand);
            case PICK_TEMP_MARBLES -> managePickTempMarblesCommand(insertedCommand);
            default -> throw new WrongCommandException();
        }

        return true;
    }

    /**
     * This method has the goal of properly parsing a show personal board command
     * @param command the command typed by the user on the cli
     * @throws WrongCommandArgsException thrown when the passed arguments aren't formatted properly
     * @throws InvalidArgsException thrown when the passed arguments are correctly formatted, but still not valid
     */
    private void managePersonalBoardCommand(String command) throws PersonalBoardException, WrongCommandArgsException, InvalidArgsException {
        Queue<String> commandArgs = new LinkedList<>(Arrays.asList(command.split(" ")));
        commandArgs.remove();//show
        commandArgs.remove();//personal
        commandArgs.remove();//board
        
        //parse index of personal board from command
        int index;
        try {
            index = Integer.parseInt(Objects.requireNonNull(commandArgs.poll()));
        }catch (NullPointerException e){
            throw new WrongCommandArgsException();
        }

        //if action is valid, send it to server
        if(inputVerifier.canSeePersonalBoard(index))
            throw new PersonalBoardException(dumbModel.getPersonalBoards().get(index-1));
        else
            throw new InvalidArgsException();
    }

    /**
     * This method has the goal of properly parsing an activate leader card command
     * @param command the command typed by the user on the cli
     * @throws WrongCommandArgsException thrown when the passed arguments aren't formatted properly
     * @throws InvalidArgsException thrown when the passed arguments are correctly formatted, but still not valid
     */
    private void manageActivateCommand(String command) throws WrongCommandArgsException, InvalidArgsException {
        Queue<String> commandArgs = new LinkedList<>(Arrays.asList(command.split(" ")));
        commandArgs.remove();//activate
        
        //parse index of leader card from command
        int index;
        try {
            index = Integer.parseInt(Objects.requireNonNull(commandArgs.poll()));
            if(index<0 || index>dumbModel.getOwnPersonalBoard().getLeaderCards().size())
                throw new WrongCommandArgsException();
        }catch (NullPointerException | NumberFormatException | WrongCommandArgsException e){
            throw new WrongCommandArgsException();
        }

        DumbLeaderCard chosenLeaderCard = dumbModel.getOwnPersonalBoard().getLeaderCards().get(index-1);

        //if action is valid, send it to server
        if(inputVerifier.canActivate(chosenLeaderCard))
            clientSocket.sendAction(
                    new ActivateLeaderCardAction(dumbModel.getOwnPersonalBoard().getLeaderCards().get(index-1))
            );
        else
            throw new InvalidArgsException();
    }

    /**
     * This method has the goal of properly parsing a discard leader card command
     * @param command the command typed by the user on the cli
     * @throws WrongCommandArgsException thrown when the passed arguments aren't formatted properly
     * @throws InvalidArgsException thrown when the passed arguments are correctly formatted, but still not valid
     */
    private void manageDiscardCommand(String command) throws WrongCommandArgsException, InvalidArgsException {
        Queue<String> commandArgs = new LinkedList<>(Arrays.asList(command.split(" ")));
        commandArgs.remove();//discard
        
        //parse index of leader card from command
        int index;
        try {
            index = Integer.parseInt(Objects.requireNonNull(commandArgs.poll()));
            if(index<0 || index>dumbModel.getOwnPersonalBoard().getLeaderCards().size())
                throw new WrongCommandArgsException();
        }catch (NullPointerException | NumberFormatException | WrongCommandArgsException e){
            throw new WrongCommandArgsException();
        }

        DumbLeaderCard chosenLeaderCard = dumbModel.getOwnPersonalBoard().getLeaderCards().get(index-1);

        //if action is valid, send it to server
        if(inputVerifier.canDiscard(chosenLeaderCard))
            clientSocket.sendAction(
                    new DiscardLeaderCardAction(dumbModel.getOwnPersonalBoard().getLeaderCards().get(index-1))
            );
        else
            throw new InvalidArgsException();
    }


    /**
     * This method has the goal of properly parsing a buy from development cards board command
     * @param command the command typed by the user on the cli
     * @throws WrongCommandArgsException thrown when the passed arguments aren't formatted properly
     * @throws InvalidArgsException thrown when the passed arguments are correctly formatted, but still not valid
     */
    private void manageBuyCommand(String command) throws WrongCommandArgsException, InvalidArgsException {
        Queue<String> commandArgs = new LinkedList<>(Arrays.asList(command.split(" ")));
        commandArgs.remove();//buy
        
        //parse level from command
        Level chosenLevel;
        try {
            chosenLevel = Level.getLevel(
                    Integer.parseInt(
                            Objects.requireNonNull(commandArgs.poll())
                    )
            );
        }catch (NullPointerException | IllegalArgumentException e){
            throw new WrongCommandArgsException();
        }

        //parse color from command
        Color chosenColor;
        try {
            chosenColor = Color.getColor(Objects.requireNonNull(commandArgs.poll()));
        }catch (NullPointerException | IllegalArgumentException e){
            throw new WrongCommandArgsException();
        }


        int developmentCardSlotIndex;
        try {
            developmentCardSlotIndex = Integer.parseInt(Objects.requireNonNull(commandArgs.poll()));
        }catch (NullPointerException | NumberFormatException e){
            throw new WrongCommandArgsException();
        }

        //manage discarded resources from depots
        Map<Resource, Integer> depotsResources = parseResources(commandArgs, "-depots", "-strongbox");

        //manage discarded resources from strongbox
        Map<Resource, Integer> strongboxResources = parseResources(commandArgs, "-strongbox", null);

        //if action is valid, send it to server
        if(inputVerifier.canBuy(chosenLevel, chosenColor, developmentCardSlotIndex, depotsResources, strongboxResources))
            clientSocket.sendAction(
                    new BuyDevelopmentCardAction(chosenLevel, chosenColor, developmentCardSlotIndex, depotsResources, strongboxResources)
            );
        else
            throw new InvalidArgsException();
    }

    /**
     * This method has the goal of properly parsing a take from market command
     * @param command the command typed by the user on the cli
     * @throws WrongCommandArgsException thrown when the passed arguments aren't formatted properly
     * @throws InvalidArgsException thrown when the passed arguments are correctly formatted, but still not valid
     */
    private void manageTakeCommand(String command) throws WrongCommandArgsException, InvalidArgsException {
        Queue<String> commandArgs = new LinkedList<>(Arrays.asList(command.split(" ")));
        commandArgs.remove();//take

        //parse place(row or column) from command
        String place;
        try {
            place = Objects.requireNonNull(commandArgs.poll()).toLowerCase();
        }catch (NullPointerException e){
            throw new WrongCommandArgsException();
        }

        //parse index of row or column from command
        int index;
        try {
            index = Integer.parseInt(Objects.requireNonNull(commandArgs.poll()));
        }catch (NullPointerException | NumberFormatException e){
            throw new WrongCommandArgsException();
        }

        //if action is valid, send it to server
        if(inputVerifier.canTake(place, index))
            clientSocket.sendAction(
                    new TakeFromMarketAction(index, place.equals("row"))
            );
        else
            throw new InvalidArgsException();
    }


    //todo: add loop for DC and flag to make sure >1 productions
    /**
     * This method has the goal of properly parsing a take from produce command
     * @param command the command typed by the user on the cli
     * @throws WrongCommandArgsException thrown when the passed arguments aren't formatted properly
     * @throws InvalidArgsException thrown when the passed arguments are correctly formatted, but still not valid
     */
    private void manageProduceCommand(String command) throws WrongCommandArgsException, InvalidArgsException {
        Queue<String> commandArgs = new LinkedList<>(Arrays.asList(command.split(" ")));
        commandArgs.remove();//produce

        List<DumbDevelopmentCard> developmentCards = new ArrayList<>();
        Map<Resource, Integer> defaultSlotOutput = new HashMap<>();
        Map<DumbProduceLeaderCard, Resource> leaderCardProduction  = new HashMap<>();
        Map<Resource, Integer> discardedResourcesFromDepots  = new HashMap<>();
        Map<Resource, Integer> discardedResourcesFromStrongbox  = new HashMap<>();

        //parse default production arguments from command
        if(commandArgs.peek()==null)
            throw new WrongCommandArgsException();

        if(commandArgs.peek().equals("-default")){
            commandArgs.remove();
            //read resource type
            Resource defaultResource;
            try {
                defaultResource = Resource.getResource(Objects.requireNonNull(commandArgs.poll()));
            }catch (NullPointerException | IllegalArgumentException e){
                throw new WrongCommandArgsException();
            }
            //add resource to the map
            defaultSlotOutput.put(defaultResource, 1);
        }

        //loop twice because there can be a maximum of 2 leader cards
        for(int i=0; i<2; i++) {
            //parse first leader card production arguments from command
            if (commandArgs.peek() == null)
                throw new WrongCommandArgsException();

            if (commandArgs.peek().equals("-leadercard")) {
                commandArgs.remove();

                //parse index of leader card
                int index;
                try {
                    index = Integer.parseInt(Objects.requireNonNull(commandArgs.poll()));
                    if(index<0 || index>dumbModel.getOwnPersonalBoard().getLeaderCards().size())
                        throw new WrongCommandArgsException();
                }catch (NullPointerException | NumberFormatException | WrongCommandArgsException e){
                    throw new WrongCommandArgsException();
                }

                //parse resource type
                Resource leaderCardResource;
                try {
                    leaderCardResource = Resource.getResource(Objects.requireNonNull(commandArgs.poll()));
                } catch (NullPointerException | IllegalArgumentException e) {
                    throw new WrongCommandArgsException();
                }

                //Retrieve the chosen leader card and make sure it is a production leader card
                DumbProduceLeaderCard leaderCard;
                try {
                    if(dumbModel.getOwnPersonalBoard().getLeaderCards().get(index - 1).getAbility() != LeaderCardAbility.PRODUCE)
                        throw new WrongCommandArgsException();
                    leaderCard = (DumbProduceLeaderCard) dumbModel.getOwnPersonalBoard().getLeaderCards().get(index - 1);
                } catch (ClassCastException | WrongCommandArgsException e) {
                    throw new InvalidArgsException();
                }

                //add resource to the map
                leaderCardProduction.put(leaderCard, leaderCardResource);
            }
        }

        //parse development cards production arguments from command
        if(commandArgs.peek()==null)
            throw new WrongCommandArgsException();

        if(commandArgs.peek().equals("-developmentcards")){
            commandArgs.remove();

            //parse index of development card
            int index;
            try {
                index = Integer.parseInt(Objects.requireNonNull(commandArgs.poll()));
                if(index<0 || index>3)
                    throw new WrongCommandArgsException();
            } catch (NullPointerException | WrongCommandArgsException | NumberFormatException e) {
                throw new WrongCommandArgsException();
            }

            //retrieve development card
            DumbDevelopmentCard developmentCard;
            try {
                 developmentCard = dumbModel.getOwnPersonalBoard().getDevelopmentCardSlots().get(index-1).getDevelopmentCards().get(0);
            }catch (IndexOutOfBoundsException e){
                throw new WrongCommandArgsException();
            }

            //add development card to the list
            developmentCards.add(developmentCard);
        }

        //manage discarded resources from depots
        discardedResourcesFromDepots = parseResources(commandArgs, "-depots", "-strongbox");

        //manage discarded resources from strongbox
        discardedResourcesFromStrongbox = parseResources(commandArgs, "-strongbox", null);

        //Create a production combo based on parameters
        ProductionCombo chosenProductionCombo = new ProductionCombo();
        if(developmentCards.size()>0)
            chosenProductionCombo.setDevelopmentCards(developmentCards);
        if(defaultSlotOutput.size()>0)
            chosenProductionCombo.setDefaultSlotOutput(defaultSlotOutput);
        if(leaderCardProduction.size()>0)
            chosenProductionCombo.setLeaderCardProduction(leaderCardProduction);
        chosenProductionCombo.setDiscardedResourcesFromDepots(discardedResourcesFromDepots);
        chosenProductionCombo.setDiscardedResourcesFromStrongbox(discardedResourcesFromStrongbox);

        //if action is valid, send it to server
        if(inputVerifier.canProduce(chosenProductionCombo))
            clientSocket.sendAction(
                    new ActivateProductionAction(chosenProductionCombo)
            );
        else
            throw new InvalidArgsException();
    }


    /**
     * This method has the goal of properly parsing a pick resource command
     * @param command the command typed by the user on the cli
     * @throws WrongCommandArgsException thrown when the passed arguments aren't formatted properly
     * @throws InvalidArgsException thrown when the passed arguments are correctly formatted, but still not valid
     */
    private void managePickResourcesCommand(String command) throws WrongCommandArgsException, InvalidArgsException {
        Queue<String> commandArgs = new LinkedList<>(Arrays.asList(command.split(" ")));
        commandArgs.remove();//pick

        Map<Resource, Integer> pickedResources = parseResources(commandArgs, "resources", null);

        //if action is valid, send it to server
        if(inputVerifier.canPickResources(pickedResources))
            clientSocket.sendAction(
                    new PregameResourceChoiceAction(pickedResources)
            );
        else
            throw new InvalidArgsException();
    }

    /**
     * This method has the goal of properly parsing a pick leader cards command
     * @param command the command typed by the user on the cli
     * @throws WrongCommandArgsException thrown when the passed arguments aren't formatted properly
     * @throws InvalidArgsException thrown when the passed arguments are correctly formatted, but still not valid
     */
    private void managePickLeaderCardCommand(String command) throws WrongCommandArgsException, InvalidArgsException {
        Queue<String> commandArgs = new LinkedList<>(Arrays.asList(command.split(" ")));
        commandArgs.remove();//pick
        commandArgs.remove();//leader
        commandArgs.remove();//cards
        List<DumbLeaderCard> chosenLeaderCards = new ArrayList<>();

        //Try to read two indexes
        for(int i=0; i<2; i++) {
            //parse index of leader card
            int index;
            try {
                index = Integer.parseInt(Objects.requireNonNull(commandArgs.poll()));
                if(index< 0 || index>4 || chosenLeaderCards.contains(dumbModel.getTempLeaderCards().get(index-1)))
                    throw new WrongCommandArgsException();
            } catch (NullPointerException | WrongCommandArgsException | NumberFormatException e) {
                throw new WrongCommandArgsException();
            }

            //retrieve correct development card
            DumbLeaderCard chosenLeaderCard = dumbModel.getTempLeaderCards().get(index-1);
            chosenLeaderCards.add(chosenLeaderCard);
        }

        //if action is valid, send it to server
        if(inputVerifier.canPickLeaderCards(chosenLeaderCards))
            clientSocket.sendAction(
                    new PregameLeaderCardsChoiceAction(chosenLeaderCards)
            );
        else
            throw new InvalidArgsException();
    }

    /**
     * This method has the goal of properly parsing a pick marbles command
     * @param command the command typed by the user on the cli
     * @throws WrongCommandArgsException thrown when the passed arguments aren't formatted properly
     * @throws InvalidArgsException thrown when the passed arguments are correctly formatted, but still not valid
     */
    private void managePickTempMarblesCommand(String command)throws WrongCommandArgsException, InvalidArgsException {
        Queue<String> commandArgs = new LinkedList<>(Arrays.asList(command.split(" ")));
        commandArgs.remove();//pick
        commandArgs.remove();//marbles

        List<MarketMarble> chosenMarbles = new ArrayList<>();

        //loop as long as there's data in the list
        while ((commandArgs.peek()!= null)){
            //parse color of market mable
            String color;
            try {
                color = Objects.requireNonNull(commandArgs.poll());
            } catch (NullPointerException e) {
                throw new WrongCommandArgsException();
            }

            //Check if color selection is valid
            MarketMarble chosenMarble;
            try {
                chosenMarble = MarketMarble.getMarble(color);
            }catch (IllegalArgumentException e){
                throw new WrongCommandArgsException();
            }
            chosenMarbles.add(chosenMarble);
        }

        Map<MarketMarble, Integer> chosenMarblesMap = chosenMarbles.stream().sorted().collect(
                Collectors.toMap(
                        marketMarble -> marketMarble,
                        marketMarble -> 1,
                        Integer::sum
                )
        );

        //if action is valid, send it to server
        if(inputVerifier.canPickTempMarbles(chosenMarblesMap))
            clientSocket.sendAction(
                    new SelectMarblesAction(chosenMarblesMap)
            );
        else
            throw new InvalidArgsException();
    }

    /**
     * This method reads arguments from a command between some delimiters and parses them as a map of resources
     * @param commandArgs the arguments written by user on cli
     * @param from the first expected word in the args
     * @param to the first element after the map of resources
     * @return
     * @throws WrongCommandArgsException
     */
    private Map<Resource, Integer> parseResources(Queue<String> commandArgs, String from, String to) throws WrongCommandArgsException {
        Map<Resource, Integer> resources = new HashMap<>();

        //manage discarded resources from depots
        if(commandArgs.peek()==null)
            throw new WrongCommandArgsException();

        if(commandArgs.peek().equals(from)){
            commandArgs.remove();
            while ((commandArgs.peek()!= null && !commandArgs.peek().equals(to))){

                //read multiplicity of resource
                int count;
                try {
                    count = Integer.parseInt(Objects.requireNonNull(commandArgs.poll()));
                }catch (NullPointerException | NumberFormatException e){
                    throw new WrongCommandArgsException();
                }

                //read resource type
                Resource resource;
                try {
                    resource = Resource.getResource(Objects.requireNonNull(commandArgs.poll()));
                }catch (NullPointerException | IllegalArgumentException e){
                    throw new WrongCommandArgsException();
                }

                //put it in the depots map
                resources.put(resource, count);
            }
        }
        return resources;
    }

    public void setClientSocket(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }
}
