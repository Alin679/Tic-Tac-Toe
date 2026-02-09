package com.tictactoe.ui.swing;

import com.tictactoe.domain.enums.Language;
import com.tictactoe.service.audio.AudioManager;
import com.tictactoe.service.i18n.LanguageManager;
import javax.swing.*;
import java.awt.*;

public class SettingsUI extends JFrame {
    private final LanguageManager langManager;
    private final AudioManager audioManager;
    private final JFrame parentFrame;
    
    private JLabel titleLabel;
    private JLabel languageLabel;
    private JLabel musicLabel;
    private JLabel effectsLabel;
    private JButton backButton;
    
    public SettingsUI(JFrame parent) {
        this.parentFrame = parent;
        this.langManager = LanguageManager.getInstance();
        this.audioManager = AudioManager.getInstance();
        
        initializeUI();
        langManager.addListener(lang -> updateTexts());
    }
    
    private void initializeUI() {
        setTitle(langManager.get("settings.title"));
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
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        titleLabel = new JLabel("⚙️ " + langManager.get("settings.title"));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(40));
        
        JPanel settingsCard = new JPanel();
        settingsCard.setLayout(new BoxLayout(settingsCard, BoxLayout.Y_AXIS));
        settingsCard.setBackground(ColorScheme.SURFACE);
        settingsCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        settingsCard.setMaximumSize(new Dimension(450, 400));
        settingsCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel langPanel = createSettingPanel();
        languageLabel = new JLabel("🌐 " + langManager.get("settings.language") + ":");
        languageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        languageLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        
        JComboBox<Language> langCombo = new JComboBox<>(Language.values());
        langCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        langCombo.setMaximumSize(new Dimension(200, 35));
        langCombo.setSelectedItem(langManager.getCurrentLanguage());
        langCombo.addActionListener(e -> {
            Language selected = (Language) langCombo.getSelectedItem();
            langManager.setLanguage(selected);
        });
        
        langPanel.add(languageLabel);
        langPanel.add(Box.createHorizontalGlue());
        langPanel.add(langCombo);
        settingsCard.add(langPanel);
        settingsCard.add(Box.createVerticalStrut(20));
        
        JPanel musicPanel = createSettingPanel();
        musicLabel = new JLabel("🎵 " + langManager.get("settings.music") + ":");
        musicLabel.setFont(new Font("Arial", Font.BOLD, 16));
        musicLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        
        JCheckBox musicCheckbox = new JCheckBox();
        musicCheckbox.setSelected(audioManager.isMusicEnabled());
        musicCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        musicCheckbox.addActionListener(e -> {
            audioManager.setMusicEnabled(musicCheckbox.isSelected());
        });
        
        JSlider musicSlider = new JSlider(0, 100, (int)(audioManager.getMusicVolume() * 100));
        musicSlider.setPreferredSize(new Dimension(150, 30));
        musicSlider.addChangeListener(e -> {
            audioManager.setMusicVolume(musicSlider.getValue() / 100.0f);
        });
        
        musicPanel.add(musicLabel);
        musicPanel.add(Box.createHorizontalGlue());
        musicPanel.add(musicCheckbox);
        musicPanel.add(Box.createHorizontalStrut(10));
        musicPanel.add(musicSlider);
        settingsCard.add(musicPanel);
        settingsCard.add(Box.createVerticalStrut(20));
        
        JPanel effectsPanel = createSettingPanel();
        effectsLabel = new JLabel("🔊 " + langManager.get("settings.soundEffects") + ":");
        effectsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        effectsLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        
        JCheckBox effectsCheckbox = new JCheckBox();
        effectsCheckbox.setSelected(audioManager.isSoundEffectsEnabled());
        effectsCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        effectsCheckbox.addActionListener(e -> {
            audioManager.setSoundEffectsEnabled(effectsCheckbox.isSelected());
        });
        
        JSlider effectsSlider = new JSlider(0, 100, (int)(audioManager.getEffectsVolume() * 100));
        effectsSlider.setPreferredSize(new Dimension(150, 30));
        effectsSlider.addChangeListener(e -> {
            audioManager.setEffectsVolume(effectsSlider.getValue() / 100.0f);
        });
        
        effectsPanel.add(effectsLabel);
        effectsPanel.add(Box.createHorizontalGlue());
        effectsPanel.add(effectsCheckbox);
        effectsPanel.add(Box.createHorizontalStrut(10));
        effectsPanel.add(effectsSlider);
        settingsCard.add(effectsPanel);
        
        mainPanel.add(settingsCard);
        mainPanel.add(Box.createVerticalStrut(30));
        
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
        backButton.setMaximumSize(new Dimension(150, 40));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        
        mainPanel.add(backButton);
        
        add(mainPanel);
    }
    
    private JPanel createSettingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(ColorScheme.SURFACE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(400, 40));
        return panel;
    }
    
    private void updateTexts() {
        setTitle(langManager.get("settings.title"));
        titleLabel.setText("⚙️ " + langManager.get("settings.title"));
        languageLabel.setText("🌐 " + langManager.get("settings.language") + ":");
        musicLabel.setText("🎵 " + langManager.get("settings.music") + ":");
        effectsLabel.setText("🔊 " + langManager.get("settings.soundEffects") + ":");
        backButton.setText("← " + langManager.get("settings.back"));
    }
    
    private void goBack() {
        parentFrame.setVisible(true);
        dispose();
    }
}
