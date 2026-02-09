package com.tictactoe.domain.enums;

public enum CellState {
    EMPTY,
    X,
    O;
    
    public CellState getOpposite() {
        return switch (this) {
            case X -> O;
            case O -> X;
            case EMPTY -> throw new IllegalStateException("EMPTY has no opposite");
        };
    }
    
    public boolean isEmpty() {
        return this == EMPTY;
    }
    
    public boolean isOccupied() {
        return this != EMPTY;
    }
}
