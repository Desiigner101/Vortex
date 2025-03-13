package com.vortex.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.vortex.SpriteSheetAnimator.SpriteSheetAnimator;

import java.util.Scanner;

public class VortexMain extends ApplicationAdapter {
    private SpriteBatch batch;
    private SpriteSheetAnimator characterAnimator;
    private float characterX, characterY;
    private Stage stage;
    private TextButton exitButton;
    private Skin skin;

    private enum TestMode {
        DISPLAY_CHARACTERS,
        SHOW_ALL_CHARACTERS
    }

    private TestMode currentMode;

    public VortexMain() {
        currentMode = selectTestMode();
    }

    private TestMode selectTestMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select test mode: \n1 - Display Characters (WASD to move) \n2 - Show All Characters");

        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equals("1")) return TestMode.DISPLAY_CHARACTERS;
            if (input.equals("2")) return TestMode.SHOW_ALL_CHARACTERS;
            System.out.println("Invalid input. Please enter 1 or 2.");
        }
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        characterAnimator = new SpriteSheetAnimator("Pictures/umbra_idle_battle_sheet.png", 4, 1, 0.1f);

        // Start position of character (centered on screen)
        characterX = Gdx.graphics.getWidth() / 2f - 32;
        characterY = Gdx.graphics.getHeight() / 2f - 32;

        // Setup UI stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load UI skin (use a default skin or create your own)
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Create Exit Button
        exitButton = new TextButton("Exit", skin);
        exitButton.setSize(100, 50);
        exitButton.setPosition(Gdx.graphics.getWidth() - 120, 20);

        // Add event listener to exit the game
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                Gdx.app.exit();
            }
        });

        // Add button to stage
        stage.addActor(exitButton);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (currentMode) {
            case DISPLAY_CHARACTERS:
                handleCharacterMovement();
                displayCharacters();
                break;
            case SHOW_ALL_CHARACTERS:
                displayAllCharacters();
                break;
        }

        // Draw the exit button
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void handleCharacterMovement() {
        float speed = 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) characterY += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) characterY -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) characterX -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) characterX += speed;
    }

    private void displayCharacters() {
        batch.begin();
        TextureRegion currentFrame = characterAnimator.getCurrentFrame(Gdx.graphics.getDeltaTime());
        batch.draw(currentFrame, characterX, characterY, 64, 64);
        batch.end();
    }

    private void displayAllCharacters() {
        batch.begin();
        TextureRegion[] frames = characterAnimator.getFrames();
        int cols = 4; // Adjust based on your sprite sheet
        int rows = frames.length / cols;
        for (int i = 0; i < frames.length; i++) {
            int x = (i % cols) * 70;
            int y = Gdx.graphics.getHeight() - (i / cols) * 70 - 70;
            batch.draw(frames[i], x, y, 64, 64);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        characterAnimator.dispose();
        stage.dispose();
        skin.dispose();
    }
}
