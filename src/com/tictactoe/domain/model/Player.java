package com.tictactoe.domain.model;

import com.tictactoe.domain.enums.CellState;
import com.tictactoe.domain.enums.PlayerType;
import com.tictactoe.service.ai.*;
import java.util.Objects;

public class Player {
    private final String name;
    private final CellState symbol;
    private final PlayerType type;
    private final AIStrategy aiStrategy;
    
    public Player(String name, CellState symbol, PlayerType type) {
        if (symbol == CellState.EMPTY) {
            throw new IllegalArgumentException("Player cannot have EMPTY as symbol");
        }
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.symbol = Objects.requireNonNull(symbol, "Symbol cannot be null");
        this.type = Objects.requireNonNull(type, "Type cannot be null");
        this.aiStrategy = createAIStrategy(type);
    }
    
    private AIStrategy createAIStrategy(PlayerType type) {
        return switch (type) {
            case AI_EASY -> new EasyAIStrategy();
            case AI_MEDIUM -> new MediumAIStrategy();
            case AI_HARD -> new HardAIStrategy();
            case HUMAN -> null;
        };
    }
    
    public String getName() {
        return name;
    }
    
    public CellState getSymbol() {
        return symbol;
    }
    
    public PlayerType getType() {
        return type;
    }
    
    public boolean isAI() {
        return type.isAI();
    }
    
    public AIStrategy getAIStrategy() {
        if (!isAI()) {
            throw new IllegalStateException("Player is not AI");
        }
        return aiStrategy;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return symbol == player.symbol;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
    
    @Override
    public String toString() {
        return name + " (" + symbol + ")";
    }
}
