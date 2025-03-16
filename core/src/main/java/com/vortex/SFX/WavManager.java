package com.vortex.SFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;

public class WavManager {
    private static HashMap<String, Sound> soundEffects = new HashMap<>();
    private static HashMap<String, Music> musicTracks = new HashMap<>();
    private static Music currentMusic;

    // Load a sound effect
    public static void loadSound(String name, String filePath) {
        if (!soundEffects.containsKey(name)) {
            soundEffects.put(name, Gdx.audio.newSound(Gdx.files.internal(filePath)));
        }
    }

    // Play a sound effect
    public static void playSound(String name) {
        Sound sound = soundEffects.get(name);
        if (sound != null) {
            sound.play();
        } else {
            Gdx.app.error("WavManager", "Sound not found: " + name);
        }
    }

    // Stop a sound effect
    public static void stopSound(String name) {
        Sound sound = soundEffects.get(name);
        if (sound != null) {
            sound.stop();
        }
    }

    // Load a music track
    public static void loadMusic(String name, String filePath) {
        if (!musicTracks.containsKey(name)) {
            musicTracks.put(name, Gdx.audio.newMusic(Gdx.files.internal(filePath)));
        }
    }

    // Play a music track
    public static void playMusic(String name, boolean loop) {
        stopMusic(); // Stop any currently playing music
        currentMusic = musicTracks.get(name);
        if (currentMusic != null) {
            currentMusic.setLooping(loop);
            currentMusic.play();
        } else {
            Gdx.app.error("WavManager", "Music not found: " + name);
        }
    }

    // Stop current music
    public static void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    // Dispose of all loaded sounds and music
    public static void dispose() {
        for (Sound sound : soundEffects.values()) {
            sound.dispose();
        }
        for (Music music : musicTracks.values()) {
            music.dispose();
        }
        soundEffects.clear();
        musicTracks.clear();
    }
}
