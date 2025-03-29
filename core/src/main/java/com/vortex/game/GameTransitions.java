package com.vortex.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Timer;
import com.vortex.game.BattleClasses.BattleClass;

public class GameTransitions extends Game {
    // A flag to track if the intro video has been played (resets when the app restarts)
    private static boolean introPlayed = false;
    private int sequenceCount=0;
    private Screen currentScreen;
    @Override
    public void create() {
        //Check if the intro has already played
        /*
       if (!introPlayed) {
          introPlayed = true; // Mark the intro as played for this session
            this.setScreen(new VideoIntro(this)); // Show the intro video
      } else {*/
            this.setScreen(new GameMenu(this)); // Skip the intro and go straight to the menu
      //}
    }

    // }
    // Method to switch to the game menu
    public void startGameMenu() {
        this.setScreen(new GameMenu(this));
    }

    // Method to start a new game session



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
                // Store the battle creation logic in a variable so we can reuse it for retries
                Runnable createBattle1 = () -> {
                    currentScreen = new BattleClass(
                        this, "Battle 1",
                        true, true, true,
                        "ruins_background.png", "RoadTile.png", "Boss-BattleMusic.wav",
                        () -> startNextSequence()
                    );
                    setScreen(currentScreen);
                };

                createBattle1.run(); // Create and start the first battle
            });
            setScreen(currentScreen);
        }else if(sequenceCount == 2) {
            currentScreen = new StoryScene(this, new String[]{
                "Nova", "Second dialogue line", "Lab", "#FFFFFF"
            }, () -> {
                Runnable createBattle1 = () -> {
                    currentScreen = new BattleClass(
                        this, "Battle 2",
                        true, true, true,
                        "ruins_background.png", "RoadTile.png", "Boss-BattleMusic.wav",
                        () -> startNextSequence()
                    );
                    setScreen(currentScreen);
                };

                createBattle1.run();
            });
            setScreen(currentScreen);
        }
        // ... rest of your sequences
    }

    // Method to open the character selection screen
    public void displayCharacters() {
        this.setScreen(new DisplayCharacters(this));
    }

    public void showControls() {
        setScreen(new GameControls(this));
    }

}
