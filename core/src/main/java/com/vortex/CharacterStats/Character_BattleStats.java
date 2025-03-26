package com.vortex.CharacterStats;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface Character_BattleStats {
    // Core stats
    int getHP();
    int getMaxHP();
    void setHP(int HP);
    void takeDamage(int damage);

    // Combat abilities
    int getBasicAttackDamage();
    int getSkillDamage();
    int getSkillCost();
    int getUltimateDamage();
    int getUltCooldown();

    // Cooldown management
    void startUltimateCooldown();
    void reduceCooldowns();
    boolean isUltimateReady();

    // Static images (for UI)
    String getBasicAtkImage();
    String getSkillImage();
    String getUltImage();

    // Animation paths
    Animation<TextureRegion> getIdleAnimation();
    String getBasicAtkAnimation();
    String getSkillAnimation();
    String getHitAnimation();
    String getUltAnimation();

    // Animation control
    void update(float deltaTime);
    TextureRegion getCurrentFrame();
    void playBasicAttackAnimation();
    void playSkillAnimation();
    void playHitAnimation();
    void playUltimateAnimation();

    // Resource management
    void dispose();
}
