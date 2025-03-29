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

    private SpriteBatch batch;
    private Texture background;
    private Stage stage;
    private BitmapFont titleFont;
    private BitmapFont messageFont;
    private BitmapFont buttonFont;
    private PlayAudio sfx;

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

    public BattleResultScreen(GameTransitions game, boolean isVictory, String backgroundPath, String musicFile, Runnable onContinueAction, Runnable onRetryAction) {
        this.game = game;
        this.isVictory = isVictory;
        this.backgroundPath = backgroundPath;
        this.musicFile = musicFile;
        this.onContinueAction = onContinueAction;
        this.onRetryAction = onRetryAction;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("Backgrounds/" + backgroundPath));
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Initialize audio
        sfx = new PlayAudio();
        sfx.playMusic(musicFile);

        // Generate fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Poppins-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Title font
        parameter.size = 72;
        parameter.color = isVictory ? Color.GOLD : Color.RED;
        titleFont = generator.generateFont(parameter);

        // Message font
        parameter.size = 36;
        parameter.color = Color.WHITE;
        messageFont = generator.generateFont(parameter);

        // Button font
        parameter.size = 32;
        parameter.color = Color.WHITE;
        buttonFont = generator.generateFont(parameter);

        generator.dispose();

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
                    onRetryAction.run(); // Use the retry action instead of newGame()
                }
            }
        });

        stage.addActor(actionButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();
        // Draw background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Create GlyphLayout instances for text measurement
        GlyphLayout titleLayout = new GlyphLayout(titleFont, isVictory ? "VICTORY" : "DEFEAT");
        GlyphLayout messageLayout = new GlyphLayout(messageFont, resultMessage);

        // Draw title
        titleFont.draw(batch, titleLayout,
            Gdx.graphics.getWidth()/2f - titleLayout.width/2,
            Gdx.graphics.getHeight() * 0.7f);

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
        batch.dispose();
        background.dispose();
        stage.dispose();
        titleFont.dispose();
        messageFont.dispose();
        buttonFont.dispose();
        sfx.dispose();
    }
}
