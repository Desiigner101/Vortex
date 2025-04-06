package com.vortex.game;

// Import necessary LibGDX components
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter; // Base class for handling screens
import com.badlogic.gdx.assets.AssetManager; // Manages game assets efficiently
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera; // Handles 2D camera positioning
import com.badlogic.gdx.graphics.Texture; // Represents images (textures)
import com.badlogic.gdx.graphics.g2d.SpriteBatch; // Efficiently renders 2D graphics
import com.badlogic.gdx.utils.Array; // A resizable array (similar to an ArrayList)
import com.badlogic.gdx.utils.ScreenUtils; // Utility for clearing the screen
import com.badlogic.gdx.utils.viewport.FitViewport; // Maintains aspect ratio while resizing
import com.badlogic.gdx.utils.viewport.Viewport; // Handles screen size adaptation

/**
 * This class plays an intro video using a sequence of images.
 * The images are loaded as frames and displayed in sequence to simulate a video.
 */
public class VideoIntro extends ScreenAdapter {
    private SpriteBatch batch; // Handles rendering images to the screen
    private AssetManager assetManager; // Manages assets like textures
    private Array<Texture> frames; // Stores all video frames as textures
    private int frameIndex = 0; // Keeps track of the current frame
    private float elapsedTime = 0f; // Measures time for frame switching
    private final GameTransitions game; // Reference to the main game for switching screens

    private OrthographicCamera camera; // Controls the camera view
    private Viewport viewport; // Ensures the screen resizes correctly
    private Sound introSound;
    /**
     * Constructor initializes the game instance for screen transitions.
     * @param game Reference to the main game class.
     */
    public VideoIntro(GameTransitions game) {
        this.game = game;
    }

    /**
     * Called when this screen is displayed. It initializes resources and loads frames.
     */
    @Override
    public void show() {
        this.introSound = Gdx.audio.newSound(Gdx.files.internal("assets/SoundEffectsFolder/introMusic.wav"));
        batch = new SpriteBatch(); // Creates a new SpriteBatch for rendering
        assetManager = new AssetManager(); // Initializes the asset manager
        frames = new Array<>(); // Initializes the array to hold video frames

        // Set up the camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera); // Sets resolution and maintains aspect ratio
        viewport.apply(); // Applies the viewport settings

        // Center the camera
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        // Load each frame as an image file (total of 86 frames)
        for (int i = 1; i <= 90; i++) {
            String fileName = "VideoIntro/ezgif-frame-" + String.format("%03d", i) + ".png";
            assetManager.load(fileName, Texture.class); // Loads the frame into the asset manager
        }

        assetManager.finishLoading(); // Ensures all assets are loaded before proceeding

        // Retrieve all loaded frames and store them in the frames array
        for (int i = 1; i <= 90; i++) {
            String fileName = "VideoIntro/ezgif-frame-" + String.format("%03d", i) + ".png";
            frames.add(assetManager.get(fileName, Texture.class)); // Adds each frame to the array
        }
        introSound.play();
    }

    /**
     * Renders the video by displaying frames sequentially.
     * @param delta Time elapsed since the last frame update.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1); // Clears the screen (black background)
        elapsedTime += delta; // Updates the timer for frame changes

        // Switch to the next frame every 0.05 seconds
        if (elapsedTime > 0.05f) {
            frameIndex++;
            elapsedTime = 0;
        }

        // If all frames have been played, transition to the main menu
        if (frameIndex >= frames.size) {
            game.setScreen(new GameMenu(game));
            return; // Exit the method to prevent rendering non-existent frames
        }

        batch.setProjectionMatrix(camera.combined); // Applies the camera's transformations
        batch.begin(); // Begins drawing

        // Get the current frame to display
        Texture currentFrame = frames.get(frameIndex);

        // Maintain aspect ratio when scaling the video
        float aspectRatio = (float) currentFrame.getWidth() / currentFrame.getHeight();
        float width = viewport.getWorldWidth();
        float height = width / aspectRatio;

        // Ensure the frame fits within the screen bounds
        if (height > viewport.getWorldHeight()) {
            height = viewport.getWorldHeight();
            width = height * aspectRatio;
        }

        // Center the frame on the screen
        float x = (viewport.getWorldWidth() - width) / 2;
        float y = (viewport.getWorldHeight() - height) / 2;

        // Draw the current frame
        batch.draw(currentFrame, x, y, width, height);
        batch.end(); // Ends drawing
    }

    /**
     * Called when the window is resized. Adjusts the viewport to match the new size.
     * @param width  The new width of the window.
     * @param height The new height of the window.
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    /**
     * Cleans up resources when the screen is no longer needed.
     */
    @Override
    public void dispose() {
        batch.dispose(); // Releases the SpriteBatch memory
        assetManager.dispose();
        introSound.dispose();
    }
}
