package com.vortex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Align;

public class GameControls implements Screen {
    private final GameTransitions game;
    private SpriteBatch batch;
    private BitmapFont font, titleFont, buttonFont;
    private ShapeRenderer shapeRenderer;
    private GlyphLayout layout;

    // Settings
    private float musicVolume = 1.0f;
    private float soundVolume = 1.0f;
    private float brightness = 1.0f;

    // UI elements
    private Rectangle musicSlider, soundSlider, brightnessSlider;
    private Rectangle backButton, restartButton, quitButton;
    private boolean isDraggingMusic = false;
    private boolean isDraggingSound = false;
    private boolean isDraggingBrightness = false;

    // Animation
    private float[] buttonAnimations = new float[3]; // For back, restart, quit
    private float animationSpeed = 5f;
    private float pulseAmount = 0.05f;
    private float pulse = 0f;
    private float pulseSpeed = 2f;

    // Colors
    private Color backgroundColor = new Color(0.05f, 0.07f, 0.15f, 1f);
    private Color accentColor = new Color(0f, 0.8f, 0.8f, 1f);
    private Color secondAccentColor = new Color(0.9f, 0.6f, 0.1f, 1f);
    private Color sliderBgColor = new Color(0.12f, 0.14f, 0.2f, 1f);
    private Color buttonColor = new Color(0.18f, 0.2f, 0.3f, 1f);
    private Color buttonHoverColor = new Color(0.22f, 0.25f, 0.35f, 1f);

    // Slider properties
    private float sliderWidth = 300f;
    private float sliderHeight = 30f;
    private float knobSize = 20f;
    private float knobRadius = 12f;
    private float sliderRadius = 15f; // Increased for more rounded corners

    // Screen dimensions
    private float screenWidth;
    private float screenHeight;

    // Preferences for saving settings
    private Preferences prefs;

    // Scale factor for UI elements
    private float scaleFactor = 1.2f;

    public GameControls(GameTransitions game) {
        this.game = game;
        prefs = Gdx.app.getPreferences("game_settings");
        loadSettings();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Poppins-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Title font
        parameter.size = (int)(42 * scaleFactor);
        parameter.borderWidth = 2f * scaleFactor;
        parameter.borderColor = new Color(0.1f, 0.1f, 0.2f, 1f);
        parameter.color = secondAccentColor;
        titleFont = generator.generateFont(parameter);

        // Regular font for labels
        parameter.size = (int)(24 * scaleFactor);
        parameter.borderWidth = 1.5f * scaleFactor;
        parameter.borderColor = new Color(0.05f, 0.05f, 0.1f, 1f);
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);

        // Button font
        parameter.size = (int)(28 * scaleFactor);
        parameter.color = Color.WHITE;
        parameter.borderWidth = 1.5f * scaleFactor;
        buttonFont = generator.generateFont(parameter);

        generator.dispose();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        // Initialize UI elements with scaling
        float centerX = screenWidth / 2 - (sliderWidth * scaleFactor) / 2;
        float startY = screenHeight * 0.7f;
        float spacing = 70f * scaleFactor;

        musicSlider = new Rectangle(centerX, startY, sliderWidth * scaleFactor, sliderHeight * scaleFactor);
        soundSlider = new Rectangle(centerX, startY - spacing, sliderWidth * scaleFactor, sliderHeight * scaleFactor);
        brightnessSlider = new Rectangle(centerX, startY - spacing * 2, sliderWidth * scaleFactor, sliderHeight * scaleFactor);

        float buttonWidth = 180f * scaleFactor; // Adjusted button width
        float buttonHeight = 60f * scaleFactor; // Adjusted button height
        float buttonSpacing = 15f * scaleFactor;
        float totalButtonWidth = buttonWidth * 3 + buttonSpacing * 2;
        float buttonsStartX = screenWidth / 2 - totalButtonWidth / 2 - 50 * scaleFactor + screenWidth * 0.02f - 10; // Shifted left by 10
        float buttonY = startY - spacing * 3.5f - 30 * scaleFactor; // Increased spacing

