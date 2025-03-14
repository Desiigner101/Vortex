package com.vortex.game;

import com.badlogic.gdx.Game;

public class GameTransitions extends Game {
    @Override
    public void create() {
        // Start with the VideoIntro screen
        this.setScreen(new VideoIntro(this));
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
