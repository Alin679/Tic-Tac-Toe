package com.tictactoe.domain.service;

import com.tictactoe.domain.enums.CellState;
import com.tictactoe.domain.enums.GameState;
import com.tictactoe.domain.model.Board;
import com.tictactoe.domain.model.Move;
import com.tictactoe.domain.model.Player;
import java.util.Objects;

public class GameEngine {
    private final Board board;
    private final Player playerX;
    private final Player playerO;
    private final WinConditionChecker winChecker;
    
    private CellState currentTurn;
    private GameState gameState;
    
    public GameEngine(Board board, Player playerX, Player playerO, WinConditionChecker winChecker) {
        this.board = Objects.requireNonNull(board, "Board cannot be null");
        this.playerX = Objects.requireNonNull(playerX, "Player X cannot be null");
        this.playerO = Objects.requireNonNull(playerO, "Player O cannot be null");
        this.winChecker = Objects.requireNonNull(winChecker, "WinConditionChecker cannot be null");
        
        this.currentTurn = CellState.X;
        this.gameState = GameState.IN_PROGRESS;
    }
    
    public boolean executeMove(Move move) {
        if (gameState.isGameOver()) {
            return false;
        }
        
        if (move.getPlayer() != currentTurn) {
            return false;
        }
        
        if (!board.makeMove(move.getPosition(), move.getPlayer())) {
            return false;
        }
        
        updateGameState();
        
        if (!gameState.isGameOver()) {
            switchTurn();
        }
        
        return true;
    }
    
    private void updateGameState() {
        if (winChecker.hasWon(board, CellState.X)) {
            gameState = GameState.X_WON;
        } else if (winChecker.hasWon(board, CellState.O)) {
            gameState = GameState.O_WON;
        } else if (board.isFull()) {
            gameState = GameState.DRAW;
        }
    }
    
    private void switchTurn() {
        currentTurn = currentTurn.getOpposite();
    }
    
    public void resetGame() {
        board.reset();
        currentTurn = CellState.X;
        gameState = GameState.IN_PROGRESS;
    }
    
    public CellState getCurrentTurn() {
        return currentTurn;
    }
    
    public GameState getGameState() {
        return gameState;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public Player getPlayerX() {
        return playerX;
    }
    
    public Player getPlayerO() {
        return playerO;
    }
    
    public Player getCurrentPlayer() {
        return currentTurn == CellState.X ? playerX : playerO;
    }
    
    public Player getWinner() {
        return switch (gameState) {
            case X_WON -> playerX;
            case O_WON -> playerO;
            default -> null;
        };
    }
}
