package com.vortex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vortex.SFX.PlayAudio;
import com.vortex.CharacterStats.Character_Umbra;

import java.util.ArrayList;
import java.util.List;

public class BattleClass implements Screen {
    private SpriteBatch spriteBatch;
    private BitmapFont bitmapFont;
    private Stage stage;
    private Skin skin;

    private ImageButton attackButton, skillButton, ultimateButton, settingsButton;
    private Viewport viewport;
    private Texture backgroundTexture;
    private Texture roadTileTexture;
    private Texture enabledSkillPointTexture;
    private Texture disabledSkillPointTexture;
    private Texture turnIndicatorTexture;
    private Character_Umbra umbra;
    private List<String> characters;
    private int currentTurn = 0;
    private int skillPoints = 1;
    private int ultimateCooldown;

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

    private boolean showControls = false; // Flag to show/hide GameControls

    public BattleClass(String universeName, boolean hasUmbra, boolean hasNova, boolean hasJina,
                       String background, String roadTile, String musicFile) {
        this.game = game;
        this.universeName = universeName;
        spriteBatch = new SpriteBatch();
        bitmapFont = new BitmapFont();
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        viewport = new FitViewport(1600, 900);
        viewport.apply();
        PlayAudio sfx = new PlayAudio();
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("Backgrounds/" + background));
        roadTileTexture = new Texture(Gdx.files.internal("Tiles/" + roadTile));
        enabledSkillPointTexture = new Texture(Gdx.files.internal("BattleAssets/enabledSkillpoint.png"));
        disabledSkillPointTexture = new Texture(Gdx.files.internal("BattleAssets/disabledSkillpoint.png"));
        turnIndicatorTexture = new Texture(Gdx.files.internal("BattleAssets/turnIndicator.png"));

        if (hasUmbra) {
            umbra = new Character_Umbra();
            ultimateCooldown = umbra.getUltCooldown();
        }

        sfx.playMusic(musicFile);

        characters = new ArrayList<>();
        if (hasUmbra) characters.add("Umbra");
        if (hasNova) characters.add("Nova");
        if (hasJina) characters.add("Jina");

        attackButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal(umbra.getBasicAtkImage()))));
        attackButton.getColor().a = 0.85f;
        int skillPointY = 60;
        attackButton.setPosition(100, skillPointY);
        attackButton.setSize(120, 120);

        skillButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal(umbra.getSkillImage()))));
        if (skillPoints < umbra.getSkillCost()) {
            skillButton.getColor().a = 0.5f;
        } else {
            skillButton.getColor().a = 0.85f;
        }
        skillButton.setPosition(attackButton.getX() + attackButton.getWidth() + 20, skillPointY);
        skillButton.setSize(120, 120);

        ultimateButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal(umbra.getUltImage()))));
        if (ultimateCooldown > 0) {
            ultimateButton.getColor().a = 0.5f;
        } else {
            ultimateButton.getColor().a = 0.85f;
        }
        ultimateButton.setPosition(skillButton.getX() + skillButton.getWidth() + 20, skillPointY);
        ultimateButton.setSize(120, 120);

        settingsButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("BattleAssets/setting_icon.png"))));
        settingsButton.setPosition(viewport.getWorldWidth() - 150, viewport.getWorldHeight() - 150);
        settingsButton.setSize(100, 100);

        attackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addSkillPoint();
                nextTurn();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                attackButton.getColor().a = 1.0f;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                attackButton.getColor().a = 0.85f;
            }
        });

        skillButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (skillPoints >= umbra.getSkillCost()) {
                    useSkill();
                    nextTurn();
                } else {
                    skillFlashing = true;
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (skillPoints >= umbra.getSkillCost()) {
                    skillButton.getColor().a = 1.0f;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (skillPoints >= umbra.getSkillCost()) {
                    skillButton.getColor().a = 0.85f;
                } else {
                    skillButton.getColor().a = 0.5f;
                }
            }
        });

        ultimateButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (ultimateCooldown <= 0) {
                    useUltimate();
                    nextTurn();
                } else {
                    ultimateFlashing = true;
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (ultimateCooldown <= 0) {
                    ultimateButton.getColor().a = 1.0f;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (ultimateCooldown <= 0) {
                    ultimateButton.getColor().a = 0.85f;
                } else {
                    ultimateButton.getColor().a = 0.5f;
                }
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showControls = true;
                initGameControls(); // Initialize GameControls here
                Gdx.input.setInputProcessor(stage); // keep the input processor to stage
            }
        });

        stage.addActor(attackButton);
        stage.addActor(skillButton);
        stage.addActor(ultimateButton);
        stage.addActor(settingsButton);
    }

    private void addSkillPoint() {
        if (skillPoints < 3) {
            skillPoints++;
        }
    }

    private void useSkill() {
        if (skillPoints >= umbra.getSkillCost()) {
            skillPoints -= umbra.getSkillCost();
        }
    }

    private void useUltimate() {
        if (ultimateCooldown <= 0) {
            ultimateCooldown = umbra.getUltCooldown();
        }
    }

    private void nextTurn() {
        if (ultimateCooldown > 0) {
            ultimateCooldown--;
        }
        currentTurn = (currentTurn + 1) % characters.size();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (!showControls) {
            renderBattle(delta);
        } else {
            renderGameControls(delta);
        }
    }

    private void renderBattle(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();

        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        bitmapFont.draw(spriteBatch, universeName, 750, 850);
        bitmapFont.draw(spriteBatch, "Boss Name", 750, 550);
        bitmapFont.draw(spriteBatch, "[ BOSS ]", 780, 500);

        spriteBatch.setColor(0, 0, 0, 1);
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), PLATFORM_Y);
        spriteBatch.setColor(1, 1, 1, 1);

        int tilesAcross = (int) Math.ceil(viewport.getWorldWidth() / (TILE_SIZE * 4));
        for (int i = 0; i < tilesAcross; i++) {
            spriteBatch.draw(roadTileTexture, i * (TILE_SIZE * 4), PLATFORM_Y, TILE_SIZE * 4, TILE_SIZE * 4);
        }

        int skillPointSize = 60;
        int skillPointX = (int) viewport.getWorldWidth() - 250;
        int skillPointY = 80;
        int spacingSkill = skillPointSize + 5;

        for (int i = 0; i < 3; i++) {
            Texture skillPointTexture = (i < skillPoints) ? enabledSkillPointTexture : disabledSkillPointTexture;
            spriteBatch.draw(skillPointTexture, skillPointX + (i * spacingSkill), skillPointY, skillPointSize, skillPointSize);
        }

        float totalCharactersWidth = characters.size() * 300;
        float startX = (viewport.getWorldWidth() - totalCharactersWidth) / 2;

        for (int i = 0; i < characters.size(); i++) {
            String character = characters.get(i);
            float characterX = startX + i * 300;
            float characterY = PLATFORM_Y + 50;

            if (character.equals("Umbra")) {
                TextureRegion currentFrame = umbra.getIdleFrame(delta);
                spriteBatch.draw(currentFrame, characterX, characterY, 250, 250);
            }

            if (i == currentTurn) {
                float indicatorX = characterX + 100 - turnIndicatorTexture.getWidth() / 2;
                float indicatorY = characterY + 170;
                float indicatorWidth = turnIndicatorTexture.getWidth() * 4f;
                float indicatorHeight = turnIndicatorTexture.getHeight() * 4f;
                spriteBatch.draw(turnIndicatorTexture, indicatorX, indicatorY, indicatorWidth, indicatorHeight);
            }
        }

        spriteBatch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        spriteBatch.begin();
        bitmapFont.setColor(0.53f, 0.81f, 0.92f, 1f);
        bitmapFont.getData().setScale(2f);

        if (skillFlashing) {
            skillFlashTimer += delta;
            if (skillFlashTimer >= 0.1f) {
                skillFlashTimer = 0;
                skillFlashCounter++;
            }

            if (skillFlashCounter % 2 == 0) {
                bitmapFont.setColor(1f, 0f, 0f, 1f);
            } else {
                bitmapFont.setColor(0.53f, 0.81f, 0.92f, 1f);
            }

            if (skillFlashCounter >= 6) {
                skillFlashing = false;
                skillFlashCounter = 0;
            }
        }

        String skillCostText = umbra.getSkillCost() + " SP";
        GlyphLayout skillGlyphLayout = new GlyphLayout(bitmapFont, skillCostText);
        float skillTextX = skillButton.getX() + skillButton.getWidth() - skillGlyphLayout.width - 10;
        float skillTextY = skillButton.getY() + 20;
        bitmapFont.draw(spriteBatch, skillGlyphLayout, skillTextX, skillTextY);

        bitmapFont.setColor(0.53f, 0.81f, 0.92f, 1f);

        if (ultimateFlashing) {
            ultimateFlashTimer += delta;
            if (ultimateFlashTimer >= 0.1f) {
                ultimateFlashTimer = 0;
                ultimateFlashCounter++;
            }

            if (ultimateFlashCounter % 2 == 0) {
                bitmapFont.setColor(1f, 0f, 0f, 1f);
            } else {
                bitmapFont.setColor(0.53f, 0.81f, 0.92f, 1f);
            }

            if (ultimateFlashCounter >= 6) {
                ultimateFlashing = false;
                ultimateFlashCounter = 0;
            }
        }

        if (ultimateCooldown > 0) {
            String cooldownText = ultimateCooldown + (ultimateCooldown == 1 ? " turn" : " turns");
            GlyphLayout ultimateGlyphLayout = new GlyphLayout(bitmapFont, cooldownText);
            float ultX = ultimateButton.getX() + (ultimateButton.getWidth() - ultimateGlyphLayout.width) / 2;
            float ultY = ultimateButton.getY() + (ultimateButton.getHeight() + ultimateGlyphLayout.height) / 2;
            bitmapFont.draw(spriteBatch, ultimateGlyphLayout, ultX, ultY);
        } else {
            String readyText = "READY";
            GlyphLayout readyGlyphLayout = new GlyphLayout(bitmapFont, readyText);
            float readyX = ultimateButton.getX() + ultimateButton.getWidth() - readyGlyphLayout.width - 10;
            float readyY = ultimateButton.getY() + 20;
            bitmapFont.draw(spriteBatch, readyGlyphLayout, readyX, readyY);
        }

        bitmapFont.getData().setScale(1f);
        bitmapFont.setColor(1, 1, 1, 1);
        spriteBatch.end();
    }

    // Initialize GameControls
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

    // Render GameControls
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
        controlsFont.draw(batch, text,
            button.x + button.width / 2 - controlsLayout.width / 2,
            button.y + button.height / 2 + controlsLayout.height / 2);
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

    private void handleControlsInput() {
        float mouseX = Gdx.input.getX();
        float mouseY = screenHeight - Gdx.input.getY();

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (backButton.contains(mouseX, mouseY)) {
                saveSettings();
                showControls = false;
                Gdx.input.setInputProcessor(stage); // Switch back to the stage
                return;
            }
            if (restartButton.contains(mouseX, mouseY)) {
                saveSettings();
                game.newGame();
                return;
            }
            if (quitButton.contains(mouseX, mouseY)) {
                saveSettings();
                Gdx.app.exit();
                return;
            }

            if (isPointNearSlider(musicSlider, mouseX, mouseY, musicVolume)) {
                isDraggingMusic = true;
            } else if (isPointNearSlider(soundSlider, mouseX, mouseY, soundVolume)) {
                isDraggingSound = true;
            } else if (isPointNearSlider(brightnessSlider, mouseX, mouseY, brightness)) {
                isDraggingBrightness = true;
            }
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (isDraggingMusic) {
                musicVolume = calculateSliderValue(musicSlider, mouseX);
            } else if (isDraggingSound) {
                soundVolume = calculateSliderValue(soundSlider, mouseX);
            } else if (isDraggingBrightness) {
                brightness = calculateSliderValue(brightnessSlider, mouseX);
            }
        } else {
            isDraggingMusic = false;
            isDraggingSound = false;
            isDraggingBrightness = false;
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (musicSlider.contains(mouseX, mouseY)) {
                musicVolume = calculateSliderValue(musicSlider, mouseX);
            } else if (soundSlider.contains(mouseX, mouseY)) {
                soundVolume = calculateSliderValue(soundSlider, mouseX);
            } else if (brightnessSlider.contains(mouseX, mouseY)) {
                brightness = calculateSliderValue(brightnessSlider, mouseX);
            }
        }
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

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        screenWidth = width;
        screenHeight = height;

        if (showControls) {
            resizeControls();
        }
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
        umbra.dispose();
        if (controlsBatch != null) controlsBatch.dispose();
        if (controlsFont != null) controlsFont.dispose();
        if (controlsTitleFont != null) controlsTitleFont.dispose();
        if (controlsButtonFont != null) controlsButtonFont.dispose();
        if (controlsShapeRenderer != null) controlsShapeRenderer.dispose();
    }
}

