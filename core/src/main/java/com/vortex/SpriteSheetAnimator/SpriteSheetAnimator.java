package com.vortex.SpriteSheetAnimator;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.Gdx;

public class SpriteSheetAnimator implements Disposable {
    private TextureRegion[] frames;
    private int currentFrameIndex;
    private float frameDuration;
    private float elapsedTime;

    // Constructor simplified to load sprite sheet and split it into frames
    public SpriteSheetAnimator(String spriteSheetPath, int cols, int rows, float frameDuration) {
        this.frameDuration = frameDuration;
        this.elapsedTime = 0;

        // Load the sprite sheet and split it into frames
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(spriteSheetPath));
        int frameWidth = atlas.getRegions().get(0).getRegionWidth();
        int frameHeight = atlas.getRegions().get(0).getRegionHeight();

        frames = new TextureRegion[cols * rows];

        // Populate the frames array with TextureRegions
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                frames[row * cols + col] = new TextureRegion(atlas.getRegions().get(row * cols + col));
            }
        }
    }

    // Get the current frame of the animation
    public TextureRegion getCurrentFrame(float deltaTime) {
        elapsedTime += deltaTime;

        if (elapsedTime >= frameDuration) {
            elapsedTime -= frameDuration;
            currentFrameIndex = (currentFrameIndex + 1) % frames.length; // Loop the frames
        }

        return frames[currentFrameIndex];
    }

    // Dispose the resources
    @Override
    public void dispose() {
        for (TextureRegion frame : frames) {
            frame.getTexture().dispose();  // Dispose the texture associated with the frame
        }
    }
}
