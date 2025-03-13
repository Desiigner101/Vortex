package com.vortex.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameMenu implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private String[] menuOptions = {"New Game", "Load Game", "View Characters", "Settings", "Exit"};
    private Vector2[] optionPositions;
    private int selectedIndex = -1; // No selection initially
    private final GameTransitions game;
    private Texture background; // Background image
    private float screenWidth, screenHeight;
    private float textScale = 2f;
    private float hoverScale = 2.5f;
    private float fadeInAlpha = 0f;

    public GameMenu(GameTransitions game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = generateFont("fonts/Poppins-Thin.ttf", 15); // Generate Poppins font at 48px size

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        background = new Texture(Gdx.files.internal("Backgrounds/background.jpg")); // Sci-fi themed background

        optionPositions = new Vector2[menuOptions.length];

        // Center the menu on the screen
        float startY = screenHeight * 0.6f;
        for (int i = 0; i < menuOptions.length; i++) {
            float textWidth = getTextWidth(menuOptions[i], textScale);
            optionPositions[i] = new Vector2((screenWidth - textWidth) / 2, startY - (i * 80));
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        fadeInAlpha = Math.min(fadeInAlpha + delta * 0.5f, 1); // Smooth fade-in effect

        batch.begin();
        batch.setColor(1, 1, 1, fadeInAlpha);
        batch.draw(background, 0, 0, screenWidth, screenHeight); // Render background

        updateMouseSelection(delta);

        for (int i = 0; i < menuOptions.length; i++) {
            float scale = (i == selectedIndex) ?
                lerp(font.getData().scaleX, hoverScale, delta * 10) :
                lerp(font.getData().scaleX, textScale, delta * 10);
            font.getData().setScale(scale);

            font.setColor(i == selectedIndex ? Color.CYAN : Color.WHITE);
            float textWidth = getTextWidth(menuOptions[i], scale);
            float adjustedX = (screenWidth - textWidth) / 2;

            font.draw(batch, menuOptions[i], adjustedX, optionPositions[i].y);
        }

        batch.end();
        handleInput();
    }

    private float getTextWidth(String text, float scale) {
        font.getData().setScale(scale);
        GlyphLayout layout = new GlyphLayout(font, text);
        return layout.width;
    }

    private void updateMouseSelection(float delta) {
        float mouseX = Gdx.input.getX();
        float mouseY = screenHeight - Gdx.input.getY(); // Convert to world coordinates

        selectedIndex = -1; // Reset selection
        for (int i = 0; i < menuOptions.length; i++) {
            float textWidth = getTextWidth(menuOptions[i], textScale);
            float textHeight = font.getLineHeight() * textScale;
            float optionX = (screenWidth - textWidth) / 2;
            float optionY = optionPositions[i].y;

            if (mouseX >= optionX && mouseX <= optionX + textWidth &&
                mouseY >= optionY - textHeight && mouseY <= optionY) {
                selectedIndex = i;
                break;
            }
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex + 1) % menuOptions.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = (selectedIndex - 1 + menuOptions.length) % menuOptions.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && selectedIndex != -1) {
            executeAction(selectedIndex);
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && selectedIndex != -1) {
            executeAction(selectedIndex);
        }
    }

    private void executeAction(int index) {
        switch (index) {
            case 0: game.newGame(); break;
            // case 1: game.loadGame(); break; (Implement if needed)
            case 2: game.displayCharacters(); break;
            // case 3: game.openSettings(); break; (Implement if needed)
            case 4: Gdx.app.exit(); break;
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
        background.dispose();
    }

    /**
     * Linear interpolation function to smoothly transition between values.
     */
    private float lerp(float start, float end, float alpha) {
        return start + alpha * (end - start);
    }

    /**
     * Generates a BitmapFont from a TTF file.
     */
    private BitmapFont generateFont(String fontPath, int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2f;
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = 2;
        parameter.shadowColor = new Color(0, 0, 0, 0.75f); // Semi-transparent black shadow

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }
}
