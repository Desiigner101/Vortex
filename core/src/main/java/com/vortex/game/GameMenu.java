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
    private int highlightedIndex = -1;
    private GameTransitions game;

    // Constructor to receive Game instance
    public GameMenu(GameTransitions game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2);

        optionPositions = new Vector2[menuOptions.length];
        for (int i = 0; i < menuOptions.length; i++) {
            optionPositions[i] = new Vector2(400, 500 - (i * 50));
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

        batch.begin();
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        highlightedIndex = -1;
        for (int i = 0; i < menuOptions.length; i++) {
            float textWidth = font.getSpaceXadvance() * menuOptions[i].length();
            float textHeight = font.getLineHeight();

            if (mouseX >= optionPositions[i].x && mouseX <= optionPositions[i].x + textWidth &&
                mouseY >= optionPositions[i].y - textHeight && mouseY <= optionPositions[i].y) {
                highlightedIndex = i;
            }

            font.setColor(i == highlightedIndex ? Color.YELLOW : Color.WHITE);
            font.draw(batch, menuOptions[i], optionPositions[i].x, optionPositions[i].y);
        }

        batch.end();

        // Handle menu option click
        if (highlightedIndex != -1 && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (highlightedIndex == 0) {
                game.newGame();  // Call the newGame() method to switch screens
            }
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
