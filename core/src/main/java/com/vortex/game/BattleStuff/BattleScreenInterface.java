package com.vortex.game.BattleStuff;

import com.badlogic.gdx.Screen;

public interface BattleScreenInterface extends Screen {



    // Method to update the buttons based on the current character's abilities
    void updateButtons();

    // Method to add a skill point
    void addSkillPoint();

    // Method to use a skill
    void useSkill();

    // Method to use an ultimate ability
    void useUltimate();

    // Method to proceed to the next turn
    void nextTurn();

    // Method to render the battle screen
    void renderBattle(float delta);

    // Method to initialize game controls
    void initGameControls();

    // Method to render game controls
    void renderGameControls(float delta);

    // Method to handle input for game controls
    void handleControlsInput();

    // Method to update animations for game controls
    void updateControlsAnimations(float delta);

    // Method to load settings from preferences
    void loadSettings();

    // Method to save settings to preferences
    void saveSettings();

    // Method to resize the game controls
    void resizeControls();

    // Method to dispose of resources
    void dispose();
}
