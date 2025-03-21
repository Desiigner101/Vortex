package com.vortex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    private ImageButton attackButton, skillButton;
    private Viewport viewport;
    private Texture backgroundTexture;
    private Texture roadTileTexture;
    private Texture enabledSkillPointTexture;
    private Texture disabledSkillPointTexture;
    private Character_Umbra umbra;
    private BattleAssetsClass battleAssets;
    private List<String> characters;
    private int currentTurn = 0;
    private int skillPoints = 1;

    private String universeName;
    private final int PLATFORM_Y = 200;
    private final int TILE_SIZE = 16;

    private int flashCounter = 0;
    private float flashTimer = 0;
    private boolean flashing = false;

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

        if (hasUmbra) {
            umbra = new Character_Umbra();
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

        attackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addSkillPoint();
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
                useSkill();
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

        stage.addActor(attackButton);
        stage.addActor(skillButton);
    }

    private void addSkillPoint() {
        if (skillPoints < 3) {
            skillPoints++;
        }
    }

    private void useSkill() {
        if (skillPoints >= umbra.getSkillCost()) {
            skillPoints -= umbra.getSkillCost();
        } else {
            flashing = true;
            flashCounter = 0;
            flashTimer = 0;
        }
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

        spriteBatch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();


        spriteBatch.begin();
        bitmapFont.setColor(0.53f, 0.81f, 0.92f, 1f);
        bitmapFont.getData().setScale(2f);

        if (flashing) {
            flashTimer += delta;
            if (flashTimer >= 0.1f) {
                flashTimer = 0;
                flashCounter++;
            }

            if (flashCounter % 2 == 0) {
                bitmapFont.setColor(1f, 0f, 0f, 1f);
            } else {
                bitmapFont.setColor(0.53f, 0.81f, 0.92f, 1f);
            }

            if (flashCounter >= 6) {
                flashing = false;
                bitmapFont.setColor(1f, 1f, 1f, 1f);
            }
        }

        float spX = skillButton.getX() + skillButton.getWidth() - 65;
        float spY = skillButton.getY() + 30;
        bitmapFont.draw(spriteBatch, "3 SP", spX, spY);

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
    }
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
