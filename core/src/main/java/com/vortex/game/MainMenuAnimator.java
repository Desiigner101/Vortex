package com.vortex.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Gdx;

public class MainMenuAnimator {
    private SpriteBatch batch;
    private AssetManager assetManager;
    private Array<Texture> frames;
    private int frameIndex = 0;
    private float elapsedTime = 0f;

    private OrthographicCamera camera;
    private Viewport viewport;

    private static final float FRAME_DURATION = 0.1f; // 50ms per frame
    private static final int TOTAL_FRAMES = 49; // ✅ 49 frames, starting from MainMenu0

    public MainMenuAnimator() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        frames = new Array<>();

        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        // ✅ Load images from MainMenuFrames directory, starting from MainMenu0
        for (int i = 0; i < TOTAL_FRAMES; i++) {
            String fileName = "MainMenuFrames/MainMenu" + i + ".png";
            assetManager.load(fileName, Texture.class);
        }
        assetManager.finishLoading();

        for (int i = 0; i < TOTAL_FRAMES; i++) {
            String fileName = "MainMenuFrames/MainMenu" + i + ".png";
            frames.add(assetManager.get(fileName, Texture.class));
        }
    }

    public void update(float delta) {
        elapsedTime += delta;

        if (elapsedTime > FRAME_DURATION) {
            frameIndex = (frameIndex + 1) % TOTAL_FRAMES; // ✅ Loop animation
            elapsedTime = 0;
        }
    }

    public void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        Texture currentFrame = frames.get(frameIndex);
        batch.draw(currentFrame, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        batch.end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        batch.dispose();
        assetManager.dispose();
    }
}
