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
    private int ultCooldown = 12; // turns

    // File paths for images
    private String basicAtkImage = "Pictures/Umbra/CharacterView/Umbra_ShadowStrike.png";
    private String skillImage = "Pictures/Umbra/CharacterView/Umbra_DistractingIllusions.png";
    private String ultImage = "Pictures/Umbra/CharacterView/Umbra_VeilOfShadows.png";

    // File paths for animations
    private String idleAnimationPath = "Pictures/Umbra/BattleView/umbra_idle_battle.png";
    private String basicAtkAnimationPath = "Pictures/Umbra/BattleView/umbra_basicAtk_battle.png";

    // Animation-related fields
    private Texture idleSheet; // Sprite sheet for idle animation
    private TextureRegion[] idleFrames; // Array to hold individual frames
    private Animation<TextureRegion> idleAnimation; // Animation object
    private float stateTime; // Tracks elapsed time for animation

    // Constructor
    public Character_Umbra() {
        // Load the idle animation sprite sheet
        idleSheet = new Texture(Gdx.files.internal(idleAnimationPath));

        // Split the sprite sheet into frames
        int frameWidth = 64; // Width of each frame
        int frameHeight = 64; // Height of each frame
        int cols = 7; // Number of columns in the sprite sheet
        int rows = 1; // Number of rows in the sprite sheet

        TextureRegion[][] tmp = TextureRegion.split(idleSheet, frameWidth, frameHeight);
        idleFrames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                idleFrames[index++] = tmp[i][j];
            }
        }

        // Create the idle animation
        float frameDuration = 0.1f; // Time between frames (adjust for speed)
        idleAnimation = new Animation<>(frameDuration, idleFrames);

        // Initialize stateTime
        stateTime = 0;
    }

    // Get the current frame of the idle animation
    public TextureRegion getIdleFrame(float deltaTime) {
        stateTime += deltaTime; // Accumulate elapsed time
        return idleAnimation.getKeyFrame(stateTime, true); // Loop the animation
    }

    // Dispose method to clean up resources
    public void dispose() {
        if (idleSheet != null) {
            idleSheet.dispose(); // Dispose of the sprite sheet texture
        }
    }

    // Getters for stats and file paths
    @Override
    public int getHP() {
        return HP;
    }
    @Override
    public void setHP(int HP){
        this.HP = HP;
    }
    @Override
    public int getMaxHP() {
        return 800;
    }
    public void takeDamage(int damage) {
        this.HP = Math.max(0, this.HP - damage); // Ensure HP doesn't go below 0
        System.out.println(this.getClass().getSimpleName() + " took " + damage + " damage! Remaining HP: " + this.HP);
    }

    @Override
    public int getBasicAttackDamage() {
        return basicAttackDamage;
    }

    @Override
    public int getSkillDamage() {
        return skillDamage;
    }

    @Override
    public int getSkillCost() {
        return skillCost;
    }

    @Override
    public int getUltimateDamage() {
        return ultimateDamage;
    }

    @Override
    public int getUltCooldown() {
        return ultCooldown;
    }

    @Override
    public String getBasicAtkImage() {
        return basicAtkImage;
    }

    @Override
    public String getSkillImage() {
        return skillImage;
    }

    @Override
    public String getUltImage() {
        return ultImage;
    }

    @Override
    public String getIdleAnimation() {
        return idleAnimationPath;
    }

    @Override
    public String getBasicAtkAnimation() {
        return basicAtkAnimationPath;
    }
}
