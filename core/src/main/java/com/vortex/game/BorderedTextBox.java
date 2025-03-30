package com.vortex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.vortex.SFX.PlayAudio;

public class BorderedTextBox {
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
    private float soundTimer = 0;
    private PlayAudio sfx = new PlayAudio();
    private boolean shouldAdvance = false;

    public BorderedTextBox(String borderName, boolean isCenter, String speaker, String text, float height, float width, String textColor) {
        this.fullText = text;
        this.speaker = speaker;
        this.isCenter = isCenter;
        this.width = width;   // Use the exact width passed (e.g., 1500)
        this.height = height;  // Use the exact height passed (e.g., 200)

        // Load border texture
        Texture borderTexture = new Texture(Gdx.files.internal("Borders/" + borderName + ".png"));
        this.textBox = new NinePatch(borderTexture, 10, 10, 10, 10); // Adjust patches if needed

        // Positioning (use screen coordinates)
        if (isCenter) {
            this.x = (Gdx.graphics.getWidth() - this.width) / 2;
            this.y = (Gdx.graphics.getHeight() - this.height) / 2;
        } else {
            this.x = (Gdx.graphics.getWidth() - this.width) / 2+160; // Center horizontally
            this.y = 20; // Fixed offset from bottom
        }

        // Font setup
        font = generateFont("fonts/PressStart-Regular.ttf", 20, textColor);
    }

    public void render(SpriteBatch batch, float delta) {
        // Draw bordered box
        textBox.draw(batch, x, y, width, height);

        // Draw speaker name (instant display)
        font.draw(batch, speaker + ":", x + 15, y + height - 15);

        // Delayed text effect
        if (!isComplete) {
            timer += delta;
            soundTimer += delta;

            if (timer >= textSpeed && charIndex < fullText.length()) {
                char currentChar = fullText.charAt(charIndex);
                displayedText += currentChar;

                // Play sound effect for Nova's dialogue (non-space characters)
                if (speaker.equals("Nova") && currentChar != ' ' && soundTimer >= 0.05f) {
                    sfx.playSoundEffect("NovaTalk.wav", 0);
                    soundTimer = 0;
                }

                charIndex++;
                timer = 0;
            }
        }
        font.draw(batch, displayedText, x + 15, y + height - 50, width - 30, -1, true);

        handleInput();
    }

    private void handleInput() {
        // Check for space key or any mouse click
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (!isComplete) {
                // Play remaining sounds if skipping
                if (speaker.equals("Nova")) {
                    for (int i = charIndex; i < fullText.length(); i++) {
                        if (fullText.charAt(i) != ' ') {
                            sfx.playSoundEffect("NovaTalk.wav", 0);
                        }
                    }
                }
                displayedText = fullText; // Skip delay
                isComplete = true;
            } else {
                shouldAdvance = true;
            }
        }
    }

    public boolean shouldAdvance() {
        return shouldAdvance;
    }

    public void resetAdvance() {
        shouldAdvance = false;
    }

    public void dispose() {
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
