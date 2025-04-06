package com.vortex.game.BattleClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vortex.CharacterStats.Character_BattleStats;
import com.vortex.SFX.PlayAudio;
import com.vortex.CharacterStats.Character_Umbra;
import com.vortex.CharacterStats.Character_Nova;
import com.vortex.CharacterStats.Character_Jina;
import com.vortex.game.GameTransitions;
import com.vortex.game.FontManager;

import java.util.*;

public class BattleClass implements Screen {

    // skill description pointer
    private BitmapFont tooltipFont;
    private String currentTooltip = "";
    private float tooltipTimer = 0;
    private final float TOOLTIP_DELAY = 0.5f; // seconds before tooltip appears
    private boolean showTooltip = false;
    private float tooltipX = 0, tooltipY = 0;

    private SpriteBatch spriteBatch;
    private BitmapFont bitmapFont;
    private Stage stage;
    private Skin skin;
    private BitmapFont BOSS_FONT;
    private BitmapFont BASIC_ENEMIES;
    private BitmapFont SKILLS_FONT;
    private BitmapFont ENEMY_TURN_FONT;
    private BitmapFont UNIVERSE_FONT;
    private BitmapFont enemyTurnFont;
    private FontManager fontHandler;
    private Runnable onBattleComplete;
    private EnemyClass enemy;

    private boolean hasStarted = true;
    private ImageButton attackButton, skillButton, ultimateButton, settingsButton;
    private Viewport viewport;
    private Texture backgroundTexture;
    private Texture roadTileTexture;
    private Texture enabledSkillPointTexture;
    private Texture disabledSkillPointTexture;
    private Texture turnIndicatorTexture;
    private Texture enemyTurnIndicatorTexture;
    private Character_Umbra umbra;
    private Character_Nova nova;
    private Character_Jina jina;
    private Character_BattleStats currentCharacter;
    private List<String> characters;
    private int currentTurn = 0;
    private int skillPoints = 1;
    private boolean isPlayingUltimate = false;
    private float ultimateAnimationTimer = 0f;
    private float ultimateAnimationDuration = 0.12f * 29; // 29 frames * 0.12s per frame
    private TextureRegion currentUltimateFrame;

    //enemyposition
    private float enemyWidth = 900f;  // Drawing width
    private float enemyHeight = 900f;
    private float enemyX = (1600 - enemyWidth) / 2f;;  // X position on screen
    private float enemyY = (900 - enemyHeight) / 2f + 100f;   // Y position on screen
    // Drawing height

    private Map<String, Integer> ultimateCooldowns;
    //HealAnimation
    private float startX;
    private Texture healAnimationSheet;
    private Animation<TextureRegion> healAnimation;
    private boolean isPlayingHeal = false;
    private float healAnimationTimer = 0f;
    private float healAnimationDuration = 0.1f * 23; // 23 frames at 0.1s each
    private Vector2 healTargetPosition;
    private List<HealingAnimation> activeHealingAnimations = new ArrayList<>();
    private class HealingAnimation {
        Vector2 position;
        float timer;

        public HealingAnimation(Vector2 position) {
            this.position = position;
            this.timer = 0f;
        }
    }

    // Target selection animation
    private Texture targetTexture;
    private float targetSelectionTimer = 0f;
    private final float TARGET_SELECTION_DURATION = 1.5f; // Time spent cycling through targets
    private final float TARGET_LOCK_DURATION = 1f;     // Time target stays on selected character
    private Character_BattleStats selectedTarget = null;
    private Character_BattleStats cyclingTarget = null; // Currently highlighted target during cycling
    private float targetCycleSpeed = 0.15f;            // How fast target cycles between characters
    private boolean isTargetLocked = false;

    private String universeName;
    private final int PLATFORM_Y = 200;
    private final int TILE_SIZE = 16;

    private int skillFlashCounter = 0;
    private float skillFlashTimer = 0;
    private boolean skillFlashing = false;

    private int ultimateFlashCounter = 0;
    private float ultimateFlashTimer = 0;
    private boolean ultimateFlashing = false;

    private float stateTime = 0;

    private GameTransitions game;

    // GameControls-related fields
    private SpriteBatch controlsBatch;
    private BitmapFont controlsFont, controlsTitleFont, controlsButtonFont;
    private ShapeRenderer controlsShapeRenderer;
    private GlyphLayout controlsLayout;
    private PlayAudio sfx = new PlayAudio();
    private float musicVolume = 1.0f;
    private float soundVolume = 1.0f;
    private float brightness = 1.0f;

    private Rectangle musicSlider, soundSlider, brightnessSlider;
    private Rectangle backButton, restartButton, quitButton;
    private boolean isDraggingMusic = false;
    private boolean isDraggingSound = false;
    private boolean isDraggingBrightness = false;

    private float[] buttonAnimations = new float[3];
    private float animationSpeed = 5f;
    private float pulseAmount = 0.05f;
    private float pulse = 0f;
    private float pulseSpeed = 2f;
    private String musicFile;
    private boolean musicStarted = false;
    private int roundCount = 0;

    private Color backgroundColor = new Color(0.05f, 0.07f, 0.15f, 1f);
    private Color accentColor = new Color(0f, 0.8f, 0.8f, 1f);
    private Color secondAccentColor = new Color(0.9f, 0.6f, 0.1f, 1f);
    private Color sliderBgColor = new Color(0.12f, 0.14f, 0.2f, 1f);
    private Color buttonColor = new Color(0.18f, 0.2f, 0.3f, 1f);
    private Color buttonHoverColor = new Color(0.22f, 0.25f, 0.35f, 1f);

    private float sliderWidth = 300f;
    private float sliderHeight = 30f;
    private float knobSize = 20f;
    private float knobRadius = 12f;
    private float sliderRadius = 15f;

    private float screenWidth;
    private float screenHeight;

    private Preferences prefs;

    private float scaleFactor = 1.2f;

    private boolean showControls = false;
    private boolean askingToQuit = false;
  // For controls screen text rendering

    // HP Bar related fields
    private Texture hpBarTexture;
    private TextureRegion[] hpBarRegions;

    // Enemy HP Bar related fields
    private Texture enemyHpBarTexture;
    private TextureRegion[] enemyHpBarRegions;
    private int enemyMaxHp;
    private int enemyCurrentHp;

    // Enemy turn related fields
    private boolean isEnemyTurn = false;
    private float enemyTurnTimer = 0f;
    private final float ENEMY_ATTACK_DELAY = 2f; // Attack happens after 3 seconds
    private final float ENEMY_TURN_DURATION = 3f;

    //for transition
    private Texture closeTransitionTexture;
    private TextureRegion[] closeTransitionFrames;
    private Animation<TextureRegion> closeTransitionAnimation;
    private float closeTransitionStateTime = 0;
    private boolean playCloseTransition = false;
    private Texture openTransitionTexture;
    private TextureRegion[] openTransitionFrames;
    private Animation<TextureRegion> openTransitionAnimation;
    private float openTransitionStateTime = 0;
    private boolean playOpenTransition = true;

    private ShapeRenderer shapeRenderer;





