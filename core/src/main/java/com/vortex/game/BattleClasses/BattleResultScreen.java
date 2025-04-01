package com.vortex.game.BattleClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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

    // new: Variables for tracking skill points, wins, defeats, and time duration
    private int totalSkillPointsUsed = 0;
    private int totalWins = 0;
    private int totalDefeats = 0;
    private long startTime; // new: to track match start time


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
        this.startTime = System.currentTimeMillis(); // new: Track match start time
    }


    @Override
    public void show() {
        try {
            batch = new SpriteBatch();
            background = new Texture(Gdx.files.internal("Backgrounds/" + backgroundPath));
            resultTexture = new Texture(Gdx.files.internal("BattleAssets/" + (isVictory ? "VictoryText.png" : "DefeatText.png")));
            stage = new Stage();
            Gdx.input.setInputProcessor(stage);

            // Initialize audio
            sfx = new PlayAudio();

            // Play appropriate sounds
            if(!isVictory) {
                sfx.playSoundEffect("defeat_sfx.wav", 0);
                sfx.playMusic("defeat_ambience.wav");
            } else {
                sfx.playSoundEffect("victory_sfx.wav", 0);
                sfx.playMusic(musicFile);
            }

            // Font generation - create separate font for buttons
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Poppins-Bold.ttf"));
            FreeTypeFontGenerator buttonGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Poppins-Bold.ttf")); // Replace with your font file

            // Generate fonts
            FreeTypeFontGenerator.FreeTypeFontParameter roundParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
            roundParam.size = 24;
            roundParam.color = Color.WHITE;
            roundFont = generator.generateFont(roundParam);

            FreeTypeFontGenerator.FreeTypeFontParameter messageParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
            messageParam.size = 36;
            messageParam.color = Color.WHITE;
            messageFont = generator.generateFont(messageParam);

            // Special font for buttons with outline
            FreeTypeFontGenerator.FreeTypeFontParameter buttonParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
            buttonParam.size = 36; // Slightly larger for buttons
            buttonParam.color = Color.WHITE;
            buttonParam.borderWidth = 2f; // Add outline
            buttonParam.borderColor = Color.BLACK; // Outline color
            buttonParam.shadowOffsetX = 2; // Text shadow
            buttonParam.shadowOffsetY = 2;
            buttonParam.shadowColor = new Color(0, 0, 0, 0.7f);
            buttonFont = buttonGenerator.generateFont(buttonParam);

            generator.dispose();
            buttonGenerator.dispose();

            // NEW
            Random random = new Random();
            if(isVictory){
                resultMessage = victoryMessages[random.nextInt(victoryMessages.length)];
                totalWins++;
            }else{
                resultMessage = victoryMessages[random.nextInt(victoryMessages.length)];
                totalDefeats++;
            }

            //NEW
            totalSkillPointsUsed += random.nextInt(50) + 1;

            //NEW
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime) / 1000;

            //NEW
            FileHandler.saveStats(totalSkillPointsUsed, totalWins, totalDefeats, duration);


            // Create button style with the new font
            TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
            buttonStyle.font = buttonFont;

            // Load button textures
            if (isVictory) {
                buttonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("ui/CONTINUE_BUTTON.png"))));
                buttonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("ui/CONTINUE_BUTTON_CLICKED.png"))));
                buttonStyle.over = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("ui/CONTINUE_BUTTON_HOVER.png"))));
            } else {
                buttonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("ui/RETRY_BUTTON.png"))));
                buttonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("ui/RETRY_BUTTON_CLICKED.png"))));
                buttonStyle.over = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("ui/RETRY_BUTTON_HOVER.png"))));
            }


            // Create button with proper text padding
            TextButton actionButton = new TextButton(isVictory ? "CONTINUE YOUR JOURNEY" : "TRY AGAIN", buttonStyle);
            actionButton.getLabel().setFontScale(0.8f); // Adjust if text is too big
            actionButton.pad(10); // Add padding around text

            // Calculate button size based on text width
            GlyphLayout buttonLayout = new GlyphLayout(buttonFont, actionButton.getText().toString());
            float buttonWidth = Math.max(500, buttonLayout.width + 80); // Minimum 500px or text width + padding
            float buttonHeight = 90;

            actionButton.setSize(buttonWidth, buttonHeight);
            actionButton.setPosition(
                Gdx.graphics.getWidth() / 2f - buttonWidth / 2, // Perfectly centered
                Gdx.graphics.getHeight() * 0.25f // Positioned at 25% from bottom
            );

            actionButton.addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    try {
                        sfx.playSoundEffect("hover_button.wav", 0.3f);
                        actionButton.getLabel().setFontScale(0.85f); // Slightly enlarge on hover
                    } catch (Exception e) {
                        Gdx.app.error("Audio", "Failed to play hover sound", e);
                    }
                    super.enter(event, x, y, pointer, fromActor);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    actionButton.getLabel().setFontScale(0.8f); // Return to normal size
                    super.exit(event, x, y, pointer, toActor);
                }

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    try {
                        sfx.playSoundEffect("whenTextIsClicked.wav", 0.5f);
                        sfx.stopMusic();
                        if (isVictory) {
                            onContinueAction.run();
                        } else {
                            onRetryAction.run();
                        }
                    } catch (Exception e) {
                        Gdx.app.error("ButtonClick", "Error handling button click", e);
                    }
                }
            });

            stage.addActor(actionButton);

        } catch (Exception e) {
            Gdx.app.error("BattleResultScreen", "Error in show()", e);
        }
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
