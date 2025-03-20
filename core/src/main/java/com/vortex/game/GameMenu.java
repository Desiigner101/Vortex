package com.vortex.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.vortex.SFX.PlayAudio;

public class GameMenu implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private String[] menuOptions = {"New Game", "Load Game", "View Characters", "Settings", "Exit"};
    private Vector2[] optionPositions;
    private int selectedIndex = -1;
    private final GameTransitions game;
    private float screenWidth, screenHeight;
    private float textScale = 2f;
    private float glowAlpha = 0.5f;
    private boolean glowIncreasing = true;
    private int lastHoveredIndex = -1; // Keeps track of the last hovered option


    private MainMenuAnimator mainMenuAnimator; // Handles the animated background
    private PlayAudio sfx; // Handles music playback

    public GameMenu(GameTransitions game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = generateFont("fonts/Poppins-ExtraBold.ttf", 32); // Load custom font
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        optionPositions = new Vector2[menuOptions.length];
        float startX = screenWidth * 0.15f;
        float startY = screenHeight * 0.5f;

        // Calculate menu item positions
        for (int i = 0; i < menuOptions.length; i++) {
            optionPositions[i] = new Vector2(startX, startY - (i * 80));
        }

        mainMenuAnimator = new MainMenuAnimator(); // Initialize animated background
        sfx = new PlayAudio(); // Initialize PlayAudio

        // Play menu background music
        sfx.playMusic("Boss-BattleMusic.wav");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1); // Clear screen with black color

        mainMenuAnimator.update(delta); // Update background animation

        batch.begin();
        mainMenuAnimator.render(); // Render animated background
        batch.end();

        batch.begin();
        updateGlowEffect(delta); // Update glow effect on selected text
        updateMouseSelection(); // Check if mouse is hovering over an option

        // Render menu options
        for (int i = 0; i < menuOptions.length; i++) {
            boolean isSelected = (i == selectedIndex);
            font.setColor(isSelected ? new Color(0, 1, 1, glowAlpha) : Color.LIGHT_GRAY);
            font.draw(batch, menuOptions[i], optionPositions[i].x, optionPositions[i].y);
        }
        batch.end();

        handleInput(); // Handle user input
    }

    @Override
    public void resize(int width, int height) {
        mainMenuAnimator.resize(width, height); // Adjust background on resize
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        // Stop music when exiting the menu
        sfx.stopMusic();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        mainMenuAnimator.dispose(); // Free resources
        sfx.stopMusic(); // Ensure music stops when disposing
    }

    /**
     * Handles a pulsing glow effect on the selected menu option.
     */
    private void updateGlowEffect(float delta) {
        glowAlpha += (glowIncreasing ? delta : -delta);
        if (glowAlpha >= 1 || glowAlpha <= 0.5f) glowIncreasing = !glowIncreasing;
    }

    /**
     * Detects if the mouse is hovering over any menu option and updates selection.
     */
    private void updateMouseSelection() {
        float mouseX = Gdx.input.getX();
        float mouseY = screenHeight - Gdx.input.getY(); // Flip Y axis for LibGDX
        int previousIndex = selectedIndex; // Store the previous selection
        selectedIndex = -1;

        for (int i = 0; i < menuOptions.length; i++) {
            float textWidth = font.getRegion().getRegionWidth() * textScale;
            float textHeight = font.getLineHeight() * textScale;
            if (mouseX >= optionPositions[i].x && mouseX <= optionPositions[i].x + textWidth &&
                mouseY >= optionPositions[i].y - textHeight && mouseY <= optionPositions[i].y) {
                selectedIndex = i;
                break;
            }
        }

        // Play sound only if hovered over a new menu option
        if (selectedIndex != -1 && selectedIndex != lastHoveredIndex) {
            sfx.playMusic("hover-button.wav"); // Replace with your actual hover sound file
            lastHoveredIndex = selectedIndex;
        }
    }


    /**
     * Handles keyboard and mouse input for menu navigation.
     */
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex + 1) % menuOptions.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = (selectedIndex - 1 + menuOptions.length) % menuOptions.length;
        }

        // Select option on Enter key or left mouse click
        if ((Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) && selectedIndex != -1) {
            executeAction(selectedIndex);
        }
    }

    /**
     * Executes an action based on the selected menu option.
     */
    private void executeAction(int index) {
        switch (index) {
            case 0: game.newGame(); break; // Start a new game
            case 2: game.displayCharacters(); break; // Open character selection
            case 3: this.game.showControls(); break; // Open controls screen
            case 4: Gdx.app.exit(); break; // Exit game
        }
    }

    /**
     * Generates a custom font from a TTF file.
     */
    private BitmapFont generateFont(String fontPath, int fontSize) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = fontSize;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = 2;
        parameter.shadowColor = new Color(0, 0, 0, 0.75f);

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); // Free font generator
        return font;
    }
}
