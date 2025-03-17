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
    private final GameTransitions game;
    private SpriteBatch batch;
    private Texture backgroundImage;
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private TextButton backButton;
    private BorderedTextBox dialogueBox;

    private String[][] storyData;
    private int dialogueIndex = 0;

    public VortexMain(GameTransitions game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
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
                game.create();
            }
        });
        stage.addActor(backButton);

        // Define the story using the easy format (Character, Dialogue, Background, Text Color)
        story(
            "Nova", "Hmm... doesn't seem to work well..?", "Lab", "#FFFFFF",
            "Nova", "Maybe I should try another approach...", "Lab", "#FFFFFF",
            "AI", "Analyzing... please wait.", "Lab", "#00FF00",
            "Nova", "Alright, let’s see what’s next.", "Umbra_CharViewBackground", "#FFFFFF"
        );
    }

    public void story(String... data) {
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
        dialogueIndex = 0;
        updateScene();
    }

    private void updateScene() {
        if (dialogueIndex < storyData.length) {
            if (dialogueBox != null) {
                dialogueBox.dispose();
            }
            backgroundImage = new Texture(Gdx.files.internal("Backgrounds/" + storyData[dialogueIndex][2] + ".png"));
            dialogueBox = new BorderedTextBox(
                "testBorder",
                false,
                storyData[dialogueIndex][0],  // Speaker
                storyData[dialogueIndex][1],  // Dialogue
                200,                          // Box height
                1500,                         // Box width
                storyData[dialogueIndex][3]   // Text color (Hex)
            );
        } else {
            dialogueBox = null;
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        if (backgroundImage != null) {
            batch.draw(backgroundImage, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        }
        batch.end();

        if (dialogueBox != null) {
            dialogueBox.render(delta);
        }

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
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
        batch.dispose();
        if (backgroundImage != null) backgroundImage.dispose();
        stage.dispose();
        skin.dispose();
        if (dialogueBox != null) dialogueBox.dispose();
    }
}
