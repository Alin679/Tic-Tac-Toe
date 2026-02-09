package com.tictactoe.service.ai;

import com.tictactoe.domain.model.Board;
import com.tictactoe.domain.model.Move;
import com.tictactoe.domain.model.Position;
import com.tictactoe.domain.enums.CellState;
import com.tictactoe.domain.service.WinConditionChecker;
import java.util.ArrayList;
import java.util.List;

public class HardAIStrategy implements AIStrategy {
    private final WinConditionChecker winChecker = new WinConditionChecker();
    
    @Override
    public Move calculateMove(Board board, CellState aiPlayer) {
        List<Position> availableMoves = getAvailableMoves(board);
        
        if (availableMoves.isEmpty()) {
            throw new IllegalStateException("No available moves");
        }
        
        Position bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        
        for (Position move : availableMoves) {
            Board tempBoard = createBoardCopy(board);
            tempBoard.makeMove(move, aiPlayer);
            
            int score = minimax(tempBoard, 0, false, aiPlayer);
            
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        
        return new Move(bestMove, aiPlayer);
    }
    
    private int minimax(Board board, int depth, boolean isMaximizing, CellState aiPlayer) {
        if (winChecker.hasWon(board, aiPlayer)) {
            return 10 - depth; // Prefer faster wins
        }
        
        CellState opponent = aiPlayer.getOpposite();
        if (winChecker.hasWon(board, opponent)) {
            return depth - 10; // Prefer slower losses
        }
        
        if (board.isFull()) {
            return 0; // Draw
        }
        
        if (depth >= 6) {
            return 0;
        }
        
        List<Position> availableMoves = getAvailableMoves(board);
        
        if (isMaximizing) {
            int maxScore = Integer.MIN_VALUE;
            for (Position move : availableMoves) {
                Board tempBoard = createBoardCopy(board);
                tempBoard.makeMove(move, aiPlayer);
                int score = minimax(tempBoard, depth + 1, false, aiPlayer);
                maxScore = Math.max(maxScore, score);
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (Position move : availableMoves) {
                Board tempBoard = createBoardCopy(board);
                tempBoard.makeMove(move, opponent);
                int score = minimax(tempBoard, depth + 1, true, aiPlayer);
                minScore = Math.min(minScore, score);
            }
            return minScore;
        }
    }
    
    private List<Position> getAvailableMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        int size = board.getSize();
        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Position pos = new Position(row, col);
                if (board.isValidMove(pos)) {
                    moves.add(pos);
                }
            }
        }
        
        return moves;
    }
    
    private Board createBoardCopy(Board original) {
        Board copy = new Board(original.getSize());
        CellState[][] state = original.getBoardStateCopy();
        
        for (int row = 0; row < original.getSize(); row++) {
            for (int col = 0; col < original.getSize(); col++) {
                if (state[row][col] != CellState.EMPTY) {
                    copy.makeMove(new Position(row, col), state[row][col]);
                }
            }
        }
        
        return copy;
    }
}
