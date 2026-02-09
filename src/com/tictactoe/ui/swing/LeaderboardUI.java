package com.tictactoe.ui.swing;

import com.tictactoe.domain.model.PlayerScore;
import com.tictactoe.service.i18n.LanguageManager;
import com.tictactoe.service.leaderboard.LeaderboardService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LeaderboardUI extends JFrame {
    private final LanguageManager langManager;
    private final LeaderboardService leaderboardService;
    private final JFrame parentFrame;
    
    private JLabel titleLabel;
    private JButton backButton;
    private DefaultTableModel tableModel;
    
    public LeaderboardUI(JFrame parent, LeaderboardService leaderboardService) {
        this.parentFrame = parent;
        this.leaderboardService = leaderboardService;
        this.langManager = LanguageManager.getInstance();
        
        initializeUI();
        langManager.addListener(lang -> updateTexts());
    }
    
    private void initializeUI() {
        setTitle(langManager.get("leaderboard.title"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true);
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                    0, 0, ColorScheme.PRIMARY_LIGHT,
                    0, getHeight(), ColorScheme.PRIMARY
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        titleLabel = new JLabel("🏆 " + langManager.get("leaderboard.title"));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(ColorScheme.SURFACE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        String[] columnNames = {
            langManager.get("leaderboard.rank"),
            langManager.get("leaderboard.player"),
            langManager.get("leaderboard.wins"),
            langManager.get("leaderboard.losses"),
            langManager.get("leaderboard.draws"),
            langManager.get("leaderboard.winRate")
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.setForeground(Color.BLACK);
        table.setBackground(ColorScheme.SURFACE);
        table.setSelectionBackground(ColorScheme.PRIMARY_LIGHT);
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(ColorScheme.BORDER);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(ColorScheme.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        loadLeaderboardData();
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(ColorScheme.SURFACE);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(tableContainer, BorderLayout.CENTER);
        
        final boolean[] backHovered = {false};
        backButton = new JButton("← " + langManager.get("settings.back")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(backHovered[0] ? new Color(255, 255, 255, 50) : new Color(255, 255, 255, 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(150, 40));
        backButton.addActionListener(e -> goBack());
        
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backHovered[0] = true;
                backButton.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backHovered[0] = false;
                backButton.repaint();
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadLeaderboardData() {
        tableModel.setRowCount(0);
        List<PlayerScore> scores = leaderboardService.getLeaderboard();
        
        int rank = 1;
        for (PlayerScore score : scores) {
            Object[] row = {
                rank++,
                score.getPlayerName(),
                score.getWins(),
                score.getLosses(),
                score.getDraws(),
                String.format("%.1f%%", score.getWinRate())
            };
            tableModel.addRow(row);
        }
    }
    
    private void updateTexts() {
        setTitle(langManager.get("leaderboard.title"));
        titleLabel.setText("🏆 " + langManager.get("leaderboard.title"));
        backButton.setText("← " + langManager.get("settings.back"));
        
        tableModel.setColumnIdentifiers(new String[]{
            langManager.get("leaderboard.rank"),
            langManager.get("leaderboard.player"),
            langManager.get("leaderboard.wins"),
            langManager.get("leaderboard.losses"),
            langManager.get("leaderboard.draws"),
            langManager.get("leaderboard.winRate")
        });
    }
    
    private void goBack() {
        parentFrame.setVisible(true);
        dispose();
    }
}
