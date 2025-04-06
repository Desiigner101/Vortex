package com.vortex.game.BattleClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.vortex.SFX.PlayAudio;

public class SkyLeviathan implements EnemyClass {
    // Stats
    private int HP = 1200;
    private int maxHP = 1200;
    private int Atk = 100;
    private String name = "Sky Leviathan";
    private boolean isBoss = true;

    // Animation
    private static final int FRAME_COUNT = 31;
    private static final float FRAME_DURATION = 0.1f;
    private Animation<TextureRegion> idleAnimation;
    private float stateTime = 0;
    private Animation<TextureRegion> currentAnimation;

    public SkyLeviathan() {
        loadAnimations();
        stateTime = 0;
        currentAnimation = idleAnimation;
    }

    private void loadAnimations() {
        Array<TextureRegion> idleFrames = new Array<TextureRegion>();

        for (int i = 1; i <= FRAME_COUNT; i++) {
            String framePath = String.format("Enemies/SkyLeviathanFrames/SkyLeviathan%d.png", i);
            try {
                Texture frameTexture = new Texture(Gdx.files.internal(framePath));
                idleFrames.add(new TextureRegion(frameTexture));
            } catch (GdxRuntimeException e) {
                Gdx.app.error("SkyLeviathan", "Failed to load frame: " + framePath, e);
                // Fallback - create an empty texture if loading fails
                Texture fallbackTexture = new Texture(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
                idleFrames.add(new TextureRegion(fallbackTexture));
            }
        }

        idleAnimation = new Animation<>(FRAME_DURATION, idleFrames);
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void update(float delta) {
        stateTime += delta;
    }

    @Override
    public TextureRegion getCurrentFrame() {
        return idleAnimation.getKeyFrame(stateTime, true);
    }

    // Animation control methods
    public void playIdleAnimation() {
        currentAnimation = idleAnimation;
        stateTime = 0;
    }

    @Override
    public int getHP() {
        return HP;
    }

    @Override
    public boolean isBoss() {
        return isBoss;
    }

    @Override
    public int getMaxHP() {
        return maxHP;
    }

    @Override
    public void setHP(int HP) {
        this.HP = Math.max(0, Math.min(HP, maxHP));
    }

    @Override
    public int getAtk() {
        return Atk;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIdleAnimation() {
        return "Enemies/SkyLeviathan1.png"; // Returns path to first frame
    }

    @Override
    public void dispose() {
        // Dispose all textures in the animation
        for (TextureRegion frame : idleAnimation.getKeyFrames()) {
            frame.getTexture().dispose();
        }
    }
}
