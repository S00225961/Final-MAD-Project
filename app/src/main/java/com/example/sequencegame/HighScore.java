package com.example.sequencegame;

public class HighScore {
    private long id;
    private String playerName;
    private int playerScore;

    // Constructors, getters, and setters

    public void setId(long id) {
        this.id = id;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }
}
