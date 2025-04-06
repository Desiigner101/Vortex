package WorldTransitions;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.vortex.game.GameTransitions;

public class Aetheris_planetTransition implements Screen {
    // Transition constants
    private static final float PLANET_FINAL_X = -250f;
    private static final float PLANET_ZOOM_FACTOR = 0.5f;
    private static final float TRANSITION_DURATION = 1.0f;
    private static final float WIPE_DURATION = 0.8f;
    private static final float RETURN_DURATION = 0.8f;

    // Updated Color constants
    private static final Color LIGHT_GREEN = new Color(144/255f, 238/255f, 144/255f, 0.85f);
    private static final Color DARK_GREEN = new Color(0/255f, 100/255f, 0/255f, 0.85f);
    private static final Color WHITE = new Color(1, 1, 1, 1);
    private static final Color SKY_BLUE = new Color(135/255f, 206/255f, 235/255f, 1f);
    private static final Color GRASS_GREEN = new Color(34/255f, 139/255f, 34/255f, 1f);

    private static final Color INFO_PANEL_COLOR = new Color(DARK_GREEN).sub(0, 0, 0, 0.15f);
    private static final Color INFO_TITLE_COLOR = new Color(LIGHT_GREEN);
    private static final Color INFO_SUBTITLE_COLOR = new Color(SKY_BLUE);
    private static final Color INFO_TEXT_COLOR = new Color(WHITE).sub(0, 0, 0, 0.2f);
    private static final Color PARTICLE_COLOR = new Color(GRASS_GREEN).add(0, 0.2f, 0.2f, 0);

    // Animation constants
    private static final float FRAME_DURATION = 0.1f;
    private static final int FRAME_COUNT = 50;
    private static final String IMAGE_PATH = "Backgrounds/worldBackGrounds_Images/aetheris/tile";
    private static final int PARTICLE_COUNT = 150;
    private static final float SCANLINE_SPEED = 100f;
    private static final float PULSE_DURATION = 2f;

    // Game objects
    private final Texture background;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final Texture[] xyberiaFrames;
    private final Stage stage;
    private final Skin skin;
    private TextButton viewButton;
    private final Texture scanlineTexture;
    private final Array<Particle> particles = new Array<>();
    private final GameTransitions game;

    // Animation state
    private int frameIndex = 0;
    private float elapsedTime = 0f;
    private float rotation;
    private float scanlineOffset = 0;
    private float pulseTimer = 0;
    private float glitchTimer = 0;
    private boolean showGlitchEffect = false;

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
    private final String fullTitle = "AETHERIS";
    private final String fullDescription = "Skyborn realm of storms and\nsoaring legends";
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
    private final String worldTitle = "Aetheris";
    private final String worldSubtitle = "universe 2";
    private final String worldDescription = "Aetheris is a majestic realm of floating islands drifting through endless skies, " +
        "where lightning crackles in storm-filled clouds and the air hums with ancient magic. " +
        "Towering above all is the Sky Leviathan, a shimmering, dragon-like guardian whose wings span miles. " +
        "Home to storm-charged creatures and crystal cities, Aetheris is a kingdom where the brave ride the winds " +
        "and the sky is both sanctuary and battleground.";

    private final GlyphLayout worldTitleLayout = new GlyphLayout();
    private final GlyphLayout worldSubtitleLayout = new GlyphLayout();
    private final GlyphLayout worldDescLayout = new GlyphLayout();

    // Fonts
    private final BitmapFont titleFont;
    private final BitmapFont subtitleFont;
    private final BitmapFont menuFont;
    private float panelWidth = 950;
    private float panelHeight = 600;

    // Particle class for effects
    private static class Particle {
        float x, y;
        float vx, vy;
        float size;
        float life;
        float maxLife;
        Color color;
    }

    public Aetheris_planetTransition(GameTransitions game) {
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

        this.scanlineTexture = createScanlineTexture();
        // Initialize particles
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particles.add(createParticle());
        }

