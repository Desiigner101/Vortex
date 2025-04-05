package com.vortex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.vortex.SFX.PlayAudio;

public class EndCreditsScreen implements Screen {
    private final SpriteBatch batch;
    private final Texture background;
    private BitmapFont titleFont;
    private BitmapFont nameFont;
    private BitmapFont headerFont;
    private final String[] credits;
    private final float scrollSpeed;
    private float yPosition;
    private final GameTransitions game;
    private Music creditsMusic;
    private PlayAudio playAudio;
    private boolean isTransitioning = false;

    public EndCreditsScreen(GameTransitions game) {
        playAudio = new PlayAudio();
        this.game = game;
        this.batch = new SpriteBatch();
        this.background = new Texture(Gdx.files.internal("Backgrounds/LabMenu_temp.png"));

        // Initialize fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Poppins-SemiBold.ttf"));

        // Header font
        FreeTypeFontGenerator.FreeTypeFontParameter headerParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        headerParams.size = 48;
        headerParams.color = Color.WHITE;
        headerParams.borderWidth = 2f;
        headerParams.borderColor = Color.BLACK;
        headerParams.spaceX = 5;
        headerFont = generator.generateFont(headerParams);

        // Title font
        FreeTypeFontGenerator.FreeTypeFontParameter titleParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleParams.size = 36;
        titleParams.color = Color.WHITE;
        titleParams.borderWidth = 2f;
        titleParams.borderColor = Color.BLACK;
        titleParams.spaceX = 3;
        titleFont = generator.generateFont(titleParams);

        // Name font
        FreeTypeFontGenerator.FreeTypeFontParameter nameParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        nameParams.size = 32;
        nameParams.color = Color.GOLD;
        nameParams.borderWidth = 1.5f;
        nameParams.borderColor = Color.BLACK;
        nameFont = generator.generateFont(nameParams);

        generator.dispose();

        // Credits text
        credits = new String[] {
            "VORTEX",
            "",
            "Game Development Team",
            "",
            "Lead Programmers",
            "Daniel luis P. Garcia",
            "Gino M. Sarsonas",
            "Sophia Bianca Aloria",
            "",
            "Game Design",
            "Daniel Luis P. Garcia",
            "Michelle Marie P. Habon",
            "Ashley Igonia",
            "",
            "Art & Animation",
            "Daniel Luis P. Garcia",
            "",
            "",
            "Sound & Music",
            "Gino M. Sarsonas",
            "Michelle Marie P. Habon",
            "Ashley Igonia",
            "",
            "Special Thanks",
            "Testers",
            "Supporters",
            "Sir Khai the Goat Gumonan",
            "",
            "© 2025 Vortex",
            "All Rights Reserved"
        };

        scrollSpeed = 60f;
        yPosition = Gdx.graphics.getHeight();
    }

    @Override
    public void show() {
        yPosition = Gdx.graphics.getHeight();
        isTransitioning = false;

       playAudio.playMusic("EndCreditMusic.wav");
    }

    @Override
    public void render(float delta) {
        if (isTransitioning) return;

        // Update scroll position
        yPosition -= scrollSpeed * delta;

        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background and credits
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float centerX = Gdx.graphics.getWidth() / 2f;
        float currentY = yPosition;

        for (String line : credits) {
            if (line.isEmpty()) {
                currentY += 30;
                continue;
            }

            boolean isMainHeader = line.equals("VORTEX") ||
                line.equals("Game Development Team") ||
                line.equals("Special Thanks");

            boolean isTitle = !isMainHeader && (line.equals("Lead Programmers") ||
                line.equals("Game Design") ||
                line.equals("Art & Animation") ||
                line.equals("Sound & Music") ||
                line.equals("Testers") ||
                line.equals("Supporters") ||
                line.equals("© 2025 Vortex") ||
                line.equals("All Rights Reserved"));

            BitmapFont currentFont = isMainHeader ? headerFont : (isTitle ? titleFont : nameFont);
            GlyphLayout layout = new GlyphLayout(currentFont, line);
            currentFont.draw(batch, layout, centerX - layout.width/2, currentY);
            currentY += layout.height + (isMainHeader ? 60 : 40);
        }

        batch.end();

        // Check if credits finished or screen was touched
        if (currentY < -calculateTotalCreditsHeight() || Gdx.input.justTouched()) {
            returnToMenu();
        }
    }

    private void returnToMenu() {
        if (isTransitioning) return;

        isTransitioning = true;

        // Stop music if playing
        if (creditsMusic != null && creditsMusic.isPlaying()) {
            creditsMusic.stop();
        }



        // Return to main menu
        game.setScreen(new GameMenu(game));
    }

    private float calculateTotalCreditsHeight() {
        float totalHeight = 0;
        GlyphLayout layout = new GlyphLayout();

        for (String line : credits) {
            if (line.isEmpty()) {
                totalHeight += 30;
                continue;
            }

            boolean isMainHeader = line.equals("VORTEX") ||
                line.equals("Game Development Team") ||
                line.equals("Special Thanks");

            boolean isTitle = !isMainHeader && (line.equals("Lead Programmers") ||
                line.equals("Game Design") ||
                line.equals("Art & Animation") ||
                line.equals("Sound & Music") ||
                line.equals("Testers") ||
                line.equals("Supporters") ||
                line.equals("© 2025 Vortex") ||
                line.equals("All Rights Reserved"));

            BitmapFont currentFont = isMainHeader ? headerFont : (isTitle ? titleFont : nameFont);
            layout.setText(currentFont, line);
            totalHeight += layout.height + (isMainHeader ? 60 : 40);
        }

        return totalHeight;
    }

    @Override
    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        // Don't dispose here - let dispose() handle cleanup
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        headerFont.dispose();
        titleFont.dispose();
        nameFont.dispose();
    }
}
