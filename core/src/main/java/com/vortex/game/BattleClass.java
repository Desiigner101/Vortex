package com.vortex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import java.util.List;

public class BattleClass implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage;
    private Skin skin;
    private TextButton attackButton, skillButton, ultButton, inventoryButton, pauseButton;
    private Viewport viewport;
    private List<Texture> backgroundLayers;

    private String[] characters = {"Umbra", "Nova", "Jina"};
    private int currentTurn = 2; // Jina's turn (index 2)
    private int skillPoints = 3;

    public BattleClass(String... backgroundImages) {
        batch = new SpriteBatch();
        font = new BitmapFont();
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        viewport = new FitViewport(1600, 900);
        viewport.apply();

        Gdx.input.setInputProcessor(stage);

        backgroundLayers = new ArrayList<>();
        for (String image : backgroundImages) {
            backgroundLayers.add(new Texture(Gdx.files.internal("Backgrounds/" + image)));
        }

        // Buttons
        attackButton = new TextButton("Basic Attack", skin);
        skillButton = new TextButton("Skill (2 SP)", skin);
        ultButton = new TextButton("Ult (5 Turns CD)", skin);
        inventoryButton = new TextButton("Inventory", skin);
        pauseButton = new TextButton("Pause", skin);

        attackButton.setPosition(50, 50);
        skillButton.setPosition(200, 50);
        ultButton.setPosition(350, 50);
        inventoryButton.setPosition(500, 50);
        pauseButton.setPosition(700, 400);

        // Button Listeners
        attackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextTurn();
            }
        });

        stage.addActor(attackButton);
        stage.addActor(skillButton);
        stage.addActor(ultButton);
        stage.addActor(inventoryButton);
        stage.addActor(pauseButton);
    }

    private void nextTurn() {
        currentTurn = (currentTurn + 1) % characters.length;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        // Draw Background
        for (Texture texture : backgroundLayers) {
            batch.draw(texture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        }

        // Draw Boss
        font.draw(batch, "Boss Name", 350, 550);
        font.draw(batch, "[ BOSS ]", 380, 500);

        // Draw Characters & Turn Indicator
        for (int i = 0; i < characters.length; i++) {
            font.draw(batch, characters[i], 200 + (i * 150), 300);
            font.draw(batch, "HP: [|||||]", 200 + (i * 150), 280);
            if (i == currentTurn) {
                font.draw(batch, "▼", 220 + (i * 150), 320); // Turn Indicator
            }
        }

        // Draw Skill Points
        font.draw(batch, "Skill Points: " + skillPoints, 600, 100);

        batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        stage.dispose();
        skin.dispose();
        for (Texture texture : backgroundLayers) {
            texture.dispose();
        }
    }

    // Unused methods
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
