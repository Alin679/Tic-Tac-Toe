package com.tictactoe.domain.enums;

public enum PlayerType {
    HUMAN("Human"),
    AI_EASY("AI - Easy"),
    AI_MEDIUM("AI - Medium"),
    AI_HARD("AI - Hard");
    
    private final String displayName;
    
    PlayerType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isAI() {
        return this != HUMAN;
    }
}
