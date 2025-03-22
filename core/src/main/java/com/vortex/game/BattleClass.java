package com.vortex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Actor;
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

    private ImageButton attackButton, skillButton, ultimateButton;
    private Viewport viewport;
    private Texture backgroundTexture;
    private Texture roadTileTexture;
    private Texture enabledSkillPointTexture;
    private Texture disabledSkillPointTexture;
    private Texture turnIndicatorTexture; // Texture for the turn indicator
    private Character_Umbra umbra;
    private BattleAssetsClass battleAssets;
    private List<String> characters;
    private int currentTurn = 0; // Tracks whose turn it is
    private int skillPoints = 1;
    private int ultimateCooldown; // Tracks the cooldown for the ultimate ability

    private String universeName;
    private final int PLATFORM_Y = 200;
    private final int TILE_SIZE = 16;

    private int skillFlashCounter = 0;
    private float skillFlashTimer = 0;
    private boolean skillFlashing = false;

    private int ultimateFlashCounter = 0;
    private float ultimateFlashTimer = 0;
    private boolean ultimateFlashing = false;

    private float stateTime = 0; // Tracks animation time

    public BattleClass(String universeName, boolean hasUmbra, boolean hasNova, boolean hasJina,
                       String background, String roadTile, String musicFile) {
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
        turnIndicatorTexture = new Texture(Gdx.files.internal("BattleAssets/turnIndicator.png")); // Load turn indicator texture

        if (hasUmbra) {
            umbra = new Character_Umbra();
            ultimateCooldown = umbra.getUltCooldown(); // Initialize ultimate cooldown to maximum
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
            skillButton.getColor().a = 0.5f; // 50% opacity
        } else {
            skillButton.getColor().a = 0.85f; // Normal opacity
        }
        skillButton.setPosition(attackButton.getX() + attackButton.getWidth() + 20, skillPointY);
        skillButton.setSize(120, 120);

        ultimateButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal(umbra.getUltImage()))));
        if (ultimateCooldown > 0) {
            ultimateButton.getColor().a = 0.5f; // 50% opacity if on cooldown
        } else {
            ultimateButton.getColor().a = 0.85f; // Normal opacity
        }
        ultimateButton.setPosition(skillButton.getX() + skillButton.getWidth() + 20, skillPointY);
        ultimateButton.setSize(120, 120);

        attackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addSkillPoint();
                nextTurn(); // Advance the turn after using basic attack
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
                    nextTurn(); // Advance the turn only if the skill is successfully used
                } else {
                    skillFlashing = true; // Flash the skill cost text if the skill is not ready
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
                    skillButton.getColor().a = 0.5f; // Keep it at 50% if not enough skill points
                }
            }
        });

        ultimateButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (ultimateCooldown <= 0) {
                    useUltimate();
                    nextTurn(); // Advance the turn only if the ultimate is successfully used
                } else {
                    ultimateFlashing = true; // Flash the ultimate cooldown text if the ultimate is not ready
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
                    ultimateButton.getColor().a = 0.5f; // Keep it at 50% if on cooldown
                }
            }
        });

        stage.addActor(attackButton);
        stage.addActor(skillButton);
        stage.addActor(ultimateButton);
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
            ultimateCooldown = umbra.getUltCooldown(); // Set the cooldown
        }
    }

    private void nextTurn() {
        if (ultimateCooldown > 0) {
            ultimateCooldown--; // Decrease cooldown by 1 turn
        }
        currentTurn = (currentTurn + 1) % characters.size(); // Advance to the next character's turn
    }

    @Override
    public void render(float delta) {
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

        // Calculate the total width required for all characters
        float totalCharactersWidth = characters.size() * 300; // Assuming each character takes 300 units of width
        float startX = (viewport.getWorldWidth() - totalCharactersWidth) / 2; // Center the characters horizontally

        // Draw characters and turn indicator
        for (int i = 0; i < characters.size(); i++) {
            String character = characters.get(i);
            float characterX = startX + i * 300; // Position characters with equal spacing
            float characterY = PLATFORM_Y + 50;

            // Draw Umbra's idle animation
            if (character.equals("Umbra")) {
                TextureRegion currentFrame = umbra.getIdleFrame(delta); // Get the current animation frame
                spriteBatch.draw(currentFrame, characterX, characterY, 250, 250); // Adjust size as needed
            }

            // Draw turn indicator above the current character's head
            if (i == currentTurn) {
                float indicatorX = characterX + 100 - turnIndicatorTexture.getWidth() / 2; // Center above the character
                float indicatorY = characterY + 170; // Lower the turn indicator (reduced from 250 + 10 to 200)
                float indicatorWidth = turnIndicatorTexture.getWidth() * 4f; // Increase width by 50%
                float indicatorHeight = turnIndicatorTexture.getHeight() * 4f; // Increase height by 50%
                spriteBatch.draw(turnIndicatorTexture, indicatorX, indicatorY, indicatorWidth, indicatorHeight);
            }
        }

        spriteBatch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        spriteBatch.begin();
        bitmapFont.setColor(0.53f, 0.81f, 0.92f, 1f);
        bitmapFont.getData().setScale(2f);

        // Handle skill cost text flashing
        if (skillFlashing) {
            skillFlashTimer += delta;
            if (skillFlashTimer >= 0.1f) {
                skillFlashTimer = 0;
                skillFlashCounter++;
            }

            if (skillFlashCounter % 2 == 0) {
                bitmapFont.setColor(1f, 0f, 0f, 1f); // Flash red
            } else {
                bitmapFont.setColor(0.53f, 0.81f, 0.92f, 1f); // Normal color
            }

            if (skillFlashCounter >= 6) {
                skillFlashing = false;
                skillFlashCounter = 0;
            }
        }

        // Draw skill cost text at the bottom right of the skill button
        String skillCostText = umbra.getSkillCost() + " SP";
        GlyphLayout skillGlyphLayout = new GlyphLayout(bitmapFont, skillCostText); // Create a GlyphLayout instance for skill cost
        float skillTextX = skillButton.getX() + skillButton.getWidth() - skillGlyphLayout.width - 10; // Bottom right with padding
        float skillTextY = skillButton.getY() + 20; // Bottom with padding
        bitmapFont.draw(spriteBatch, skillGlyphLayout, skillTextX, skillTextY); // Draw the skill cost text

        // Reset font color after drawing skill cost text
        bitmapFont.setColor(0.53f, 0.81f, 0.92f, 1f);

        // Handle ultimate cooldown text flashing
        if (ultimateFlashing) {
            ultimateFlashTimer += delta;
            if (ultimateFlashTimer >= 0.1f) {
                ultimateFlashTimer = 0;
                ultimateFlashCounter++;
            }

            if (ultimateFlashCounter % 2 == 0) {
                bitmapFont.setColor(1f, 0f, 0f, 1f); // Flash red
            } else {
                bitmapFont.setColor(0.53f, 0.81f, 0.92f, 1f); // Normal color
            }

            if (ultimateFlashCounter >= 6) {
                ultimateFlashing = false;
                ultimateFlashCounter = 0;
            }
        }

        // Draw ultimate cooldown or "READY" text
        if (ultimateCooldown > 0) {
            // Draw cooldown text in the middle of the ultimate button
            String cooldownText = ultimateCooldown + (ultimateCooldown == 1 ? " turn" : " turns");
            GlyphLayout ultimateGlyphLayout = new GlyphLayout(bitmapFont, cooldownText); // Create a GlyphLayout instance for ultimate cooldown
            float ultX = ultimateButton.getX() + (ultimateButton.getWidth() - ultimateGlyphLayout.width) / 2; // Center horizontally
            float ultY = ultimateButton.getY() + (ultimateButton.getHeight() + ultimateGlyphLayout.height) / 2; // Center vertically
            bitmapFont.draw(spriteBatch, ultimateGlyphLayout, ultX, ultY); // Draw the ultimate cooldown text
        } else {
            // Draw "READY" text at the bottom right of the ultimate button
            String readyText = "READY";
            GlyphLayout readyGlyphLayout = new GlyphLayout(bitmapFont, readyText); // Create a GlyphLayout instance for "READY"
            float readyX = ultimateButton.getX() + ultimateButton.getWidth() - readyGlyphLayout.width - 10; // Bottom right with padding
            float readyY = ultimateButton.getY() + 20; // Bottom with padding
            bitmapFont.draw(spriteBatch, readyGlyphLayout, readyX, readyY); // Draw the "READY" text
        }

        bitmapFont.getData().setScale(1f); // Reset scale back to normal
        bitmapFont.setColor(1, 1, 1, 1);
        spriteBatch.end();
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void dispose() {
        spriteBatch.dispose();
        bitmapFont.dispose();
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        roadTileTexture.dispose();
        enabledSkillPointTexture.dispose();
        disabledSkillPointTexture.dispose();
        turnIndicatorTexture.dispose();
        umbra.dispose(); // Dispose of the character's resources
    }
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
