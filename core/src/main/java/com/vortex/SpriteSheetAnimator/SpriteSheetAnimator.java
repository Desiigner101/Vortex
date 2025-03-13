package com.vortex.SpriteSheetAnimator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;

public class SpriteSheetAnimator implements Animatable {
    private Animation<TextureRegion> animation;
    private float stateTime;
    private Texture spriteSheet;
    private TextureRegion[] animationFrames; // Store frames here

    public SpriteSheetAnimator(String spriteSheetPath, int frameCols, int frameRows, float frameDuration) {
        spriteSheet = new Texture(Gdx.files.internal(spriteSheetPath));

        // Split the sprite sheet into frames
        TextureRegion[][] tmpFrames = TextureRegion.split(spriteSheet,
            64,64
        );

        animationFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int row = 0; row < frameRows; row++) {
            for (int col = 0; col < frameCols; col++) {
                animationFrames[index++] = tmpFrames[row][col];
            }
        }

        animation = new Animation<>(frameDuration, animationFrames);
        stateTime = 0f;
    }

    @Override
    public TextureRegion getCurrentFrame(float deltaTime) {
        stateTime += deltaTime;
        return animation.getKeyFrame(stateTime, true);
    }

    public TextureRegion[] getFrames() { // Add this method
        return animationFrames;
    }

    @Override
    public void dispose() {
        spriteSheet.dispose();
    }
}
