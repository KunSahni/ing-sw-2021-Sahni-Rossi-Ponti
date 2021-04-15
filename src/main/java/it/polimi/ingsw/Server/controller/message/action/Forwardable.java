package it.polimi.ingsw.server.controller.message.action;

/**
 * This interface represents any type of message sent from the View to the Controller
 */
public interface Forwardable {
    void forward();
}