package com.vortex.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class VortexMain implements Screen {
    private final GameTransitions game; // Reference to the main game manager
    private SpriteBatch batch; // Used for rendering textures
    private Texture backgroundImage; // Background image of the scene
    private Viewport viewport; // Manages screen scaling
    private Stage stage; // Handles UI elements
    private Skin skin; // UI skin for buttons
    private TextButton backButton; // Button to return to the main menu

    public VortexMain(GameTransitions game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Initialize rendering batch
        batch = new SpriteBatch();

        // Load the background image
        backgroundImage = new Texture("Pictures/nova'sLab.jpg");

        // Create a viewport that stretches to fit the screen
        viewport = new StretchViewport(backgroundImage.getWidth(), backgroundImage.getHeight());
        viewport.apply();

        // Set up the stage for UI elements
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Load UI skin (make sure "uiskin.json" exists in the assets folder)
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Create and configure the back button
        backButton = new TextButton("Back", skin);
        backButton.setSize(120, 50); // Button dimensions
        backButton.setPosition(20, 20); // Position in the bottom-left corner

        // Add functionality to return to the main menu when clicked
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.create(); // Switch back to the main menu
            }
        });

        // Add the button to the stage
        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a dark background color
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // Update the viewport to match the window size
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        // Set up the camera for rendering
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // Draw the background image to fit the screen
        batch.begin();
        batch.draw(backgroundImage, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

        // Update and render UI elements
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        // Clean up resources to avoid memory leaks
        batch.dispose();
        backgroundImage.dispose();
        stage.dispose();
        skin.dispose();
    }
}
