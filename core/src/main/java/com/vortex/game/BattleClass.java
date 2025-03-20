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
    private SpriteBatch spriteBatch;
    private BitmapFont bitmapFont;
    private Stage stage;
    private Skin skin;
    private TextButton attackButton, skillButton, ultButton, inventoryButton, pauseButton;
    private Viewport viewport;
    private Texture backgroundTexture;
    private Texture roadTileTexture;

    private List<String> characters;
    private int currentTurn = 0;
    private int skillPoints = 3;

    private String universeName;
    private final int PLATFORM_Y = 200;
    private final int TILE_SIZE = 16;

    public BattleClass(String universeName, boolean hasUmbra, boolean hasNova, boolean hasJina, String background, String roadTile) {
        this.universeName = universeName;
        spriteBatch = new SpriteBatch();
        bitmapFont = new BitmapFont();
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        viewport = new FitViewport(1600, 900);
        viewport.apply();

        Gdx.input.setInputProcessor(stage);

        // Load Background and Platform Tile
        backgroundTexture = new Texture(Gdx.files.internal("Backgrounds/" + background));
        roadTileTexture = new Texture(Gdx.files.internal("Tiles/" + roadTile));

        // Add Characters Dynamically
        characters = new ArrayList<>();
        if (hasUmbra) characters.add("Umbra");
        if (hasNova) characters.add("Nova");
        if (hasJina) characters.add("Jina");

        // Buttons
        attackButton = new TextButton("Basic Attack", skin);
        skillButton = new TextButton("Skill (2 SP)", skin);
        ultButton = new TextButton("Ult (5 Turns CD)", skin);
        inventoryButton = new TextButton("Inventory", skin);
        pauseButton = new TextButton("Pause", skin);

        // Position Buttons
        attackButton.setPosition(100, PLATFORM_Y - 60);
        skillButton.setPosition(100, PLATFORM_Y - 110);
        ultButton.setPosition(100, PLATFORM_Y - 160);
        inventoryButton.setPosition(1050, 50);
        pauseButton.setPosition(1400, 750);

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
        currentTurn = (currentTurn + 1) % characters.size();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();

        // Draw Background
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        // Draw Universe Name & Boss Info (Centered)
        bitmapFont.draw(spriteBatch, universeName, 750, 850); // Universe Name
        bitmapFont.draw(spriteBatch, "Boss Name", 750, 550);
        bitmapFont.draw(spriteBatch, "[ BOSS ]", 780, 500);

        // Draw Black Background Below Platform
        spriteBatch.setColor(0, 0, 0, 1);
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), PLATFORM_Y);
        spriteBatch.setColor(1, 1, 1, 1); // Reset color

        // Draw Platform (Spanning Full Width)
        int tilesAcross = (int) Math.ceil(viewport.getWorldWidth() / (TILE_SIZE * 4));
        for (int i = 0; i < tilesAcross; i++) {
            spriteBatch.draw(roadTileTexture, i * (TILE_SIZE * 4), PLATFORM_Y, TILE_SIZE * 4, TILE_SIZE * 4);
        }

        // Draw Characters on the Platform (Centered)
        int startX = 650;
        int spacing = 200;

        for (int i = 0; i < characters.size(); i++) {
            int xPos = startX + (i * spacing);
            bitmapFont.draw(spriteBatch, characters.get(i), xPos, PLATFORM_Y + 60);
            bitmapFont.draw(spriteBatch, "HP: [|||||]", xPos, PLATFORM_Y + 40);
            if (i == currentTurn) {
                bitmapFont.draw(spriteBatch, "▼", xPos + 20, PLATFORM_Y + 80); // Turn Indicator
            }
        }

        // Draw Skill Points
        bitmapFont.draw(spriteBatch, "Skill Points: " + skillPoints, 750, 150);

        spriteBatch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        bitmapFont.dispose();
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        roadTileTexture.dispose();
    }

    // Unused methods
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
