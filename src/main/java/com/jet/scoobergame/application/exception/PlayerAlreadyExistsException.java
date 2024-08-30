package com.jet.scoobergame.application.exception;

/**
 * Exception thrown when trying to add a player that already exists in the game.
 *
 * @author Nikhil
 */
public class PlayerAlreadyExistsException extends RuntimeException {
    public PlayerAlreadyExistsException(String message) {
        super(message);
    }
}
