package com.vortex.SFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Timer;
import java.util.HashMap;
import java.util.Map;

public class PlayAudio implements SoundEffects {
    /*
     * Instructions for soundClass by Danidani
     * There's 5 methods:
     * - playMusic(String) → Enter the music name as a parameter (with the .wav). It will continuously loop until the stopMusic() method is called.
     * - playSoundEffect(String, double) → Enter the SFX name as a parameter (with the .wav). It will automatically stop once the SFX finishes.
     * - stopMusic() → Stops the music loop.
     * - stopSoundEffect() → Stops the sound effect currently playing.
     * - stopAudio() → Stops both the music loop and sound effect currently playing.
     *
     * Requirements:
     * - You need to have 2 folders inside `assets/`:
     *   1. `MusicFolder` → Should contain your background music.
     *   2. `SoundEffectsFolder` → Should contain your SFX files.
     *
     * LEZGAW MU GANA NA JUD KAPOYA
     */

    private Music musicClip;
    private Sound soundEffectClip;
    private final Map<String, Sound> soundCache = new HashMap<>(); // Stores sound effects to avoid reloading

    @Override
    public void playSoundEffect(String soundFile, double delay) {
        int delayMs = (int) (delay * 1000); // Convert seconds to milliseconds

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                FileHandle file = Gdx.files.internal("SoundEffectsFolder/" + soundFile);
                if (!file.exists()) {
                    Gdx.app.error("PlayAudio", "Sound effect file not found: " + soundFile);
                    return;
                }

                soundEffectClip = soundCache.computeIfAbsent(soundFile, key -> Gdx.audio.newSound(file));
                long soundId = soundEffectClip.play();
                Gdx.app.log("PlayAudio", "Playing sound effect: " + soundFile + " (ID: " + soundId + ")");
            }
        }, (float) delay);
    }

    @Override
    public void playMusic(String musicFile) { // amp duagay nako na human ani bruhhhh basta mu gana na wtffff
        stopMusic(); // Stop any currently playing music

        FileHandle file = Gdx.files.internal("MusicFolder/" + musicFile);
        if (!file.exists()) {
            Gdx.app.error("PlayAudio", "Music file not found: " + musicFile);
            return;
        }

        musicClip = Gdx.audio.newMusic(file);
        musicClip.setLooping(true);
        musicClip.play();

        Gdx.app.log("PlayAudio", "Music is playing: " + musicFile);
    }

    @Override
    public void stopAudio() {
        // Stops both sound effects and music
        stopMusic();
        stopSoundEffect();
    }

    @Override
    public void stopMusic() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip.dispose(); // Free memory
            musicClip = null;
            Gdx.app.log("PlayAudio", "Music stopped.");
        }
    }

    @Override
    public void stopSoundEffect() {
        if (soundEffectClip != null) {
            soundEffectClip.stop();
            Gdx.app.log("PlayAudio", "Sound effect stopped.");
        }
    }
}
