package com.vortex.game;

import com.badlogic.gdx.Game;

public class GameTransitions extends Game {

    @Override
    public void create() {
        this.setScreen(new GameMenu(this)); // Show the menu by default
    }

    public void newGame() {
        this.setScreen(new VortexMain()); // Switch to VortexMain
    }
}
