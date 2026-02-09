package com.tictactoe.domain.service;

import com.tictactoe.domain.enums.CellState;
import com.tictactoe.domain.model.Board;
import com.tictactoe.domain.model.Position;

public class WinConditionChecker {
    
    public boolean hasWon(Board board, CellState player) {
        int size = board.getSize();
        
        for (int row = 0; row < size; row++) {
            if (checkRow(board, row, player)) {
                return true;
            }
        }
        
        for (int col = 0; col < size; col++) {
            if (checkColumn(board, col, player)) {
                return true;
            }
        }
        
        return checkMainDiagonal(board, player) || checkAntiDiagonal(board, player);
    }
    
    private boolean checkRow(Board board, int row, CellState player) {
        int size = board.getSize();
        for (int col = 0; col < size; col++) {
            if (board.getCellState(new Position(row, col)) != player) {
                return false;
            }
        }
        return true;
    }
    
    private boolean checkColumn(Board board, int col, CellState player) {
        int size = board.getSize();
        for (int row = 0; row < size; row++) {
            if (board.getCellState(new Position(row, col)) != player) {
                return false;
            }
        }
        return true;
    }
    
    private boolean checkMainDiagonal(Board board, CellState player) {
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            if (board.getCellState(new Position(i, i)) != player) {
                return false;
            }
        }
        return true;
    }
    
    private boolean checkAntiDiagonal(Board board, CellState player) {
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            if (board.getCellState(new Position(i, size - 1 - i)) != player) {
                return false;
            }
        }
        return true;
    }
}
