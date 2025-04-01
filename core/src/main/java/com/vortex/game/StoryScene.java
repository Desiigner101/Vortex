package com.vortex.game;

// Import all necessary libraries
import com.badlogic.gdx.Screen;  // Interface for game screens
import com.badlogic.gdx.Gdx;     // Main GDX class for core functionality
import com.badlogic.gdx.graphics.Color;  // Color class for RGBA values
import com.badlogic.gdx.graphics.Texture;  // Image/texture handling
import com.badlogic.gdx.graphics.g2d.SpriteBatch;  // Batch rendering
import com.badlogic.gdx.graphics.g2d.TextureRegion;  // Part of a texture
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;  // Font generation
import com.badlogic.gdx.scenes.scene2d.Stage;  // 2D scene container
import com.badlogic.gdx.scenes.scene2d.ui.Skin;  // UI styling
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;  // Clickable button
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;  // Button click listener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;  // Texture as drawable
import com.badlogic.gdx.utils.ScreenUtils;  // Screen utilities
import com.badlogic.gdx.utils.Timer;  // For delayed actions
import com.badlogic.gdx.utils.viewport.StretchViewport;  // Viewport type
import com.badlogic.gdx.utils.viewport.Viewport;  // Manages camera/view
import com.badlogic.gdx.scenes.scene2d.Actor;  // Base class for scene elements
import com.badlogic.gdx.Input.Keys;  // Keyboard key constants
import com.vortex.SFX.PlayAudio;  // Custom audio player

import java.util.ArrayList;
import java.util.List;

public class StoryScene implements Screen {
    // Class variables with explanations:

    // Core game reference for transitions
    private final GameTransitions game;

    // Rendering
    private SpriteBatch spriteBatch;  // Draws textures efficiently
    private Texture backgroundImage;  // Current background texture
    private Viewport viewport;  // Manages screen dimensions
    private Stage stage;  // Holds UI elements

    // UI Components
    private Skin skin;  // Stores button styles and fonts
    private TextButton backButton;  // Back button (not used in current code)
    private BorderedTextBox dialogueBox;  // Custom text display box

    // Game flow
    private Runnable nextSceneCallback;  // What to do when story ends
    private List<Texture> texturesToDispose = new ArrayList<>();  // Track textures for cleanup
    private PlayAudio sfx = new PlayAudio();  // Sound effects player

    // Story data
    private String[][] storyData;  // 2D array: [scene][character,dialogue,background,color]
    private int dialogueIndex = 0;  // Current story segment

    // Constructor - called when creating the scene
    public StoryScene(GameTransitions game, String[] storyData, Runnable nextSceneCallback) {
        this.game = game;  // Store game reference
        this.nextSceneCallback = nextSceneCallback;  // What to do after story
        loadStoryData(storyData);  // Process the story content
    }

