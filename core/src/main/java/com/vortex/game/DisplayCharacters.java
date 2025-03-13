package com.vortex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.vortex.Images.ImageHandler;

public class DisplayCharacters extends ScreenAdapter {
    private final GameTransitions game;
    private SpriteBatch batch;
    private ImageHandler novaImage, umbraImage, jinaImage;
    private Vector2 umbraPosition, novaPosition, jinaPosition;
    private Stage stage;
    private Skin skin;
    private TextButton backButton;

    private float screenWidth, screenHeight;
    private float characterWidth = 128, characterHeight = 128; // Character size

    public DisplayCharacters(GameTransitions game) {
        this.game = game;
        initialize();
    }

    private void initialize() {
        batch = new SpriteBatch();

        // ✅ Load images (Jina uses Umbra as a placeholder)
        novaImage = new ImageHandler("Pictures/Nova/CharacterView/Nova_CharView.png");
        umbraImage = new ImageHandler("Pictures/Umbra/CharacterView/Umbra_CharView.png");
        jinaImage = new ImageHandler("Pictures/Umbra/CharacterView/Umbra_CharView.png"); // Placeholder for Jina

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        // ✅ Properly spacing characters (Nova - Left, Umbra - Center, Jina - Right)
        float spacing = screenWidth * 0.2f; // Adjusted spacing
        float centerX = screenWidth * 0.5f;

        novaPosition = new Vector2(centerX - spacing - characterWidth, screenHeight * 0.5f - characterHeight / 2);
        umbraPosition = new Vector2(centerX - characterWidth / 2, screenHeight * 0.5f - characterHeight / 2);
        jinaPosition = new Vector2(centerX + spacing, screenHeight * 0.5f - characterHeight / 2);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        createBackButton();
    }

    private void createBackButton() {
        backButton = new TextButton("Back", skin);
        backButton.setSize(100, 50);
        backButton.setPosition(20, 20);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.create();
            }
        });
        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        novaImage.render(batch, novaPosition.x, novaPosition.y, characterWidth, characterHeight);
        umbraImage.render(batch, umbraPosition.x, umbraPosition.y, characterWidth, characterHeight);
        jinaImage.render(batch, jinaPosition.x, jinaPosition.y, characterWidth, characterHeight);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        novaImage.dispose();
        umbraImage.dispose();
        jinaImage.dispose();
        stage.dispose();
        skin.dispose();
    }
}
