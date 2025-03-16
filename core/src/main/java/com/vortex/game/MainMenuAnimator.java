package com.vortex.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Gdx;

/**
 * Handles frame-based animation for the main menu background.
 * Loads a sequence of images and cycles through them to create an animated effect.
 */
public class MainMenuAnimator {
    private SpriteBatch batch; // Renders textures onto the screen
    private AssetManager assetManager; // Manages and loads assets efficiently
    private Array<Texture> frames; // Stores the animation frames
    private int frameIndex = 0; // Tracks the current animation frame
    private float elapsedTime = 0f; // Tracks time to control frame switching

    private OrthographicCamera camera; // Camera for rendering the animation
    private Viewport viewport; // Ensures proper scaling for different screen sizes


    // Constants for animation speed and frame count
    private static final float FRAME_DURATION = 0.1f; // 100ms per frame (10 FPS)
    private static final int TOTAL_FRAMES = 49; // 49 frames, from "MainMenu0" to "MainMenu48"

    /**
     * Constructor - Initializes the animation system, loads frames, and sets up the camera.
     */
    public MainMenuAnimator() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        frames = new Array<>();

        // Set up the camera and viewport for consistent scaling
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera);
        viewport.apply();

        // Center the camera
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        // Load animation frames from the "MainMenuFrames" folder
        loadFrames();
    }

    /**
     * Loads animation frames into memory.
     * Uses AssetManager for optimized asset handling.
     */
    private void loadFrames() {
        for (int i = 0; i < TOTAL_FRAMES; i++) {
            String fileName = "MainMenuFrames/MainMenu" + i + ".png";
            assetManager.load(fileName, Texture.class);
        }

        // Ensure all assets are loaded before accessing them
        assetManager.finishLoading();

        // Retrieve loaded textures and store them in the frames array
        for (int i = 0; i < TOTAL_FRAMES; i++) {
            String fileName = "MainMenuFrames/MainMenu" + i + ".png";
            frames.add(assetManager.get(fileName, Texture.class));
        }
    }

    /**
     * Updates the animation, switching frames based on time.
     *
     * @param delta Time elapsed since the last frame update.
     */
    public void update(float delta) {
        elapsedTime += delta;

        // If enough time has passed, switch to the next frame
        if (elapsedTime > FRAME_DURATION) {
            frameIndex = (frameIndex + 1) % TOTAL_FRAMES; // Loop back to the first frame
            elapsedTime = 0; // Reset elapsed time for the next frame cycle
        }
    }

    /**
     * Renders the current frame of the animation.
     */
    public void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw the current frame, stretched to fit the viewport
        Texture currentFrame = frames.get(frameIndex);
        batch.draw(currentFrame, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        batch.end();
    }

    /**
     * Adjusts the viewport size when the window is resized.
     *
     * @param width  New width of the screen.
     * @param height New height of the screen.
     */
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    /**
     * Cleans up memory and resources to prevent leaks.
     */
    public void dispose() {
        batch.dispose();
        assetManager.dispose();
    }
}
