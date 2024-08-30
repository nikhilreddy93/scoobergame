package com.jet.scoobergame.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * The game domain object.
 *
 * @author Nikhil
 */
public class Game {
	
	private final UUID gameId;
    private final Player currentPlayer;
    private final GameState gameState;
    private final List<Player> players;
    private final long version;

    @JsonCreator
    public Game(
            @JsonProperty("gameId") UUID gameId,
            @JsonProperty("currentPlayer") Player currentPlayer,
            @JsonProperty("gameState") GameState gameState,
            @JsonProperty("players") List<Player> players,
            @JsonProperty("version") long version) {
        this.gameId = gameId;
        this.currentPlayer = currentPlayer;
        this.gameState = gameState;
        this.players = Collections.unmodifiableList(players);  // Ensure immutability
        this.version = version;
    }

    public UUID getGameId() {
        return gameId;
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

    public long getVersion() {
        return version;
    }

    /**
     * Creates a new game with the next player and version.
     *
     * @return The updated game state after the move has been processed.
     */
    public Game registerPlayer(Player newPlayer) {
        List<Player> newPlayers = new ArrayList<>(this.players);
        newPlayers.add(newPlayer);
        return new Game(this.gameId, this.currentPlayer, this.gameState, newPlayers, this.version + 1);
    }

    /**
     * Proceeds the game to the next move by updating the current player and version.
     *
     * @return The updated game state after the move has been processed.
     */
    public Game proceedToNextMove() {
        int number = gameState.getNumber();
        int toSubtract = 0;

        if ((number - 1) % 3 == 0) {
            toSubtract = -1;
        } else if ((number + 1) % 3 == 0) {
            toSubtract = 1;
        }

        int newNumber = (number + toSubtract) / 3;
        Player nextPlayer = getNextPlayer();  // Update to the next player
        GameState newGameState = new GameState(newNumber);
        newGameState.setOldNumber(number);
        newGameState.setAddedNumber(toSubtract);
        return new Game(this.gameId, nextPlayer, newGameState, this.players, this.version + 1);
    }

    /**
     * Gets the next player in the list of players.
     *
     * @return The next player.
     */
    private Player getNextPlayer() {
        int currentIndex = players.indexOf(currentPlayer);
        int nextIndex = (currentIndex + 1) % players.size();
        return players.get(nextIndex);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", currentPlayer=" + currentPlayer +
                ", gameState=" + gameState +
                ", players=" + players +
                ", version=" + version +
                '}';
    }
}
