package it.polimi.ingsw.server.connection.state;

import it.polimi.ingsw.network.clienttoserver.SerializedMessage;
import it.polimi.ingsw.network.clienttoserver.messages.CreateLobbyMessage;
import it.polimi.ingsw.server.connection.Connection;
import it.polimi.ingsw.server.Lobby;

import java.util.logging.Logger;

public class WaitingForGameSizeState extends ConnectionState {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Connection connection;

    public WaitingForGameSizeState(Connection connection) {
        super(connection);
        this.connection = connection;
    }

    @Override
    public boolean messageAllowed(SerializedMessage serializedMessage) {
        logger.info("Checking if message is allowed.");
        return serializedMessage.getMessage() instanceof CreateLobbyMessage;
    }

    @Override
    public void invalidMessage() {
        connection.invalidMessage();
        connection.readFromInputStream();
    }

    @Override
    public void readMessage(SerializedMessage serializedMessage) {
        logger.info("Reading lobby size");
        int selectedSize = ((CreateLobbyMessage) serializedMessage.getMessage()).getSize();
        if (selectedSize <= 0 || selectedSize > 4) {
            connection.invalidSize();
        } else {
            logger.info("Received a valid lobby size.");
            Lobby.getInstance().setSize(selectedSize);
            Lobby.getInstance().addPlayer(connection.getNickname(), connection);
        }
    }
}
