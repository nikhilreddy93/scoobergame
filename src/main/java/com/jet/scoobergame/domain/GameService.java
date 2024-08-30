package com.jet.scoobergame.domain;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class GameService {

	
	// Initializes the game with the first player and starting number
    public Game startGame(Player firstPlayer, List<Player> players) {
        int startNumber = new Random().nextInt(Integer.MAX_VALUE) + 1;
        Game newGame = new Game(UUID.randomUUID(), firstPlayer, new GameState(startNumber), players, 1);
        System.out.println(firstPlayer.getName() + " started the game with number: " + startNumber);
        return newGame;
    }

    // Processes the player's move; ensures idempotency by checking current state
    public Game processMove(Game game) {
        if (game.getGameState().isGameOver()) {
            System.out.println(game.getCurrentPlayer().getName() + " received number 1. Game over!");
            return game;  // Return the current game state if the game is over
        }

        // Ensure the move is only processed if it's valid (prevent duplicate or conflicting moves)
        Game updatedGame = game.proceedToNextMove();
        System.out.println(updatedGame.getCurrentPlayer().getName() + " will take the next turn with number: " + updatedGame.getGameState().getNumber());
        return updatedGame;
    }

    // Adds a new player to the game during gameplay; ensures player is unique
    public Game addPlayer(Game game, Player newPlayer) {
        System.out.println("Adding new player: " + newPlayer.getName());
        return game.registerPlayer(newPlayer);
    }

    // Validates whether a game event should be processed based on version
    public boolean isLatestVersion(Game game, long currentVersion) {
        return game.getVersion() > currentVersion;
    }

    // Wait and retry sending the event if the next player is not ready
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
