package com.tictactoe.ui.swing;

import com.tictactoe.service.i18n.LanguageManager;
import com.tictactoe.service.leaderboard.LeaderboardService;
import javax.swing.*;
import java.awt.*;

public class GameModeSelectionUI extends JFrame {
    private final LanguageManager langManager;
    private final JFrame parentFrame;
    private final LeaderboardService leaderboardService;
    
    public GameModeSelectionUI(JFrame parent, LeaderboardService leaderboardService) {
        this.parentFrame = parent;
        this.leaderboardService = leaderboardService;
        this.langManager = LanguageManager.getInstance();
        
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle(langManager.get("app.title"));
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
                    0, 0, ColorScheme.PRIMARY,
                    0, getHeight(), ColorScheme.PRIMARY_DARK
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel titleLabel = new JLabel("Alege Modul de Joc");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        JLabel subtitleLabel = new JLabel("Choose Game Mode");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(60));
        
        JButton singleplayerBtn = createModeButton(
            "🎮  Singleplayer",
            "Joacă împotriva AI",
            ColorScheme.ACCENT
        );
        singleplayerBtn.addActionListener(e -> onSingleplayer());
        mainPanel.add(singleplayerBtn);
        mainPanel.add(Box.createVerticalStrut(25));
        
        JButton multiplayerBtn = createModeButton(
            "👥  Multiplayer",
            "Joacă cu un prieten",
            ColorScheme.SUCCESS
        );
        multiplayerBtn.addActionListener(e -> onMultiplayer());
        mainPanel.add(multiplayerBtn);
        mainPanel.add(Box.createVerticalStrut(40));
        
        JButton backBtn = createSmallButton("← Înapoi");
        backBtn.addActionListener(e -> goBack());
        mainPanel.add(backBtn);
        
        add(mainPanel);
    }
    
    private JButton createModeButton(String title, String subtitle, Color bgColor) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(350, 80));
        buttonPanel.setPreferredSize(new Dimension(350, 80));
        buttonPanel.setBackground(bgColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        buttonPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        buttonPanel.add(titleLabel);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(subtitleLabel);
        
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(ColorScheme.getShadowColor());
                g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 15, 15);
                
                Color color = getModel().isRollover() ? 
                    bgColor.brighter() : bgColor;
                g2d.setColor(color);
                g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 15, 15);
                
                g2d.dispose();
            }
        };
        
        button.setLayout(new BorderLayout());
        button.add(buttonPanel);
        button.setMaximumSize(new Dimension(350, 80));
        button.setPreferredSize(new Dimension(350, 80));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private JButton createSmallButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(255, 255, 255, 30));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(120, 35));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 255, 255, 50));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 255, 255, 30));
            }
        });
        
        return button;
    }
    
    private void onSingleplayer() {
        SingleplayerSetupUI setupUI = new SingleplayerSetupUI(parentFrame, leaderboardService);
        setupUI.setVisible(true);
        dispose();
    }
    
    private void onMultiplayer() {
        MultiplayerSetupUI setupUI = new MultiplayerSetupUI(parentFrame, leaderboardService);
        setupUI.setVisible(true);
        dispose();
    }
    
    private void goBack() {
        parentFrame.setVisible(true);
        dispose();
    }
}
