package com.vortex.CharacterStats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.vortex.SFX.PlayAudio;

public class Character_Jina implements Character_BattleStats {
    // Stats
    private int HP = 800;
    private int basicAttackDamage = 68;
    private int skillDamage = 130;
    private int skillCost = 2;
    private int ultimateDamage = 170;
    private int ultCooldown = 9;
    private int currentUltCooldown = 0;
    private int HealAmount = 350;
    private PlayAudio sfx = new PlayAudio();
    // File paths
    private String basicAtkImage = "Pictures/Jina/CharacterView/Jina_SledgeStrike.png";
    private String skillImage = "Pictures/Jina/CharacterView/Jina_PrecisionShot.png";
    private String ultImage = "Pictures/Jina/CharacterView/Jina_VanguardsResolve.png";

    // Animation paths
    private final String idleAnimationPath = "Pictures/Jina/CharacterView/jina_battle_idle.png";
    private final String basicAtkAnimationPath = "Pictures/Jina/CharacterView/jina_battle_basicAtk.png";
    private final String skillAnimationPath = "Pictures/Jina/CharacterView/jina_battle_skill.png";
    private final String hitAnimationPath = "Pictures/Jina/CharacterView/jina_battle_hit.png";
    private final String ultAnimationBasePath = "Pictures/Jina/CharacterView/JinaUltFrames/jina-ult-frame";

    private Texture idleSheet, basicAtkSheet, skillSheet, hitSheet;
    private Texture[] ultFrames;
    private Animation<TextureRegion> idleAnimation, basicAtkAnimation, skillAnimation, hitAnimation, ultAnimation;
    private float stateTime;
    private Animation<TextureRegion> currentAnimation;

    // Ultimate animation system
    private Animation<TextureRegion> fullscreenUltAnimation;
    private float ultStateTime = 0;
    private boolean isUltimatePlaying = false;

    public Character_Jina() {
        try {
            loadAnimations();
            loadUltimateAnimation();
            stateTime = 0;
            currentAnimation = idleAnimation;
        } catch (Exception e) {
            System.err.println("Error loading Jina animations: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Jina character", e);
        }
    }

    private void loadAnimations() {
        // Load with error handling
        idleSheet = new Texture(Gdx.files.internal(idleAnimationPath));
        basicAtkSheet = new Texture(Gdx.files.internal(basicAtkAnimationPath));
        skillSheet = new Texture(Gdx.files.internal(skillAnimationPath));
        hitSheet = new Texture(Gdx.files.internal(hitAnimationPath));

        // Create animations with appropriate frame counts
        idleAnimation = createAnimation(idleSheet, 64, 64, 7, 1, 0.1f);
        basicAtkAnimation = createAnimation(basicAtkSheet, 64, 64, 13, 1, 0.08f);
        skillAnimation = createAnimation(skillSheet, 64, 64, 10, 1, 0.1f);
        hitAnimation = createAnimation(hitSheet, 64, 64, 3, 1, 0.15f);

        // Set play modes
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
        basicAtkAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        skillAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        hitAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    private void loadUltimateAnimation() {
        int totalUltFrames = 46; // Adjust based on actual frame count
        TextureRegion[] ultRegions = new TextureRegion[totalUltFrames];
        ultFrames = new Texture[totalUltFrames];

        try {
            for (int i = 0; i < totalUltFrames; i++) {
                String framePath = ultAnimationBasePath + (i + 1) + ".png";
                ultFrames[i] = new Texture(Gdx.files.internal(framePath));
                ultRegions[i] = new TextureRegion(ultFrames[i]);
            }
        } catch (Exception e) {
            System.err.println("Error loading ultimate frames: " + e.getMessage());
            throw new RuntimeException("Failed to load Jina's ultimate animation frames", e);
        }

        fullscreenUltAnimation = new Animation<>(0.1f, ultRegions);
        fullscreenUltAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    private Animation<TextureRegion> createAnimation(Texture sheet, int frameWidth, int frameHeight,
                                                     int cols, int rows, float frameDuration) {
        try {
            TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, frameHeight);
            TextureRegion[] frames = new TextureRegion[cols * rows];
            int index = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (index >= frames.length) {
                        throw new RuntimeException("Frame index out of bounds. Expected " +
                            (cols * rows) + " frames but texture doesn't contain enough.");
                    }
                    frames[index++] = tmp[i][j];
                }
            }
            return new Animation<>(frameDuration, frames);
        } catch (Exception e) {
            System.err.println("Error creating animation from texture: " + sheet +
                "\nExpected size: " + (cols * frameWidth) + "x" + (rows * frameHeight) +
                "\nActual size: " + sheet.getWidth() + "x" + sheet.getHeight());
            throw new RuntimeException("Failed to create animation", e);
        }
    }

    public void startUltimate() {
        isUltimatePlaying = true;
        ultStateTime = 0;
    }
    public float getUltimateAnimationDuration() {
        return fullscreenUltAnimation.getAnimationDuration();
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

        if (ultFrames != null) {
            for (Texture frame : ultFrames) {
                if (frame != null) {
                    frame.dispose();
                }
            }
        }
    }

    @Override public int getHP() { return HP; }
    @Override public void setHP(int HP) { this.HP = Math.max(0, Math.min(HP, getMaxHP())); }
    @Override public int getMaxHP() { return 800; }
    @Override public void takeDamage(int damage) { setHP(this.HP - damage); playHitAnimation(); }
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
    @Override public String getUltAnimation() { return ultAnimationBasePath; }
    @Override public void startUltimateCooldown() { currentUltCooldown = ultCooldown; }
    @Override public void reduceCooldowns() { if (currentUltCooldown > 0) currentUltCooldown--; }
    @Override public boolean isUltimateReady() { return currentUltCooldown == 0; }

    public int getHealAmount() {
        return HealAmount;
    }

    @Override
    public String getBasicAtkDescription() {
        return "Sledge Strike: A powerful melee attack with Jina's weapon, dealing " +
            basicAttackDamage + " damage.";
    }

    @Override
    public String getSkillDescription() {
        return "Precision Shot: A carefully aimed attack dealing " + skillDamage +
            " damage and heals 350 hp to the character with the current lowest HP. Costs " + skillCost + " SP.";
    }

    @Override
    public String getUltDescription() {
        return "Vanguard's Resolve: A defensive ultimate that deals " + ultimateDamage +
            " damage to enemies and heals all allies for " + HealAmount +
            " HP. Cooldown: " + ultCooldown + " turns.";
    }

    public TextureRegion getUltimateFirstFrame() {
        return fullscreenUltAnimation.getKeyFrame(0, false);
    }

    public float getUltimateAnimationProgress() {
        return stateTime / ultAnimation.getAnimationDuration();
    }

    public boolean isUltimateFinished() {
        return ultAnimation.isAnimationFinished(stateTime);
    }
}
