package com.jet.scoobergame.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.jet.scoobergame.domain.model.Game;
import com.jet.scoobergame.domain.model.GameState;
import com.jet.scoobergame.domain.model.Player;
import com.jet.scoobergame.domain.service.GameService;

@SpringBootTest
class GameServiceTest {
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
    }

    @Test
    void startGameShouldInitializeGameWithTwoPlayers() {
        Player player1 = new Player("Nikhil");
        Player player2 = new Player("Akhil");
        Game game = gameService.startGame(player1, List.of(player1, player2));

        assertNotNull(game);
        assertEquals(player1, game.getCurrentPlayer());
        assertEquals(2, game.getPlayers().size());
    }

    @Test
    void processMoveShouldProceedToNextMoveWhenGameIsNotOver() {
        Player player1 = new Player("Nikhil");
        Player player2 = new Player("Akhil");
        Game game = new Game(UUID.randomUUID(), player1, new GameState(10), List.of(player1, player2), 1);

        Game updatedGame = gameService.processMove(game);

        assertNotNull(updatedGame);
        assertEquals(player2, updatedGame.getCurrentPlayer()); // Next player's turn
        assertTrue(updatedGame.getGameState().getNumber() < 10); // Number should decrease
    }

    @Test
    void processMoveShouldEndGameWhenGameIsOver() {
        Player player1 = new Player("Nikhil");
        Game game = new Game(UUID.randomUUID(), player1, new GameState(1), List.of(player1), 1);

        Game updatedGame = gameService.processMove(game);

        assertNotNull(updatedGame);
        assertTrue(updatedGame.getGameState().isGameOver());
    }

    @Test
    void addPlayerShouldAddPlayerToGame() {
        Player player1 = new Player("Nikhil");
        Game game = new Game(UUID.randomUUID(), player1, new GameState(10), List.of(player1), 1);

        Player newPlayer = new Player("Divya");
        Game updatedGame = gameService.addPlayer(game, newPlayer);

        assertNotNull(updatedGame);
        assertEquals(2, updatedGame.getPlayers().size());
        assertTrue(updatedGame.getPlayers().contains(newPlayer));
    }

    @Test
    void isLatestVersionShouldReturnTrueWhenVersionIsHigher() {
        Game game = new Game(UUID.randomUUID(), new Player("Nikhil"), new GameState(10), List.of(new Player("Nikhil")), 2);
        assertTrue(gameService.isLatestVersion(game, 1));
    }

    @Test
    void isLatestVersionShouldReturnFalseWhenVersionIsLowerOrEqual() {
        Game game = new Game(UUID.randomUUID(), new Player("Nikhil"), new GameState(10), List.of(new Player("Nikhil")), 1);
        assertFalse(gameService.isLatestVersion(game, 2));
        assertFalse(gameService.isLatestVersion(game, 1));
    }
}