    public BattleClass(GameTransitions game,String universeName,EnemyClass enemy, boolean hasUmbra, boolean hasNova, boolean hasJina,
                       String background, String roadTile, String musicFile, Runnable onBattleComplete) {
        //new
        FileHandler.startMatchTimer();
        this.game = game;
        this.sfx = game.getAudioManager();
        this.onBattleComplete = onBattleComplete;
        this.universeName = universeName;
        spriteBatch = new SpriteBatch();
        bitmapFont = new BitmapFont();
        fontHandler = new FontManager();
        this.enemy = enemy;
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        viewport = new FitViewport(1600, 900);
        viewport.apply();
        loadHealAnimation();
        Gdx.input.setInputProcessor(stage);

        // Load fonts
        fontHandler.loadFont("Poppins-Bold", "Fonts/Poppins-Bold.ttf");
        fontHandler.loadFont("skillsfont", "Fonts/SKILLS_FONTS/Orbitron-SemiBold.ttf");

        // Generate fonts
        ENEMY_TURN_FONT = fontHandler.getFont("Poppins-Bold", 32, Color.RED);
        UNIVERSE_FONT = fontHandler.getFont("Poppins-Bold", 32, Color.WHITE);
        SKILLS_FONT = fontHandler.getFont("skillsfont", 20, Color.WHITE);

        // Create a large font for "Enemy's Turn" text
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Poppins-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 72;
        parameter.color = Color.RED;
        enemyTurnFont = generator.generateFont(parameter);
        generator.dispose();

        // Add this in the constructor after other font initializations
        FreeTypeFontGenerator tooltipGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Poppins-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter tooltipParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        tooltipParam.size = 18;
        tooltipParam.color = Color.WHITE;
        tooltipFont = tooltipGenerator.generateFont(tooltipParam);
        tooltipGenerator.dispose();

        shapeRenderer = new ShapeRenderer();

        //for transition
        closeTransitionTexture = new Texture(Gdx.files.internal("Backgrounds/closeTransitionSpriteSheet.png"));
        TextureRegion[][] tmp = TextureRegion.split(closeTransitionTexture,
            closeTransitionTexture.getWidth()/8,
            closeTransitionTexture.getHeight());
        closeTransitionFrames = new TextureRegion[8];
        for (int i = 0; i < 8; i++) {
            closeTransitionFrames[i] = tmp[0][i];
        }
        closeTransitionAnimation = new Animation<>(0.2f, closeTransitionFrames);

        openTransitionTexture = new Texture(Gdx.files.internal("Backgrounds/closeTransitionSpriteSheet.png"));
        TextureRegion[][] tmpOpen = TextureRegion.split(openTransitionTexture,
            openTransitionTexture.getWidth()/8,
            openTransitionTexture.getHeight());
        openTransitionFrames = new TextureRegion[20];
        for (int i = 0; i < 13; i++) {
            openTransitionFrames[i] = tmpOpen[0][7];
        }
        for (int i = 13; i < 20; i++) {
            openTransitionFrames[i] = tmpOpen[0][19 - i];
        }
        openTransitionAnimation = new Animation<>(0.2f, openTransitionFrames);
        System.out.println("The Battle is starting!");
        // Load textures
        backgroundTexture = new Texture(Gdx.files.internal("Backgrounds/" + background));
        roadTileTexture = new Texture(Gdx.files.internal("Tiles/" + roadTile));
        enabledSkillPointTexture = new Texture(Gdx.files.internal("BattleAssets/enabledSkillpoint.png"));
        disabledSkillPointTexture = new Texture(Gdx.files.internal("BattleAssets/disabledSkillpoint.png"));
        turnIndicatorTexture = new Texture(Gdx.files.internal("BattleAssets/turnIndicator.png"));
        targetTexture = new Texture(Gdx.files.internal("BattleAssets/target.png"));
        enemyTurnIndicatorTexture = new Texture(Gdx.files.internal("BattleAssets/EnemyTurnIndicator.png"));

        // Load HP Bar sprite sheet
        hpBarTexture = new Texture(Gdx.files.internal("BattleAssets/HP_Bar.png"));
        hpBarRegions = new TextureRegion[16];
        int regionWidth = hpBarTexture.getWidth() / 16;
        for (int i = 0; i < 16; i++) {
            hpBarRegions[i] = new TextureRegion(hpBarTexture, i * regionWidth, 0, regionWidth, hpBarTexture.getHeight());
        }

        // Load Enemy HP Bar sprite sheet
        enemyHpBarTexture = new Texture(Gdx.files.internal("BattleAssets/EnemyHP_bar.png"));
        enemyHpBarRegions = new TextureRegion[62];
        int enemyRegionHeight = enemyHpBarTexture.getHeight() / 62;
        for (int i = 0; i < 62; i++) {
            enemyHpBarRegions[i] = new TextureRegion(enemyHpBarTexture, 0, i * enemyRegionHeight, enemyHpBarTexture.getWidth(), enemyRegionHeight);
        }

        ultimateCooldowns = new HashMap<>();

        if (hasUmbra) {
            umbra = new Character_Umbra();
            ultimateCooldowns.put("Umbra", umbra.getUltCooldown());
        }
        if (hasNova) {
            nova = new Character_Nova();
            ultimateCooldowns.put("Nova", nova.getUltCooldown());
        }
        if (hasJina) {
            jina = new Character_Jina();
            ultimateCooldowns.put("Jina", jina.getUltCooldown());
        }

        TestBossClass testBoss = new TestBossClass();
        enemyMaxHp = enemy.getMaxHP();
        enemyCurrentHp = enemy.getMaxHP();

        this.musicFile = musicFile;
        characters = new ArrayList<>();
        if (hasUmbra) characters.add("Umbra");
        if (hasNova) characters.add("Nova");
        if (hasJina) characters.add("Jina");

        // Initialize buttons with Umbra's abilities (default)
        Character_BattleStats initialCharacter = null;
        if (hasUmbra) {
            initialCharacter = umbra;
        } else if (hasNova) {
            initialCharacter = nova;
        } else if (hasJina) {
            initialCharacter = jina;
        }

        if (initialCharacter == null) {
            throw new IllegalStateException("Battle must have at least one character");
        }

        attackButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal(initialCharacter.getBasicAtkImage()))));
        skillButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal(initialCharacter.getSkillImage()))));
        ultimateButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal(initialCharacter.getUltImage()))));

