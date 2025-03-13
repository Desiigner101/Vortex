package com.vortex.SpriteSheetAnimator;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface Animatable {
    TextureRegion getCurrentFrame(float deltaTime);
    void dispose();
}
