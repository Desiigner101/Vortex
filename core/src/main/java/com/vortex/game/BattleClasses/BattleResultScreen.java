package com.vortex.game.BattleClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.vortex.game.GameTransitions;
import com.vortex.SFX.PlayAudio;

import java.util.Random;

public class BattleResultScreen implements Screen {
    private final GameTransitions game;
    private final boolean isVictory;
    private final String backgroundPath;
    private final String musicFile;
    private final Runnable onContinueAction;
    private final Runnable onRetryAction;
    private final int roundCount;
    private BitmapFont roundFont;

    private SpriteBatch batch;
    private Texture background;
    private Texture resultTexture;
    private Stage stage;
    private BitmapFont messageFont;
    private BitmapFont buttonFont;
    private PlayAudio sfx;

    // Animation variables
    private float animationTime = 0;
    private final float animationDuration = 1f;
    private final float startYOffset = 10f;
    private float currentYOffset = startYOffset;
    private float currentAlpha = 0.5f;

    private String resultMessage;
    private String[] victoryMessages = {
        "That wasn't so bad.",
        "Woah.. The tech works wonders!",
        "That was a close one, wasn't it",
        "Another victory for the team!",
        "The enemy didn't stand a chance!"
    };

    private String[] defeatMessages = {
        "My Gear!! I worked really hard on those...",
        "Ouch... That's gonna leave a scar.",
        "That hurts... Let's go again",
        "We'll get them next time!",
        "I need to rethink my strategy..."
    };

    public BattleResultScreen(GameTransitions game, boolean isVictory, String backgroundPath,
                              String musicFile, Runnable onContinueAction,
                              Runnable onRetryAction, int roundCount) {
        this.game = game;
        this.isVictory = isVictory;
        this.backgroundPath = backgroundPath;
        this.musicFile = musicFile;
        this.onContinueAction = onContinueAction;
        this.onRetryAction = onRetryAction;
        this.roundCount = roundCount;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("Backgrounds/" + backgroundPath));
        resultTexture = new Texture(Gdx.files.internal("BattleAssets/" + (isVictory ? "VictoryText.png" : "DefeatText.png")));
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Initialize audio
        sfx = new PlayAudio();

        if(!isVictory){
            sfx.playSoundEffect("defeat_sfx.wav", 0);
            sfx.playMusic("defeat_ambience.wav");
        }else{
            sfx.playSoundEffect("victory_sfx.wav",0);
            sfx.playMusic(musicFile);
        }

        // Font generation with proper resource management
        FreeTypeFontGenerator generator = null;
        try {
            generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Poppins-Bold.ttf"));

            // Round font
            FreeTypeFontGenerator.FreeTypeFontParameter roundParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
            roundParam.size = 24;
            roundParam.color = Color.WHITE;
            roundFont = generator.generateFont(roundParam);

            // Message font
            FreeTypeFontGenerator.FreeTypeFontParameter messageParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
            messageParam.size = 36;
            messageParam.color = Color.WHITE;
            messageFont = generator.generateFont(messageParam);

            // Button font
            FreeTypeFontGenerator.FreeTypeFontParameter buttonParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
            buttonParam.size = 32;
            buttonParam.color = Color.WHITE;
            buttonFont = generator.generateFont(buttonParam);

        } finally {
            if (generator != null) {
                generator.dispose();
            }
        }

        // Select random message
        Random random = new Random();
        if (isVictory) {
            resultMessage = victoryMessages[random.nextInt(victoryMessages.length)];
        } else {
            resultMessage = defeatMessages[random.nextInt(defeatMessages.length)];
        }

        // Create button
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = buttonFont;

        TextButton actionButton = new TextButton(isVictory ? "Continue your journey" : "Try Again", buttonStyle);
        actionButton.setSize(400, 80);
        actionButton.setPosition(Gdx.graphics.getWidth()/2f - 200, Gdx.graphics.getHeight()/4f);

        actionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sfx.stopMusic();
                if (isVictory) {
                    onContinueAction.run();
                } else {
                    onRetryAction.run();
                }
            }
        });

        stage.addActor(actionButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // Update animation
        if (animationTime < animationDuration) {
            animationTime += delta;
            float progress = Math.min(animationTime / animationDuration, 1f);
            float easedProgress = 1 - (1 - progress) * (1 - progress);
            currentYOffset = startYOffset * (1 - easedProgress);
            currentAlpha = 0.5f + (0.5f * easedProgress);
        }

        batch.begin();
        // Draw background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Create GlyphLayout for text measurement
        GlyphLayout messageLayout = new GlyphLayout(messageFont, resultMessage);

        if (isVictory) {
            String roundText = "Total Rounds: " + roundCount;
            GlyphLayout roundLayout = new GlyphLayout(roundFont, roundText);
            roundFont.draw(batch, roundLayout,
                Gdx.graphics.getWidth()/2f - roundLayout.width/2,
                Gdx.graphics.getHeight() * 0.5f);
        }

        // Draw result image with animation
        float resultWidth = resultTexture.getWidth();
        float resultHeight = resultTexture.getHeight();
        float resultX = Gdx.graphics.getWidth()/2f - resultWidth/2;
        float resultY = Gdx.graphics.getHeight() * 0.7f - currentYOffset;

        batch.setColor(1, 1, 1, currentAlpha);
        batch.draw(resultTexture, resultX, resultY, resultWidth, resultHeight);
        batch.setColor(1, 1, 1, 1); // Reset color/alpha

        // Draw message
        messageFont.draw(batch, messageLayout,
            Gdx.graphics.getWidth()/2f - messageLayout.width/2,
            Gdx.graphics.getHeight() * 0.55f);

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        sfx.stopMusic();
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (background != null) background.dispose();
        if (resultTexture != null) resultTexture.dispose();
        if (stage != null) stage.dispose();
        if (messageFont != null) messageFont.dispose();
        if (buttonFont != null) buttonFont.dispose();
        if (roundFont != null) roundFont.dispose();
        if (sfx != null) sfx.dispose();
    }
}
