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

public class StoryScene implements Screen {
    private final GameTransitions game;
    private SpriteBatch spriteBatch;
    private Texture backgroundImage;
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private TextButton backButton;
    private BorderedTextBox dialogueBox;
    private Runnable nextSceneCallback;

    private String[][] storyData;
    private int dialogueIndex = 0;

    public StoryScene(GameTransitions game, String[] storyData, Runnable nextSceneCallback) {
        this.game = game;
        this.nextSceneCallback = nextSceneCallback;
        loadStoryData(storyData);
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        viewport = new StretchViewport(1920, 1080);
        viewport.apply();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        backButton = new TextButton("Back", skin);
        backButton.setSize(120, 50);
        backButton.setPosition(20, Gdx.graphics.getHeight() - 70);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameMenu(game));
            }
        });
        stage.addActor(backButton);

        updateScene();
    }

    private void loadStoryData(String... data) {
        if (data.length % 4 != 0) {
            throw new IllegalArgumentException("Each entry must have exactly 4 values: Character, Dialogue, Background, TextColor.");
        }

        int length = data.length / 4;
        storyData = new String[length][4];
        for (int i = 0; i < length; i++) {
            storyData[i][0] = data[i * 4];     // Character name
            storyData[i][1] = data[i * 4 + 1]; // Dialogue text
            storyData[i][2] = data[i * 4 + 2]; // Background
            storyData[i][3] = data[i * 4 + 3]; // Text color (Hex)
        }
    }

    private void updateScene() {
        if (dialogueIndex >= storyData.length) {
            if (nextSceneCallback != null) {
                nextSceneCallback.run();
            } else {
                game.startGameMenu();
            }
            return;
        }

        if (dialogueBox != null) {
            dialogueBox.dispose();
        }

        String backgroundPath = "Backgrounds/" + storyData[dialogueIndex][2] + ".png";
        if (Gdx.files.internal(backgroundPath).exists()) {
            backgroundImage = new Texture(Gdx.files.internal(backgroundPath));
        } else {
            Gdx.app.log("ERROR", "Missing background file: " + backgroundPath);
            backgroundImage = new Texture(Gdx.files.internal("Backgrounds/default.png"));
        }

        dialogueBox = new BorderedTextBox(
            "testBorder",
            false,
            storyData[dialogueIndex][0],
            storyData[dialogueIndex][1],
            250,
            1800,
            storyData[dialogueIndex][3]
        );
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        if (backgroundImage != null) {
            spriteBatch.draw(backgroundImage, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        }

        if (dialogueBox != null) {
            dialogueBox.render(spriteBatch, delta);
        }
        spriteBatch.end();

        stage.act(delta);
        stage.draw();

        if (dialogueBox != null && dialogueBox.shouldAdvance()) {
            dialogueBox.resetAdvance();
            dialogueIndex++;
            updateScene();
        }
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
        if (spriteBatch != null) spriteBatch.dispose();
        if (backgroundImage != null) backgroundImage.dispose();
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (dialogueBox != null) dialogueBox.dispose();
    }
}
