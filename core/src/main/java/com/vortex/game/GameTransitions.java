package com.vortex.game;

import com.badlogic.gdx.Game;

public class GameTransitions extends Game {
    // A flag to track if the intro video has been played (resets when the app restarts)
    private static boolean introPlayed = false;

    @Override
    public void create() {
        // Check if the intro has already played
        if (!introPlayed) {
            introPlayed = true; // Mark the intro as played for this session
            this.setScreen(new VideoIntro(this)); // Show the intro video
        } else {
            this.setScreen(new GameMenu(this)); // Skip the intro and go straight to the menu
        }
    }

    // Method to switch to the game menu
    public void startGameMenu() {
        this.setScreen(new GameMenu(this));
    }

    // Method to start a new game session
    public void newGame() {
        this.setScreen(new VortexMain(this));
    }

    // Method to open the character selection screen
    public void displayCharacters() {
        this.setScreen(new ViewCharacters(this));
    }
    public void showControls() {
        setScreen(new GameControls(this));
    }
}


