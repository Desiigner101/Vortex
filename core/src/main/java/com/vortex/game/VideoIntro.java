package com.vortex.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class VideoIntro extends ScreenAdapter {
    private SpriteBatch batch;
    private AssetManager assetManager;
    private Array<Texture> frames;
    private int frameIndex = 0;
    private float elapsedTime = 0f;
    private final GameTransitions game;

    private OrthographicCamera camera;
    private Viewport viewport;

    public VideoIntro(GameTransitions game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        frames = new Array<>();

        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera); // Adjust resolution as needed
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        // Load images as frames manually
        for (int i = 1; i <= 86; i++) {
            String fileName = "VideoIntro/ezgif-frame-" + String.format("%03d", i) + ".png";
            assetManager.load(fileName, Texture.class);
        }

        assetManager.finishLoading();

        for (int i = 1; i <= 86; i++) {
            String fileName = "VideoIntro/ezgif-frame-" + String.format("%03d", i) + ".png";
            frames.add(assetManager.get(fileName, Texture.class));
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        elapsedTime += delta;

        if (elapsedTime > 0.05f) {
            frameIndex++;
            elapsedTime = 0;
        }

        if (frameIndex >= frames.size) {
            game.setScreen(new GameMenu(game));
            return;
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        Texture currentFrame = frames.get(frameIndex);

        float aspectRatio = (float) currentFrame.getWidth() / currentFrame.getHeight();
        float width = viewport.getWorldWidth();
        float height = width / aspectRatio;

        if (height > viewport.getWorldHeight()) {
            height = viewport.getWorldHeight();
            width = height * aspectRatio;
        }

        float x = (viewport.getWorldWidth() - width) / 2;
        float y = (viewport.getWorldHeight() - height) / 2;

        batch.draw(currentFrame, x, y, width, height);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        assetManager.dispose();
    }
}