// Also update the currentCharacter initialization
        currentCharacter = initialCharacter;
        attackButton.getColor().a = 0.85f;
        int skillPointY = 60;
        attackButton.setPosition(100, skillPointY);
        attackButton.setSize(120, 120);

        skillButton.setPosition(attackButton.getX() + attackButton.getWidth() + 20, skillPointY);
        skillButton.setSize(120, 120);

        ultimateButton.setPosition(skillButton.getX() + skillButton.getWidth() + 20, skillPointY);
        ultimateButton.setSize(120, 120);

        settingsButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("BattleAssets/setting_icon.png"))));
        settingsButton.setPosition(viewport.getWorldWidth() - 150, viewport.getWorldHeight() - 150);
        settingsButton.setSize(100, 100);

        currentCharacter = initialCharacter;

        // Add button listeners
        attackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dealBasicAttackDamage();

                addSkillPoint();
                nextTurn();
                debugTurnState("Basic attack used");
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                attackButton.getColor().a = 1.0f;
                currentTooltip = currentCharacter.getBasicAtkDescription();
                tooltipX = event.getStageX();
                tooltipY = event.getStageY();
                tooltipTimer = 0;
                showTooltip = false;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                attackButton.getColor().a = 0.85f;
                currentTooltip = "";
                showTooltip = false;
            }
        });

        skillButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentTurn >= 0 && currentTurn < characters.size() &&
                    skillPoints >= currentCharacter.getSkillCost()) {
                    dealSkillDamage();
                    useSkill();
                    nextTurn();
                    SKILLS_FONT.setColor(1f, 0f, 0f, 1f);
                    debugTurnState("skill attack used");
                } else {
                    skillFlashing = true;
                    SKILLS_FONT.setColor(0.53f, 0.81f, 0.92f, 1f);
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Add bounds checking
                if (currentTurn >= 0 && currentTurn < characters.size() &&
                    skillPoints >= currentCharacter.getSkillCost()) {
                    skillButton.getColor().a = 1.0f;
                    currentTooltip = currentCharacter.getSkillDescription();
                    tooltipX = event.getStageX();
                    tooltipY = event.getStageY();
                    tooltipTimer = 0;
                    showTooltip = false;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                // Add bounds checking
                if (currentTurn >= 0 && currentTurn < characters.size()) {
                    if (skillPoints >= currentCharacter.getSkillCost()) {
                        skillButton.getColor().a = 0.85f;
                    } else {
                        skillButton.getColor().a = 0.5f;
                    }
                }
                currentTooltip = "";
                showTooltip = false;
            }
        });

        ultimateButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentTurn >= 0 && currentTurn < characters.size()) {
                    int currentCooldown = ultimateCooldowns.get(characters.get(currentTurn));
                    if (currentCooldown <= 0) {
                        dealUltimateDamage();
                        useUltimate();
                        nextTurn();
                    } else {
                        ultimateFlashing = true;
                    }
                    debugTurnState("ult attack used");
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (currentTurn >= 0 && currentTurn < characters.size()) {
                    int currentCooldown = ultimateCooldowns.get(characters.get(currentTurn));
                    if (currentCooldown <= 0) {
                        ultimateButton.getColor().a = 1.0f;
                        currentTooltip = currentCharacter.getUltDescription();
                        tooltipX = event.getStageX();
                        tooltipY = event.getStageY();
                        tooltipTimer = 0;
                        showTooltip = false;
                    }
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (currentTurn >= 0 && currentTurn < characters.size()) {
                    int currentCooldown = ultimateCooldowns.get(characters.get(currentTurn));
                    if (currentCooldown <= 0) {
                        ultimateButton.getColor().a = 0.85f;
                    } else {
                        ultimateButton.getColor().a = 0.5f;
                    }
                }
                currentTooltip = "";
                showTooltip = false;
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showControls = true;
                initGameControls();
                Gdx.input.setInputProcessor(stage);
            }
        });

        stage.addActor(attackButton);
        stage.addActor(skillButton);
        stage.addActor(ultimateButton);
        stage.addActor(settingsButton);
    }

    private void dealBasicAttackDamage() {
        int damage = currentCharacter.getBasicAttackDamage();
        enemyCurrentHp = Math.max(0, enemyCurrentHp - damage);
        System.out.println("Character dealt " + damage + " damage to the enemy!");



        if (currentCharacter instanceof Character_Umbra) {
            umbra.playBasicAttackAnimation();
        }else if (currentCharacter instanceof Character_Nova) {
            nova.playBasicAttackAnimation();
        } else if (currentCharacter instanceof Character_Jina) {
            jina.playBasicAttackAnimation();
        }
        debugEnemyHP();
        checkBattleConditions();
    }

    private void dealSkillDamage() {
        if (currentCharacter instanceof Character_Jina) {
            // Jina's skill is healing and damage
            int damage = currentCharacter.getSkillDamage();
            Character_BattleStats target = healLowestCharacter(); // Get the target that was healed
            enemyCurrentHp = Math.max(0, enemyCurrentHp - damage);
            jina.playSkillAnimation();
            System.out.println("Character used their skill and dealt " + damage + " damage to the enemy!");

            // Only play heal animation if there was a target
            if (target != null) {
                isPlayingHeal = true;
                healAnimationTimer = 0f;
                setHealTargetPosition(target);
            }
        } else {
            // Normal skill damage for other characters
            int damage = currentCharacter.getSkillDamage();
            enemyCurrentHp = Math.max(0, enemyCurrentHp - damage);
            System.out.println("Character used their skill and dealt " + damage + " damage to the enemy!");
        }

        // Play appropriate animation
        if (currentCharacter instanceof Character_Umbra) {
            umbra.playSkillAnimation();
        } else if (currentCharacter instanceof Character_Nova) {
            nova.playSkillAnimation();
        }
        debugEnemyHP();
        checkBattleConditions();
    }
    private void setHealTargetPosition(Character_BattleStats target) {
        // Calculate character positions
        float totalCharactersWidth = characters.size() * 300;
        float currentStartX = (viewport.getWorldWidth() - totalCharactersWidth) / 2;

        // Set position based on which character is being healed
        if (target instanceof Character_Umbra) {
            healTargetPosition = new Vector2(
                currentStartX + characters.indexOf("Umbra") * 300 + 125,
                PLATFORM_Y + 160
            );
        } else if (target instanceof Character_Nova) {
            healTargetPosition = new Vector2(
                currentStartX + characters.indexOf("Nova") * 300 + 125,
                PLATFORM_Y + 160
            );
        } else if (target instanceof Character_Jina) {
            healTargetPosition = new Vector2(
                currentStartX + characters.indexOf("Jina") * 300 + 125,
                PLATFORM_Y + 160
            );
        }
    }
    private void loadHealAnimation() {
        healAnimationSheet = new Texture(Gdx.files.internal("BattleAssets/JinaHeal.png"));
        TextureRegion[][] tmp = TextureRegion.split(healAnimationSheet,
            healAnimationSheet.getWidth()/22,
            healAnimationSheet.getHeight());

        TextureRegion[] frames = new TextureRegion[22];
        for (int i = 0; i < 22; i++) {
            frames[i] = tmp[0][i];
        }
        healAnimation = new Animation<>(0.1f, frames);
    }

    private Character_BattleStats healLowestCharacter() {
        Character_BattleStats target = findCharacterWithLowestHP();

        if (target != null) {
            int healAmount = ((Character_Jina)currentCharacter).getHealAmount();
            int newHP = Math.min(target.getHP() + healAmount, target.getMaxHP());
            target.setHP(newHP);

            // Create healing animation
            activeHealingAnimations.add(new HealingAnimation(calculateHealPosition(target)));

            System.out.println("Jina healed " +
                target.getClass().getSimpleName().replace("Character_", "") +
                " for " + healAmount + " HP!");

            // Play healing sound
            sfx.playSoundEffect("heal_sound.wav", 0.5f);
        }

        return target;
    }

    private Character_BattleStats findCharacterWithLowestHP() {
        Character_BattleStats lowestHPChar = null;
        float lowestPercentage = 1.0f; // Start at 100%

        // Check all available characters
        if (umbra != null && umbra.getHP() > 0) {
            float percentage = (float)umbra.getHP() / umbra.getMaxHP();
            if (percentage < lowestPercentage) {
                lowestPercentage = percentage;
                lowestHPChar = umbra;
            }
        }

        if (nova != null && nova.getHP() > 0) {
            float percentage = (float)nova.getHP() / nova.getMaxHP();
            if (percentage < lowestPercentage) {
                lowestPercentage = percentage;
                lowestHPChar = nova;
            }
        }

        if (jina != null && jina.getHP() > 0) {
            float percentage = (float)jina.getHP() / jina.getMaxHP();
            if (percentage < lowestPercentage) {
                lowestHPChar = jina;
            }
        }

        return lowestHPChar;
    }
    private boolean isAnyUltimatePlaying() {
        return (umbra != null && umbra.isUltimatePlaying()) ||
            (nova != null && nova.isUltimatePlaying()) ||
            (jina != null && jina.isUltimatePlaying());
    }
    private void dealUltimateDamage() {
        int damage = currentCharacter.getUltimateDamage();
        enemyCurrentHp = Math.max(0, enemyCurrentHp - damage);
        if (currentCharacter instanceof Character_Umbra) {
            umbra.startUltimate();
            sfx.playSoundEffect("umbra_ult_sfx.wav", 0); // Make sure you have this sound file
        }
        else if (currentCharacter instanceof Character_Nova) {
            nova.startUltimate();
            sfx.playSoundEffect("nova_ult_sfx.wav", 0); // Make sure you have this sound file
        }
        else if (currentCharacter instanceof Character_Jina) {
            jina.startUltimate();
            sfx.playSoundEffect("jina_ult_sfx.wav", 0);

            // Clear any existing animations
            activeHealingAnimations.clear();

            // Schedule healing for all characters
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    sfx.playSoundEffect("ult_heal.wav", 1f);

                    // Heal all characters and create animations
                    if (umbra != null && umbra.getHP() > 0) {
                        umbra.setHP(Math.min(umbra.getMaxHP(), umbra.getHP() + jina.getHealAmount()));
                        activeHealingAnimations.add(new HealingAnimation(calculateHealPosition(umbra)));
                    }
                    if (nova != null && nova.getHP() > 0) {
                        nova.setHP(Math.min(nova.getMaxHP(), nova.getHP() + jina.getHealAmount()));
                        activeHealingAnimations.add(new HealingAnimation(calculateHealPosition(nova)));
                    }
                    if (jina != null && jina.getHP() > 0) {
                        jina.setHP(Math.min(jina.getMaxHP(), jina.getHP() + jina.getHealAmount()));
                        activeHealingAnimations.add(new HealingAnimation(calculateHealPosition(jina)));
                    }
                }
            }, jina.getUltimateAnimationDuration() * 1);
        }
        debugEnemyHP();
        checkBattleConditions();
    }
    private void startHealAnimation(Character_BattleStats character) {
        isPlayingHeal = true;
        healAnimationTimer = 0f;

        float totalCharactersWidth = characters.size() * 300;
        float currentStartX = (viewport.getWorldWidth() - totalCharactersWidth) / 2;

        if (character instanceof Character_Umbra) {
            healTargetPosition = new Vector2(
                currentStartX + characters.indexOf("Umbra") * 300 + 125,
                PLATFORM_Y + 160
            );
        } else if (character instanceof Character_Nova) {
            healTargetPosition = new Vector2(
                currentStartX + characters.indexOf("Nova") * 300 + 125,
                PLATFORM_Y + 160
            );
        } else {
            healTargetPosition = new Vector2(
                currentStartX + characters.indexOf("Jina") * 300 + 125,
                PLATFORM_Y + 160
            );
        }
    }

    public void addSkillPoint() {
        if (skillPoints < 3) {
            skillPoints++;
        }
    }

    public void useSkill() {
        if (skillPoints >= currentCharacter.getSkillCost()) {
            skillPoints -= currentCharacter.getSkillCost();
        }
    }

    public void useUltimate() {
        String currentCharacterName = characters.get(currentTurn);
        int currentCooldown = ultimateCooldowns.get(currentCharacterName);
        if (currentCooldown <= 0) {
            ultimateCooldowns.put(currentCharacterName, currentCharacter.getUltCooldown());
        }
    }

    public void nextTurn() {
        // Decrease cooldown for current character
        if (!characters.isEmpty() && !isCharacterDefeated(characters.get(currentTurn))) {
            String currentCharacterName = characters.get(currentTurn);
            int currentCooldown = ultimateCooldowns.get(currentCharacterName);
            if (currentCooldown > 0) {
                ultimateCooldowns.put(currentCharacterName, currentCooldown - 1);
            }
        }

        // Find next alive character (skip defeated ones)
        int nextTurn = findNextAliveCharacter();

        // If no alive characters, battle ends
        if (nextTurn == -1) {
            checkBattleConditions();
            return;
        }

        // If we've looped through all alive characters, trigger enemy turn
        if (nextTurn <= currentTurn) {
            enemyAttack(); // This now handles the full enemy turn sequence
            return;
        }

        currentTurn = nextTurn;
        updateButtons();
        updateCurrentCharacter(); // This ensures buttons & indicator match the log
    }

    private int findNextAliveCharacter() {
        int nextTurn = currentTurn;
        int attempts = 0;

        do {
            nextTurn = (nextTurn + 1) % characters.size();
            attempts++;

            if (!isCharacterDefeated(characters.get(nextTurn))) {
                return nextTurn;
            }
        } while (attempts < characters.size());

        return -1; // No alive characters found
    }

    private boolean areAllCharactersDefeated() {
        for (String character : characters) {
            if (!isCharacterDefeated(character)) {
                return false;
            }
        }
        return true;
    }
    private void updateCurrentCharacter() {
        if (characters.isEmpty()) return; // Safety check

        String characterName = characters.get(currentTurn);

        // If current character is defeated, find next alive one
        if (currentTurn >= 0 && currentTurn < characters.size() &&
            isCharacterDefeated(characters.get(currentTurn))) {
            currentTurn = findFirstAliveCharacter();
            if (currentTurn == -1) {
                checkBattleConditions();
                return;  // Add this return to prevent further execution
            }
            characterName = characters.get(currentTurn);
            debugTurnState("Turn changed to " + characters.get(currentTurn));
        }

        // Update currentCharacter
        switch (characterName) {
            case "Umbra":
                currentCharacter = umbra;
                if (umbra != null) umbra.setAnimation(umbra.getIdleAnimation());
                break;
            case "Nova":
                currentCharacter = nova;
                if(nova!= null) nova.setAnimation(nova.getIdleAnimation());
                break;
            case "Jina":
                currentCharacter = jina;
                break;
        }

        System.out.println("It is currently " + characterName + "'s turn!"); // Log
        updateButtons(); // Refresh buttons immediately
    }

    private boolean isCharacterDefeated(String characterName) {
        if (characterName.equals("Umbra")) {
            return umbra.getHP() <= 0;
        } else if (characterName.equals("Nova")) {
            return nova.getHP() <= 0;
        } else if (characterName.equals("Jina")) {
            return jina.getHP() <= 0;
        }
        return true;
    }

    public void updateButtons() {
        // Disable all buttons if no valid turn
        if (currentTurn < 0 || currentTurn >= characters.size() ||
            isCharacterDefeated(characters.get(currentTurn))) {
            attackButton.setDisabled(true);
            skillButton.setDisabled(true);
            ultimateButton.setDisabled(true);
            attackButton.getColor().a = 0.5f;
            skillButton.getColor().a = 0.5f;
            ultimateButton.getColor().a = 0.5f;
            return;
        }

        attackButton.getStyle().imageUp = new TextureRegionDrawable(new Texture(Gdx.files.internal(currentCharacter.getBasicAtkImage())));
        skillButton.getStyle().imageUp = new TextureRegionDrawable(new Texture(Gdx.files.internal(currentCharacter.getSkillImage())));
        ultimateButton.getStyle().imageUp = new TextureRegionDrawable(new Texture(Gdx.files.internal(currentCharacter.getUltImage())));

        if (skillPoints < currentCharacter.getSkillCost()) {
            skillButton.getColor().a = 0.5f;
            skillButton.setDisabled(true);
        } else {
            skillButton.getColor().a = 0.85f;
            skillButton.setDisabled(false);
        }

        int currentCooldown = ultimateCooldowns.get(characters.get(currentTurn));
        if (currentCooldown > 0) {
            ultimateButton.getColor().a = 0.5f;
            ultimateButton.setDisabled(true);
        } else {
            ultimateButton.getColor().a = 0.85f;
            ultimateButton.setDisabled(false);
        }

        attackButton.setDisabled(false);
        attackButton.getColor().a = 0.85f;
    }

    @Override
    public void show() {
        // Load current volume settings from GameTransitions
        musicVolume = game.getMusicVolume();
        soundVolume = game.getSoundVolume();
        sfx.setMusicVolume(musicVolume);
        sfx.setSoundVolume(soundVolume);

        if (!musicStarted) {
            sfx.playMusic(musicFile);
            musicStarted = true;
        }
    }

    @Override
    public void render(float delta) {
        if (!showControls) {
            // Update ultimate animation
            if (umbra != null) {
                umbra.updateUltimate(delta);
            }
            if (nova != null) {
                nova.updateUltimate(delta);
            }
            if (jina != null) {
                jina.updateUltimate(delta);
            }
            enemy.update(delta);

            // Update target selection during enemy's turn
            if (isEnemyTurn) {
                updateTargetSelection(delta);
            }

            renderBattle(delta);
        } else {
            renderGameControls(delta);

            // Render quit confirmation message on top of controls if needed
            if (askingToQuit) {
                controlsBatch.begin();
                String message = "Are you sure? Click QUIT again to exit.";
                controlsLayout.setText(controlsFont, message);
                float messageX = screenWidth / 2 - controlsLayout.width / 2;
                float messageY = quitButton.y - 30 * scaleFactor;  // Position below the button

                // New color settings - using bright red for warning message
                Color warningColor = new Color(1f, 0.2f, 0.2f, 1f); // Bright red
                Color outlineColor = new Color(0.2f, 0f, 0f, 1f);   // Dark red outline

                // Draw outline (for better visibility)
                controlsFont.setColor(outlineColor);
                controlsFont.draw(controlsBatch, message, messageX-1, messageY-1);
                controlsFont.draw(controlsBatch, message, messageX+1, messageY-1);
                controlsFont.draw(controlsBatch, message, messageX-1, messageY+1);
                controlsFont.draw(controlsBatch, message, messageX+1, messageY+1);

                // Draw main text
                controlsFont.setColor(warningColor);
                controlsFont.draw(controlsBatch, message, messageX, messageY);

                controlsFont.setColor(Color.WHITE); // Reset to default
                controlsBatch.end();
            }
        }

        // Handle transition animations (keep these outside the showControls check)
        if (playCloseTransition) {
            renderCloseTransition(delta);
        }
        if (playOpenTransition) {
            renderOpenTransition(delta);
        }
    }

    private void renderCloseTransition(float delta) {
        spriteBatch.begin();
        closeTransitionStateTime += delta;
        TextureRegion currentFrame = closeTransitionAnimation.getKeyFrame(closeTransitionStateTime, false);
        spriteBatch.draw(currentFrame, 0, viewport.getWorldHeight()-900, 1600, 900);

        if (closeTransitionAnimation.isAnimationFinished(closeTransitionStateTime)) {
            playCloseTransition = false;
            closeTransitionStateTime = 0;
        }
        spriteBatch.end();
    }

    private void renderOpenTransition(float delta) {
        openTransitionStateTime += delta;
        spriteBatch.begin();
        TextureRegion currentFrame = openTransitionAnimation.getKeyFrame(openTransitionStateTime, false);
        spriteBatch.draw(currentFrame, 0, viewport.getWorldHeight() - 900, 1600, 900);
        spriteBatch.end();

        if (openTransitionAnimation.isAnimationFinished(openTransitionStateTime)) {
            playOpenTransition = false;
            openTransitionStateTime = 0;
            hasStarted = false;
        }
    }

    public void renderBattle(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        if (isPlayingUltimate) {
            ultimateAnimationTimer += delta;
            if (ultimateAnimationTimer >= ultimateAnimationDuration) {
                isPlayingUltimate = false;
            } else {
                currentUltimateFrame = umbra.getCurrentFrame();
            }
        }

        spriteBatch.begin();
        // Draw Enemy
        TextureRegion currentEnemyFrame = enemy.getCurrentFrame();
        spriteBatch.draw(currentEnemyFrame,
            enemyX, enemyY,
            enemyWidth, enemyHeight);
        // Draw background and other elements
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());


        // Draw Enemy HP Bar
        float enemyHpPercentage = (float) enemyCurrentHp / enemyMaxHp;

