package com.vortex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class BorderedTextBox {
    private SpriteBatch batch;
    private NinePatch textBox;
    private BitmapFont font;
    private String fullText, displayedText = "";
    private String speaker;
    private boolean isCenter;
    private float x, y, width, height;
    private float textSpeed = 0.05f; // Delay between characters (seconds)
    private float timer = 0;
    private int charIndex = 0;
    private boolean isComplete = false;

    public BorderedTextBox(String borderName, boolean isCenter, String speaker, String text, double height, double width, String textColor) {
        this.batch = new SpriteBatch();
        this.fullText = text;
        this.speaker = speaker;
        this.isCenter = isCenter;
        this.width = (float) width;
        this.height = (float) height;

        // Load border texture and create NinePatch
        Texture borderTexture = new Texture(Gdx.files.internal("Borders/" + borderName + ".png"));
        if (borderTexture.getWidth() >= 3 && borderTexture.getHeight() >= 3) {
            this.textBox = new NinePatch(borderTexture, 10, 10, 10, 10);
        } else {
            this.textBox = new NinePatch(borderTexture);
        }

        // Positioning
        if (isCenter) {
            this.x = (Gdx.graphics.getWidth() - this.width) / 2;
            this.y = (Gdx.graphics.getHeight() - this.height) / 2;
        } else {
            this.x = (Gdx.graphics.getWidth() - this.width) / 2; // Center horizontally
            this.y = 20; // Position slightly above the bottom
        }

        // Font setup
        font = generateFont("fonts/PressStart-Regular.ttf", 16, textColor);
    }

    public void render(float delta) {
        batch.begin();

        // Draw bordered box
        textBox.draw(batch, x, y, width, height);

        // Draw speaker name (instant display)
        font.draw(batch, speaker + ":", x + 15, y + height - 15);

        // Delayed text effect
        if (!isComplete) {
            timer += delta;
            if (timer >= textSpeed && charIndex < fullText.length()) {
                displayedText += fullText.charAt(charIndex);
                charIndex++;
                timer = 0;
            }
        }
        font.draw(batch, displayedText, x + 15, y + height - 50, width - 30, -1, true);
        batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (!isComplete) {
                displayedText = fullText; // Skip delay
                isComplete = true;
            } else {
                dispose(); // Close and free memory
            }
        }
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        textBox.getTexture().dispose();
    }

    private BitmapFont generateFont(String fontPath, int size, String hexColor) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = Color.valueOf(hexColor);
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2f;
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = 2;
        parameter.shadowColor = new Color(0, 0, 0, 0.75f);

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }
}
