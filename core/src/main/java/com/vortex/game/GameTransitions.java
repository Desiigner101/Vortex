package com.vortex.game;
import com.badlogic.gdx.Game;
import com.vortex.game.BattleClasses.BattleClass;

public class GameTransitions extends Game {
    // A flag to track if the intro video has been played (resets when the app restarts)
    private static boolean introPlayed = false;

    @Override
    public void create() {
        //Check if the intro has already played
       if (!introPlayed) {
          introPlayed = true; // Mark the intro as played for this session
            this.setScreen(new VideoIntro(this)); // Show the intro video
      } else {
            this.setScreen(new GameMenu(this)); // Skip the intro and go straight to the menu
      }
    }

    // }
    // Method to switch to the game menu
    public void startGameMenu() {
        this.setScreen(new GameMenu(this));
    }

    // Method to start a new game session
    public void newGame() {
        this.setScreen(new StoryScene(this, new String[]{
            "Nova", "Hmm... doesn't seem to work well..?", "Lab", "#FFFFFF",
            "Nova", "Maybe I should try another approach...", "Lab", "#FFFFFF",
            "AI", "Analyzing... please wait.", "Lab", "#00FF00",
            "Nova", "Alright, let’s see what’s next.", "Umbra_CharViewBackground", "#FFFFFF",
            "Umbra", "What the frick", "Jina_CharViewBackground", "#FFFFFF"
        }, () -> this.setScreen(new BattleClass(
            "Nyxarion",
            true, true, true,
            "ruins_background.png",
            "RoadTile.png",
            "Boss-BattleMusic.wav")) // Pass music file
        ));
    }


    // Method to open the character selection screen
    public void displayCharacters() {
        this.setScreen(new DisplayCharacters(this));
    }

    public void showControls() {
        setScreen(new GameControls(this));
    }

}
