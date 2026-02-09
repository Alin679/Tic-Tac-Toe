package com.tictactoe.ui.swing;

import com.tictactoe.controller.GameController;
import com.tictactoe.domain.enums.CellState;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class BoardPanel extends JPanel {
    private static final int CELL_SIZE = 120;
    private static final int LINE_WIDTH = 4;
    private static final Color GRID_COLOR = new Color(226, 232, 240);
    private static final Color BACKGROUND_COLOR = new Color(248, 250, 252);
    private static final Color HOVER_COLOR = new Color(241, 245, 249);
    
    private CellState[][] boardState;
    private final int boardSize;
    private GameController controller;
    private int hoverRow = -1;
    private int hoverCol = -1;
    
    public BoardPanel(int boardSize) {
        this.boardSize = boardSize;
        this.boardState = new CellState[boardSize][boardSize];
        
        int panelSize = boardSize * CELL_SIZE;
        setPreferredSize(new Dimension(panelSize, panelSize));
        setBackground(BACKGROUND_COLOR);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int col = e.getX() / CELL_SIZE;
                int row = e.getY() / CELL_SIZE;
                if (row != hoverRow || col != hoverCol) {
                    hoverRow = row;
                    hoverCol = col;
                    repaint();
                }
            }
        });
    }
    
    public void setController(GameController controller) {
        this.controller = controller;
    }
    
    public void updateBoard(CellState[][] boardState) {
        this.boardState = boardState;
        repaint();
    }
    
    private void handleClick(int x, int y) {
        if (controller == null) {
            return;
        }
        
        int col = x / CELL_SIZE;
        int row = y / CELL_SIZE;
        
        if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
            controller.onCellClicked(row, col);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        
        drawHoverEffect(g2d);
        drawGrid(g2d);
        drawSymbols(g2d);
    }
    
    private void drawHoverEffect(Graphics2D g2d) {
        if (hoverRow >= 0 && hoverRow < boardSize && hoverCol >= 0 && hoverCol < boardSize) {
            if (boardState[hoverRow][hoverCol] == CellState.EMPTY) {
                g2d.setColor(HOVER_COLOR);
                int x = hoverCol * CELL_SIZE;
                int y = hoverRow * CELL_SIZE;
                g2d.fillRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
            }
        }
    }
    
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(GRID_COLOR);
        g2d.setStroke(new BasicStroke(LINE_WIDTH));
        
        for (int i = 1; i < boardSize; i++) {
            int x = i * CELL_SIZE;
            g2d.drawLine(x, 0, x, boardSize * CELL_SIZE);
        }
        
        for (int i = 1; i < boardSize; i++) {
            int y = i * CELL_SIZE;
            g2d.drawLine(0, y, boardSize * CELL_SIZE, y);
        }
    }
    
    private void drawSymbols(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                CellState cell = boardState[row][col];
                if (cell != CellState.EMPTY) {
                    drawSymbol(g2d, row, col, cell);
                }
            }
        }
    }
    
    private void drawSymbol(Graphics2D g2d, int row, int col, CellState symbol) {
        int x = col * CELL_SIZE;
        int y = row * CELL_SIZE;
        int padding = CELL_SIZE / 4;
        
        if (symbol == CellState.X) {
            g2d.setColor(ColorScheme.X_COLOR);
            g2d.drawLine(x + padding, y + padding, 
                        x + CELL_SIZE - padding, y + CELL_SIZE - padding);
            g2d.drawLine(x + CELL_SIZE - padding, y + padding, 
                        x + padding, y + CELL_SIZE - padding);
        } else if (symbol == CellState.O) {
            g2d.setColor(ColorScheme.O_COLOR);
            g2d.drawOval(x + padding, y + padding, 
                        CELL_SIZE - 2 * padding, CELL_SIZE - 2 * padding);
        }
    }
}
