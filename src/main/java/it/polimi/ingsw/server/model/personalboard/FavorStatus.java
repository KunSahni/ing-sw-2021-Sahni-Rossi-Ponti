package it.polimi.ingsw.server.model.personalboard;

import it.polimi.ingsw.client.utils.constants.Constants;

public enum FavorStatus {
    INACTIVE(Constants.ANSI_RED),
    ACTIVE(Constants.ANSI_GREEN),
    DISCARDED(Constants.ANSI_GREY);

    private final String statusColor;

    FavorStatus(String statusColor) {
        this.statusColor = statusColor;
    }

    public String getStatusColor() {
        return statusColor;
    }
}