    @Override
    public void show() {
        // Called when this screen becomes active

        // Initialize rendering
        spriteBatch = new SpriteBatch();
        viewport = new StretchViewport(1920, 1080);  // Fixed 16:9 ratio
        viewport.apply();  // Apply viewport settings

        // Create stage with custom key listener
        stage = new Stage(viewport) {
            @Override
            public boolean keyDown(int keyCode) {
                // Handle Enter key press
                if (keyCode == Keys.ENTER) {
                    skipToNextScene();  // Skip story when Enter pressed
                    return true;  // Key event handled
                }
                return super.keyDown(keyCode);  // Default handling
            }
        };

        // Set stage to receive input
        Gdx.input.setInputProcessor(stage);

        // Initialize UI skin (styles)
        skin = new Skin();

        // --- BUTTON SETUP ---

        // Load button textures for different states
        Texture upTexture = new Texture(Gdx.files.internal("UI/SKIP_BUTTON.png"));  // Normal
        Texture downTexture = new Texture(Gdx.files.internal("UI/SKIP_BUTTON_PRESSED.png"));  // Pressed
        Texture overTexture = new Texture(Gdx.files.internal("UI/SKIP_BUTTON_HOVER.png"));  // Hover

        // Create button style with states
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(upTexture));  // Normal
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(downTexture));  // Pressed
        buttonStyle.over = new TextureRegionDrawable(new TextureRegion(overTexture));  // Hover

        // --- FONT SETUP ---

        // Generate custom font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Poppins-SemiBold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 24;  // Font size
        params.color = new Color(0.8f, 0.9f, 1f, 1f);  // Light blue
        params.borderWidth = 1.5f;  // Outline thickness
        params.borderColor = new Color(0.2f, 0.4f, 0.8f, 0.8f);  // Blue outline
        buttonStyle.font = generator.generateFont(params);  // Create font
        generator.dispose();  // Clean up generator

        // Add style to skin
        skin.add("default", buttonStyle);

        // --- SKIP BUTTON CREATION ---

        final TextButton skipButton = new TextButton("SKIP", skin);
        skipButton.setSize(180, 60);  // Width, height
        skipButton.setPosition(Gdx.graphics.getWidth() - (-80), 280);  // X, Y position
        skipButton.getLabel().setFontScale(1.0f);  // Slightly smaller text

        // Hover sound effect
        skipButton.addListener(new ChangeListener() {
            private boolean wasOver = false;  // Track hover state

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Play click sound when pressed
                if (skipButton.isPressed()) {
                    sfx.playSoundEffect("whenTextIsClicked.wav",0);
                }

                // Play hover sound only when first entering hover state
                if (skipButton.isOver() && !wasOver) {
                    sfx.playSoundEffect("hover_button.wav",0);
                }
                wasOver = skipButton.isOver();  // Update hover state
            }
        });

        // Click action
        skipButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                skipToNextScene();  // Skip to next scene when clicked
            }
        });

        // Hover animation
        skipButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (skipButton.isOver()) {
                    // Enlarge and brighten when hovered
                    skipButton.setTransform(true);  // Enable transformations
                    skipButton.setScale(1.02f);  // 2% larger
                    skipButton.setColor(1.1f, 1.1f, 1.1f, 1f);  // Brighter
                } else {
                    // Return to normal when not hovered
                    skipButton.setScale(1f);
                    skipButton.setColor(Color.WHITE);
                }
            }
        });

        // Add button to stage
        stage.addActor(skipButton);

        // Track textures for cleanup
        texturesToDispose.add(upTexture);
        texturesToDispose.add(downTexture);
        texturesToDispose.add(overTexture);

        // Initialize first scene
        updateScene();
    }

    // Skip to next scene (called by button or Enter key)
    private void skipToNextScene() {
        // Play click sound
        sfx.playSoundEffect("whenTextIsClicked.wav", 0);

        // Trigger next scene if callback exists
        if (nextSceneCallback != null) {
            nextSceneCallback.run();
        }
    }

    // Process story data into structured format
    private void loadStoryData(String... data) {
        // Validate data format (must be divisible by 4)
        if (data.length % 4 != 0) {
            throw new IllegalArgumentException("Each entry must have exactly 4 values: Character, Dialogue, Background, TextColor.");
        }

        // Organize into 2D array [scene][properties]
        int length = data.length / 4;
        storyData = new String[length][4];
        for (int i = 0; i < length; i++) {
            storyData[i][0] = data[i * 4];     // Character name
            storyData[i][1] = data[i * 4 + 1]; // Dialogue text
            storyData[i][2] = data[i * 4 + 2]; // Background image
            storyData[i][3] = data[i * 4 + 3]; // Text color (Hex)
        }
    }

    // Update current scene display
    private void updateScene() {
        // Check if story ended
        if (dialogueIndex >= storyData.length) {
            if (nextSceneCallback != null) {
                nextSceneCallback.run();  // Go to next scene
            } else {
                game.startGameMenu();  // Default to menu
            }
            return;
        }

        // Clean up previous dialogue box
        if (dialogueBox != null) {
            dialogueBox.dispose();
        }

        // Load background image
        String backgroundPath = "Backgrounds/" + storyData[dialogueIndex][2] + ".png";
        if (Gdx.files.internal(backgroundPath).exists()) {
            backgroundImage = new Texture(Gdx.files.internal(backgroundPath));
        } else {
            Gdx.app.log("ERROR", "Missing background file: " + backgroundPath);
            backgroundImage = new Texture(Gdx.files.internal("Backgrounds/default.png"));
        }

        // Create new dialogue box with current scene data
        dialogueBox = new BorderedTextBox(
            "testBorder",  // Border style
            false,  // Not sure - likely some display flag
            storyData[dialogueIndex][0],  // Character name
            storyData[dialogueIndex][1],  // Dialogue text
            250,  // X position
            1800,  // Width
            storyData[dialogueIndex][3]  // Text color
        );
    }

    // Main game loop - called every frame
    @Override
    public void render(float delta) {
        // Clear screen with dark color
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // Update viewport if window resized
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        // Draw background
        spriteBatch.begin();
        if (backgroundImage != null) {
            spriteBatch.draw(backgroundImage, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        }

        // Draw dialogue box
        if (dialogueBox != null) {
            dialogueBox.render(spriteBatch, delta);
        }
        spriteBatch.end();

        // Update and draw UI
        stage.act(delta);
        stage.draw();

        // Advance dialogue if needed
        if (dialogueBox != null && dialogueBox.shouldAdvance()) {
            dialogueBox.resetAdvance();
            dialogueIndex++;
            updateScene();
        }
    }

    // Handle window resize
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);  // Update viewport dimensions
    }

    // Required Screen interface methods (not used here)
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    // Clean up resources
    @Override
    public void dispose() {
        // Release all resources to prevent memory leaks
        if (spriteBatch != null) spriteBatch.dispose();
        if (backgroundImage != null) backgroundImage.dispose();
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (dialogueBox != null) dialogueBox.dispose();

        // Dispose all loaded textures
        for (Texture texture : texturesToDispose) {
            if (texture != null) texture.dispose();
        }
    }
}
