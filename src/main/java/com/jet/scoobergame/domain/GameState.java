package com.jet.scoobergame.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameState {
    private final int number;
    private int oldNumber;
    private int addedNumber;

    @JsonCreator
    public GameState(@JsonProperty("number") int number) {
        this.number = number;
    }

    public void setOldNumber(int newNumber) {
        this.oldNumber = newNumber;
    }

    public void setAddedNumber(int addedNumber) {
        this.addedNumber = addedNumber;
    }

    public int getNumber() {
        return number;
    }

    public int getOldNumber() {
        return oldNumber;
    }

    public int getAddedNumber() {
        return addedNumber;
    }

    public boolean isGameOver() {
        return number == 1;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "number=" + number +
                ", newNumber=" + oldNumber +
                ", addedNumber=" + addedNumber +
                '}';
    }
}
