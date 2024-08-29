package com.jet.scoobergame.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Player {

    private String name;
    private Boolean isOnline;
    
    @JsonCreator
    public Player(@JsonProperty("name") String name) {
        this.name = name;
        this.isOnline = true;
    }

    public String getName() {
        return name;
    }

    public void setOnline(Boolean isOnline) {
    	this.isOnline = isOnline;
    }
    
    public Boolean isOnline() {
    	return isOnline;
    }

    // Override equals() to compare players based on their names
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    // Override hashCode() to ensure consistency with equals()
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }

}
