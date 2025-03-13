package com.vortex.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class VortexMain extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private Viewport viewport;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("Pictures/nova'sLab.jpg");
        viewport = new StretchViewport(image.getWidth(), image.getHeight());
        viewport.apply();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(image, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();
    }


    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }


}
