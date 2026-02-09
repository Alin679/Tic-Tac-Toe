package com.tictactoe.domain.model;

import com.tictactoe.domain.enums.CellState;
import java.util.Arrays;

public class Board {
    private final int size;
    private final CellState[][] cells;
    
    public Board(int size) {
        if (size < 3) {
            throw new IllegalArgumentException("Board size must be at least 3");
        }
        this.size = size;
        this.cells = new CellState[size][size];
        initializeBoard();
    }
    
    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            Arrays.fill(cells[i], CellState.EMPTY);
        }
    }
    
    public boolean makeMove(Position position, CellState player) {
        if (!isValidMove(position)) {
            return false;
        }
        cells[position.getRow()][position.getCol()] = player;
        return true;
    }
    
    public boolean isValidMove(Position position) {
        if (!position.isValidFor(size)) {
            return false;
        }
        return cells[position.getRow()][position.getCol()].isEmpty();
    }
    
    public CellState getCellState(Position position) {
        if (!position.isValidFor(size)) {
            throw new IllegalArgumentException("Position out of bounds: " + position);
        }
        return cells[position.getRow()][position.getCol()];
    }
    
    public int getSize() {
        return size;
    }
    
    public boolean isFull() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (cells[row][col].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void reset() {
        initializeBoard();
    }
    
    public CellState[][] getBoardStateCopy() {
        CellState[][] copy = new CellState[size][size];
        for (int i = 0; i < size; i++) {
            copy[i] = Arrays.copyOf(cells[i], size);
        }
        return copy;
    }
}
