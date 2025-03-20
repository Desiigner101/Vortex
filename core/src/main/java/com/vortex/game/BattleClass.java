package com.vortex.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import java.util.List;

public class BattleClass implements Screen {
    private final GameTransitions game;
    private SpriteBatch spriteBatch;
    private Viewport viewport;
    private List<Texture> backgroundLayers;

    public BattleClass(GameTransitions game, String... backgroundImages) {
        this.game = game;
        backgroundLayers = new ArrayList<>();

        for (String image : backgroundImages) {
            backgroundLayers.add(new Texture(Gdx.files.internal("Backgrounds/" + image)));
        }
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(1600, 900);
        viewport.apply();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        for (Texture texture : backgroundLayers) {
            spriteBatch.draw(texture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        }
        spriteBatch.end();
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
        spriteBatch.dispose();
        for (Texture texture : backgroundLayers) {
            texture.dispose();
        }
    }
}

