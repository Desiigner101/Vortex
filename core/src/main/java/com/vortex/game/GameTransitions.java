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
    private String NovaTextColor = "#5EABF7";
    private String WhiteText = "#FFFFFF";
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
                "", "In the dimly lit lab, surrounded by blinking screens and the soft hum of machinery, Nova stood before her latest creation—a sleek, metallic device designed to bridge the gaps between alternate realities.", "Lab", WhiteText,
                "", "Her heart raced with excitement and trepidation. This was the moment she had spent years preparing for-", "Lab", WhiteText,
                "", "And now, she was on the brink of discovering the secrets of the multiverse.", "Lab", WhiteText,
                "Nova", "Alright, Nova, this is it.", "Lab", NovaTextColor,
                "Nova", "The moment you've been waiting for.", "Lab", NovaTextColor,
                "Nova", "Just a few more adjustments, and we'll unlock the secrets of the multiverse.", "Lab", NovaTextColor,
                "", "She adjusted the settings, her fingers dancing over the controls as she spoke to herself, seeking reassurance.", "Lab", WhiteText,
                "Nova", "Come on, Nova.", "Lab", NovaTextColor,
                "Nova", "You’ve studied for this.", "Lab", NovaTextColor,
                "Nova", "Just focus on the calculations.", "Lab", NovaTextColor,
                "Nova", "You've got this!", "Lab", NovaTextColor,
                "", "But as she cranked up the energy output, a warning light flashed and alarms blared, cutting through her concentration.", "Lab", WhiteText,
                "Nova", "Wha-", "Lab", NovaTextColor,
                "Nova", "No... no!", "Lab", NovaTextColor,
                "Nova", "This can't be happening!", "Lab", NovaTextColor,
                "Nova", "Not now!", "Lab", NovaTextColor,
                "", "Frantically, she scanned the screens with her mind racing.", "Lab", WhiteText,
                "Nova", "The energy levels are spiking! I need to redirect power--quick!", "Lab", NovaTextColor,
                "", "As she made adjustments, her voice trembled with fear and determination.", "Lab", WhiteText,
                "Nova", "Stay calm, stay calm... ", "Lab", NovaTextColor,
                "Nova", "You've faced worse before. Just... a little more fine-tuning", "Lab", NovaTextColor,
                "", "Suddenly, the device let out a low hum, growing louder and more chaotic.", "Lab", WhiteText,
                "Nova", "Wha... What are you doing!?", "Lab", NovaTextColor,
                "Nova", "No, no! Don't overload!", "Lab", NovaTextColor,
                "", "With a final desperate attempt, she shouted at the machine.", "Lab", WhiteText,
                "", "... As if it could hear her..", "Lab", WhiteText,
                "Nova", "AAAAAAHHHHHH!! Can't let you fail now!", "Lab", NovaTextColor,
                "Nova", "Not after everything I've worked for!", "Lab", NovaTextColor,
                "", "But before she could react, a blinding light enveloped her, and in an instant, she was torn from her lab...", "Lab", WhiteText,
                "", "... And catapulted into the unknown.", "Lab", WhiteText,
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
