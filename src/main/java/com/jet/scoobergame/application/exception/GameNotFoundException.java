package com.jet.scoobergame.application.exception;

/**
 * Exception thrown when the game with the given id is not found.
 *
 * @author Nikhil
 */
public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String message) {
        super(message);
    }
}
