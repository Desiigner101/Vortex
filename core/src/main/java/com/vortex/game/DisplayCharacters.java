package com.vortex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.vortex.Images.ImageHandler;

public class DisplayCharacters extends ScreenAdapter {
    private final GameTransitions game;
    private SpriteBatch batch;
    private ImageHandler novaImage, umbraImage, jinaImage;
    private Vector2 umbraPosition, novaPosition, jinaPosition;
    private Stage stage;
    private Skin skin;
    private TextButton backButton;

    private float screenWidth, screenHeight;
    private float characterWidth = 128, characterHeight = 128; // Character size
    private float hoverScale = .5f; // Increased scale effect when hovered
    private float defaultScale = 1.0f;
    private float transitionSpeed = 5f; // Smooth transition speed

    // Hover animation variables
    private float novaScale = defaultScale, umbraScale = defaultScale, jinaScale = defaultScale;
    private float novaAlpha = 1f, umbraAlpha = 1f, jinaAlpha = 1f;

    public DisplayCharacters(GameTransitions game) {
        this.game = game;
        initialize();
    }

    private void initialize() {
        batch = new SpriteBatch();

        // ✅ Load images (Jina uses Umbra as a placeholder)
        novaImage = new ImageHandler("Pictures/Nova/CharacterView/Nova_CharView.png");
        umbraImage = new ImageHandler("Pictures/Umbra/CharacterView/Umbra_CharView.png");
        jinaImage = new ImageHandler("Pictures/Umbra/CharacterView/Umbra_CharView.png"); // Placeholder for Jina

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        // ✅ Properly spacing characters (Nova - Left, Umbra - Center, Jina - Right)
        float spacing = screenWidth * 0.2f; // Adjusted spacing
        float centerX = screenWidth * 0.5f;

        novaPosition = new Vector2(centerX - spacing - characterWidth / 2, screenHeight * 0.5f - characterHeight / 2);
        umbraPosition = new Vector2(centerX - characterWidth / 2, screenHeight * 0.5f - characterHeight / 2);
        jinaPosition = new Vector2(centerX + spacing - characterWidth / 2, screenHeight * 0.5f - characterHeight / 2);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        createBackButton();
    }

    private void createBackButton() {
        backButton = new TextButton("Back", skin);
        backButton.setSize(100, 50);
        backButton.setPosition(20, 20);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.create();
            }
        });
        stage.addActor(backButton);
    }

    private boolean isHovered(Vector2 position) {
        float mouseX = Gdx.input.getX();
        float mouseY = screenHeight - Gdx.input.getY(); // Flip Y-axis for LibGDX

        return mouseX >= position.x && mouseX <= position.x + characterWidth &&
            mouseY >= position.y && mouseY <= position.y + characterHeight;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // ✅ Smooth hover effects with transitions
        updateCharacterEffects(delta);

        renderCharacter(novaImage, novaPosition, novaScale, novaAlpha);
        renderCharacter(umbraImage, umbraPosition, umbraScale, umbraAlpha);
        renderCharacter(jinaImage, jinaPosition, jinaScale, jinaAlpha);

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    private float maxScale = 1.2f; // Maximum allowed scale when hovered

    private void updateCharacterEffects(float delta) {
        boolean novaHovered = isHovered(novaPosition);
        boolean umbraHovered = isHovered(umbraPosition);
        boolean jinaHovered = isHovered(jinaPosition);

        // Smooth scaling transition but with an upper limit
        novaScale += (novaHovered ? hoverScale : defaultScale - novaScale) * delta * transitionSpeed;
        umbraScale += (umbraHovered ? hoverScale : defaultScale - umbraScale) * delta * transitionSpeed;
        jinaScale += (jinaHovered ? hoverScale : defaultScale - jinaScale) * delta * transitionSpeed;

        // **Apply scaling limit**
        novaScale = Math.min(novaScale, maxScale);
        umbraScale = Math.min(umbraScale, maxScale);
        jinaScale = Math.min(jinaScale, maxScale);

        novaAlpha += (novaHovered ? 1f : 0.6f - novaAlpha) * delta * transitionSpeed;
        umbraAlpha += (umbraHovered ? 1f : 0.6f - umbraAlpha) * delta * transitionSpeed;
        jinaAlpha += (jinaHovered ? 1f : 0.6f - jinaAlpha) * delta * transitionSpeed;
    }

    private void renderCharacter(ImageHandler image, Vector2 position, float scale, float alpha) {
        float scaledWidth = characterWidth * scale;
        float scaledHeight = characterHeight * scale;

        // ✅ Adjust positioning so scaling happens inward, not outward
        float adjustedX = position.x + (characterWidth - scaledWidth) / 2;
        float adjustedY = position.y + (characterHeight - scaledHeight) / 2;

        // Apply transparency (highlight effect)
        batch.setColor(1, 1, 1, alpha);
        image.render(batch, adjustedX, adjustedY, scaledWidth, scaledHeight);
        batch.setColor(Color.WHITE); // Reset color to avoid affecting other elements
    }

    @Override
    public void dispose() {
        batch.dispose();
        novaImage.dispose();
        umbraImage.dispose();
        jinaImage.dispose();
        stage.dispose();
        skin.dispose();
    }
}
