package com.vortex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.vortex.Images.ImageHandler;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.Color;

public class CharacterDetailScreen extends ScreenAdapter {
    private final GameTransitions game;
    private SpriteBatch batch;
    private Texture nova_background;
    private Texture jina_background;
    private Texture umbra_background;
    private ImageHandler characterImage;
    private Vector2 characterPosition;
    private Stage stage;
    private Skin skin;
    private TextButton backButton;
    private Label nameLabel, attackLabel, skillLabel, ultimateLabel;
    private String backgroundIdentifier;

    private float screenWidth, screenHeight;
    private float characterWidth = 350, characterHeight = 500; // Enlarged character size

    public CharacterDetailScreen(GameTransitions game, String characterName, String imagePath, String basicAttack, String skill, String ultimate) {
        this.game = game;
        batch = new SpriteBatch();
        characterImage = new ImageHandler(imagePath);
        backgroundIdentifier = characterName;

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        // Load background textures based on character selection
        nova_background = new Texture(Gdx.files.internal("Backgrounds/Nova_CharViewBackground.png"));
        jina_background = new Texture(Gdx.files.internal("Backgrounds/Jina_CharViewBackground.png"));
        umbra_background = new Texture(Gdx.files.internal("Backgrounds/Umbra_CharViewBackground.png"));

        // Set character image position (center-left of the screen)
        characterPosition = new Vector2(screenWidth * 0.2f, screenHeight * 0.6f - characterHeight / 2 + 50);

        // Initialize UI components
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        createUI(characterName, basicAttack, skill, ultimate);
    }

    private void createUI(String characterName, String basicAttack, String skill, String ultimate) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/PressStart-Regular.ttf"));

        // Font settings for the character name (Large & Red)
        FreeTypeFontGenerator.FreeTypeFontParameter nameParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        nameParam.size = 35; // Larger font size for emphasis
        BitmapFont nameFont = generator.generateFont(nameParam);

        LabelStyle nameLabelStyle = new LabelStyle();
        nameLabelStyle.font = nameFont;
        nameLabelStyle.fontColor = Color.RED; // Name displayed in red

        // Font settings for attributes (Normal & White)
        FreeTypeFontGenerator.FreeTypeFontParameter attrParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        attrParam.size = 24; // Standard font size
        BitmapFont attrFont = generator.generateFont(attrParam);

        LabelStyle attrLabelStyle = new LabelStyle();
        attrLabelStyle.font = attrFont;
        attrLabelStyle.fontColor = Color.WHITE; // White text for attributes

        generator.dispose(); // Dispose font generator after use

        // Create labels for character details
        nameLabel = new Label(characterName, nameLabelStyle);
        attackLabel = new Label("Basic Attack: " + basicAttack, attrLabelStyle);
        skillLabel = new Label("Skill: " + skill, attrLabelStyle);
        ultimateLabel = new Label("Ultimate: " + ultimate, attrLabelStyle);

        // Positioning UI elements on the screen
        nameLabel.setPosition(screenWidth * 0.38f, screenHeight * 0.75f);
        attackLabel.setPosition(screenWidth * 0.4f, screenHeight * 0.6f);
        skillLabel.setPosition(screenWidth * 0.4f, screenHeight * 0.5f);
        ultimateLabel.setPosition(screenWidth * 0.4f, screenHeight * 0.4f);

        // Add labels to the stage
        stage.addActor(nameLabel);
        stage.addActor(attackLabel);
        stage.addActor(skillLabel);
        stage.addActor(ultimateLabel);

        // Create back button to return to character selection
        backButton = new TextButton("Back", skin);
        backButton.setSize(100, 50);
        backButton.setPosition(20, 20);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(new ViewCharacters(game)); // Navigate back
            }
        });
        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        batch.begin();

        // Select and draw the correct background based on character selection
        if (backgroundIdentifier.equals("Kaia 'Nova' Novere")) {
            batch.draw(nova_background, 0, 0, screenWidth, screenHeight);
        } else if (backgroundIdentifier.equals("Jina Melody")) {
            batch.draw(jina_background, 0, 0, screenWidth, screenHeight);
        } else {
            batch.draw(umbra_background, 0, 0, screenWidth, screenHeight);
        }

        // Draw the character image on screen
        characterImage.render(batch, characterPosition.x, characterPosition.y, characterWidth, characterHeight);

        batch.end();

        // Update and render UI elements
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose(); // Dispose sprite batch
        nova_background.dispose(); // Free memory for backgrounds
        umbra_background.dispose();
        jina_background.dispose();
        characterImage.dispose(); // Dispose character image
        stage.dispose(); // Dispose stage
        skin.dispose(); // Dispose UI skin
    }
}