        createButtons();
        calculateTextLayouts();
    }

    private Texture createScanlineTexture() {
        Pixmap pixmap = new Pixmap(4, 4, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(1, 1, 1, 0.1f));
        pixmap.fill();
        pixmap.setColor(new Color(1, 1, 1, 0.05f));
        pixmap.drawLine(0, 1, 4, 1);
        pixmap.drawLine(0, 3, 4, 3);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Particle createParticle() {
        Particle p = new Particle();
        p.x = MathUtils.random(Gdx.graphics.getWidth());
        p.y = MathUtils.random(Gdx.graphics.getHeight());
        p.vx = MathUtils.random(-50f, 50f);
        p.vy = MathUtils.random(-50f, 50f);
        p.size = MathUtils.random(2f, 8f);
        p.life = MathUtils.random(0f, 5f);
        p.maxLife = MathUtils.random(3f, 8f);

        // Updated particle color to green shades
        p.color = new Color(
            GRASS_GREEN.r * MathUtils.random(0.8f, 1.2f),
            GRASS_GREEN.g * MathUtils.random(0.8f, 1.2f),
            GRASS_GREEN.b * MathUtils.random(0.8f, 1.2f),
            PARTICLE_COLOR.a * MathUtils.random(0.5f, 1f)
        );
        return p;
    }

    private void updateParticles(float delta) {
        for (Particle p : particles) {
            p.x += p.vx * delta;
            p.y += p.vy * delta;
            p.life += delta;

            if (p.life > p.maxLife ||
                p.x < 0 || p.x > Gdx.graphics.getWidth() ||
                p.y < 0 || p.y > Gdx.graphics.getHeight()) {
                particles.removeValue(p, true);
                particles.add(createParticle());
            }
        }
    }

    private BitmapFont createTitleFont() {
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Geoform-Bold.otf"));
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size = 85;
            parameter.color = WHITE;
            parameter.borderColor = GRASS_GREEN;
            parameter.borderWidth = 2;
            parameter.shadowOffsetX = 3;
            parameter.shadowOffsetY = 3;
            parameter.shadowColor = new Color(0, 0, 0, 0.5f);
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
            parameter.color = WHITE;
            parameter.borderColor = GRASS_GREEN;
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

    private void fadeOutAllElements(Runnable onComplete) {
        // Fade out all stage actors
        for (Actor actor : stage.getActors()) {
            actor.addAction(Actions.fadeOut(0.5f));
        }

        // Fade out other visual elements by setting a global fade
        stage.addAction(Actions.sequence(
            Actions.fadeOut(0.5f),
            Actions.run(onComplete)
        ));
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

        // Create button background with green gradient
        Pixmap pixmap = new Pixmap(120, 40, Pixmap.Format.RGBA8888);
        for (int y = 0; y < pixmap.getHeight(); y++) {
            float ratio = (float) y / pixmap.getHeight();
            pixmap.setColor(new Color(
                DARK_GREEN.r * (1 - ratio) + LIGHT_GREEN.r * ratio,
                DARK_GREEN.g * (1 - ratio) + LIGHT_GREEN.g * ratio,
                DARK_GREEN.b * (1 - ratio) + LIGHT_GREEN.b * ratio,
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
        textButtonStyle.fontColor = WHITE;

        // View Button
        viewButton = new TextButton("View", textButtonStyle);
        viewButton.setSize(120, 40);
        viewButton.setPosition(Gdx.graphics.getWidth() - 428, 92);

        // Add hover and zoom effects to the View button
        viewButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                viewButton.addAction(Actions.parallel(
                    Actions.color(SKY_BLUE, 0.3f),
                    Actions.scaleTo(1.1f, 1.1f, 0.3f),
                    Actions.sequence(
                        Actions.moveBy(-2, 0, 0.05f),
                        Actions.moveBy(4, 0, 0.1f),
                        Actions.moveBy(-2, 0, 0.05f)
                    )
                ));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                viewButton.addAction(Actions.parallel(
                    Actions.color(WHITE, 0.3f),
                    Actions.scaleTo(1f, 1f, 0.3f)
                ));
            }
        });

        viewButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                viewButton.addAction(Actions.sequence(
                    Actions.parallel(
                        Actions.fadeOut(0.5f),
                        Actions.scaleTo(0.8f, 0.8f, 0.5f)
                    ),
                    Actions.removeActor()
                ));

                TextButton traverseButton = new TextButton("Traverse", textButtonStyle);
                traverseButton.setSize(120, 40);
                traverseButton.setPosition(Gdx.graphics.getWidth() - 600, 92);
                traverseButton.setColor(1, 1, 1, 0);
                traverseButton.setScale(0.8f);

                traverseButton.addListener(new ClickListener() {
                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        traverseButton.addAction(Actions.parallel(
                            Actions.color(SKY_BLUE, 0.3f),
                            Actions.scaleTo(1.1f, 1.1f, 0.3f),
                            Actions.sequence(
                                Actions.moveBy(-2, 0, 0.05f),
                                Actions.moveBy(4, 0, 0.1f),
                                Actions.moveBy(-2, 0, 0.05f)
                            )
                        ));
                    }

                    @Override
                    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                        traverseButton.addAction(Actions.parallel(
                            Actions.color(WHITE, 0.3f),
                            Actions.scaleTo(1f, 1f, 0.3f)
                        ));
                    }

                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // Disable button to prevent multiple clicks
                        traverseButton.setDisabled(true);

                        // Fade out all elements
                        fadeOutAllElements(new Runnable() {
                            @Override
                            public void run() {
                                // Transition to DisplayCharacters screen
                                game.startNextSequence();
                                dispose();
                            }
                        });
                    }
                });

                traverseButton.addAction(Actions.sequence(
                    Actions.parallel(
                        Actions.fadeIn(0.5f),
                        Actions.scaleTo(1f, 1f, 0.5f)
                    )
                ));

                stage.addActor(traverseButton);

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

        stage.addActor(viewButton);
    }

    private void calculateTextLayouts() {
        worldTitleLayout.setText(titleFont, worldTitle);
        worldSubtitleLayout.setText(subtitleFont, worldSubtitle);
        worldDescLayout.setText(menuFont, worldDescription, INFO_TEXT_COLOR, panelWidth - 80, Align.left, true);
    }

    @Override
    public void render(float delta) {
        // Clear screen with black (or your preferred background)
        ScreenUtils.clear(0, 0, 0, 1);

        // Update all animations and transitions
        updateTransition(delta);
        updateAnimation(delta);
        updateTypewriterEffect(delta);
        updateWipeAnimation(delta);
        updateReturnAnimation(delta);
        updateParticles(delta);
        updatePulseEffect(delta);

        // Render all elements (they will fade out with the stage's alpha)
        renderBackground();
        renderParticles();
        renderOverlay();
        renderPlanet(delta);
        renderInfoPanel();
        renderScanlines();

        stage.act(delta);
        stage.draw();
    }

    private void updatePulseEffect(float delta) {
        pulseTimer += delta;
        if (pulseTimer > PULSE_DURATION) {
            pulseTimer = 0;
        }
    }

    private void updateReturnAnimation(float delta) {
        if (returningToOriginal && returnProgress < 1f) {
            returnProgress = Math.min(1f, returnProgress + delta / RETURN_DURATION);
            if (returnProgress >= 1f) {
                returningToOriginal = false;
                showInfoPanel = false;
            }
        }
    }

    private void updateWipeAnimation(float delta) {
        if (wipeOpening && !returningToOriginal) {
            wipeProgress = Math.min(1f, wipeProgress + delta / WIPE_DURATION);
            if (wipeProgress >= 1f) {
                wipeOpening = false;
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

    private void renderParticles() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Particle p : particles) {
            float alpha = (1 - (p.life / p.maxLife)) * stage.getRoot().getColor().a;
            shapeRenderer.setColor(p.color.r, p.color.g, p.color.b, p.color.a * alpha);
            shapeRenderer.rect(p.x, p.y, p.size, p.size);
        }
        shapeRenderer.end();
    }

    private void renderPlanet(float delta) {
        Texture currentFrame = xyberiaFrames[frameIndex];
        float floatOffset = calculateFloatOffset();

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

        rotation += delta * 5f;

        batch.begin();
        // Apply stage alpha to planet too
        batch.setColor(1, 1, 1, stage.getRoot().getColor().a);
        batch.setColor(Color.WHITE);

        // Render planet with rotation
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

        float overlayWidth = 300 * (1 - transitionProgress);
        float overlayX = 50 + (transitionProgress * -400);
        float borderThickness = 3f;
        float cornerSize = 15f;

        // Main gradient fill with greens
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < overlayWidth; i++) {
            float ratio = i/overlayWidth;
            float alpha = (0.8f - ratio * 0.3f) * (1 - transitionProgress);

            shapeRenderer.setColor(
                DARK_GREEN.r * (1-ratio) + LIGHT_GREEN.r * ratio,
                DARK_GREEN.g * (1-ratio) + LIGHT_GREEN.g * ratio,
                DARK_GREEN.b * (1-ratio) + LIGHT_GREEN.b * ratio,
                alpha
            );
            shapeRenderer.rect(overlayX + i, 0, 1, Gdx.graphics.getHeight());
        }
        shapeRenderer.end();

        // Stylish border with corners
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Border glow effect
        float glowAlpha = 0.6f * (1 - transitionProgress);
        shapeRenderer.setColor(LIGHT_GREEN.r, LIGHT_GREEN.g, LIGHT_GREEN.b, glowAlpha * 0.3f);

        // Main border
        shapeRenderer.rect(overlayX - borderThickness, -borderThickness,
            overlayWidth + 2*borderThickness, borderThickness); // Top
        shapeRenderer.rect(overlayX - borderThickness, Gdx.graphics.getHeight(),
            overlayWidth + 2*borderThickness, borderThickness); // Bottom
        shapeRenderer.rect(overlayX - borderThickness, 0,
            borderThickness, Gdx.graphics.getHeight()); // Left
        shapeRenderer.rect(overlayX + overlayWidth, 0,
            borderThickness, Gdx.graphics.getHeight()); // Right

        // Corner accents
        shapeRenderer.setColor(LIGHT_GREEN.r, LIGHT_GREEN.g, LIGHT_GREEN.b, glowAlpha);

        // Top-left corner
        shapeRenderer.rect(overlayX - borderThickness, Gdx.graphics.getHeight() - cornerSize,
            cornerSize, borderThickness);
        shapeRenderer.rect(overlayX - borderThickness, Gdx.graphics.getHeight() - borderThickness,
            borderThickness, cornerSize);

        // Bottom-left corner
        shapeRenderer.rect(overlayX - borderThickness, 0,
            cornerSize, borderThickness);
        shapeRenderer.rect(overlayX - borderThickness, borderThickness,
            borderThickness, cornerSize);

        // Top-right corner
        shapeRenderer.rect(overlayX + overlayWidth - cornerSize + borderThickness,
            Gdx.graphics.getHeight() - borderThickness,
            cornerSize, borderThickness);
        shapeRenderer.rect(overlayX + overlayWidth,
            Gdx.graphics.getHeight() - cornerSize,
            borderThickness, cornerSize);

        // Bottom-right corner
        shapeRenderer.rect(overlayX + overlayWidth - cornerSize + borderThickness,
            0, cornerSize, borderThickness);
        shapeRenderer.rect(overlayX + overlayWidth,
            borderThickness, borderThickness, cornerSize);

        shapeRenderer.end();

        // Animated border pulse effect
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(2f);
        shapeRenderer.rect(overlayX, 0, overlayWidth, Gdx.graphics.getHeight());
        Gdx.gl.glLineWidth(1f);
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

        // Fade-in effect for the entire panel
        infoPanelAlpha = Math.min(1f, infoPanelAlpha + Gdx.graphics.getDeltaTime() * 2f);

        float panelX = Gdx.graphics.getWidth() - panelWidth - 70;
        float panelY = (Gdx.graphics.getHeight() - panelHeight) / 2;
        float centerX = panelX + panelWidth / 2;

        float currentHalfWidth = (panelWidth / 2) * wipeProgress;
        float leftEdge = centerX - currentHalfWidth;
        float rightEdge = centerX + currentHalfWidth;

        // Panel Fade-In Effect
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(INFO_PANEL_COLOR.r, INFO_PANEL_COLOR.g, INFO_PANEL_COLOR.b, INFO_PANEL_COLOR.a * infoPanelAlpha);
        shapeRenderer.rect(leftEdge, panelY, currentHalfWidth * 2, panelHeight);

        // Glow Effect (Light Green)
        if (wipeProgress < 0.95f) {
            float glowAlpha = Math.max(0, 0.5f * (1 - wipeProgress)) * infoPanelAlpha;
            shapeRenderer.setColor(LIGHT_GREEN.r, LIGHT_GREEN.g, LIGHT_GREEN.b, glowAlpha);
            shapeRenderer.rect(leftEdge - 5, panelY, 5, panelHeight);
            shapeRenderer.rect(rightEdge, panelY, 5, panelHeight);
        }
        shapeRenderer.end();

        // Border Fade-In Effect (Light Green)
        if (wipeProgress < 1f) {
            Gdx.gl.glLineWidth(3);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(LIGHT_GREEN.r, LIGHT_GREEN.g, LIGHT_GREEN.b, infoPanelAlpha);
            shapeRenderer.rect(leftEdge, panelY, currentHalfWidth * 2, panelHeight);
            shapeRenderer.end();
            Gdx.gl.glLineWidth(1);
        }

        // Decorative Lines and Corner Fade-In Effect (Light Green)
        if (wipeProgress > 0.3f) {
            float decoAlpha = Math.min(1f, (wipeProgress - 0.3f) / 0.7f) * infoPanelAlpha;

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(LIGHT_GREEN.r, LIGHT_GREEN.g, LIGHT_GREEN.b, decoAlpha);

            float lineWidth = currentHalfWidth * 2 - 40;
            // Horizontal divider lines
            shapeRenderer.rect(centerX - lineWidth / 2, panelY + panelHeight - 10, lineWidth, 2);
            shapeRenderer.rect(centerX - lineWidth / 2, panelY + 8, lineWidth, 2);

            if (wipeProgress > 0.8f) {
                float cornerSize = 15f;
                // Corner accents
                shapeRenderer.rect(leftEdge + 5, panelY + panelHeight - cornerSize - 5, cornerSize, cornerSize);
                shapeRenderer.rect(rightEdge - cornerSize - 5, panelY + 5, cornerSize, cornerSize);
            }
            shapeRenderer.end();
        }

        // Content (Text) Fade-In Effect
        if (wipeProgress > 0.3f) {
            float contentAlpha = Math.min(1f, (wipeProgress - 0.3f) / 0.7f) * infoPanelAlpha;

            batch.begin();

            // Title Fade-In Effect
            titleFont.setColor(INFO_TITLE_COLOR.r, INFO_TITLE_COLOR.g, INFO_TITLE_COLOR.b, contentAlpha);
            float titleX = centerX - worldTitleLayout.width / 2;
            float titleY = panelY + panelHeight - 50;
            titleFont.draw(batch, worldTitleLayout, titleX, titleY);

            // Subtitle Fade-In Effect
            subtitleFont.setColor(INFO_SUBTITLE_COLOR.r, INFO_SUBTITLE_COLOR.g, INFO_SUBTITLE_COLOR.b, contentAlpha);
            float subtitleX = centerX - worldSubtitleLayout.width / 2;
            float subtitleY = titleY - worldTitleLayout.height - 20;
            subtitleFont.draw(batch, worldSubtitleLayout, subtitleX, subtitleY);

            // Description Fade-In Effect
            if (wipeProgress > 0.6f) {
                float descAlpha = Math.min(1f, (wipeProgress - 0.6f) / 0.4f) * infoPanelAlpha;
                menuFont.setColor(INFO_TEXT_COLOR.r, INFO_TEXT_COLOR.g, INFO_TEXT_COLOR.b, descAlpha);
                menuFont.draw(batch, worldDescription,
                    leftEdge + 30,
                    subtitleY - worldSubtitleLayout.height - 58,
                    currentHalfWidth * 2 - 60,
                    Align.center, true);
            }

            batch.end();
        }

        // Square Particles Fade-In Effect (Light Green)
        if (wipeProgress < 1f) {
            float particleAlpha = Math.min(1f, (wipeProgress) * infoPanelAlpha);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            for (int i = 0; i < 20; i++) {
                float particleX = MathUtils.random(leftEdge, rightEdge);
                float particleY = MathUtils.random(panelY, panelY + panelHeight);
                float size = MathUtils.random(5f, 10f);

                shapeRenderer.setColor(LIGHT_GREEN.r, LIGHT_GREEN.g, LIGHT_GREEN.b, particleAlpha);
                shapeRenderer.rect(particleX, particleY, size, size);
            }

            shapeRenderer.end();
        }
    }

    private void renderScanlines() {
        if (!showInfoPanel) return;

        scanlineOffset += Gdx.graphics.getDeltaTime() * SCANLINE_SPEED;
        if (scanlineOffset > 4) scanlineOffset = 0;

        batch.begin();
        batch.setColor(1, 1, 1, 0.15f);
        batch.draw(scanlineTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
            0, (int)scanlineOffset, 4, 4);
        batch.setColor(Color.WHITE);
        batch.end();
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
