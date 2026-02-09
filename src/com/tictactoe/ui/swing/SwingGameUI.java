package com.tictactoe.ui.swing;

import com.tictactoe.controller.GameController;
import com.tictactoe.domain.enums.CellState;
import com.tictactoe.ui.GameUI;
import javax.swing.*;
import java.awt.*;

public class SwingGameUI implements GameUI {
    private final JFrame frame;
    private final BoardPanel boardPanel;
    private final JLabel statusLabel;
    private final JButton restartButton;
    private GameController controller;
    
    public SwingGameUI(int boardSize) {
        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(0, 0));
        
        JPanel mainContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(240, 245, 255),
                    0, getHeight(), new Color(230, 240, 255)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainContainer.setLayout(new BorderLayout(0, 0));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        boardPanel = new BoardPanel(boardSize);
        JPanel boardContainer = new JPanel(new GridBagLayout());
        boardContainer.setOpaque(false);
        boardContainer.add(boardPanel);
        mainContainer.add(boardContainer, BorderLayout.CENTER);
        
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        controlPanel.setOpaque(false);
        
        JPanel statusCard = new JPanel();
        statusCard.setBackground(ColorScheme.SURFACE);
        statusCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        statusCard.setMaximumSize(new Dimension(400, 60));
        
        statusLabel = new JLabel("Welcome to Tic Tac Toe!", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        statusCard.add(statusLabel);
        
        restartButton = new JButton("🔄  New Game") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor = getModel().isRollover() ? 
                    ColorScheme.PRIMARY_LIGHT : ColorScheme.PRIMARY;
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));
        restartButton.setForeground(Color.WHITE);
        restartButton.setFocusPainted(false);
        restartButton.setBorderPainted(false);
        restartButton.setContentAreaFilled(false);
        restartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        restartButton.setMaximumSize(new Dimension(200, 45));
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.addActionListener(e -> {
            if (controller != null) {
                controller.onRestartClicked();
            }
        });
        
        controlPanel.add(statusCard);
        controlPanel.add(Box.createVerticalStrut(15));
        controlPanel.add(restartButton);
        
        mainContainer.add(controlPanel, BorderLayout.SOUTH);
        frame.add(mainContainer);
        
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
    }
    
    @Override
    public void initialize(GameController controller) {
        this.controller = controller;
        boardPanel.setController(controller);
    }
    
    @Override
    public void show() {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
    
    @Override
    public void updateBoard(CellState[][] boardState) {
        SwingUtilities.invokeLater(() -> boardPanel.updateBoard(boardState));
    }
    
    @Override
    public void updateStatus(String message) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(message));
    }
    
    @Override
    public void showGameOver(String message) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(message);
            JOptionPane.showMessageDialog(
                frame, 
                message, 
                "Game Over", 
                JOptionPane.INFORMATION_MESSAGE
            );
        });
    }
    
    public void close() {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(false);
            frame.dispose();
        });
    }
    
    public JFrame getFrame() {
        return frame;
    }
}
