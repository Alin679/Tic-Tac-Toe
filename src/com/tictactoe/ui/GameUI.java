package com.tictactoe.ui;

import com.tictactoe.controller.GameController;
import com.tictactoe.domain.enums.CellState;

public interface GameUI {
    
    void initialize(GameController controller);
    
    void show();
    
    void updateBoard(CellState[][] boardState);
    
    void updateStatus(String message);
    
    void showGameOver(String message);
}
