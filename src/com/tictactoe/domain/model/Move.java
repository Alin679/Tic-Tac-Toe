package com.tictactoe.domain.model;

import com.tictactoe.domain.enums.CellState;
import java.util.Objects;

public final class Move {
    private final Position position;
    private final CellState player;
    
    public Move(Position position, CellState player) {
        if (player == CellState.EMPTY) {
            throw new IllegalArgumentException("Move cannot be made with EMPTY state");
        }
        this.position = Objects.requireNonNull(position, "Position cannot be null");
        this.player = Objects.requireNonNull(player, "Player cannot be null");
    }
    
    public Position getPosition() {
        return position;
    }
    
    public CellState getPlayer() {
        return player;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(position, move.position) && player == move.player;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(position, player);
    }
    
    @Override
    public String toString() {
        return "Move{" + "position=" + position + ", player=" + player + '}';
    }
}
