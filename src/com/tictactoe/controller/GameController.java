package com.tictactoe.controller;

import com.tictactoe.domain.enums.GameState;
import com.tictactoe.domain.model.Move;
import com.tictactoe.domain.model.Position;
import com.tictactoe.domain.service.GameEngine;
import com.tictactoe.ui.GameUI;
import java.util.Objects;

public class GameController {
    protected final GameEngine gameEngine;
    protected final GameUI gameUI;
    
    public GameController(GameEngine gameEngine, GameUI gameUI) {
        this.gameEngine = Objects.requireNonNull(gameEngine, "GameEngine cannot be null");
        this.gameUI = Objects.requireNonNull(gameUI, "GameUI cannot be null");
    }
    
    public void startGame() {
        gameUI.initialize(this);
        updateUI();
        gameUI.show();
    }
    
    public void onCellClicked(int row, int col) {
        if (gameEngine.getGameState().isGameOver()) {
            return;
        }
        
        Position position = new Position(row, col);
        Move move = new Move(position, gameEngine.getCurrentTurn());
        
        boolean success = gameEngine.executeMove(move);
        
        if (success) {
            updateUI();
            checkGameOver();
        }
    }
    
    public void onRestartClicked() {
        gameEngine.resetGame();
        updateUI();
    }
    
    private void updateUI() {
        gameUI.updateBoard(gameEngine.getBoard().getBoardStateCopy());
        gameUI.updateStatus(buildStatusMessage());
    }
    
    private void checkGameOver() {
        GameState state = gameEngine.getGameState();
        
        if (state.isGameOver()) {
            gameUI.showGameOver(buildGameOverMessage(state));
        }
    }
    
    private String buildStatusMessage() {
        GameState state = gameEngine.getGameState();
        
        if (state == GameState.IN_PROGRESS) {
            return "Current turn: " + gameEngine.getCurrentPlayer();
        } else {
            return buildGameOverMessage(state);
        }
    }
    
    private String buildGameOverMessage(GameState state) {
        return switch (state) {
            case X_WON, O_WON -> gameEngine.getWinner() + " wins!";
            case DRAW -> "It's a draw!";
            case IN_PROGRESS -> "Game in progress";
        };
    }
}
