package com.jet.scoobergame.domain;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
/**
 * Manages the game lifecycle and tracks the current game state.
 *
 * @author Nikhil
 */
@Service
public class GameService {

    /**
     * Initializes the game with the first player and starting number
     *
     * @param firstPlayer The first player to start the game
     * @param players The list of players in the game
     * @return Game
     */
    public Game startGame(Player firstPlayer, List<Player> players) {
        int startNumber = new Random().nextInt(Integer.MAX_VALUE) + 1;
        Game newGame = new Game(UUID.randomUUID(), firstPlayer, new GameState(startNumber), players, 1);
        System.out.println(firstPlayer.getName() + " started the game with number: " + startNumber);
        return newGame;
    }

    /**
     * Processes the player's move and ensures idempotency by checking current state.
     *
     * @param game The game state to process the move for.
     * @return The updated game state after the move has been processed.
     */
    public Game processMove(Game game) {
        if (game.getGameState().isGameOver()) {
            System.out.println(game.getCurrentPlayer().getName() + " received number 1. Game over!");
            return game;  // Return the current game state if the game is over
        }

        // Ensure the move is only processed if it's valid (prevent duplicate or conflicting moves)
        Game updatedGame = game.proceedToNextMove();
        System.out.println("Received Number: "+ game.getGameState().getNumber() + ", New number: " + game.getGameState().getOldNumber() + ", Added number: " + game.getGameState().getAddedNumber());
        System.out.println(updatedGame.getCurrentPlayer().getName() + " will take the next turn with number: " + updatedGame.getGameState().getNumber());
        return updatedGame;
    }

    /**
     * Adds a new player to the game during gameplay; ensures player is unique
     * @param game The current game state.
     * @param newPlayer The new player to add.
     * @return The updated game state after waiting and retrying.
     */
    public Game addPlayer(Game game, Player newPlayer) {
        System.out.println("Adding new player: " + newPlayer.getName());
        return game.registerPlayer(newPlayer);
    }

    /**
     * Validates whether a game event should be processed based on version.
     *
     * @param game The game state to check.
     * @param currentVersion The current version of the game.
     * @return true if the game event should be processed, false otherwise.
     */
    public boolean isLatestVersion(Game game, long currentVersion) {
        return game.getVersion() > currentVersion;
    }

    /**
     * Waits for 3 seconds and retries sending the game event if the next player is not ready.
     *
     * @param currentGame The current game state.
     * @return The current game state after waiting and retrying.
     */
    public Game waitAndRetry(Game currentGame) {
        try {
            TimeUnit.SECONDS.sleep(3); // Wait for 3 seconds before retrying
            System.out.println("Retrying turn for player: " + currentGame.getCurrentPlayer().getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Retry interrupted.");
        }
        return currentGame;  // Return the current game state after retrying
    }

}
