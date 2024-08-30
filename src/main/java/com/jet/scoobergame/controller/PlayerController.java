/**
 * 
 */
package com.jet.scoobergame.controller;

import com.jet.scoobergame.application.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 
 */
@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/add/{playerName}")
	public void addPlayer(@PathVariable("playerName") String playerName) {
        playerService.addNewPlayer(playerName);
    }
	
}
