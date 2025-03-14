package com.vortex.SpriteSheetAnimator;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class SpriteSheetAnimator implements Disposable {
    private TextureRegion[] frames;
    private int frameCount;
    private int currentFrameIndex;
    private float frameDuration;
    private float elapsedTime;

    public SpriteSheetAnimator(String spriteSheetPath, int cols, int rows, float frameDuration) {
        this.frameDuration = frameDuration;
        this.elapsedTime = 0;

        // Load the sprite sheet and split it into frames
        TextureAtlas atlas = new TextureAtlas(spriteSheetPath);
        int frameWidth = atlas.getRegions().get(0).getRegionWidth();
        int frameHeight = atlas.getRegions().get(0).getRegionHeight();

        frames = new TextureRegion[cols * rows];
        frameCount = frames.length;

        // Populate the frames array
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                frames[row * cols + col] = new TextureRegion(atlas.getRegions().get(row * cols + col).getTexture(), frameWidth, frameHeight);
            }
        }
    }

    // Get the current frame of the animation
    public TextureRegion getCurrentFrame(float deltaTime) {
        elapsedTime += deltaTime;

        if (elapsedTime >= frameDuration) {
            elapsedTime -= frameDuration;
            currentFrameIndex = (currentFrameIndex + 1) % frameCount; // Loop the frames
        }

        return frames[currentFrameIndex];
    }

    // Get all frames in the sprite sheet
    public TextureRegion[] getFrames() {
        return frames;
    }

    // Dispose the resources
    @Override
    public void dispose() {
        for (TextureRegion frame : frames) {
            frame.getTexture().dispose();
        }
    }
}
