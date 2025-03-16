package com.vortex.game;

import com.badlogic.gdx.Game;

public class GameTransitions extends Game {
    private static boolean introPlayed = false; // Temporary flag (resets when app restarts)

    @Override
    public void create() {
        if (!introPlayed) {
            introPlayed = true; // Mark intro as played for this session
            this.setScreen(new VideoIntro(this));
        } else {
            this.setScreen(new GameMenu(this));
        }
    }

    public void startGameMenu() {
        this.setScreen(new GameMenu(this));
    }

    public void newGame() {
        this.setScreen(new VortexMain(this));
    }

    public void displayCharacters() {
        this.setScreen(new DisplayCharacters(this));
    }
}