// Draw enemy name:


// Draw enemy sprite:
        TextureRegion enemyFrame = enemy.getCurrentFrame();
        spriteBatch.draw(enemyFrame, enemyX, enemyY, enemyWidth, enemyHeight);
        int enemyHpIndex = (int) (61 * (1 - enemyHpPercentage));
        TextureRegion enemyHpRegion = enemyHpBarRegions[enemyHpIndex];
        float enemyHpBarWidth = 800;
        float enemyHpBarHeight = 50;
        float enemyHpBarX = (viewport.getWorldWidth() - enemyHpBarWidth) / 2;
        float enemyHpBarY = viewport.getWorldHeight() - 48;
        spriteBatch.draw(enemyHpRegion, enemyHpBarX, enemyHpBarY, enemyHpBarWidth, enemyHpBarHeight);
        UNIVERSE_FONT.draw(spriteBatch, universeName, 50, 880);
        UNIVERSE_FONT.draw(spriteBatch, universeName, 50, 880);
        UNIVERSE_FONT.draw(spriteBatch, "Round: " + roundCount, 50, 840);
// Draw enemy hp
        // Draw enemy hp
        String enemyHpText = String.format("%.0f%%", enemyHpPercentage * 100);
        GlyphLayout enemyHpLayout = new GlyphLayout(SKILLS_FONT, enemyHpText);
        GlyphLayout enemyNameLayout = new GlyphLayout(SKILLS_FONT, enemy.getName());

        float enemyHpTextX = enemyHpBarX + 40;
        float enemyHpTextY = enemyHpBarY + enemyHpBarHeight / 2 + enemyHpLayout.height / 2 + 8;
        float enemyNameTextX = enemyHpBarX + enemyHpBarWidth - enemyNameLayout.width - 40;
        float enemyNameTextY = enemyHpBarY + enemyHpBarHeight / 2 + enemyNameLayout.height / 2 + 8;

        SKILLS_FONT.setColor(Color.WHITE);
        SKILLS_FONT.draw(spriteBatch, enemyHpText, enemyHpTextX, enemyHpTextY);
        SKILLS_FONT.draw(spriteBatch, enemy.getName(), enemyNameTextX, enemyNameTextY);

        // Draw platform and tiles
        spriteBatch.setColor(0, 0, 0, 1);
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), PLATFORM_Y);
        spriteBatch.setColor(1, 1, 1, 1);

        int tilesAcross = (int) Math.ceil(viewport.getWorldWidth() / (TILE_SIZE * 4));
        for (int i = 0; i < tilesAcross; i++) {
            spriteBatch.draw(roadTileTexture, i * (TILE_SIZE * 4), PLATFORM_Y, TILE_SIZE * 4, TILE_SIZE * 4);
        }

        // Draw skill points
        int skillPointSize = 60;
        int skillPointX = (int) viewport.getWorldWidth() - 250;
        int skillPointY = 80;
        int spacingSkill = skillPointSize + 5;

        for (int i = 0; i < 3; i++) {
            Texture skillPointTexture = (i < skillPoints) ? enabledSkillPointTexture : disabledSkillPointTexture;
            spriteBatch.draw(skillPointTexture, skillPointX + (i * spacingSkill), skillPointY, skillPointSize, skillPointSize);
        }

        // Draw characters and their HP bars
        float totalCharactersWidth = characters.size() * 300;
        startX = (viewport.getWorldWidth() - totalCharactersWidth) / 2;
        for (int i = 0; i < characters.size(); i++) {
            String character = characters.get(i);
            float characterX = startX + i * 300;
            float characterY = PLATFORM_Y + 60;

            if (character.equals("Umbra")) {
                umbra.update(delta);
                TextureRegion currentFrame = umbra.getCurrentFrame();
                spriteBatch.draw(currentFrame, characterX, characterY, 250, 250);
            } else if (character.equals("Nova")) {
                nova.update(delta);
                TextureRegion currentFrame = nova.getCurrentFrame();
                spriteBatch.draw(currentFrame, characterX, characterY, 250, 250);
            } else if (character.equals("Jina")) {
                jina.update(delta);
                TextureRegion currentFrame = jina.getCurrentFrame();
                spriteBatch.draw(currentFrame, characterX, characterY, 250, 250);
            }



            // Draw HP Bar for each character
            int currentHP = 0;
            int maxHP = 0;
            if (character.equals("Umbra")) {
                currentHP = umbra.getHP();
                maxHP = umbra.getMaxHP();
            } else if (character.equals("Nova")) {
                currentHP = nova.getHP();
                maxHP = nova.getMaxHP();
            } else if (character.equals("Jina")) {
                currentHP = jina.getHP();
                maxHP = jina.getMaxHP();
            }

            float hpPercentage = (float) currentHP / maxHP;
            int hpBarIndex = 0;
            if (hpPercentage == 1f) {
            } else if (hpPercentage >= 0.9333f) {
                hpBarIndex = 1;
            } else if (hpPercentage >= 0.8667f) {
                hpBarIndex = 2;
            } else if (hpPercentage >= 0.8f) {
                hpBarIndex = 3;
            } else if (hpPercentage >= 0.7333f) {
                hpBarIndex = 4;
            } else if (hpPercentage >= 0.6667) {
                hpBarIndex = 5;
            } else if (hpPercentage >= 0.6) {
                hpBarIndex = 6;
            } else if (hpPercentage >= 0.5333) {
                hpBarIndex = 7;
            } else if (hpPercentage >= 0.4667) {
                hpBarIndex = 8;
            } else if (hpPercentage >= 0.4) {
                hpBarIndex = 9;
            } else if (hpPercentage >= 0.3333) {
                hpBarIndex = 10;
            } else if (hpPercentage >= 0.2667) {
                hpBarIndex = 11;
            } else if (hpPercentage >= 0.2) {
                hpBarIndex = 12;
            } else if (hpPercentage >= 0.1333) {
                hpBarIndex = 13;
            } else if (hpPercentage > 0) {
                hpBarIndex = 14;
            } else {
                hpBarIndex = 15;
            }

            float hpBarX = characterX + 5;
            float hpBarY = characterY + 10;
            float hpBarWidth = 95;
            float hpBarHeight = 100;
            spriteBatch.draw(hpBarRegions[hpBarIndex], hpBarX, hpBarY, hpBarWidth, hpBarHeight);

            if (i == currentTurn && !isEnemyTurn) {
                float indicatorX = characterX + 100 - turnIndicatorTexture.getWidth() / 2;
                float indicatorY = characterY + 170;
                float indicatorWidth = turnIndicatorTexture.getWidth() * 4f;
                float indicatorHeight = turnIndicatorTexture.getHeight() * 4f;
                spriteBatch.draw(turnIndicatorTexture, indicatorX, indicatorY, indicatorWidth, indicatorHeight);
            }
        }

        // Draw target selection if enemy's turn
        // Draw target selection if enemy's turn
        if (isEnemyTurn) {
            Character_BattleStats targetToDraw = isTargetLocked ? selectedTarget : cyclingTarget;

            if (targetToDraw != null) {
                float targetX = 0, targetY = 0;
                float charWidth = 250;
                float charHeight = 50;

                if (targetToDraw instanceof Character_Umbra && characters.contains("Umbra")) {
                    int index = characters.indexOf("Umbra");
                    targetX = startX + index * 300 + (charWidth/2) - (targetTexture.getWidth()/2);
                    targetY = PLATFORM_Y + 60 + charHeight;
                }
                else if (targetToDraw instanceof Character_Nova && characters.contains("Nova")) {
                    int index = characters.indexOf("Nova");
                    targetX = startX + index * 300 + (charWidth/2) - (targetTexture.getWidth()/2);
                    targetY = PLATFORM_Y + 60 + charHeight;
                }
                else if (targetToDraw instanceof Character_Jina && characters.contains("Jina")) {
                    int index = characters.indexOf("Jina");
                    targetX = startX + index * 300 + (charWidth/2) - (targetTexture.getWidth()/2);
                    targetY = PLATFORM_Y + 60 + charHeight;
                }

                if (targetX != 0 && targetY != 0) {
                    // Pulse effect when locked on
                    float scale = isTargetLocked ? 1.1f + (float)Math.sin(stateTime * 10) * 0.1f : 1.0f;
                    float sizeScale = 4f;
                    spriteBatch.draw(targetTexture,
                        targetX, targetY,
                        targetTexture.getWidth()/2f, targetTexture.getHeight()/2f,
                        targetTexture.getWidth(), targetTexture.getHeight(),
                        scale*sizeScale, scale*sizeScale, 0,
                        0, 0, targetTexture.getWidth(), targetTexture.getHeight(),
                        false, false);
                }
            }
        }

        // Draw Enemy Turn Indicator if it's the enemy's turn
        if (isEnemyTurn) {
            float indicatorX = (viewport.getWorldWidth() - 55) / 2;
            float indicatorY = viewport.getWorldHeight() - 160;
            spriteBatch.draw(enemyTurnIndicatorTexture, indicatorX, indicatorY, 60, 95);
        }

        // Disable buttons and display "Enemy's Turn" text during the enemy's turn
        if (isEnemyTurn) {
            attackButton.setVisible(false);
            skillButton.setVisible(false);
            ultimateButton.setVisible(false);

            String enemyTurnText = "Enemy's Turn";
            GlyphLayout enemyTurnLayout = new GlyphLayout(enemyTurnFont, enemyTurnText);
            float enemyTurnTextX = attackButton.getX() + (ultimateButton.getX() + ultimateButton.getWidth() - attackButton.getX() - enemyTurnLayout.width) / 2;
            float enemyTurnTextY = attackButton.getY() + attackButton.getHeight() / 2 + enemyTurnLayout.height / 2;
            enemyTurnFont.draw(spriteBatch, enemyTurnText, enemyTurnTextX, enemyTurnTextY);
        } else {
            attackButton.setVisible(true);
            skillButton.setVisible(true);
            ultimateButton.setVisible(true);
        }
        if (isPlayingUltimate && umbra != null && umbra.isUltimatePlaying()) {
            TextureRegion currentUltimateFrame = umbra.getCurrentUltimateFrame();

            // Save the original color
            Color originalColor = spriteBatch.getColor();

            // Draw fullscreen ultimate
            spriteBatch.setColor(1, 1, 1, 1); // Ensure full opacity
            spriteBatch.draw(currentUltimateFrame,
                0, 0,  // Start at bottom-left corner
                viewport.getWorldWidth(),  // Full width
                viewport.getWorldHeight()); // Full height

            // Restore original color
            spriteBatch.setColor(originalColor);
        }

        spriteBatch.end();

        // Add this in the render method, before the stage.draw() call
        if (!currentTooltip.isEmpty()) {
            tooltipTimer += Gdx.graphics.getDeltaTime();
            if (tooltipTimer >= TOOLTIP_DELAY) {
                showTooltip = true;
            }
        }

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();





        if (!isEnemyTurn && currentTurn >= 0 && currentTurn < characters.size()) {
            spriteBatch.begin();
            // Draw Skill Cost Text
            String skillCostText = currentCharacter.getSkillCost() + " SP";
            GlyphLayout skillGlyphLayout = new GlyphLayout(SKILLS_FONT, skillCostText);
            float skillTextX = skillButton.getX() + skillButton.getWidth() - skillGlyphLayout.width - 10;
            float skillTextY = skillButton.getY() + 20;
            SKILLS_FONT.draw(spriteBatch, skillGlyphLayout, skillTextX, skillTextY);

            // Draw Ultimate Cooldown Text
            int currentCooldown = ultimateCooldowns.get(characters.get(currentTurn));

            if (currentCooldown > 0) {
                String cooldownText = currentCooldown + (currentCooldown == 1 ? " turn" : " turns");
                GlyphLayout ultimateGlyphLayout = new GlyphLayout(SKILLS_FONT, cooldownText);
                float ultX = ultimateButton.getX() + (ultimateButton.getWidth() - ultimateGlyphLayout.width) / 2;
                float ultY = ultimateButton.getY() + (ultimateButton.getHeight() + ultimateGlyphLayout.height) / 2;
                SKILLS_FONT.draw(spriteBatch, ultimateGlyphLayout, ultX, ultY);
            } else {
                String readyText = "READY";
                GlyphLayout readyGlyphLayout = new GlyphLayout(SKILLS_FONT, readyText);
                float readyX = ultimateButton.getX() + ultimateButton.getWidth() - readyGlyphLayout.width - 10;
                float readyY = ultimateButton.getY() + 20;
                SKILLS_FONT.draw(spriteBatch, readyGlyphLayout, readyX, readyY);
            }
            spriteBatch.end();
        }
        spriteBatch.begin();
        if (umbra != null && umbra.isUltimatePlaying()) {
            TextureRegion ultimateFrame = umbra.getCurrentUltimateFrame();

            // Save original color
            Color original = spriteBatch.getColor();
            spriteBatch.setColor(Color.WHITE);

            // Draw fullscreen
            spriteBatch.draw(ultimateFrame,
                0, 0,
                viewport.getWorldWidth(),
                viewport.getWorldHeight());

            // Restore color
            spriteBatch.setColor(original);
        }
        if (nova != null && nova.isUltimatePlaying()) {
            TextureRegion ultimateFrame = nova.getCurrentUltimateFrame();

            // Save original color
            Color original = spriteBatch.getColor();
            spriteBatch.setColor(Color.WHITE);

            // Draw fullscreen
            spriteBatch.draw(ultimateFrame,
                0, 0,
                viewport.getWorldWidth(),
                viewport.getWorldHeight());

            // Restore color
            spriteBatch.setColor(original);
        }
        if (jina != null && jina.isUltimatePlaying()) {
            TextureRegion ultimateFrame = jina.getCurrentUltimateFrame();
            // Save original color
            Color original = spriteBatch.getColor();
            spriteBatch.setColor(Color.WHITE);
            // Draw fullscreen
            spriteBatch.draw(ultimateFrame,
                0, 0,
                viewport.getWorldWidth(),
                viewport.getWorldHeight());
            // Restore color
            spriteBatch.setColor(original);
        }
        spriteBatch.end();
        // Handle enemy turn
        if (isEnemyTurn) {
            enemyTurnTimer -= delta;
            if (enemyTurnTimer <= 0) {
                isEnemyTurn = false;
                currentTurn = findFirstAliveCharacter();
                if (currentTurn == -1) {
                    checkBattleConditions();
                } else {
                    updateCurrentCharacter();
                    updateButtons();
                }
                debugTurnState("Enemy turn finished");
            }
        }
        spriteBatch.begin();
        Iterator<HealingAnimation> iterator = activeHealingAnimations.iterator();
        while (iterator.hasNext()) {
            HealingAnimation anim = iterator.next();
            anim.timer += delta;

            if (anim.timer < healAnimationDuration) {
                TextureRegion frame = healAnimation.getKeyFrame(anim.timer, false);
                spriteBatch.draw(frame, anim.position.x - 100, anim.position.y - 120, 180, 180);
            } else {
                iterator.remove();
            }
        }
        spriteBatch.end();

        if (playCloseTransition) {
            spriteBatch.begin();
            closeTransitionStateTime += delta;
            TextureRegion currentFrame = closeTransitionAnimation.getKeyFrame(closeTransitionStateTime, false);

            // Draw in top left corner (adjust size as needed)
            spriteBatch.draw(currentFrame, 0, viewport.getWorldHeight()-900, 1600, 900);

            // Stop animation when it's finished
            if (closeTransitionAnimation.isAnimationFinished(closeTransitionStateTime)) {
                playCloseTransition = false;
                closeTransitionStateTime = 0;
            }
            spriteBatch.end();
        }
        if(hasStarted) {
            if (playOpenTransition) {
                openTransitionStateTime += delta;
                spriteBatch.begin();
                TextureRegion currentFrame = openTransitionAnimation.getKeyFrame(openTransitionStateTime, false);
                spriteBatch.draw(currentFrame, 0, viewport.getWorldHeight() - 900, 1600, 900);
                spriteBatch.end();

                if (openTransitionAnimation.isAnimationFinished(openTransitionStateTime)) {
                    playOpenTransition = false;
                    openTransitionStateTime = 0;
                }
                return;
            }
            hasStarted=false;
        }

        if (showTooltip && !currentTooltip.isEmpty()) {
            // Calculate tooltip dimensions
            GlyphLayout layout = new GlyphLayout(tooltipFont, currentTooltip);
            float padding = 10f;
            float width = layout.width + padding * 2;
            float height = layout.height + padding * 2;

            // Position tooltip (adjust as needed)
            float x = Math.min(tooltipX, viewport.getWorldWidth() - width - 10);
            float y = Math.min(tooltipY + 30, viewport.getWorldHeight() - height - 10);

            // Draw background
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.9f);
            shapeRenderer.rect(x, y, width, height);
            shapeRenderer.end();

            // Draw text
            spriteBatch.begin();
            tooltipFont.draw(spriteBatch, currentTooltip, x + padding, y + height - padding);
            spriteBatch.end();
        }
    }

    private void enemyAttack() {
        // First determine if we need to wait based on the last attack
        float waitTime = 0f;

        // Check which character's turn it was and if they used an ultimate
        if (currentTurn >= 0 && currentTurn < characters.size()) {
            String characterName = characters.get(currentTurn);
            if (characterName.equals("Umbra") && umbra != null && umbra.isUltimatePlaying()) {
                waitTime = 2.61f;
            } else if (characterName.equals("Nova") && nova != null && nova.isUltimatePlaying()) {
                waitTime = 2.9f;
            } else if (characterName.equals("Jina") && jina != null && jina.isUltimatePlaying()) {
                waitTime = 4.1f;
            }
        }

        if (waitTime > 0) {
            // Use a Timer to schedule the enemy attack after the delay
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    startEnemyTurn();
                }
            }, waitTime);
        } else {
            // No delay needed, start enemy turn immediately
            startEnemyTurn();
        }
    }
    private void startEnemyTurn() {
        isEnemyTurn = true;
        enemyTurnTimer = ENEMY_TURN_DURATION;
        roundCount++;

        // Reset target selection state
        selectedTarget = null;
        cyclingTarget = null;
        isTargetLocked = false;
        targetSelectionTimer = 0f;

        // Wait for any ultimate to finish before proceeding
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (isAnyUltimatePlaying()) {
                    // If an ultimate is still playing, check again in 0.1 seconds
                    Timer.schedule(this, 0.1f);
                    return;
                }

                // Only proceed with target selection if it's still the enemy's turn
                if (isEnemyTurn) {
                    startTargetSelection();
                }
            }
        }, 0f);
    }
    private void startTargetSelection() {
        // Begin the target selection phase
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!isEnemyTurn) return;

                List<Character_BattleStats> aliveTargets = getAliveCharacters();
                if (!aliveTargets.isEmpty()) {
                    // Start cycling through targets
                    targetSelectionTimer = 0f;
                    isTargetLocked = false;

                    // After cycling duration, lock onto a target
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            if (!isEnemyTurn) return;

                            // Lock onto a random target
                            int randomIndex = (int)(Math.random() * aliveTargets.size());
                            selectedTarget = aliveTargets.get(randomIndex);
                            isTargetLocked = true;

                            // After a brief delay, deal damage
                            Timer.schedule(new Timer.Task() {
                                @Override
                                public void run() {
                                    if (selectedTarget != null && selectedTarget.getHP() > 0) {
                                        int damage = enemy.getAtk();
                                        selectedTarget.takeDamage(damage);

                                        if (selectedTarget instanceof Character_Umbra) {
                                            umbra.playHitAnimation();
                                        } else if (selectedTarget instanceof Character_Nova) {
                                            nova.playHitAnimation();
                                        } else if (selectedTarget instanceof Character_Jina) {
                                            jina.playHitAnimation();
                                        }

                                        // Clear the target after damage is dealt
                                        selectedTarget = null;
                                        isTargetLocked = false;
                                    }

                                    // End enemy turn after attack
                                    endEnemyTurn();
                                }
                            }, 0.5f); // Delay after locking target
                        }
                    }, TARGET_SELECTION_DURATION); // Cycling duration
                } else {
                    // No alive targets, end turn immediately
                    endEnemyTurn();
                }
            }
        }, 0f);
    }
    private void endEnemyTurn() {
        isEnemyTurn = false;
        currentTurn = findFirstAliveCharacter();
        if (currentTurn == -1) {
            checkBattleConditions();
        } else {
            updateCurrentCharacter();
            updateButtons();
        }
        debugTurnState("Enemy turn finished");
    }
    private void updateTargetSelection(float delta) {
        if (!isEnemyTurn || isTargetLocked || isAnyUltimatePlaying()) return;

        targetSelectionTimer += delta;

        List<Character_BattleStats> aliveTargets = getAliveCharacters();
        if (aliveTargets.isEmpty()) return;

        // Cycling phase
        if (targetSelectionTimer < TARGET_SELECTION_DURATION) {
            int targetIndex = (int)((targetSelectionTimer / targetCycleSpeed) % aliveTargets.size());
            cyclingTarget = aliveTargets.get(targetIndex);
        }
    }
    private List<Character_BattleStats> getAliveCharacters() {
        List<Character_BattleStats> aliveTargets = new ArrayList<>();
        if (umbra != null && umbra.getHP() > 0 && characters.contains("Umbra")) {
            aliveTargets.add(umbra);
        }
        if (nova != null && nova.getHP() > 0 && characters.contains("Nova")) {
            aliveTargets.add(nova);
        }
        if (jina != null && jina.getHP() > 0 && characters.contains("Jina")) {
            aliveTargets.add(jina);
        }
        return aliveTargets;
    }

    private int findFirstAliveCharacter() {
        for (int i = 0; i < characters.size(); i++) {
            String characterName = characters.get(i);
            if (!isCharacterDefeated(characterName)) {
                // Additional verification
                if (characterName.equals("Umbra") && umbra.getHP() <= 0) {
                    continue; // Double-check Umbra's HP
                }
                return i;
            }
        }
        return -1;
    }

    // ===== GAME CONTROLS SECTION =====
    private void initGameControls() {
        controlsBatch = new SpriteBatch();
        controlsShapeRenderer = new ShapeRenderer();
        controlsLayout = new GlyphLayout();

        prefs = Gdx.app.getPreferences("game_settings");
        loadSettings();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Poppins-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = (int) (42 * scaleFactor);
        parameter.borderWidth = 2f * scaleFactor;
        parameter.borderColor = new Color(0.1f, 0.1f, 0.2f, 1f);
        parameter.color = secondAccentColor;
        controlsTitleFont = generator.generateFont(parameter);

        parameter.size = (int) (24 * scaleFactor);
        parameter.borderWidth = 1.5f * scaleFactor;
        parameter.borderColor = new Color(0.05f, 0.05f, 0.1f, 1f);
        parameter.color = Color.WHITE;
        controlsFont = generator.generateFont(parameter);

        parameter.size = (int) (28 * scaleFactor);
        parameter.color = Color.WHITE;
        parameter.borderWidth = 1.5f * scaleFactor;
        controlsButtonFont = generator.generateFont(parameter);

        generator.dispose();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        float centerX = screenWidth / 2 - (sliderWidth * scaleFactor) / 2;
        float startY = screenHeight * 0.7f;
        float spacing = 70f * scaleFactor;

        musicSlider = new Rectangle(centerX, startY, sliderWidth * scaleFactor, sliderHeight * scaleFactor);
        soundSlider = new Rectangle(centerX, startY - spacing, sliderWidth * scaleFactor, sliderHeight * scaleFactor);
        brightnessSlider = new Rectangle(centerX, startY - spacing * 2, sliderWidth * scaleFactor, sliderHeight * scaleFactor);

        float buttonWidth = 180f * scaleFactor;
        float buttonHeight = 60f * scaleFactor;
        float buttonSpacing = 15f * scaleFactor;
        float totalButtonWidth = buttonWidth * 3 + buttonSpacing * 2;
        float buttonsStartX = screenWidth / 2 - totalButtonWidth / 2 - 50 * scaleFactor + screenWidth * 0.02f - 10;
        float buttonY = startY - spacing * 3.5f - 30 * scaleFactor;

        backButton = new Rectangle(buttonsStartX, buttonY, buttonWidth, buttonHeight);
        restartButton = new Rectangle(buttonsStartX + buttonWidth + buttonSpacing, buttonY, buttonWidth, buttonHeight);
        quitButton = new Rectangle(buttonsStartX + buttonWidth * 2 + buttonSpacing * 2, buttonY, buttonWidth, buttonHeight);
    }

    private void renderGameControls(float delta) {
        Color bgWithBrightness = backgroundColor.cpy().mul(brightness * 0.5f + 0.5f);
        ScreenUtils.clear(bgWithBrightness.r, bgWithBrightness.g, bgWithBrightness.b, 1);

        updateControlsAnimations(delta);
        handleControlsInput();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        controlsShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        controlsShapeRenderer.setColor(accentColor.r, accentColor.g, accentColor.b, 0.05f);
        controlsShapeRenderer.rect(0, screenHeight * 0.4f, screenWidth, screenHeight * 0.2f);
        drawControlsSliders();
        drawControlsButtons();
        controlsShapeRenderer.end();

        controlsShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        drawControlsSliderOutlines();
        drawControlsButtonOutlines();
        controlsShapeRenderer.end();

        controlsBatch.begin();
        drawControlsText();
        controlsBatch.end();

        saveSettings();
    }

    private void handleControlsInput() {
        float mouseX = Gdx.input.getX();
        float mouseY = screenHeight - Gdx.input.getY();
        boolean volumeChanged = false;

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            // Play click sound for any button press
            sfx.playSoundEffect("buttonClicked.wav", 0.5f);

            if (backButton.contains(mouseX, mouseY)) {
                saveSettings();
                showControls = false;
                Gdx.input.setInputProcessor(stage);
                return;
            }

            if (restartButton.contains(mouseX, mouseY)) {
                saveSettings();
                resetCurrentBattle();
                showControls = false;
                return;
            }

            if (quitButton.contains(mouseX, mouseY)) {
                if (!askingToQuit) {
                    // First click - show confirmation
                    askingToQuit = true;
                    sfx.playSoundEffect("buttonClicked.wav", 0.5f);
                } else {
                    // Second click - actually quit
                    saveSettings();
                    Gdx.app.exit();
                }
                return;
            }

            // If clicked elsewhere after asking to quit, cancel the quit confirmation
            if (askingToQuit) {
                askingToQuit = false;
            }

            // Slider handling remains the same
            if (isPointNearSlider(musicSlider, mouseX, mouseY, musicVolume)) {
                isDraggingMusic = true;
                volumeChanged = true;
            } else if (isPointNearSlider(soundSlider, mouseX, mouseY, soundVolume)) {
                isDraggingSound = true;
                volumeChanged = true;
            } else if (isPointNearSlider(brightnessSlider, mouseX, mouseY, brightness)) {
                isDraggingBrightness = true;
            }
        }

        // Rest of the method remains unchanged...
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (isDraggingMusic) {
                float newVolume = calculateSliderValue(musicSlider, mouseX);
                if (newVolume != musicVolume) {
                    musicVolume = newVolume;
                    sfx.setMusicVolume(musicVolume);
                    volumeChanged = true;
                }
            } else if (isDraggingSound) {
                float newVolume = calculateSliderValue(soundSlider, mouseX);
                if (newVolume != soundVolume) {
                    soundVolume = newVolume;
                    sfx.setSoundVolume(soundVolume);
                    volumeChanged = true;

                    if (soundVolume > 0.01f && !Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                        sfx.playSoundEffect("hover_button.wav", 0.1f);
                    }
                }
            } else if (isDraggingBrightness) {
                brightness = calculateSliderValue(brightnessSlider, mouseX);
            }
        } else {
            if (isDraggingMusic || isDraggingSound) {
                saveSettings();
                if (isDraggingSound && soundVolume > 0.01f) {
                    sfx.playSoundEffect("buttonClicked.wav", 0);
                }
            }
            isDraggingMusic = false;
            isDraggingSound = false;
            isDraggingBrightness = false;
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (musicSlider.contains(mouseX, mouseY)) {
                musicVolume = calculateSliderValue(musicSlider, mouseX);
                sfx.setMusicVolume(musicVolume);
                game.setMusicVolume(musicVolume); // Update GameTransitions
            } else if (soundSlider.contains(mouseX, mouseY)) {
                soundVolume = calculateSliderValue(soundSlider, mouseX);
                sfx.setSoundVolume(soundVolume);
                game.setSoundVolume(soundVolume); // Update GameTransitions
            }
        }

        if (volumeChanged && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            saveSettings();
        }
    }

    private void drawControlsSliders() {
        Color musicSliderColor = new Color(0.1f, 0.5f, 0.8f, 1f);
        Color soundSliderColor = new Color(0.8f, 0.6f, 0.2f, 1f);
        Color brightnessSliderColor = new Color(0.4f, 0.8f, 0.3f, 1f);

        controlsShapeRenderer.setColor(sliderBgColor);
        controlsShapeRenderer.rect(musicSlider.x, musicSlider.y, musicSlider.width, musicSlider.height);
        controlsShapeRenderer.setColor(musicSliderColor);
        controlsShapeRenderer.rect(musicSlider.x, musicSlider.y, musicSlider.width * musicVolume, musicSlider.height);

        controlsShapeRenderer.setColor(sliderBgColor);
        controlsShapeRenderer.rect(soundSlider.x, soundSlider.y, soundSlider.width, soundSlider.height);
        controlsShapeRenderer.setColor(soundSliderColor);
        controlsShapeRenderer.rect(soundSlider.x, soundSlider.y, soundSlider.width * soundVolume, soundSlider.height);

        controlsShapeRenderer.setColor(sliderBgColor);
        controlsShapeRenderer.rect(brightnessSlider.x, brightnessSlider.y, brightnessSlider.width, brightnessSlider.height);
        controlsShapeRenderer.setColor(brightnessSliderColor);
        controlsShapeRenderer.rect(brightnessSlider.x, brightnessSlider.y, brightnessSlider.width * brightness, brightnessSlider.height);

        drawControlsKnob(musicSlider.x + musicSlider.width * musicVolume, musicSlider.y + musicSlider.height / 2, musicSliderColor);
        drawControlsKnob(soundSlider.x + soundSlider.width * soundVolume, soundSlider.y + soundSlider.height / 2, soundSliderColor);
        drawControlsKnob(brightnessSlider.x + brightnessSlider.width * brightness, brightnessSlider.y + sliderHeight / 2, brightnessSliderColor);
    }

    private void drawControlsKnob(float x, float y, Color color) {
        controlsShapeRenderer.setColor(color);
        controlsShapeRenderer.circle(x, y, knobRadius * scaleFactor);
        controlsShapeRenderer.setColor(color.r * 1.2f, color.g * 1.2f, color.b * 1.2f, 0.5f);
        controlsShapeRenderer.circle(x, y, (knobRadius + 4) * scaleFactor);
    }

    private void drawControlsSliderOutlines() {
        controlsShapeRenderer.setColor(accentColor.r, accentColor.g, accentColor.b, 0.5f);
        controlsShapeRenderer.rect(musicSlider.x, musicSlider.y, musicSlider.width, musicSlider.height);
        controlsShapeRenderer.rect(soundSlider.x, soundSlider.y, soundSlider.width, soundSlider.height);
        controlsShapeRenderer.rect(brightnessSlider.x, brightnessSlider.y, brightnessSlider.width, brightnessSlider.height);
    }

    private void drawControlsButtons() {
        drawAnimatedControlsButton(backButton, buttonColor, buttonHoverColor, 0);
        drawAnimatedControlsButton(restartButton, buttonColor, buttonHoverColor, 1);
        drawAnimatedControlsButton(quitButton, buttonColor, buttonHoverColor, 2);
    }

    private void drawAnimatedControlsButton(Rectangle button, Color baseColor, Color hoverColor, int buttonIndex) {
        float animation = buttonAnimations[buttonIndex];
        Color renderColor = baseColor.cpy().lerp(hoverColor, animation);

        controlsShapeRenderer.setColor(renderColor);
        controlsShapeRenderer.rect(button.x, button.y, button.width, button.height);

        if (animation > 0) {
            controlsShapeRenderer.setColor(accentColor.r, accentColor.g, accentColor.b, animation * 0.3f);
            controlsShapeRenderer.rect(button.x - 2, button.y - 2, button.width + 4, button.height + 4);
        }
    }

    private void drawControlsButtonOutlines() {
        controlsShapeRenderer.setColor(accentColor.r, accentColor.g, accentColor.b, 0.8f + pulse * 0.2f);
        controlsShapeRenderer.rect(backButton.x, backButton.y, backButton.width, backButton.height);
        controlsShapeRenderer.rect(restartButton.x, restartButton.y, restartButton.width, restartButton.height);
        controlsShapeRenderer.rect(quitButton.x, quitButton.y, quitButton.width, quitButton.height);
    }

    private void drawControlsText() {
        controlsLayout.setText(controlsTitleFont, "VORTEX CONTROLS");
        controlsTitleFont.draw(controlsBatch, "VORTEX CONTROLS",
            screenWidth / 2 - controlsLayout.width / 2,
            screenHeight * 0.88f + 20 * scaleFactor);

        controlsFont.setColor(Color.WHITE);
        controlsLayout.setText(controlsFont, "MUSIC VOLUME");
        float musicLabelX = musicSlider.x - controlsLayout.width - 40 * scaleFactor;
        float musicLabelY = musicSlider.y + (sliderHeight * scaleFactor) / 2 + controlsLayout.height / 2;
        controlsFont.draw(controlsBatch, "MUSIC VOLUME", musicLabelX, musicLabelY);

        String musicPercent = String.format("%.0f%%", musicVolume * 100);
        controlsLayout.setText(controlsFont, musicPercent);
        float musicValueX = musicSlider.x + (sliderWidth * scaleFactor) + 40 * scaleFactor;
        float musicValueY = musicSlider.y + (sliderHeight * scaleFactor) / 2 + controlsLayout.height / 2;
        controlsFont.draw(controlsBatch, musicPercent, musicValueX, musicValueY);

        controlsLayout.setText(controlsFont, "SOUND EFFECTS");
        float soundLabelX = soundSlider.x - controlsLayout.width - 40 * scaleFactor;
        float soundLabelY = soundSlider.y + (sliderHeight * scaleFactor) / 2 + controlsLayout.height / 2;
        controlsFont.draw(controlsBatch, "SOUND EFFECTS", soundLabelX, soundLabelY);

        String soundPercent = String.format("%.0f%%", soundVolume * 100);
        controlsLayout.setText(controlsFont, soundPercent);
        float soundValueX = soundSlider.x + (sliderWidth * scaleFactor) + 40 * scaleFactor;
        float soundValueY = soundSlider.y + (sliderHeight * scaleFactor) / 2 + controlsLayout.height / 2;
        controlsFont.draw(controlsBatch, soundPercent, soundValueX, soundValueY);

        controlsLayout.setText(controlsFont, "BRIGHTNESS");
        float brightnessLabelX = brightnessSlider.x - controlsLayout.width - 40 * scaleFactor;
        float brightnessLabelY = brightnessSlider.y + (sliderHeight * scaleFactor) / 2 + controlsLayout.height / 2;
        controlsFont.draw(controlsBatch, "BRIGHTNESS", brightnessLabelX, brightnessLabelY);

        String brightnessPercent = String.format("%.0f%%", brightness * 100);
        controlsLayout.setText(controlsFont, brightnessPercent);
        float brightnessValueX = brightnessSlider.x + (sliderWidth * scaleFactor) + 40 * scaleFactor;
        float brightnessValueY = brightnessSlider.y + (sliderHeight * scaleFactor) / 2 + controlsLayout.height / 2;
        controlsFont.draw(controlsBatch, brightnessPercent, brightnessValueX, brightnessValueY);

        drawCenteredControlsButtonText(controlsBatch, backButton, "BACK", 0);
        drawCenteredControlsButtonText(controlsBatch, restartButton, "RESTART", 1);
        drawCenteredControlsButtonText(controlsBatch, quitButton, "QUIT", 2);
    }

    private void drawCenteredControlsButtonText(SpriteBatch batch, Rectangle button, String text, int buttonIndex) {
        controlsLayout.setText(controlsButtonFont, text);
        float scale = 1.0f + buttonAnimations[buttonIndex] * 0.1f;

        controlsButtonFont.setColor(Color.WHITE);
        controlsButtonFont.draw(batch, text,
            button.x + button.width / 2 - controlsLayout.width / 2,
            button.y + button.height / 2 + controlsLayout.height / 2);
    }

    private void updateControlsAnimations(float delta) {
        updateControlsButtonAnimation(backButton, 0, delta);
        updateControlsButtonAnimation(restartButton, 1, delta);
        updateControlsButtonAnimation(quitButton, 2, delta);

        pulse = (float) Math.sin(Gdx.graphics.getFrameId() * 0.05f) * pulseAmount;
    }

    private void updateControlsButtonAnimation(Rectangle button, int buttonIndex, float delta) {
        boolean isHovered = button.contains(Gdx.input.getX(), screenHeight - Gdx.input.getY());

        if (isHovered) {
            buttonAnimations[buttonIndex] = Math.min(1.0f, buttonAnimations[buttonIndex] + delta * animationSpeed);
        } else {
            buttonAnimations[buttonIndex] = Math.max(0.0f, buttonAnimations[buttonIndex] - delta * animationSpeed);
        }
    }

    public void resetCurrentBattle() {
        // Reset character stats
        if (umbra != null) {
            umbra.setHP(umbra.getMaxHP());
            umbra.setAnimation(umbra.getIdleAnimation());
        }
        if (nova != null) {
            nova.setHP(nova.getMaxHP());
            nova.setAnimation(nova.getIdleAnimation());
        }
        if (jina != null) {
            jina.setHP(jina.getMaxHP());
            jina.setAnimation(jina.getIdleAnimation());
        }

        // Reset enemy
        enemyCurrentHp = enemy.getMaxHP();

        // Reset battle state
        currentTurn = 0;
        skillPoints = 1;
        roundCount = 0;
        isEnemyTurn = false;

        // Reset cooldowns
        ultimateCooldowns.clear();
        if (umbra != null) ultimateCooldowns.put("Umbra", umbra.getUltCooldown());
        if (nova != null) ultimateCooldowns.put("Nova", nova.getUltCooldown());
        if (jina != null) ultimateCooldowns.put("Jina", jina.getUltCooldown());

        // Reset animations
        isPlayingUltimate = false;
        isPlayingHeal = false;

        // Update UI
        updateCurrentCharacter();
        updateButtons();

        // Restart music
        sfx.stopAudio();
        sfx.playMusic(musicFile);

        // Play opening transition
        playOpenTransition = true;
        openTransitionStateTime = 0;
        hasStarted = true;
    }

    private boolean isPointNearSlider(Rectangle slider, float x, float y, float value) {
        float knobX = slider.x + slider.width * value;
        float knobY = slider.y + slider.height / 2;
        return Vector2.dst(knobX, knobY, x, y) <= knobRadius * 2;
    }

    private float calculateSliderValue(Rectangle slider, float mouseX) {
        float relativeX = mouseX - slider.x;
        float value = relativeX / slider.width;
        return Math.max(0, Math.min(1, value));
    }

    private void loadSettings() {
        musicVolume = prefs.getFloat("musicVolume", 1.0f);
        soundVolume = prefs.getFloat("soundVolume", 1.0f);
        brightness = prefs.getFloat("brightness", 1.0f);
    }

    private void saveSettings() {
        prefs.putFloat("musicVolume", musicVolume);
        prefs.putFloat("soundVolume", soundVolume);
        prefs.putFloat("brightness", brightness);
        prefs.flush();
    }

    private void resizeControls() {
        float centerX = screenWidth / 2 - (sliderWidth * scaleFactor) / 2;
        float startY = screenHeight * 0.7f;
        float spacing = 70f * scaleFactor;

        musicSlider.x = centerX;
        musicSlider.y = startY;
        musicSlider.width = sliderWidth * scaleFactor;
        musicSlider.height = sliderHeight * scaleFactor;

        soundSlider.x = centerX;
        soundSlider.y = startY - spacing;
        soundSlider.width = sliderWidth * scaleFactor;
        soundSlider.height = sliderHeight * scaleFactor;

        brightnessSlider.x = centerX;
        brightnessSlider.y = startY - spacing * 2;
        brightnessSlider.width = sliderWidth * scaleFactor;
        brightnessSlider.height = sliderHeight * scaleFactor;

        float buttonWidth = 180f * scaleFactor;
        float buttonHeight = 60f * scaleFactor;
        float buttonSpacing = 15f * scaleFactor;
        float totalButtonWidth = buttonWidth * 3 + buttonSpacing * 2;
        float buttonsStartX = screenWidth / 2 - totalButtonWidth / 2 - 50 * scaleFactor + screenWidth * 0.02f - 10;
        float buttonY = startY - spacing * 3.5f - 30 * scaleFactor;

        backButton.x = buttonsStartX;
        backButton.y = buttonY;
        backButton.width = buttonWidth;
        backButton.height = buttonHeight;

        restartButton.x = buttonsStartX + buttonWidth + buttonSpacing;
        restartButton.y = buttonY;
        restartButton.width = buttonWidth;
        restartButton.height = buttonHeight;

        quitButton.x = buttonsStartX + buttonWidth * 2 + buttonSpacing * 2;
        quitButton.y = buttonY;
        quitButton.width = buttonWidth;
        quitButton.height = buttonHeight;
    }
