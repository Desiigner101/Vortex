package com.vortex.CharacterStats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Character_Umbra implements Character_BattleStats {
    // Stats
    private int HP = 800;
    private int basicAttackDamage = 80;
    private int skillDamage = 230;
    private int skillCost = 3;
    private int ultimateDamage = 300;
    private int ultCooldown = 1;
    private int currentUltCooldown = 0;

    // File paths
    private String basicAtkImage = "Pictures/Umbra/CharacterView/Umbra_ShadowStrike.png";
    private String skillImage = "Pictures/Umbra/CharacterView/Umbra_DistractingIllusions.png";
    private String ultImage = "Pictures/Umbra/CharacterView/Umbra_VeilOfShadows.png";

    // Animation paths
    private final String idleAnimationPath = "Pictures/Umbra/BattleView/umbra_idle_battle.png";
    private final String basicAtkAnimationPath = "Pictures/Umbra/BattleView/umbra_basicAtk_battle.png";
    private final String skillAnimationPath = "Pictures/Umbra/BattleView/umbra_skill_battle.png";
    private final String hitAnimationPath = "Pictures/Umbra/BattleView/umbra_hit_battle.png";
    private final String ultAnimationBasePath = "Pictures/Umbra/BattleView/UmbraUltFrames/umbra_ult_battle";

    // Animation textures
    private Texture idleSheet, basicAtkSheet, skillSheet, hitSheet;
    private Texture[] ultFrames;
    private Animation<TextureRegion> idleAnimation, basicAtkAnimation, skillAnimation, hitAnimation, ultAnimation;
    private float stateTime;
    private Animation<TextureRegion> currentAnimation;

    public Character_Umbra() {
        loadAnimations();
        loadUltimateAnimation();
        stateTime = 0;
        currentAnimation = idleAnimation;
    }
    private void loadAnimations() {
        // Load standard animations
        idleSheet = new Texture(Gdx.files.internal(idleAnimationPath));
        basicAtkSheet = new Texture(Gdx.files.internal(basicAtkAnimationPath));
        skillSheet = new Texture(Gdx.files.internal(skillAnimationPath));
        hitSheet = new Texture(Gdx.files.internal(hitAnimationPath));

        // Load ultimate frames (29 frames)
        int totalUltFrames = 29;
        ultFrames = new Texture[totalUltFrames];
        TextureRegion[] ultRegions = new TextureRegion[totalUltFrames];

        for (int i = 0; i < totalUltFrames; i++) {
            String framePath = ultAnimationBasePath + (i + 1) + ".png";
            ultFrames[i] = new Texture(Gdx.files.internal(framePath));
            ultRegions[i] = new TextureRegion(ultFrames[i]);
        }


        // Create animations
        idleAnimation = createAnimation(idleSheet, 64, 64, 7, 1, 0.1f);
        basicAtkAnimation = createAnimation(basicAtkSheet, 64, 64, 12, 1, 0.1f);
        skillAnimation = createAnimation(skillSheet, 64, 64, 12, 1, 0.1f);
        hitAnimation = createAnimation(hitSheet, 64, 64, 6, 1, 0.15f);
        ultAnimation = new Animation<>(0.2f, ultRegions);

        // Set loop modes
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
        basicAtkAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        skillAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        hitAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        ultAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    private Animation<TextureRegion> createAnimation(Texture sheet, int frameWidth, int frameHeight,
                                                     int cols, int rows, float frameDuration) {
        TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return new Animation<>(frameDuration, frames);
    }

    private Animation<TextureRegion> fullscreenUltAnimation;
    private float ultStateTime = 0;
    private boolean isUltimatePlaying = false;



    private void loadUltimateAnimation() {
        int totalUltFrames = 29;
        TextureRegion[] ultRegions = new TextureRegion[totalUltFrames];

        for (int i = 0; i < totalUltFrames; i++) {
            String framePath = ultAnimationBasePath + (i + 1) + ".png";
            Texture frame = new Texture(Gdx.files.internal(framePath));
            frame.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            ultRegions[i] = new TextureRegion(frame);
        }

        fullscreenUltAnimation = new Animation<>(0.09f, ultRegions);
        fullscreenUltAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    public void startUltimate() {
        isUltimatePlaying = true;
        ultStateTime = 0;
    }

    public TextureRegion getCurrentUltimateFrame() {
        return fullscreenUltAnimation.getKeyFrame(ultStateTime, false);
    }

    public boolean isUltimatePlaying() {
        return isUltimatePlaying && !fullscreenUltAnimation.isAnimationFinished(ultStateTime);
    }

    public void updateUltimate(float delta) {
        if (isUltimatePlaying) {
            ultStateTime += delta;
            if (fullscreenUltAnimation.isAnimationFinished(ultStateTime)) {
                isUltimatePlaying = false;
            }
        }
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        if (currentAnimation != idleAnimation && currentAnimation.isAnimationFinished(stateTime)) {
            setAnimation(idleAnimation);
        }
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.currentAnimation = animation;
        this.stateTime = 0;
    }

    public TextureRegion getCurrentFrame() {
        return currentAnimation.getKeyFrame(stateTime, false);
    }

    // Animation control methods
    public void playBasicAttackAnimation() {
        setAnimation(basicAtkAnimation);
    }

    public void playSkillAnimation() {
        setAnimation(skillAnimation);
    }

    public void playHitAnimation() {
        setAnimation(hitAnimation);
    }

    public void playUltimateAnimation() {
        setAnimation(ultAnimation);
    }

    public void dispose() {
        idleSheet.dispose();
        basicAtkSheet.dispose();
        skillSheet.dispose();
        hitSheet.dispose();

        // Dispose ultimate frames
        for (Texture frame : ultFrames) {
            if (frame != null) {
                frame.dispose();
            }
        }
    }

    @Override
    public int getHP() { return HP; }

    @Override
    public void setHP(int HP) {
        this.HP = Math.max(0, Math.min(HP, getMaxHP()));
    }

    @Override
    public int getMaxHP() { return 800; }

    @Override
    public void takeDamage(int damage) {
        setHP(this.HP - damage);
        playHitAnimation();
    }

    @Override
    public int getBasicAttackDamage() { return basicAttackDamage; }

    @Override
    public int getSkillDamage() { return skillDamage; }

    @Override
    public int getSkillCost() { return skillCost; }

    @Override
    public int getUltimateDamage() { return ultimateDamage; }

    @Override
    public int getUltCooldown() { return ultCooldown; }

    @Override
    public String getBasicAtkImage() { return basicAtkImage; }

    @Override
    public String getSkillImage() { return skillImage; }

    @Override
    public String getUltImage() { return ultImage; }

    @Override
    public String getSkillAnimation() { return skillAnimationPath; }

    @Override
    public String getHitAnimation() { return hitAnimationPath; }

    @Override
    public String getUltAnimation() { return ultAnimationBasePath; }

    @Override
    public Animation<TextureRegion> getIdleAnimation() { return idleAnimation; }

    @Override
    public String getBasicAtkAnimation() { return basicAtkAnimationPath; }

    @Override
    public void startUltimateCooldown() { currentUltCooldown = ultCooldown; }

    @Override
    public void reduceCooldowns() {
        if (currentUltCooldown > 0) currentUltCooldown--;
    }
    //for ultimate animations
    @Override
    public boolean isUltimateReady() { return currentUltCooldown == 0; }

    public TextureRegion getUltimateFirstFrame() {
        return ultAnimation.getKeyFrame(0, false);
    }

    public float getUltimateAnimationProgress() {
        return stateTime / ultAnimation.getAnimationDuration();
    }

    public boolean isUltimateFinished() {
        return ultAnimation.isAnimationFinished(stateTime);
    }
}
