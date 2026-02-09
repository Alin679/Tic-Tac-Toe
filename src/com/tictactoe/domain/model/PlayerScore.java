package com.tictactoe.domain.model;

import java.util.Objects;

public class PlayerScore implements Comparable<PlayerScore> {
    private String playerName;
    private int wins;
    private int losses;
    private int draws;
    
    public PlayerScore(String playerName) {
        this.playerName = Objects.requireNonNull(playerName);
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
    }
    
    public PlayerScore(String playerName, int wins, int losses, int draws) {
        this.playerName = Objects.requireNonNull(playerName);
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }
    
    public void addWin() {
        wins++;
    }
    
    public void addLoss() {
        losses++;
    }
    
    public void addDraw() {
        draws++;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public int getWins() {
        return wins;
    }
    
    public int getLosses() {
        return losses;
    }
    
    public int getDraws() {
        return draws;
    }
    
    public int getTotalGames() {
        return wins + losses + draws;
    }
    
    public double getWinRate() {
        int total = getTotalGames();
        return total > 0 ? (double) wins / total * 100.0 : 0.0;
    }
    
    @Override
    public int compareTo(PlayerScore other) {
        int winsCompare = Integer.compare(other.wins, this.wins);
        if (winsCompare != 0) {
            return winsCompare;
        }
        return Double.compare(other.getWinRate(), this.getWinRate());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerScore that = (PlayerScore) o;
        return playerName.equals(that.playerName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(playerName);
    }
}
