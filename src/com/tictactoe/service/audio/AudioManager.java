package com.tictactoe.service.audio;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private static AudioManager instance;
    
    private Clip backgroundMusic;
    private final Map<String, Clip> soundEffects;
    
    private boolean musicEnabled;
    private boolean soundEffectsEnabled;
    private float musicVolume; // 0.0 to 1.0
    private float effectsVolume; // 0.0 to 1.0
    
    private AudioManager() {
        this.soundEffects = new HashMap<>();
        this.musicEnabled = true;
        this.soundEffectsEnabled = true;
        this.musicVolume = 0.7f;
        this.effectsVolume = 0.8f;
    }
    
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }
    
    public void playBackgroundMusic(String resourcePath) {
        if (!musicEnabled) return;
        
        try {
            stopBackgroundMusic();
            
            var audioInputStream = AudioSystem.getAudioInputStream(
                new BufferedInputStream(getClass().getResourceAsStream(resourcePath))
            );
            
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
            setClipVolume(backgroundMusic, musicVolume);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
            
        } catch (Exception e) {
            System.err.println("Failed to load background music: " + e.getMessage());
        }
    }
    
    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }
    
    public void playSound(String soundName, String resourcePath) {
        if (!soundEffectsEnabled) return;
        
        try {
            if (!soundEffects.containsKey(soundName)) {
                var audioInputStream = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(getClass().getResourceAsStream(resourcePath))
                );
                
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                soundEffects.put(soundName, clip);
            }
            
            Clip clip = soundEffects.get(soundName);
            clip.setFramePosition(0); // Rewind to beginning
            setClipVolume(clip, effectsVolume);
            clip.start();
            
        } catch (Exception e) {
            System.err.println("Failed to play sound " + soundName + ": " + e.getMessage());
        }
    }
    
    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        if (backgroundMusic != null) {
            setClipVolume(backgroundMusic, musicVolume);
        }
    }
    
    public void setEffectsVolume(float volume) {
        this.effectsVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }
    
    private void setClipVolume(Clip clip, float volume) {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(Math.max(gainControl.getMinimum(), dB));
        }
    }
    
    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled) {
            stopBackgroundMusic();
        }
    }
    
    public void setSoundEffectsEnabled(boolean enabled) {
        this.soundEffectsEnabled = enabled;
    }
    
    public boolean isMusicEnabled() {
        return musicEnabled;
    }
    
    public boolean isSoundEffectsEnabled() {
        return soundEffectsEnabled;
    }
    
    public float getMusicVolume() {
        return musicVolume;
    }
    
    public float getEffectsVolume() {
        return effectsVolume;
    }
    
    public void dispose() {
        stopBackgroundMusic();
        for (Clip clip : soundEffects.values()) {
            if (clip != null) {
                clip.close();
            }
        }
        soundEffects.clear();
    }
}
