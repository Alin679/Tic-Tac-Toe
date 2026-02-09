package com.tictactoe.service.ai;

import com.tictactoe.domain.model.Board;
import com.tictactoe.domain.model.Move;
import com.tictactoe.domain.model.Position;
import com.tictactoe.domain.enums.CellState;
import com.tictactoe.domain.service.WinConditionChecker;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MediumAIStrategy implements AIStrategy {
    private final Random random = new Random();
    private final WinConditionChecker winChecker = new WinConditionChecker();
    
    @Override
    public Move calculateMove(Board board, CellState aiPlayer) {
        List<Position> availableMoves = getAvailableMoves(board);
        
        if (availableMoves.isEmpty()) {
            throw new IllegalStateException("No available moves");
        }
        
        Position winningMove = findWinningMove(board, aiPlayer, availableMoves);
        if (winningMove != null) {
            return new Move(winningMove, aiPlayer);
        }
        
        CellState opponent = aiPlayer.getOpposite();
        Position blockingMove = findWinningMove(board, opponent, availableMoves);
        if (blockingMove != null) {
            return new Move(blockingMove, aiPlayer);
        }
        
        int center = board.getSize() / 2;
        Position centerPos = new Position(center, center);
        if (board.isValidMove(centerPos)) {
            return new Move(centerPos, aiPlayer);
        }
        
        Position randomPosition = availableMoves.get(random.nextInt(availableMoves.size()));
        return new Move(randomPosition, aiPlayer);
    }
    
    private Position findWinningMove(Board board, CellState player, List<Position> moves) {
        for (Position pos : moves) {
            Board tempBoard = createBoardCopy(board);
            tempBoard.makeMove(pos, player);
            
            if (winChecker.hasWon(tempBoard, player)) {
                return pos;
            }
        }
        return null;
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
