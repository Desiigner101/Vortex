package com.vortex.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;

public class EndCreditsScreen implements Screen {
    private final SpriteBatch batch;
    private final Texture background;
    private BitmapFont font; // Changed to non-final since we initialize it after generator
    private final String[] credits;
    private final float scrollSpeed;
    private float yPosition;
    private final GameTransitions game;

    public EndCreditsScreen(GameTransitions game) {
        this.game = game;
        batch = new SpriteBatch();

        // Load background image
        background = new Texture(Gdx.files.internal("Backgrounds/LabMenu_temp.png"));

        // Set up font - IMPORTANT: Make sure the font file exists in the correct path
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Poppins-Bold.ttf")); // Changed path to lowercase 'fonts'
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Configure font parameters
        parameter.size = 36; // Set font size
        parameter.color = Color.GOLD;
        parameter.borderWidth = 1.5f;
        parameter.borderColor = Color.BLACK;

        font = generator.generateFont(parameter);
        generator.dispose(); // Important to dispose the generator after creating the font

        // Credits text
        credits = new String[] {
            "VORTEX",
            "",
            "Game Development Team",
            "",
            "Lead Programmer",
            "Your Name",
            "",
            "Game Design",
            "Team Member 1",
            "Team Member 2",
            "",
            "Art & Animation",
            "Artist 1",
            "Artist 2",
            "",
            "Sound & Music",
            "Composer 1",
            "",
            "Special Thanks",
            "Testers",
            "Supporters",
            "",
            "© 2023 Your Studio Name",
            "All Rights Reserved"
        };

        scrollSpeed = 60f;
        yPosition = 0;
    }

    @Override
    public void show() {
        yPosition = -50;
    }

    @Override
    public void render(float delta) {
        // Update scroll position
        yPosition += scrollSpeed * delta;

        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float centerX = Gdx.graphics.getWidth() / 2f;
        float currentY = yPosition;

        for (String line : credits) {
            if (line.isEmpty()) {
                currentY += 30;
                continue;
            }

            GlyphLayout layout = new GlyphLayout(font, line);

            // Header styling
            if (line.equals("VORTEX") || line.equals("Game Development Team") ||
                line.equals("Special Thanks")) {
                font.setColor(Color.SKY);
                font.getData().setScale(1.5f);
            } else {
                font.setColor(Color.WHITE);
                font.getData().setScale(1.0f);
            }

            font.draw(batch, layout, centerX - layout.width/2, currentY);
            currentY += layout.height + 40;

            // Reset styling
            font.setColor(Color.WHITE);
            font.getData().setScale(1.0f);
        }

        batch.end();

        // Check if credits finished scrolling
        if (currentY - calculateTotalCreditsHeight() > Gdx.graphics.getHeight()) {
            game.setScreen(new GameMenu(game));
        }

        // Skip credits on touch/click
        if (Gdx.input.justTouched()) {
            game.setScreen(new GameMenu(game));
        }
    }

    private float calculateTotalCreditsHeight() {
        float totalHeight = 0;
        GlyphLayout layout = new GlyphLayout();

        for (String line : credits) {
            if (line.isEmpty()) {
                totalHeight += 30;
                continue;
            }

            layout.setText(font, line);
            totalHeight += layout.height + 40;
        }

        return totalHeight;
    }

    @Override
    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        font.dispose();
    }
}
