package com.tictactoe.service.ai;

import com.tictactoe.domain.model.Board;
import com.tictactoe.domain.model.Move;
import com.tictactoe.domain.model.Position;
import com.tictactoe.domain.enums.CellState;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EasyAIStrategy implements AIStrategy {
    private final Random random = new Random();
    
    @Override
    public Move calculateMove(Board board, CellState aiPlayer) {
        List<Position> availableMoves = getAvailableMoves(board);
        
        if (availableMoves.isEmpty()) {
            throw new IllegalStateException("No available moves");
        }
        
        Position randomPosition = availableMoves.get(random.nextInt(availableMoves.size()));
        return new Move(randomPosition, aiPlayer);
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
}
