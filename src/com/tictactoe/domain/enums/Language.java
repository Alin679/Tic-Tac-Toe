package com.tictactoe.domain.enums;

public enum Language {
    ENGLISH("en", "English"),
    ROMANIAN("ro", "Română");
    
    private final String code;
    private final String displayName;
    
    Language(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static Language fromCode(String code) {
        for (Language lang : values()) {
            if (lang.code.equals(code)) {
                return lang;
            }
        }
        return ENGLISH; // Default
    }
}
