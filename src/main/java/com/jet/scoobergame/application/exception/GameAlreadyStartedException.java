package com.jet.scoobergame.application.exception;

/**
 * Exception thrown when trying to start a game that has already been started.
 *
 * @author Nikhil
 */
public class GameAlreadyStartedException extends RuntimeException {
    public GameAlreadyStartedException(String message) {
        super(message);
    }
}
