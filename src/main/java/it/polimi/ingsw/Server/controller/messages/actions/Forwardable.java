package it.polimi.ingsw.server.controller.messages.actions;

/**
 * This interface represents any type of message sent from the View to the Controller
 */
public interface Forwardable {
    void forward();
}