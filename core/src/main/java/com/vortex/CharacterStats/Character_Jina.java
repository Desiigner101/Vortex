package com.vortex.CharacterStats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Character_Jina implements Character_BattleStats {
    // Stats
    private int HP = 800;
    private int basicAttackDamage = 68;
    private int skillDamage = 130;
    private int skillCost = 1;
    private int ultimateDamage = 170;
    private int ultCooldown = 6;
    private int currentUltCooldown = 0;

    // Static image paths
    private final String basicAtkImage = "Pictures/Jina/CharacterView/Jina_SledgeStrike.png";
    private final String skillImage = "Pictures/Jina/CharacterView/Jina_PrecisionShot.png";
    private final String ultImage = "Pictures/Jina/CharacterView/Jina_VanguardsResolve.png";

    // Animation paths (corrected from Umbra to Jina)
    private final String idleAnimationPath = "Pictures/Umbra/BattleView/umbra_idle_battle.png";
    private final String basicAtkAnimationPath = "Pictures/Umbra/BattleView/umbra_basicAtk_battle.png";//12 frames
    private final String skillAnimationPath = "Pictures/Umbra/BattleView/umbra_skill_battle.png";//12 frames
    private final String hitAnimationPath = "Pictures/Umbra/BattleView/umbra_hit_battle.png";//6 frames
    private final String ultAnimationPath = "Pictures/Umbra/BattleView/umbra_ult_battle.png";

    // Animation fields
    private Texture idleSheet, basicAtkSheet, skillSheet, hitSheet, ultSheet;
    private Animation<TextureRegion> idleAnimation, basicAtkAnimation, skillAnimation, hitAnimation, ultAnimation;
    private float stateTime;
    private Animation<TextureRegion> currentAnimation;

    public Character_Jina() {
        loadIdleAnimation();
        loadBasicAttackAnimation();
        loadSkillAnimation();
        loadHitAnimation();
        loadUltimateAnimation();
        stateTime = 0f;
        currentAnimation = idleAnimation;
    }

    private void loadIdleAnimation() {
        idleSheet = new Texture(Gdx.files.internal(idleAnimationPath));
        idleAnimation = createAnimation(idleSheet, 64, 64, 7, 1, 0.1f);
    }

    private void loadBasicAttackAnimation() {
        basicAtkSheet = new Texture(Gdx.files.internal(basicAtkAnimationPath));
        basicAtkAnimation = createAnimation(basicAtkSheet, 64, 64, 5, 1, 0.08f);
    }

    private void loadSkillAnimation() {
        skillSheet = new Texture(Gdx.files.internal(skillAnimationPath));
        skillAnimation = createAnimation(skillSheet, 64, 64, 6, 1, 0.1f);
    }

    private void loadHitAnimation() {
        hitSheet = new Texture(Gdx.files.internal(hitAnimationPath));
        hitAnimation = createAnimation(hitSheet, 64, 64, 3, 1, 0.15f);
    }

    private void loadUltimateAnimation() {
        ultSheet = new Texture(Gdx.files.internal(ultAnimationPath));
        ultAnimation = createAnimation(ultSheet, 64, 64, 8, 1, 0.12f);
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

    public void update(float deltaTime) {
        stateTime += deltaTime;
        if (currentAnimation != idleAnimation && currentAnimation.isAnimationFinished(stateTime)) {
            setAnimation(idleAnimation);
        }
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.currentAnimation = animation;
        this.stateTime = 0f;
    }

    public TextureRegion getCurrentFrame() {
        return currentAnimation.getKeyFrame(stateTime, false);
    }

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
        if (idleSheet != null) idleSheet.dispose();
        if (basicAtkSheet != null) basicAtkSheet.dispose();
        if (skillSheet != null) skillSheet.dispose();
        if (hitSheet != null) hitSheet.dispose();
        if (ultSheet != null) ultSheet.dispose();
    }

    // Cooldown management
    public void startUltimateCooldown() {
        currentUltCooldown = ultCooldown;
    }

    public void reduceCooldowns() {
        if (currentUltCooldown > 0) {
            currentUltCooldown--;
        }
    }

    public boolean isUltimateReady() {
        return currentUltCooldown == 0;
    }

    @Override
    public void takeDamage(int damage) {
        this.HP = Math.max(0, this.HP - damage);
        playHitAnimation();
        System.out.println("Jina took " + damage + " damage! Remaining HP: " + this.HP);
    }

    // Getters
    @Override public int getHP() { return HP; }
    @Override public void setHP(int HP) { this.HP = Math.min(Math.max(0, HP), getMaxHP()); }
    @Override public int getMaxHP() { return 800; }
    @Override public int getBasicAttackDamage() { return basicAttackDamage; }
    @Override public int getSkillDamage() { return skillDamage; }
    @Override public int getSkillCost() { return skillCost; }
    @Override public int getUltimateDamage() { return ultimateDamage; }
    @Override public int getUltCooldown() { return ultCooldown; }
    @Override public String getBasicAtkImage() { return basicAtkImage; }
    @Override public String getSkillImage() { return skillImage; }
    @Override public String getUltImage() { return ultImage; }
    @Override public Animation<TextureRegion> getIdleAnimation() { return idleAnimation; }
    @Override public String getBasicAtkAnimation() { return basicAtkAnimationPath; }
    @Override public String getSkillAnimation() { return skillAnimationPath; }
    @Override public String getHitAnimation() { return hitAnimationPath; }
    @Override public String getUltAnimation() { return ultAnimationPath; }


}
