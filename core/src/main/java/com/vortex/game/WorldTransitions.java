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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class WorldTransitions implements Screen {
    // Transition constants
    private static final float PLANET_INITIAL_X = -100f;
    private static final float PLANET_FINAL_X = -250f;
    private static final float PLANET_ZOOM_FACTOR = 0.5f;
    private static final float TRANSITION_DURATION = 1.0f;
    private static final float WIPE_DURATION = 0.8f;
    private static final float RETURN_DURATION = 0.8f;

    // Color constants
    private static final Color CYBER_PURPLE = new Color(150/255f, 0/255f, 255/255f, 0.85f);
    private static final Color NEON_BLUE = new Color(30/255f, 144/255f, 255/255f, 0.7f);
    private static final Color DARK_OVERLAY = new Color(20/255f, 0/255f, 40/255f, 0.8f);
    private static final Color GRID_COLOR = new Color(100/255f, 255/255f, 255/255f, 0.15f);
    private static final Color INFO_PANEL_COLOR = new Color(0.1f, 0.1f, 0.2f, 0.85f);
    private static final Color INFO_TITLE_COLOR = new Color(0.6f, 0.2f, 1f, 1f);
    private static final Color INFO_SUBTITLE_COLOR = new Color(0.4f, 0.7f, 1f, 1f);
    private static final Color INFO_TEXT_COLOR = new Color(0.8f, 0.8f, 1f, 1f);

    // Animation constants
    private static final float FRAME_DURATION = 0.1f;
    private static final int FRAME_COUNT = 50;
    private static final String IMAGE_PATH = "Backgrounds/worldBackGrounds_Images/spriteDrinksFor_eachWorld/tile";

    // Game objects
    private final Game game;
    private final Texture background;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final Texture[] xyberiaFrames;
    private final Stage stage;
    private final Skin skin;
    private TextButton viewButton;
    private TextButton closeButton;

    // Animation state
    private int frameIndex = 0;
    private float elapsedTime = 0f;
    private float rotation;

    // Transition state
    private boolean transitionStarted = false;
    private float transitionProgress = 0f;
    private final float planetFinalX = -200f;
    private float wipeProgress = 0f;
    private boolean wipeOpening = false;
    private boolean wipeClosing = false;
    private boolean returningToOriginal = false;
    private float returnProgress = 0f;

    // Typewriter effect
    private final String fullTitle = "XYBERIA";
    private final String fullDescription = "Cybernetic dystopia of digital\nrebellion";
    private final StringBuilder displayedTitle = new StringBuilder();
    private final StringBuilder displayedDescription = new StringBuilder();
    private float typeTimer = 0;
    private final float typeInterval = 0.06f;
    private boolean titleComplete = false;
    private boolean descriptionComplete = false;
    private float descriptionDelayTimer = 0;
    private final float descriptionDelay = 0.05f;
    private final GlyphLayout titleLayout = new GlyphLayout();
    private final GlyphLayout descLayout = new GlyphLayout();

    // Info panel
    private boolean showInfoPanel = false;
    private float infoPanelAlpha = 0f;
    private final String worldTitle = "Xyberia";
    private final String worldSubtitle = "universe 1";
    private final String worldDescription = "A neon-drenched cyberpunk world where the skies flicker with holograms " +
        "and the streets crawl with cyber-enhanced bounty hunters. In this lawless city " +
        "ruled by tech and corruption, Nova crash-lands with a broken device her only " +
        "way home. Hunted and outmatched, she must scavenge, fight, and outsmart her " +
        "enemies before Xyberia swallows her whole.";
    private final GlyphLayout worldTitleLayout = new GlyphLayout();
    private final GlyphLayout worldSubtitleLayout = new GlyphLayout();
    private final GlyphLayout worldDescLayout = new GlyphLayout();

    // Fonts
    private final BitmapFont titleFont;
    private final BitmapFont subtitleFont;
    private final BitmapFont menuFont;
    private float panelWidth = 950;
    private float panelHeight = 600;

    public WorldTransitions(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.background = new Texture(Gdx.files.internal("Backgrounds/worldBackGrounds_Images/Space_Background.png"));
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        this.titleFont = createTitleFont();
        this.subtitleFont = createSubtitleFont();
        this.menuFont = createMenuFont();
        this.xyberiaFrames = loadFrames();
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        createButtons();

        calculateTextLayouts();
    }

    private BitmapFont createTitleFont() {
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Geoform-Bold.otf"));
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size = 75;
            parameter.color = Color.WHITE;
            parameter.borderColor = CYBER_PURPLE;
            parameter.borderWidth = 2;
            BitmapFont font = generator.generateFont(parameter);
            generator.dispose();
            return font;
        } catch (Exception e) {
            Gdx.app.error("Font", "Failed to load title font, using default", e);
            return new BitmapFont();
        }
    }

    private BitmapFont createSubtitleFont() {
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Geoform-Bold.otf"));
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size = 25;
            parameter.color = Color.WHITE;
            parameter.borderColor = CYBER_PURPLE;
            parameter.borderWidth = 1;
            BitmapFont font = generator.generateFont(parameter);
            generator.dispose();
            return font;
        } catch (Exception e) {
            Gdx.app.error("Font", "Failed to load subtitle font, using default", e);
            return new BitmapFont();
        }
    }

    private BitmapFont createMenuFont() {
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Geoform-Medium.otf"));
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size = 30;
            parameter.borderWidth = 0;
            BitmapFont font = generator.generateFont(parameter);
            generator.dispose();
            return font;
        } catch (Exception e) {
            Gdx.app.error("Font", "Failed to load menu font, using default", e);
            return new BitmapFont();
        }
    }

    private Texture[] loadFrames() {
        Texture[] frames = new Texture[FRAME_COUNT];
        for (int i = 0; i < FRAME_COUNT; i++) {
            String filePath = IMAGE_PATH + String.format("%03d.png", i);
            frames[i] = new Texture(Gdx.files.internal(filePath));
        }
        return frames;
    }

    private void createButtons() {
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

        // View Button
        viewButton = new TextButton("View", textButtonStyle);
        viewButton.setSize(120, 40);
        viewButton.setPosition(Gdx.graphics.getWidth() - 428, 92);

        viewButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                transitionStarted = true;
                transitionProgress = 0f;
                showInfoPanel = true;
                infoPanelAlpha = 0f;
                wipeOpening = true;
                wipeClosing = false;
                wipeProgress = 0f;
                returningToOriginal = false;
                displayedTitle.setLength(0);
                displayedDescription.setLength(0);
                titleComplete = false;
                descriptionComplete = false;
                typeTimer = 0;
                descriptionDelayTimer = 0;
            }
        });

        // Close Button
        closeButton = new TextButton("X", textButtonStyle);
        closeButton.setSize(40, 40);
        closeButton.setVisible(false);

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                wipeClosing = true;
                wipeOpening = false;
                returningToOriginal = true;
                returnProgress = 0f;
            }
        });

        stage.addActor(viewButton);
        stage.addActor(closeButton);
    }

    private void calculateTextLayouts() {
        worldTitleLayout.setText(titleFont, worldTitle);
        worldSubtitleLayout.setText(subtitleFont, worldSubtitle);
        worldDescLayout.setText(menuFont, worldDescription, INFO_TEXT_COLOR, panelWidth - 80, Align.left, true);
    }

    @Override
    public void render(float delta) {
        updateTransition(delta);
        updateAnimation(delta);
        updateTypewriterEffect(delta);
        updateWipeAnimation(delta);
        updateReturnAnimation(delta);

        // Correct rendering order:
        renderBackground();  // Back layer
        renderOverlay();     // Mid layer (text behind planet)
        renderPlanet(delta); // Front layer (planet on top)
        renderInfoPanel();   // UI elements (always on top)

        stage.act(delta);
        stage.draw();

        checkTransitionComplete();
    }

    private void updateReturnAnimation(float delta) {
        if (returningToOriginal && returnProgress < 1f) {
            returnProgress = Math.min(1f, returnProgress + delta / RETURN_DURATION);
            if (returnProgress >= 1f) {
                returningToOriginal = false;
                showInfoPanel = false;
                closeButton.setVisible(false);
            }
        }
    }

    private void updateWipeAnimation(float delta) {
        if (wipeOpening && !returningToOriginal) {
            wipeProgress = Math.min(1f, wipeProgress + delta / WIPE_DURATION);
            if (wipeProgress >= 1f) {
                wipeOpening = false;
                closeButton.setVisible(true);
                float panelX = Gdx.graphics.getWidth() - panelWidth - 70;
                float panelY = (Gdx.graphics.getHeight() - panelHeight) / 2;
                closeButton.setPosition(panelX + panelWidth - 50, panelY + panelHeight - 50);
            }
        } else if (wipeClosing) {
            wipeProgress = Math.max(0f, wipeProgress - delta / WIPE_DURATION);
            if (wipeProgress <= 0f) {
                wipeClosing = false;
            }
        }
    }

    private void updateTransition(float delta) {
        if (transitionStarted && transitionProgress < 1f && !returningToOriginal) {
            transitionProgress = Math.min(1f, transitionProgress + delta / TRANSITION_DURATION);
        } else if (returningToOriginal && transitionProgress > 0f) {
            transitionProgress = Math.max(0f, transitionProgress - delta / TRANSITION_DURATION);
            if (transitionProgress <= 0f) {
                transitionStarted = false;
            }
        }
    }

    private void updateAnimation(float delta) {
        elapsedTime += delta;
        if (elapsedTime > FRAME_DURATION) {
            frameIndex = (frameIndex + 1) % FRAME_COUNT;
            elapsedTime = 0;
        }
    }

    private void renderBackground() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    private void renderPlanet(float delta) {
        Texture currentFrame = xyberiaFrames[frameIndex];
        float floatOffset = calculateFloatOffset();

        // Calculate progress based on whether we're returning
        float progress;
        if (returningToOriginal) {
            progress = (1f - returnProgress) * transitionProgress;
        } else {
            progress = (float)(Math.sin(transitionProgress * Math.PI / 2));
        }

        float scale = 5.3f * (1 + progress * PLANET_ZOOM_FACTOR);

        float initialX = Gdx.graphics.getWidth() - currentFrame.getWidth() * 5.3f - 100;
        float xPos = initialX + (PLANET_FINAL_X - initialX) * progress;
        float yPos = (Gdx.graphics.getHeight() - currentFrame.getHeight() * scale) / 2f + floatOffset;
        float newWidth = currentFrame.getWidth() * scale;
        float newHeight = currentFrame.getHeight() * scale;

        rotation += delta * 5f; // Rotation speed

        batch.begin();
        batch.setTransformMatrix(batch.getTransformMatrix().idt()
            .translate(xPos + newWidth/2, yPos + newHeight/2, 0)
            .rotate(0, 0, 1, rotation)
            .translate(-(xPos + newWidth/2), -(yPos + newHeight/2), 0));

        batch.draw(currentFrame, xPos, yPos, newWidth, newHeight);
        batch.setTransformMatrix(batch.getTransformMatrix().idt());
        batch.end();
    }

    private float calculateFloatOffset() {
        return (float) (Math.sin(Gdx.graphics.getFrameId() * 0.02f * 0.50f) * 25f * 0.7f
            + Math.sin(Gdx.graphics.getFrameId() * 0.035f * 0.50f) * 25f * 0.3f);
    }

    private void renderOverlay() {
        if (transitionProgress >= 0.99f && !returningToOriginal) return;

        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float overlayWidth = 300 * (1 - transitionProgress);
        float overlayX = 50 + (transitionProgress * -400);

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

        if (transitionProgress < 0.9f || returningToOriginal) {
            renderText();
        }
    }

    private void renderText() {
        batch.begin();
        titleLayout.setText(titleFont, displayedTitle.toString());
        float textX = 100 + (transitionProgress * -400);

        float textAlpha = returningToOriginal ? (1 - returnProgress) : (1 - transitionProgress);
        titleFont.setColor(1, 1, 1, textAlpha);
        titleFont.draw(batch, titleLayout, textX, Gdx.graphics.getHeight() * 0.78f);

        if (titleComplete) {
            descLayout.setText(subtitleFont, displayedDescription.toString());
            subtitleFont.setColor(1, 1, 1, textAlpha);
            subtitleFont.draw(batch, descLayout, textX, Gdx.graphics.getHeight() * 0.78f - titleLayout.height - 30);
        }
        batch.end();
    }

    private void renderInfoPanel() {
        if (!showInfoPanel || transitionProgress <= 0) return;

        infoPanelAlpha = Math.min(1f, infoPanelAlpha + Gdx.graphics.getDeltaTime() * 2f);

        // Panel dimensions and position
        float panelX = Gdx.graphics.getWidth() - panelWidth - 70;
        float panelY = (Gdx.graphics.getHeight() - panelHeight) / 2;
        float centerX = panelX + panelWidth / 2;

        // Center-out wipe effect
        float currentHalfWidth = (panelWidth / 2) * wipeProgress;
        float leftEdge = centerX - currentHalfWidth;
        float rightEdge = centerX + currentHalfWidth;

        // Draw the expanding panel
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(INFO_PANEL_COLOR.r, INFO_PANEL_COLOR.g, INFO_PANEL_COLOR.b, INFO_PANEL_COLOR.a * infoPanelAlpha);
        shapeRenderer.rect(leftEdge, panelY, currentHalfWidth * 2, panelHeight);

        // Glow effect at expanding edges
        if (wipeProgress < 0.95f) {
            float glowAlpha = 0.5f * (1 - wipeProgress);
            shapeRenderer.setColor(CYBER_PURPLE.r, CYBER_PURPLE.g, CYBER_PURPLE.b, glowAlpha);
            shapeRenderer.rect(leftEdge - 5, panelY, 5, panelHeight); // Left glow
            shapeRenderer.rect(rightEdge, panelY, 5, panelHeight);   // Right glow
        }
        shapeRenderer.end();

        // Draw border (only when panel is expanding)
        if (wipeProgress < 1f) {
            Gdx.gl.glLineWidth(3);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(CYBER_PURPLE.r, CYBER_PURPLE.g, CYBER_PURPLE.b, infoPanelAlpha);
            shapeRenderer.rect(leftEdge, panelY, currentHalfWidth * 2, panelHeight);
            shapeRenderer.end();
            Gdx.gl.glLineWidth(1);
        }

        // Draw decorative elements
        if (wipeProgress > 0.3f) {
            float decoAlpha = Math.min(1f, (wipeProgress - 0.3f) / 0.7f) * infoPanelAlpha;

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(NEON_BLUE.r, NEON_BLUE.g, NEON_BLUE.b, decoAlpha * 0.7f);

            // Top and bottom accent lines
            float lineWidth = currentHalfWidth * 2 - 40;
            shapeRenderer.rect(centerX - lineWidth/2, panelY + panelHeight - 10, lineWidth, 2);
            shapeRenderer.rect(centerX - lineWidth/2, panelY + 8, lineWidth, 2);

            // Corner accents (appear when mostly open)
            if (wipeProgress > 0.8f) {
                float cornerSize = 15f;
                shapeRenderer.rect(leftEdge + 5, panelY + panelHeight - cornerSize - 5, cornerSize, cornerSize);
                shapeRenderer.rect(rightEdge - cornerSize - 5, panelY + 5, cornerSize, cornerSize);
            }
            shapeRenderer.end();
        }

        // Draw content with fade-in effect
        if (wipeProgress > 0.3f) {
            float contentAlpha = Math.min(1f, (wipeProgress - 0.3f) / 0.7f) * infoPanelAlpha;

            batch.begin();

            // Title (centered)
            titleFont.setColor(INFO_TITLE_COLOR.r, INFO_TITLE_COLOR.g, INFO_TITLE_COLOR.b, contentAlpha);
            float titleX = centerX - worldTitleLayout.width / 2;
            float titleY = panelY + panelHeight - 50;
            titleFont.draw(batch, worldTitleLayout, titleX, titleY);

            // Subtitle (centered)
            subtitleFont.setColor(INFO_SUBTITLE_COLOR.r, INFO_SUBTITLE_COLOR.g, INFO_SUBTITLE_COLOR.b, contentAlpha);
            float subtitleX = centerX - worldSubtitleLayout.width / 2;
            float subtitleY = titleY - worldTitleLayout.height - 20;
            subtitleFont.draw(batch, worldSubtitleLayout, subtitleX, subtitleY);

            // Description (appears later)
            if (wipeProgress > 0.6f) {
                float descAlpha = Math.min(1f, (wipeProgress - 0.6f) / 0.4f) * infoPanelAlpha;
                menuFont.setColor(INFO_TEXT_COLOR.r, INFO_TEXT_COLOR.g, INFO_TEXT_COLOR.b, descAlpha);
                menuFont.draw(batch, worldDescription,
                    leftEdge + 30,
                    subtitleY - worldSubtitleLayout.height - 40,
                    currentHalfWidth * 2 - 60,
                    Align.center, true);
            }

            batch.end();
        }

        // Draw square particles during the panel's wipe effect
        if (wipeProgress < 1f) {
            float particleAlpha = Math.min(1f, (wipeProgress) * infoPanelAlpha);  // Increase as panel expands
            batch.begin();

            // Draw random square particles within the wipe area
            for (int i = 0; i < 20; i++) {  // Number of particles
                float particleX = MathUtils.random(leftEdge, rightEdge);  // Random X within the wipe area
                float particleY = MathUtils.random(panelY, panelY + panelHeight);  // Random Y within the panel
                float size = MathUtils.random(5f, 10f);  // Random size for the square particles

                shapeRenderer.setColor(NEON_BLUE.r, NEON_BLUE.g, NEON_BLUE.b, particleAlpha);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.rect(particleX, particleY, size, size);  // Draw square particle
                shapeRenderer.end();
            }

            batch.end();
        }

        // Show close button when fully open
        if (wipeProgress >= 1f && !closeButton.isVisible() && !returningToOriginal) {
            closeButton.setVisible(true);
            closeButton.setPosition(panelX + panelWidth - 50, panelY + panelHeight - 50);
        }
    }

    private void checkTransitionComplete() {
        if (transitionProgress >= 1f) {
            // game.setScreen(new NextScreen(game));
        }
    }

    private void updateTypewriterEffect(float delta) {
        if (returningToOriginal) return;

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
    public void resize(int width, int height) {
        calculateTextLayouts();
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        calculateTextLayouts();
    }

    @Override
    public void hide() {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
}
