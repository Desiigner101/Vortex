package com.vortex.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.Input.Keys;
import com.vortex.SFX.PlayAudio;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class StoryScene implements Screen {
    private final GameTransitions game;
    private SpriteBatch spriteBatch;
    private Texture backgroundImage;
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private BorderedTextBox dialogueBox;
    private Runnable nextSceneCallback;
    private List<Texture> texturesToDispose = new ArrayList<>();
    private PlayAudio sfx = new PlayAudio();
    private String[][] storyData;
    private int dialogueIndex = 0;

    // Character image management
    private Texture currentSpeakerImage;
    private String currentSpeakerKey = "";
    private final Map<String, Texture> characterImages = new HashMap<>();

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

        stage = new Stage(viewport) {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Keys.ENTER) {
                    skipToNextScene();
                    return true;
                }
                return super.keyDown(keyCode);
            }
        };
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();

        // Initialize skip button
        Texture upTexture = new Texture(Gdx.files.internal("UI/SKIP_BUTTON.png"));
        Texture downTexture = new Texture(Gdx.files.internal("UI/SKIP_BUTTON_PRESSED.png"));
        Texture overTexture = new Texture(Gdx.files.internal("UI/SKIP_BUTTON_HOVER.png"));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(upTexture));
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(downTexture));
        buttonStyle.over = new TextureRegionDrawable(new TextureRegion(overTexture));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Poppins-SemiBold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 24;
        params.color = new Color(0.8f, 0.9f, 1f, 1f);
        params.borderWidth = 1.5f;
        params.borderColor = new Color(0.2f, 0.4f, 0.8f, 0.8f);
        buttonStyle.font = generator.generateFont(params);
        generator.dispose();

        skin.add("default", buttonStyle);

        final TextButton skipButton = new TextButton("SKIP", skin);
        skipButton.setSize(180, 60);
        skipButton.setPosition(Gdx.graphics.getWidth() - (-80), 280);
        skipButton.getLabel().setFontScale(1.0f);

        skipButton.addListener(new ChangeListener() {
            private boolean wasOver = false;

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (skipButton.isPressed()) {
                    sfx.playSoundEffect("whenTextIsClicked.wav",0);
                }
                if (skipButton.isOver() && !wasOver) {
                    sfx.playSoundEffect("hover_button.wav",0);
                }
                wasOver = skipButton.isOver();
            }
        });

        skipButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                skipToNextScene();
            }
        });

        skipButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (skipButton.isOver()) {
                    skipButton.setTransform(true);
                    skipButton.setScale(1.02f);
                    skipButton.setColor(1.1f, 1.1f, 1.1f, 1f);
                } else {
                    skipButton.setScale(1f);
                    skipButton.setColor(Color.WHITE);
                }
            }
        });

        stage.addActor(skipButton);
        texturesToDispose.add(upTexture);
        texturesToDispose.add(downTexture);
        texturesToDispose.add(overTexture);

        updateScene();
    }

    private void skipToNextScene() {
        sfx.playSoundEffect("whenTextIsClicked.wav", 0);
        if (nextSceneCallback != null) {
            nextSceneCallback.run();
        }
    }

    private void loadStoryData(String... data) {
        if (data.length % 4 != 0) {
            throw new IllegalArgumentException("Each entry must have exactly 4 values: Character, Dialogue, Background, TextColor.");
        }

        int length = data.length / 4;
        storyData = new String[length][4];
        for (int i = 0; i < length; i++) {
            storyData[i][0] = data[i * 4];
            storyData[i][1] = data[i * 4 + 1];
            storyData[i][2] = data[i * 4 + 2];
            storyData[i][3] = data[i * 4 + 3];
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

        // Load background
        String backgroundPath = "Backgrounds/" + storyData[dialogueIndex][2] + ".png";
        backgroundImage = new Texture(Gdx.files.internal(
            Gdx.files.internal(backgroundPath).exists() ?
                backgroundPath : "Backgrounds/default.png"
        ));

        // Set current speaker
        String speakerKey = storyData[dialogueIndex][0].toLowerCase().trim(); // Added trim()

        // Clear current speaker if empty or unknown
        if (speakerKey.isEmpty() || !isKnownCharacter(speakerKey)) {
            if (currentSpeakerImage != null && !characterImages.containsValue(currentSpeakerImage)) {
                currentSpeakerImage.dispose();
            }
            currentSpeakerImage = null;
            currentSpeakerKey = "";
        }
        else if (!speakerKey.equals(currentSpeakerKey)) {
            if (currentSpeakerImage != null && !characterImages.containsValue(currentSpeakerImage)) {
                currentSpeakerImage.dispose();
            }

            if (!characterImages.containsKey(speakerKey)) {
                Texture newImage = loadCharacterImage(speakerKey);
                if (newImage != null) {
                    characterImages.put(speakerKey, newImage);
                    texturesToDispose.add(newImage);
                }
            }

            currentSpeakerImage = characterImages.get(speakerKey);
            currentSpeakerKey = speakerKey;
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
    private boolean isKnownCharacter(String characterKey) {
        return characterKey.equals("nova") ||
            characterKey.equals("jina") ||
            characterKey.equals("umbra");
    }
    private Texture loadCharacterImage(String characterKey) {
        if (!isKnownCharacter(characterKey)) {
            return null; // Return null for unknown characters
        }

        switch (characterKey) {
            case "nova":
                return new Texture("Pictures/Nova/CharacterView/Nova_CharView.png");
            case "jina":
                return new Texture("Pictures/Jina/CharacterView/Jina_CharView.png");
            case "umbra":
                return new Texture("Pictures/Umbra/CharacterView/Umbra_CharView.png");
            default:
                return null;
        }
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

        // Draw character image (left side, centered vertically)
        if (currentSpeakerImage != null) {
            float charWidth = 590;
            float charHeight = 850;
            float charX = 100;
            float charY = (viewport.getWorldHeight() - charHeight) / 2-200;
            spriteBatch.draw(currentSpeakerImage, charX, charY, charWidth, charHeight);
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

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (spriteBatch != null) spriteBatch.dispose();
        if (backgroundImage != null) backgroundImage.dispose();
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (dialogueBox != null) dialogueBox.dispose();

        for (Texture texture : texturesToDispose) {
            if (texture != null) texture.dispose();
        }

        for (Texture tex : characterImages.values()) {
            if (tex != null) tex.dispose();
        }
        if (currentSpeakerImage != null && !characterImages.containsValue(currentSpeakerImage)) {
            currentSpeakerImage.dispose();
        }
    }
}
