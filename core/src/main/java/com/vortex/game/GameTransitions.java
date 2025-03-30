package com.vortex.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.Preferences;
import com.vortex.SFX.PlayAudio;
import com.vortex.game.BattleClasses.BattleClass;
import com.vortex.game.BattleClasses.TestBossClass;

public class GameTransitions extends Game {
    private static boolean introPlayed = false;
    private int sequenceCount = 0;
    private Screen currentScreen;
    private Preferences prefs;
    private PlayAudio audioManager;

    // Volume settings
    private float musicVolume = 1.0f;
    private float soundVolume = 1.0f;

    @Override
    public void create() {
        prefs = com.badlogic.gdx.Gdx.app.getPreferences("game_settings");
        audioManager = new PlayAudio();
        loadSettings();

        if (!introPlayed) {
            introPlayed = true;
            this.setScreen(new VideoIntro(this));
        } else {
            this.setScreen(new GameMenu(this));
        }
    }

    public void loadSettings() {
        musicVolume = prefs.getFloat("musicVolume", 1.0f);
        soundVolume = prefs.getFloat("soundVolume", 1.0f);
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = volume;
        prefs.putFloat("musicVolume", volume);
        prefs.flush();
        audioManager.setMusicVolume(volume); // Propagate to audio system
    }

    public void setSoundVolume(float volume) {
        this.soundVolume = volume;
        prefs.putFloat("soundVolume", volume);
        prefs.flush();
        audioManager.setSoundVolume(volume); // Propagate to audio system
    }
    public PlayAudio getAudioManager() {
        return audioManager;
    }

    public void startGameMenu() {
        this.setScreen(new GameMenu(this));
    }

    public void newGame() {
        sequenceCount = 0;
        startNextSequence();
    }

    private void startNextSequence() {
        sequenceCount++;

        if(sequenceCount == 1) {
            currentScreen = new StoryScene(this, new String[]{
                "Nova", "First dialogue line", "Lab", "#FFFFFF",
                "AI", "Finally you istoopid!", "ResultScreenBG", "#008080"
            }, () -> {
                Runnable createBattle1 = () -> {
                    currentScreen = new BattleClass(
                        this, "NYXARION",
                        new TestBossClass(),
                        true, true, true,
                        "ResultScreenBG.png", "StoneTile.png", "Boss-BattleMusic.wav",
                        () -> startNextSequence()
                    );
                    setScreen(currentScreen);
                };
                createBattle1.run();
            });
            setScreen(currentScreen);
        } else if(sequenceCount == 2) {
            currentScreen = new StoryScene(this, new String[]{
                "Nova", "Second dialogue line", "Lab", "#FFFFFF"
            }, () -> {
                Runnable createBattle1 = () -> {
                    currentScreen = new BattleClass(
                        this, "Battle 2",
                        new TestBossClass(),
                        true, true, true,
                        "ResultScreenBG.png", "RoadTile.png", "Boss-BattleMusic.wav",
                        () -> startNextSequence()
                    );
                    setScreen(currentScreen);
                };
                createBattle1.run();
            });
            setScreen(currentScreen);
        }
    }

    public void displayCharacters() {
        this.setScreen(new DisplayCharacters(this));
    }

    public void showControls() {
        setScreen(new GameControls(this));
    }
}
