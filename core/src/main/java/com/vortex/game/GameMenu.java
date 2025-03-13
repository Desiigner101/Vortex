package com.vortex.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameMenu implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private String[] menuOptions = {"New Game", "Load Game", "View Characters", "Settings", "Exit"};
    private Vector2[] optionPositions;
    private int selectedIndex = -1; // No selection initially
    private final GameTransitions game;

    public GameMenu(GameTransitions game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2);
        optionPositions = new Vector2[menuOptions.length];

        // Positioning menu options
        for (int i = 0; i < menuOptions.length; i++) {
            optionPositions[i] = new Vector2(400, 500 - (i * 50));
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

        // Check for mouse hover and update selectedIndex
        updateMouseSelection();

        batch.begin();
        for (int i = 0; i < menuOptions.length; i++) {
            font.setColor(i == selectedIndex ? Color.YELLOW : Color.WHITE);
            font.draw(batch, menuOptions[i], optionPositions[i].x, optionPositions[i].y);
        }
        batch.end();

        handleInput();
    }

    private void updateMouseSelection() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Convert to world coordinates

        selectedIndex = -1; // Reset selection
        for (int i = 0; i < menuOptions.length; i++) {
            float textWidth = font.getScaleX() * menuOptions[i].length() * 10; // Approximate width
            float textHeight = font.getScaleY() * 20; // Approximate height

            float optionX = optionPositions[i].x;
            float optionY = optionPositions[i].y;

            if (mouseX >= optionX && mouseX <= optionX + textWidth &&
                mouseY >= optionY - textHeight && mouseY <= optionY) {
                selectedIndex = i;
                break;
            }
        }
    }

    private void handleInput() {
        // Keyboard navigation
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex + 1) % menuOptions.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = (selectedIndex - 1 + menuOptions.length) % menuOptions.length;
        }

        // Enter key selection
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && selectedIndex != -1) {
            executeAction(selectedIndex);
        }

        // Mouse click selection
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && selectedIndex != -1) {
            executeAction(selectedIndex);
        }
    }

    private void executeAction(int index) {
        switch (index) {
            case 0: game.newGame(); break;
            //case 1: game.loadGame(); break;
            case 2: game.displayCharacters(); break;
            //case 3: game.openSettings(); break;
            case 4: Gdx.app.exit(); break;
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { batch.dispose(); font.dispose(); }
}
