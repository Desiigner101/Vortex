package com.vortex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

    private static final float FRAME_DURATION = 0.1f;
    private static final int FRAME_COUNT = 50;
    private static final String IMAGE_PATH = "Backgrounds/worldBackGrounds_Images/spriteDrinksFor_eachWorld/tile";

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
            parameter.color = Color.WHITE;  // Set the font color to white
            // Remove border settings
            titleFont = generator.generateFont(parameter);
            generator.dispose();

            generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Geoform-Light.otf"));
            parameter.size = 48;
            subtitleFont = generator.generateFont(parameter);
            generator.dispose();

            generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Geoform-Medium.otf"));
            parameter.size = 36;
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

        // Load the custom Geoform-Bold font
        BitmapFont font;
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Geoform-Bold.otf"));
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size = 25;  // Adjust the size of the font here
            font = generator.generateFont(parameter);
            generator.dispose();
        } catch (Exception e) {
            // If the font fails to load, fall back to a default font
            font = new BitmapFont();
        }

        // Create a gradient background using Pixmap with dark purple and purple only
        Pixmap pixmap = new Pixmap(120, 40, Pixmap.Format.RGBA8888);
        for (int y = 0; y < pixmap.getHeight(); y++) {
            float ratio = (float) y / pixmap.getHeight();

            // Interpolate between dark purple and purple (no white highlight)
            float lerpRatio = ratio;  // Linear interpolation ratio from dark purple to purple
            pixmap.setColor(new Color(
                0.2f * (1 - lerpRatio) + 0.5f * lerpRatio,  // Red: From dark purple (0.2f) to purple (0.5f)
                0f * (1 - lerpRatio) + 0f * lerpRatio,      // Green: Stay constant (0f)
                0.2f * (1 - lerpRatio) + 1f * lerpRatio,    // Blue: From dark purple (0.2f) to purple (1f)
                1f
            ));

            pixmap.drawLine(0, y, pixmap.getWidth(), y);
        }

        // Convert the Pixmap into a Texture
        Texture gradientTexture = new Texture(pixmap);
        pixmap.dispose();  // Dispose Pixmap to free memory

        // Create the Drawable from the Texture
        Drawable gradientDrawable = new TextureRegionDrawable(new TextureRegion(gradientTexture));

        // Create a new button style with custom gradient and font
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;  // Use the custom font (Geoform-Bold)
        textButtonStyle.up = gradientDrawable;  // Apply the gradient to the button's 'up' state
        textButtonStyle.down = gradientDrawable;  // Apply the gradient to the button's 'down' state
        textButtonStyle.fontColor = Color.WHITE;  // Set the font color to white

        // Create the button
        viewButton = new TextButton("View", textButtonStyle);
        viewButton.setSize(120, 40);  // Original size
        viewButton.setPosition(Gdx.graphics.getWidth() - 428, 92);  // Set button position

        // Add the click listener
        viewButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("View button clicked!");
            }
        });

        // Add hover effect listeners for zooming in and out symmetrically, centered
        viewButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Zoom in effect (increase size symmetrically)
                float zoomFactor = 1.1f;  // 10% zoom
                float newWidth = viewButton.getWidth() * zoomFactor;
                float newHeight = viewButton.getHeight() * zoomFactor;

                // Reposition the button to keep it centered on its original position
                float deltaX = (newWidth - viewButton.getWidth()) / 2;
                float deltaY = (newHeight - viewButton.getHeight()) / 2;

                // Adjust position to make the zoom centered
                viewButton.setSize(newWidth, newHeight);
                viewButton.setPosition(viewButton.getX() - deltaX, viewButton.getY() - deltaY);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                // Zoom out effect (reset size symmetrically, zoom out from the center)
                float zoomFactor = 1 / 1.1f;  // Reverse of 10% zoom factor (to shrink it back)
                float newWidth = viewButton.getWidth() * zoomFactor;
                float newHeight = viewButton.getHeight() * zoomFactor;

                // Reposition the button to keep it centered on its original position after zooming out
                float deltaX = (newWidth - viewButton.getWidth()) / 2;
                float deltaY = (newHeight - viewButton.getHeight()) / 2;

                // Adjust position to keep the zoom symmetrical
                viewButton.setSize(newWidth, newHeight);
                viewButton.setPosition(viewButton.getX() - deltaX, viewButton.getY() - deltaY);
            }
        });

        // Add the button to the stage
        stage.addActor(viewButton);
    }


    @Override
    public void render(float delta) {
        elapsedTime += delta;
        if (elapsedTime > FRAME_DURATION) {
            frameIndex = (frameIndex + 1) % FRAME_COUNT;
            elapsedTime = 0;
        }

        // Get the current planet frame
        Texture currentFrame = xyberiaFrames[frameIndex];

        // Enhanced floating effect parameters
        float floatHeight = 20f; // Increased height for more pronounced floating motion
        float floatSpeed = 0.70f; // Speed of floating (adjust for faster/slower movement)
        float rotationSpeed = 1.5f; // Slight rotation speed (in degrees per second)

        // Calculate floating offset using sine wave for smooth up/down motion
        // Using two sine waves at different frequencies for more organic movement
        float floatOffset = (float) (Math.sin(Gdx.graphics.getFrameId() * 0.02f * floatSpeed) * floatHeight * 0.7f
            + Math.sin(Gdx.graphics.getFrameId() * 0.035f * floatSpeed) * floatHeight * 0.3f);

        // Calculate slight rotation for more dynamic effect
        float rotation = (float) Math.sin(Gdx.graphics.getFrameId() * 0.015f * rotationSpeed) * 2f;

        // Positioning the planet
        float xPos = Gdx.graphics.getWidth() - currentFrame.getWidth() * 5.3f - 100;
        float yPos = (Gdx.graphics.getHeight() - currentFrame.getHeight() * 5.3f) / 2f + floatOffset;

        // Draw background and planet
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw planet with enhanced floating effect and slight rotation
        float newWidth = currentFrame.getWidth() * 5.3f;
        float newHeight = currentFrame.getHeight() * 5.3f;

        // Calculate center for rotation
        float centerX = xPos + newWidth / 2;
        float centerY = yPos + newHeight / 2;

        // Save current transform
        batch.flush();
        batch.setTransformMatrix(batch.getTransformMatrix().idt().translate(centerX, centerY, 0).rotate(0, 0, 1, rotation).translate(-centerX, -centerY, 0));

        // Draw the planet with rotation
        batch.draw(currentFrame, xPos, yPos, newWidth, newHeight);

        // Reset transform
        batch.setTransformMatrix(batch.getTransformMatrix().idt());

        batch.end();

        // Rest of your rendering code...
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f);
        shapeRenderer.rect(50, 0, 300, Gdx.graphics.getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);

        // Draw text with fonts
        batch.begin();
        titleFont.draw(batch, "IMAGINE EARTH", 100, Gdx.graphics.getHeight() * 0.75f);
        subtitleFont.draw(batch, "PLANETARY COLONIZATION", 100, Gdx.graphics.getHeight() * 0.75f - 80);
        batch.end();

        // Act and draw the stage (buttons, etc.)
        stage.act();
        stage.draw();
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
