package com.tictactoe.service.ai;

import com.tictactoe.domain.model.Board;
import com.tictactoe.domain.model.Move;
import com.tictactoe.domain.model.Position;
import com.tictactoe.domain.enums.CellState;

public interface AIStrategy {
    Move calculateMove(Board board, CellState aiPlayer);
}