        backButton = new Rectangle(buttonsStartX, buttonY, buttonWidth, buttonHeight);
        restartButton = new Rectangle(buttonsStartX + buttonWidth + buttonSpacing, buttonY, buttonWidth, buttonHeight);
        quitButton = new Rectangle(buttonsStartX + buttonWidth * 2 + buttonSpacing * 2, buttonY, buttonWidth, buttonHeight);
    }

    @Override
    public void render(float delta) {
        // Apply brightness to background
        Color bgWithBrightness = backgroundColor.cpy().mul(brightness * 0.5f + 0.5f);
        ScreenUtils.clear(bgWithBrightness.r, bgWithBrightness.g, bgWithBrightness.b, 1);

        // Update animations
        updateAnimations(delta);
        handleInput();

        // Begin shape rendering
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Draw decorative background elements
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(accentColor.r, accentColor.g, accentColor.b, 0.05f);
        shapeRenderer.rect(0, screenHeight * 0.4f, screenWidth, screenHeight * 0.2f);
        shapeRenderer.end();

        // Draw sliders and buttons with shading
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawSliders();
        drawButtons();
        shapeRenderer.end();

        // Draw outlines
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        drawSliderOutlines();
        drawButtonOutlines();
        shapeRenderer.end();

        // Draw text
        batch.begin();
        drawText();
        batch.end();

        // Save settings if they changed
        saveSettings();
    }

    private void drawText() {
        // Draw title with shadow and added space above and below
        layout.setText(titleFont, "VORTEX CONTROLS");
        titleFont.draw(batch, "VORTEX CONTROLS",
            screenWidth / 2 - layout.width / 2,
            screenHeight * 0.88f + 20 * scaleFactor); // Added 20 pixels above

        // Draw slider labels, more precise alignment
        font.setColor(Color.WHITE);
        layout.setText(font, "MUSIC VOLUME");
        float musicLabelX = musicSlider.x - layout.width - 40 * scaleFactor;
        float musicLabelY = musicSlider.y + (sliderHeight * scaleFactor) / 2 + layout.height / 2;
        font.draw(batch, "MUSIC VOLUME", musicLabelX, musicLabelY);

        String musicPercent = String.format("%.0f%%", musicVolume * 100);
        layout.setText(font, musicPercent);
        float musicValueX = musicSlider.x + (sliderWidth * scaleFactor) + 40 * scaleFactor;
        float musicValueY = musicSlider.y + (sliderHeight * scaleFactor) / 2 + layout.height / 2;
        font.draw(batch, musicPercent, musicValueX, musicValueY);

        layout.setText(font, "SOUND EFFECTS");
        float soundLabelX = soundSlider.x - layout.width - 40 * scaleFactor;
        float soundLabelY = soundSlider.y + (sliderHeight * scaleFactor) / 2 + layout.height / 2;
        font.draw(batch, "SOUND EFFECTS", soundLabelX, soundLabelY);

        String soundPercent = String.format("%.0f%%", soundVolume * 100);
        layout.setText(font, soundPercent);
        float soundValueX = soundSlider.x + (sliderWidth * scaleFactor) + 40 * scaleFactor;
        float soundValueY = soundSlider.y + (sliderHeight * scaleFactor) / 2 + layout.height / 2;
        font.draw(batch, soundPercent, soundValueX, soundValueY);

        layout.setText(font, "BRIGHTNESS");
        float brightnessLabelX = brightnessSlider.x - layout.width - 40 * scaleFactor;
        float brightnessLabelY = brightnessSlider.y + (sliderHeight * scaleFactor) / 2 + layout.height / 2;
        font.draw(batch, "BRIGHTNESS", brightnessLabelX, brightnessLabelY);

        String brightnessPercent = String.format("%.0f%%", brightness * 100);
        layout.setText(font, brightnessPercent);
        float brightnessValueX = brightnessSlider.x + (sliderWidth * scaleFactor) + 40 * scaleFactor;
        float brightnessValueY = brightnessSlider.y + (sliderHeight * scaleFactor) / 2 + layout.height / 2;
        font.draw(batch, brightnessPercent, brightnessValueX, brightnessValueY);

        // Draw button text
        drawCenteredButtonText(batch, backButton, "BACK", 0);
        drawCenteredButtonText(batch, restartButton, "RESTART", 1);
        drawCenteredButtonText(batch, quitButton, "QUIT", 2);
    }

    private void drawCenteredButtonText(SpriteBatch batch, Rectangle button, String text, int buttonIndex) {
        layout.setText(buttonFont, text);
        float scale = 1.0f + buttonAnimations[buttonIndex] * 0.1f;

        buttonFont.setColor(Color.WHITE);
        // Center the text within the button
        font.draw(batch, text,
            button.x + button.width / 2 - layout.width / 2,
            button.y + button.height / 2 + layout.height / 2);
    }

    private void drawSliders() {
        // Define distinct colors for each slider
        Color musicSliderColor = new Color(0.1f, 0.5f, 0.8f, 1f); // Blueish
        Color soundSliderColor = new Color(0.8f, 0.6f, 0.2f, 1f); // Orangeish
        Color brightnessSliderColor = new Color(0.4f, 0.8f, 0.3f, 1f); // Greenish

        // Draw music slider
        shapeRenderer.setColor(sliderBgColor);
        shapeRenderer.rect(musicSlider.x, musicSlider.y, musicSlider.width, musicSlider.height);
        shapeRenderer.setColor(musicSliderColor);
        shapeRenderer.rect(musicSlider.x, musicSlider.y, musicSlider.width * musicVolume, musicSlider.height);

        // Draw sound slider
        shapeRenderer.setColor(sliderBgColor);
        shapeRenderer.rect(soundSlider.x, soundSlider.y, soundSlider.width, soundSlider.height);
        shapeRenderer.setColor(soundSliderColor);
        shapeRenderer.rect(soundSlider.x, soundSlider.y, soundSlider.width * soundVolume, soundSlider.height);

        // Draw brightness slider
        shapeRenderer.setColor(sliderBgColor);
        shapeRenderer.rect(brightnessSlider.x, brightnessSlider.y, brightnessSlider.width, brightnessSlider.height);
        shapeRenderer.setColor(brightnessSliderColor);
        shapeRenderer.rect(brightnessSlider.x, brightnessSlider.y, brightnessSlider.width * brightness, brightnessSlider.height);

        // Draw knobs with corresponding colors
        drawKnob(musicSlider.x + musicSlider.width * musicVolume, musicSlider.y + musicSlider.height / 2, musicSliderColor);
        drawKnob(soundSlider.x + soundSlider.width * soundVolume, soundSlider.y + soundSlider.height / 2, soundSliderColor);
        drawKnob(brightnessSlider.x + brightnessSlider.width * brightness, brightnessSlider.y + sliderHeight / 2, brightnessSliderColor);
    }

    private void drawKnob(float x, float y, Color color) {
        // Inner circle
        shapeRenderer.setColor(color);
        shapeRenderer.circle(x, y, knobRadius * scaleFactor);
        // Outer circle
        shapeRenderer.setColor(color.r * 1.2f, color.g * 1.2f, color.b * 1.2f, 0.5f);
        shapeRenderer.circle(x, y, (knobRadius + 4) * scaleFactor);
    }

    private void drawSliderOutlines() {
        shapeRenderer.setColor(accentColor.r, accentColor.g, accentColor.b, 0.5f);
        shapeRenderer.rect(musicSlider.x, musicSlider.y, musicSlider.width, musicSlider.height);
        shapeRenderer.rect(soundSlider.x, soundSlider.y, soundSlider.width, soundSlider.height);
        shapeRenderer.rect(brightnessSlider.x, brightnessSlider.y, brightnessSlider.width, brightnessSlider.height);
    }

    private void drawButtons() {
        // Draw back button with animation
        drawAnimatedButton(backButton, buttonColor, buttonHoverColor, 0);
        // Draw restart button with animation
        drawAnimatedButton(restartButton, buttonColor, buttonHoverColor, 1);
        // Draw quit button with animation
        drawAnimatedButton(quitButton, buttonColor, buttonHoverColor, 2);
    }

    private void drawAnimatedButton(Rectangle button, Color baseColor, Color hoverColor, int buttonIndex) {
        float animation = buttonAnimations[buttonIndex];
        Color renderColor = baseColor.cpy().lerp(hoverColor, animation);

        shapeRenderer.setColor(renderColor);
        shapeRenderer.rect(button.x, button.y, button.width, button.height);

        if (animation > 0) {
            shapeRenderer.setColor(accentColor.r, accentColor.g, accentColor.b, animation * 0.3f);
            shapeRenderer.rect(button.x - 2, button.y - 2, button.width + 4, button.height + 4);
        }
    }

    private void drawButtonOutlines() {
        shapeRenderer.setColor(accentColor.r, accentColor.g, accentColor.b, 0.8f + pulse * 0.2f);
        shapeRenderer.rect(backButton.x, backButton.y, backButton.width, backButton.height);
        shapeRenderer.rect(restartButton.x, restartButton.y, restartButton.width, restartButton.height);
        shapeRenderer.rect(quitButton.x, quitButton.y, quitButton.width, quitButton.height);
    }

    private void updateAnimations(float delta) {
        // Update button hover animations
        updateButtonAnimation(backButton, 0, delta);
        updateButtonAnimation(restartButton, 1, delta);
        updateButtonAnimation(quitButton, 2, delta);

        // Update pulsing effect
        pulse = (float)Math.sin(Gdx.graphics.getFrameId() * 0.05f) * pulseAmount;
    }

    private void updateButtonAnimation(Rectangle button, int buttonIndex, float delta) {
        boolean isHovered = button.contains(Gdx.input.getX(), screenHeight - Gdx.input.getY());

        if (isHovered) {
            buttonAnimations[buttonIndex] = Math.min(1.0f, buttonAnimations[buttonIndex] + delta * animationSpeed);
        } else {
            buttonAnimations[buttonIndex] = Math.max(0.0f, buttonAnimations[buttonIndex] - delta * animationSpeed);
        }
    }

    private void handleInput() {
        float mouseX = Gdx.input.getX();
        float mouseY = screenHeight - Gdx.input.getY();

        // Check for button clicks
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            // Check buttons
            if (backButton.contains(mouseX, mouseY)) {
                saveSettings();
                game.setScreen(new GameMenu(game));
                return;
            }
            if (restartButton.contains(mouseX, mouseY)) {
                saveSettings();
                game.newGame();
                return;
            }
            if (quitButton.contains(mouseX, mouseY)) {
                saveSettings();
                Gdx.app.exit();
                return;
            }

            // Check slider interactions
            if (isPointNearSlider(musicSlider, mouseX, mouseY, musicVolume)) {
                isDraggingMusic = true;
            } else if (isPointNearSlider(soundSlider, mouseX, mouseY, soundVolume)) {
                isDraggingSound = true;
            } else if (isPointNearSlider(brightnessSlider, mouseX, mouseY, brightness)) {
                isDraggingBrightness = true;
            }
        }

        // Handle dragging
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (isDraggingMusic) {
                musicVolume = calculateSliderValue(musicSlider, mouseX);
            } else if (isDraggingSound) {
                soundVolume = calculateSliderValue(soundSlider, mouseX);
            } else if (isDraggingBrightness) {
                brightness = calculateSliderValue(brightnessSlider, mouseX);
            }
        } else {
            isDraggingMusic = false;
            isDraggingSound = false;
            isDraggingBrightness = false;
        }

        // Allow clicking directly on slider
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (musicSlider.contains(mouseX, mouseY)) {
                musicVolume = calculateSliderValue(musicSlider, mouseX);
            } else if (soundSlider.contains(mouseX, mouseY)) {
                soundVolume = calculateSliderValue(soundSlider, mouseX);
            } else if (brightnessSlider.contains(mouseX, mouseY)) {
                brightness = calculateSliderValue(brightnessSlider, mouseX);
            }
        }
    }

    private boolean isPointNearSlider(Rectangle slider, float x, float y, float value) {
        float knobX = slider.x + slider.width * value;
        float knobY = slider.y + slider.height / 2;
        return Vector2.dst(knobX, knobY, x, y) <= knobRadius * 2;
    }

    private float calculateSliderValue(Rectangle slider, float mouseX) {
        float relativeX = mouseX - slider.x;
        float value = relativeX / slider.width;
        return Math.max(0, Math.min(1, value));
    }

    private void loadSettings() {
        musicVolume = prefs.getFloat("musicVolume", 1.0f);
        soundVolume = prefs.getFloat("soundVolume", 1.0f);
        brightness = prefs.getFloat("brightness", 1.0f);
    }

    private void saveSettings() {
        prefs.putFloat("musicVolume", musicVolume);
        prefs.putFloat("soundVolume", soundVolume);
        prefs.putFloat("brightness", brightness);
        prefs.flush();
    }

    @Override
    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        // Re-position UI elements with scaling
        float centerX = screenWidth / 2 - (sliderWidth * scaleFactor) / 2;
        float startY = screenHeight * 0.7f;
        float spacing = 70f * scaleFactor;

        musicSlider.x = centerX;
        musicSlider.y = startY;
        musicSlider.width = sliderWidth * scaleFactor;
        musicSlider.height = sliderHeight * scaleFactor;

        soundSlider.x = centerX;
        soundSlider.y = startY - spacing;
        soundSlider.width = sliderWidth * scaleFactor;
        soundSlider.height = sliderHeight * scaleFactor;

        brightnessSlider.x = centerX;
        brightnessSlider.y = startY - spacing * 2;
        brightnessSlider.width = sliderWidth * scaleFactor;
        brightnessSlider.height = sliderHeight * scaleFactor;

        float buttonWidth = 180f * scaleFactor; // Adjusted button width
        float buttonHeight = 60f * scaleFactor; // Adjusted button height
        float buttonSpacing = 15f * scaleFactor;
        float totalButtonWidth = buttonWidth * 3 + buttonSpacing * 2;
        float buttonsStartX = screenWidth / 2 - totalButtonWidth / 2 - 50 * scaleFactor + screenWidth * 0.02f - 10; // Shifted left by 10
        float buttonY = startY - spacing * 3.5f - 30 * scaleFactor; // Increased spacing

        backButton.x = buttonsStartX;
        backButton.y = buttonY;
        backButton.width = buttonWidth;
        backButton.height = buttonHeight;

        restartButton.x = buttonsStartX + buttonWidth + buttonSpacing;
        restartButton.y = buttonY;
        restartButton.width = buttonWidth;
        restartButton.height = buttonHeight;

        quitButton.x = buttonsStartX + buttonWidth * 2 + buttonSpacing * 2;
        quitButton.y = buttonY;
        quitButton.width = buttonWidth;
        quitButton.height = buttonHeight;
    }

    @Override
    public void pause() {
        saveSettings();
    }

    @Override
    public void resume() {
        loadSettings();
    }

    @Override
    public void hide() {
        saveSettings();
    }

    @Override
    public void dispose() {
        saveSettings();
        batch.dispose();
        font.dispose();
        titleFont.dispose();
        buttonFont.dispose();
        shapeRenderer.dispose();
    }
}

