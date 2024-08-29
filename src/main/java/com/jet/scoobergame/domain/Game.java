package com.jet.scoobergame.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Game {

    private final Player currentPlayer;
    private final GameState gameState;
    private final List<Player> players;

    @JsonCreator
    public Game(@JsonProperty("currentPlayer") Player currentPlayer, 
    		@JsonProperty("gameState") GameState gameState, 
    		@JsonProperty("players") List<Player> players) {
        this.currentPlayer = currentPlayer;
        this.gameState = gameState;
        this.players = new ArrayList<>(players);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameState getGameState() {
        return gameState;
    }

    public List<Player> getPlayers() {
        return players;
    }
    
 // Register a new player to the game
    public Game registerPlayer(Player newPlayer) {
        if (!players.contains(newPlayer)) {
            players.add(newPlayer);
            System.out.println(newPlayer.getName() + " has joined the game.");
            return new Game(newPlayer, gameState, players);
        }
        return new Game(currentPlayer, gameState, players);  // Return the updated game state
    }

    // Determine the next player based on the current list of players
    private Player getNextPlayer() {
        int currentIndex = players.indexOf(getCurrentPlayer());
        int nextIndex = (currentIndex + 1) % getPlayers().size();
//        System.out.println("currentIndex = "+ currentIndex + " and nextIndex = "+ nextIndex);
        return getPlayers().get(nextIndex);
    }

    // Proceed to the next move, updating the current player to the next in line
    public Game proceedToNextMove() {
        int number = gameState.getNumber();
        int toSubtract = 0;

        if ((number - 1) % 3 == 0) {
            toSubtract = -1;
        } else if ((number + 1) % 3 == 0) {
            toSubtract = 1;
        }

        int newNumber = (number + toSubtract) / 3;
        Player nextPlayer = getNextPlayer(); // Update to the next player

        return new Game(nextPlayer, new GameState(newNumber), players);
    }
}
