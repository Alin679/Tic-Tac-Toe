package com.tictactoe.service.i18n;

import com.tictactoe.domain.enums.Language;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LanguageManager {
    private static LanguageManager instance;
    
    private Language currentLanguage;
    private ResourceBundle messages;
    private final List<LanguageChangeListener> listeners;
    
    private LanguageManager() {
        this.currentLanguage = Language.ENGLISH;
        this.messages = ResourceBundle.getBundle("messages", 
            new java.util.Locale(currentLanguage.getCode()));
        this.listeners = new ArrayList<>();
    }
    
    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }
    
    public void setLanguage(Language language) {
        this.currentLanguage = language;
        this.messages = ResourceBundle.getBundle("messages", 
            new java.util.Locale(language.getCode()));
        notifyListeners();
    }
    
    public Language getCurrentLanguage() {
        return currentLanguage;
    }
    
    public String get(String key) {
        try {
            return messages.getString(key);
        } catch (Exception e) {
            return key; // Return key if translation not found
        }
    }
    
    public void addListener(LanguageChangeListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(LanguageChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners() {
        for (LanguageChangeListener listener : listeners) {
            listener.onLanguageChanged(currentLanguage);
        }
    }
    
    @FunctionalInterface
    public interface LanguageChangeListener {
        void onLanguageChanged(Language newLanguage);
    }
}
