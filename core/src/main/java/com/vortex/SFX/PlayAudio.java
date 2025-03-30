package com.vortex.SFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Timer;
import java.util.HashMap;
import java.util.Map;

public class PlayAudio implements SoundEffects {
    private Music currentMusic;
    private final Map<String, Sound> soundCache = new HashMap<>();
    private float musicVolume = 1.0f;
    private float soundVolume = 1.0f;
    private long currentSoundId = -1;

    @Override
    public void playSoundEffect(String soundFile, double delay) {
        if (soundVolume <= 0.001f) return; // Don't play if muted

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                try {
                    FileHandle file = Gdx.files.internal("SoundEffectsFolder/" + soundFile);
                    if (!file.exists()) {
                        Gdx.app.error("PlayAudio", "Sound effect not found: " + soundFile);
                        return;
                    }

                    Sound sound = soundCache.computeIfAbsent(soundFile,
                            key -> Gdx.audio.newSound(file));

                    // Stop previous sound if still playing
                    if (currentSoundId != -1) {
                        sound.stop(currentSoundId);
                    }

                    // Play with current volume
                    currentSoundId = sound.play(soundVolume);
                    sound.setVolume(currentSoundId, soundVolume);

                    Gdx.app.debug("PlayAudio", "Playing: " + soundFile +
                            " at volume: " + soundVolume);
                } catch (Exception e) {
                    Gdx.app.error("PlayAudio", "Error playing sound: " + e.getMessage());
                }
            }
        }, (float) delay);
    }

    @Override
    public void playMusic(String musicFile) {
        stopMusic(); // Stop current music first

        try {
            FileHandle file = Gdx.files.internal("MusicFolder/" + musicFile);
            if (!file.exists()) {
                Gdx.app.error("PlayAudio", "Music file not found: " + musicFile);
                return;
            }

            currentMusic = Gdx.audio.newMusic(file);
            currentMusic.setLooping(true);

            // Apply current volume settings
            if (musicVolume <= 0.001f) {
                currentMusic.setVolume(0);
                currentMusic.pause(); // Pause instead of stop to maintain state
            } else {
                currentMusic.setVolume(musicVolume);
                currentMusic.play();
            }

            Gdx.app.debug("PlayAudio", "Playing music: " + musicFile +
                    " at volume: " + musicVolume);
        } catch (Exception e) {
            Gdx.app.error("PlayAudio", "Error loading music: " + e.getMessage());
        }
    }

    @Override
    public void stopAudio() {
        stopMusic();
        stopSoundEffect();
    }

    @Override
    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
            Gdx.app.debug("PlayAudio", "Music stopped");
        }
    }

    @Override
    public void stopSoundEffect() {
        soundCache.values().forEach(Sound::stop);
        currentSoundId = -1;
        Gdx.app.debug("PlayAudio", "All sound effects stopped");
    }

    public void setSoundVolume(float volume) {
        float newVolume = Math.max(0, Math.min(1, volume)); // Clamp 0-1
        if (Math.abs(newVolume - soundVolume) > 0.01f) { // Only if significant change
            soundVolume = newVolume;

            // Update all playing sounds
            soundCache.values().forEach(sound -> {
                if (currentSoundId != -1) {
                    sound.setVolume(currentSoundId, soundVolume);
                }
            });

            Gdx.app.debug("PlayAudio", "Sound volume set to: " + soundVolume);
        }
    }

    public void setMusicVolume(float volume) {
        float newVolume = Math.max(0, Math.min(1, volume)); // Clamp 0-1
        if (Math.abs(newVolume - musicVolume) > 0.01f) { // Only if significant change
            musicVolume = newVolume;

            if (currentMusic != null) {
                if (musicVolume <= 0.001f) {
                    currentMusic.setVolume(0);
                    currentMusic.pause();
                } else {
                    if (!currentMusic.isPlaying()) {
                        currentMusic.play();
                    }
                    currentMusic.setVolume(musicVolume);
                }
            }

            Gdx.app.debug("PlayAudio", "Music volume set to: " + musicVolume);
        }
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public boolean isMusicPlaying() {
        return currentMusic != null && currentMusic.isPlaying();
    }

    public void dispose() {
        stopAudio();
        soundCache.values().forEach(Sound::dispose);
        soundCache.clear();
        Gdx.app.debug("PlayAudio", "All resources disposed");
    }
}
