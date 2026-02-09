package com.tictactoe.domain.enums;

public enum GameState {
    IN_PROGRESS,
    X_WON,
    O_WON,
    DRAW;
    
    public boolean isGameOver() {
        return this != IN_PROGRESS;
    }
    
    public boolean hasWinner() {
        return this == X_WON || this == O_WON;
    }
}
