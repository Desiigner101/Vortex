package com.vortex.game.BattleClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.vortex.SFX.PlayAudio;

public class CyberBountyHunter implements EnemyClass {
    // Stats
    private int HP = 600;
    private int maxHP = 600;
    private int Atk = 60;
    private String name = "Cyber Bounty Hunter";
    private boolean isBoss = false;

    // Animation
    private final String idleAnimationPath = "Enemies/CyberBountyHunter.png";
    private Animation<TextureRegion> idleAnimation;
    private float stateTime = 0;
    private Animation<TextureRegion> currentAnimation;

    // Animation parameters
    private static final int IDLE_FRAME_COUNT = 15;
    private static final float IDLE_FRAME_DURATION = 0.1f;

    public CyberBountyHunter() {
        loadAnimations();
        stateTime = 0;
        currentAnimation = idleAnimation;
    }

    private void loadAnimations() {
        Texture idleSheet = new Texture(Gdx.files.internal(idleAnimationPath));
        TextureRegion[][] frames = TextureRegion.split(idleSheet,
            idleSheet.getWidth()/15,
            idleSheet.getHeight());

        TextureRegion[] idleFrames = new TextureRegion[IDLE_FRAME_COUNT];
        System.arraycopy(frames[0], 0, idleFrames, 0, IDLE_FRAME_COUNT);

        idleAnimation = new Animation<>(IDLE_FRAME_DURATION, idleFrames);
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void update(float delta) {
        stateTime += delta;
    }

    @Override
    public TextureRegion getCurrentFrame() {
        return idleAnimation.getKeyFrame(stateTime, true);
    }

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
        return idleAnimationPath;
    }

    @Override
    public void dispose() {
        idleAnimation.getKeyFrames()[0].getTexture().dispose();
    }
}
