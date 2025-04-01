package com.vortex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class WorldTransitions implements Screen {
    // In your class variables, add these constants:
    private static final float PLANET_INITIAL_X = -100f; // Initial X offset
    private static final float PLANET_FINAL_X = -250f;   // Final leftmost position
    private static final float PLANET_ZOOM_FACTOR = 0.5f; // How much it zooms (0.3f was too subtle)


    private boolean transitionStarted = false;
    private float transitionProgress = 0f;
    private final float TRANSITION_DURATION = 1.0f; // 1 second transition
    private float planetFinalX = -200f; // Final X position after transition


    private Game game;
    private Texture background;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture[] xyberiaFrames;
    private int frameIndex = 0;
    private float elapsedTime = 0f;
    private BitmapFont titleFont;
    private BitmapFont subtitleFont;
    private BitmapFont menuFont;
    private Stage stage;
    private TextButton viewButton;
    private Skin skin;

    // Typewriter effect variables
    private String fullTitle = "XYBERIA";
    private String fullDescription = "Cybernetic dystopia of digital\nrebellion";
    private StringBuilder displayedTitle = new StringBuilder();
    private StringBuilder displayedDescription = new StringBuilder();
    private float typeTimer = 0;
    private float typeInterval = 0.06f;
    private boolean titleComplete = false;
    private boolean descriptionComplete = false;
    private float descriptionDelayTimer = 0;
    private float descriptionDelay = 0.05f;
    private GlyphLayout titleLayout = new GlyphLayout();
    private GlyphLayout descLayout = new GlyphLayout();

    // Color constants
    private static final Color CYBER_PURPLE = new Color(150/255f, 0/255f, 255/255f, 0.85f);
    private static final Color NEON_BLUE = new Color(30/255f, 144/255f, 255/255f, 0.7f);
    private static final Color DARK_OVERLAY = new Color(20/255f, 0/255f, 40/255f, 0.8f);
    private static final Color GRID_COLOR = new Color(100/255f, 255/255f, 255/255f, 0.15f);

    private static final float FRAME_DURATION = 0.1f;
    private static final int FRAME_COUNT = 50;
    private static final String IMAGE_PATH = "Backgrounds/worldBackGrounds_Images/spriteDrinksFor_eachWorld/tile";
    private float rotation;

    public WorldTransitions(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        background = new Texture(Gdx.files.internal("Backgrounds/worldBackGrounds_Images/Space_Background.png"));
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        loadFonts();
        loadFrames();
        createButton();
    }

    private void loadFonts() {
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Geoform-Bold.otf"));
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size = 72;
            parameter.color = Color.WHITE;
            parameter.borderColor = CYBER_PURPLE;
            parameter.borderWidth = 2;
            titleFont = generator.generateFont(parameter);
            generator.dispose();

            generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Geoform-Light.otf"));
            parameter.size = 40;
            parameter.borderWidth = 1;
            subtitleFont = generator.generateFont(parameter);
            generator.dispose();

            generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Geoform-Medium.otf"));
            parameter.size = 36;
            parameter.borderWidth = 0;
            menuFont = generator.generateFont(parameter);
            generator.dispose();
        } catch (Exception e) {
            Gdx.app.error("Font", "Failed to load custom fonts, using defaults", e);
            titleFont = new BitmapFont();
            subtitleFont = new BitmapFont();
            menuFont = new BitmapFont();
        }
    }

    private void loadFrames() {
        xyberiaFrames = new Texture[FRAME_COUNT];
        for (int i = 0; i < FRAME_COUNT; i++) {
            String filePath = IMAGE_PATH + String.format("%03d.png", i);
            xyberiaFrames[i] = new Texture(Gdx.files.internal(filePath));
        }
    }


    private void createButton() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        BitmapFont font;
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Geoform-Bold.otf"));
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size = 25;
            font = generator.generateFont(parameter);
            generator.dispose();
        } catch (Exception e) {
            font = new BitmapFont();
        }

        // Create button background
        Pixmap pixmap = new Pixmap(120, 40, Pixmap.Format.RGBA8888);
        for (int y = 0; y < pixmap.getHeight(); y++) {
            float ratio = (float) y / pixmap.getHeight();
            pixmap.setColor(new Color(
                0.2f * (1 - ratio) + 0.5f * ratio,
                0f * (1 - ratio) + 0f * ratio,
                0.2f * (1 - ratio) + 1f * ratio,
                1f
            ));
            pixmap.drawLine(0, y, pixmap.getWidth(), y);
        }
        Texture buttonTexture = new Texture(pixmap);
        pixmap.dispose();

        Drawable gradientDrawable = new TextureRegionDrawable(new TextureRegion(buttonTexture));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = gradientDrawable;
        textButtonStyle.down = gradientDrawable;
        textButtonStyle.fontColor = Color.WHITE;

        viewButton = new TextButton("View", textButtonStyle);
        viewButton.setSize(120, 40);
        viewButton.setPosition(Gdx.graphics.getWidth() - 428, 92); // Original position

        viewButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("View button clicked!");
                transitionStarted = true;
                transitionProgress = 0f;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Original zoom effect
                float zoomFactor = 1.1f;
                float newWidth = viewButton.getWidth() * zoomFactor;
                float newHeight = viewButton.getHeight() * zoomFactor;
                float deltaX = (newWidth - viewButton.getWidth()) / 2;
                float deltaY = (newHeight - viewButton.getHeight()) / 2;
                viewButton.setSize(newWidth, newHeight);
                viewButton.setPosition(viewButton.getX() - deltaX, viewButton.getY() - deltaY);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                // Original zoom out effect
                float zoomFactor = 1 / 1.1f;
                float newWidth = viewButton.getWidth() * zoomFactor;
                float newHeight = viewButton.getHeight() * zoomFactor;
                float deltaX = (newWidth - viewButton.getWidth()) / 2;
                float deltaY = (newHeight - viewButton.getHeight()) / 2;
                viewButton.setSize(newWidth, newHeight);
                viewButton.setPosition(viewButton.getX() - deltaX, viewButton.getY() - deltaY);
            }
        });

        stage.addActor(viewButton);
    }
    private Texture createPressedButtonTexture() {
        Pixmap pixmap = new Pixmap(180, 50, Pixmap.Format.RGBA8888);
        for (int y = 0; y < pixmap.getHeight(); y++) {
            float ratio = (float) y / pixmap.getHeight();
            pixmap.setColor(new Color(
                0.1f * (1 - ratio) + 0.4f * ratio,
                0f * (1 - ratio) + 0.1f * ratio,
                0.1f * (1 - ratio) + 0.7f * ratio,
                1f
            ));
            pixmap.drawLine(0, y, pixmap.getWidth(), y);
        }
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void render(float delta) {
        // Update transition progress if active
        if (transitionStarted && transitionProgress < 1f) {
            transitionProgress = Math.min(1f, transitionProgress + delta / TRANSITION_DURATION);
        }

        // Animation updates
        elapsedTime += delta;
        if (elapsedTime > FRAME_DURATION) {
            frameIndex = (frameIndex + 1) % FRAME_COUNT;
            elapsedTime = 0;
        }
        updateTypewriterEffect(delta);

        Texture currentFrame = xyberiaFrames[frameIndex];

        // Calculate transition offsets
        float transitionOffsetX = transitionProgress * planetFinalX;
        float transitionOffsetText = transitionProgress * -400; // Fast text disappearance

        // Draw background
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Planet animation parameters
        float floatHeight = 25f;
        float floatSpeed = 0.50f;
        float floatOffset = (float) (Math.sin(Gdx.graphics.getFrameId() * 0.02f * floatSpeed) * floatHeight * 0.7f
            + Math.sin(Gdx.graphics.getFrameId() * 0.035f * floatSpeed) * floatHeight * 0.3f);

        // Draw planet with transition - with controlled rotation
        batch.begin();

// Calculate eased progress
        float easedProgress = (float)(Math.sin(transitionProgress * Math.PI / 2));

// Enhanced zoom calculation
        float scale = 5.3f * (1 + easedProgress * PLANET_ZOOM_FACTOR);

// Position calculation
        float initialX = Gdx.graphics.getWidth() - currentFrame.getWidth() * 5.3f - 100;
        float xPos = initialX + (PLANET_FINAL_X - initialX) * easedProgress;

// Y position with floating animation
        float yPos = (Gdx.graphics.getHeight() - currentFrame.getHeight() * scale) / 2f + floatOffset;
        float newWidth = currentFrame.getWidth() * scale;
        float newHeight = currentFrame.getHeight() * scale;

// PROPER ROTATION CONTROL
        float rotationSpeed = 5f; // Degrees per second (adjust this value)
        rotation += delta * rotationSpeed;

        batch.setTransformMatrix(batch.getTransformMatrix().idt()
            .translate(xPos + newWidth/2, yPos + newHeight/2, 0)
            .rotate(0, 0, 1, rotation)
            .translate(-(xPos + newWidth/2), -(yPos + newHeight/2), 0));

        batch.draw(currentFrame, xPos, yPos, newWidth, newHeight);
        batch.setTransformMatrix(batch.getTransformMatrix().idt());
        batch.end();

        // Only draw overlay and text if transition isn't complete
        if (transitionProgress < 0.99f) {
            // Draw overlay with transition
            Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
            Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            // Overlay disappears completely
            float overlayWidth = 300 * (1 - transitionProgress);
            float overlayX = 50 + transitionOffsetText;

            // Main gradient overlay with fade-out effect
            for (int i = 0; i < overlayWidth; i++) {
                float ratio = i/overlayWidth;
                float alpha = (0.8f - ratio * 0.3f) * (1 - transitionProgress);
                shapeRenderer.setColor(
                    0.1f * (1-ratio) + 0.5f * ratio,
                    0f * (1-ratio) + 0.1f * ratio,
                    0.2f * (1-ratio) + 0.8f * ratio,
                    alpha
                );
                shapeRenderer.rect(overlayX + i, 0, 1, Gdx.graphics.getHeight());
            }
            shapeRenderer.end();
            Gdx.gl.glDisable(Gdx.gl.GL_BLEND);

            // Draw text with transition and fade-out
            if (transitionProgress < 0.9f) {
                batch.begin();
                titleLayout.setText(titleFont, displayedTitle.toString());
                float textX = 100 + transitionOffsetText;

                titleFont.setColor(1, 1, 1, 1 - transitionProgress);
                titleFont.draw(batch, titleLayout, textX, Gdx.graphics.getHeight() * 0.78f);

                if (titleComplete) {
                    descLayout.setText(subtitleFont, displayedDescription.toString());
                    subtitleFont.setColor(1, 1, 1, 1 - transitionProgress);
                    subtitleFont.draw(batch, descLayout, textX, Gdx.graphics.getHeight() * 0.78f - titleLayout.height - 30);
                }
                batch.end();
            }
        }

        stage.act(delta);
        stage.draw();

        // When transition is complete, you might want to switch screens here
        if (transitionProgress >= 1f) {
            // game.setScreen(new NextScreen(game));
        }
    }

    private void updateTypewriterEffect(float delta) {
        typeTimer += delta;

        if (typeTimer >= typeInterval) {
            typeTimer = 0;

            if (!titleComplete && displayedTitle.length() < fullTitle.length()) {
                displayedTitle.append(fullTitle.charAt(displayedTitle.length()));
            }
            else if (!titleComplete) {
                titleComplete = true;
            }
            else if (titleComplete) {
                descriptionDelayTimer += delta;
                if (descriptionDelayTimer >= descriptionDelay &&
                    !descriptionComplete &&
                    displayedDescription.length() < fullDescription.length()) {
                    displayedDescription.append(fullDescription.charAt(displayedDescription.length()));
                }
                else if (!descriptionComplete && displayedDescription.length() >= fullDescription.length()) {
                    descriptionComplete = true;
                }
            }
        }
    }

    @Override
    public void dispose() {
        background.dispose();
        for (Texture frame : xyberiaFrames) {
            if (frame != null) frame.dispose();
        }
        titleFont.dispose();
        subtitleFont.dispose();
        menuFont.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void show() {}
    @Override
    public void hide() {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
}
