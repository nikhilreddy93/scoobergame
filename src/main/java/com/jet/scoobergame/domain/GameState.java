package com.jet.scoobergame.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameState {
    private final int number;

    @JsonCreator
    public GameState(@JsonProperty("number") int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public boolean isGameOver() {
        return number == 1;
    }
}
