/**
 * 
 */
package com.jet.scoobergame.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 */
@RestController
@RequestMapping("/game")
public class GameController {
	
	private final PlayerService playerService;

    @Autowired
    public GameController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/add/{playerName}")
	public void addPlayer(@PathVariable("playerName") String playerName) {
        playerService.addNewPlayer(playerName);
    }
    
    @GetMapping("/start")
	public void start() {
		playerService.startGame();
	}
	
}
