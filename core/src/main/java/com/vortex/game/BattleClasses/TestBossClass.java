package com.vortex.game.BattleClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.vortex.SFX.PlayAudio;

public class TestBossClass implements EnemyClass {
    // Stats
    private int HP = 4000;
    private int maxHP = 4000;
    private int Atk = 100 ;
    private String name = "Moking The Great";
    private boolean isBoss = true;
    // Animation
    private final String idleAnimationPath = "Enemies/TestBoss.png";
    private Texture idleSheet;
    private Animation<TextureRegion> idleAnimation;
    private float stateTime = 0;
    private Animation<TextureRegion> currentAnimation;

    // Animation parameters
    private static final int IDLE_FRAME_COUNT = 15;
    private static final float IDLE_FRAME_DURATION = 0.1f; // 0.1s per frame = 1.5s total animation

    public TestBossClass() {
        loadAnimations();
        stateTime = 0;
        currentAnimation = idleAnimation;
    }

    private void loadAnimations() {
        Texture idleSheet = new Texture(Gdx.files.internal("Enemies/TestBoss.png"));
        TextureRegion[][] frames = TextureRegion.split(idleSheet,
            idleSheet.getWidth()/15, // 15 frames wide
            idleSheet.getHeight());

        TextureRegion[] idleFrames = new TextureRegion[15];
        System.arraycopy(frames[0], 0, idleFrames, 0, 15);

        idleAnimation = new Animation<>(0.1f, idleFrames);
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
        return idleAnimationPath;
    }

    @Override
    public void dispose() {
        idleAnimation.getKeyFrames()[0].getTexture().dispose();
    }
}