// ===== END OF GAME CONTROLS SECTION =====

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        screenWidth = width;
        screenHeight = height;

        if (showControls) {
            resizeControls();
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        bitmapFont.dispose();
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        roadTileTexture.dispose();
        enabledSkillPointTexture.dispose();
        disabledSkillPointTexture.dispose();
        turnIndicatorTexture.dispose();
        enemyTurnIndicatorTexture.dispose();
        if (targetTexture != null) targetTexture.dispose();
        if (umbra != null) umbra.dispose();
        if (nova != null) nova.dispose();
        if (jina != null) jina.dispose();
        if (openTransitionTexture != null) openTransitionTexture.dispose();
        if (closeTransitionTexture != null) closeTransitionTexture.dispose();
        if (controlsBatch != null) controlsBatch.dispose();
        if (controlsFont != null) controlsFont.dispose();
        if (controlsTitleFont != null) controlsTitleFont.dispose();
        if (controlsButtonFont != null) controlsButtonFont.dispose();
        if (controlsShapeRenderer != null) controlsShapeRenderer.dispose();
        if (hpBarTexture != null) hpBarTexture.dispose();
        if (enemyHpBarTexture != null) enemyHpBarTexture.dispose();
        if (healAnimationSheet != null) healAnimationSheet.dispose();

        // Add to dispose() method
        if (tooltipFont != null) tooltipFont.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }

    private void checkBattleConditions() {
        // Check if enemy is defeated
        if (enemyCurrentHp <= 0) {
            playCloseTransition = true;
            closeTransitionStateTime = 0;
            sfx.stopAudio();
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    game.setScreen(new BattleResultScreen(
                        game,
                        true,
                        "ResultScreenBG.png",
                        "MainMenuMusic.wav",
                        onBattleComplete,
                        () -> {},
                        roundCount // Add round count here
                    ));
                }
            }, 3.75f);
            return;
        }

        // Check if all characters are defeated
        boolean allDefeated = true;
        if (umbra != null && umbra.getHP() > 0) allDefeated = false;
        if (nova != null && nova.getHP() > 0) allDefeated = false;
        if (jina != null && jina.getHP() > 0) allDefeated = false;

        if (allDefeated) {
            playCloseTransition = true;
            closeTransitionStateTime = 0;
            sfx.stopAudio();
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    game.setScreen(new BattleResultScreen(
                        game,
                        false,
                        "ResultScreenBG.png",
                        "MainMenuMusic.wav",
                        () -> {},
                        () -> {
                            game.setScreen(new BattleClass(
                                game, universeName,
                                enemy,
                                umbra != null, nova != null, jina != null,
                                backgroundTexture.toString().replace("Backgrounds/", ""),
                                roadTileTexture.toString().replace("Tiles/", ""),
                                musicFile,
                                onBattleComplete
                            ));
                        },
                        0 // Pass 0 for defeat (won't be shown anyway)
                    ));
                }
            }, 3.75f);
        }
    }
    private void debugTurnState(String event) {
        String turnCharacter = (currentTurn >= 0 && currentTurn < characters.size())
            ? characters.get(currentTurn) : "NONE";

        String actualCharacter = (currentCharacter != null)
            ? currentCharacter.getClass().getSimpleName().replace("Character_", "")
            : "NONE";

        String buttonSkills = (currentCharacter != null)
            ? "[Basic: " + currentCharacter.getBasicAtkImage() + "]"
            : "NO_SKILLS";

        System.out.println(
            "\n===== TURN DEBUG =====" +
                "\nEvent: " + event +
                "\nTurn Indicator: " + turnCharacter +
                "\nActual Character: " + actualCharacter +
                "\nButtons Showing: " + buttonSkills +
                "\nUmbra HP: " + (umbra != null ? umbra.getHP() : "DEAD") +
                "\nNova HP: " + (nova != null ? nova.getHP() : "DEAD") +
                "\nJina HP: " + (jina != null ? jina.getHP() : "DEAD") +
                "\n======================="
        );
    }
    private void debugEnemyHP() {
        System.out.println(
            "===== ENEMY HP DEBUG ====\n" +
                "Current HP: " + enemyCurrentHp + "/" + enemyMaxHp + "\n" +
                "Percentage: " + ((float)enemyCurrentHp/enemyMaxHp)*100 + "%\n" +
                "========================="
        );
    }

    public int getRoundCount() {
        return roundCount;
    }
    private Vector2 calculateHealPosition(Character_BattleStats character) {
        float totalCharactersWidth = characters.size() * 300;
        float currentStartX = (viewport.getWorldWidth() - totalCharactersWidth) / 2;

        if (character instanceof Character_Umbra) {
            return new Vector2(
                currentStartX + characters.indexOf("Umbra") * 300 + 125,
                PLATFORM_Y + 160
            );
        } else if (character instanceof Character_Nova) {
            return new Vector2(
                currentStartX + characters.indexOf("Nova") * 300 + 125,
                PLATFORM_Y + 160
            );
        } else {
            return new Vector2(
                currentStartX + characters.indexOf("Jina") * 300 + 125,
                PLATFORM_Y + 160
            );
        }
    }
}
