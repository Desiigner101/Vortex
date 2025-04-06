package com.vortex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.vortex.SFX.PlayAudio;
import com.badlogic.gdx.utils.Array;

public class BorderedTextBox {
    // Visual elements
    private NinePatch textBox;
    private BitmapFont font;
    private GlyphLayout layout = new GlyphLayout();

    // Text content
    private String fullText, displayedText = "";
    private String speaker;
    private Array<String> wrappedLines = new Array<String>();

    // Positioning and sizing
    private boolean isCenter;
    private float x, y, width, height;
    private final float PADDING = 20f;
    private final float LINE_SPACING = 30f; // Further increased line spacing
    private float lineHeight;

    // Text animation
    private float textSpeed = 0.03f; // Slightly faster typing
    private float timer = 0;
    private int charIndex = 0;
    private boolean isComplete = false;

    // Audio
    private float soundTimer = 0;
    private PlayAudio sfx = new PlayAudio();

    // Control
    private boolean shouldAdvance = false;

    public BorderedTextBox(String borderName, boolean isCenter, String speaker, String text, float height, float width, String textColor) {
        this.fullText = text;
        this.speaker = speaker;
        this.isCenter = isCenter;
        this.width = width;
        this.height = height;

        // Load border texture
        Texture borderTexture = new Texture(Gdx.files.internal("Borders/" + borderName + ".png"));
        this.textBox = new NinePatch(borderTexture, 10, 10, 10, 10);

        // Position calculation
        if (isCenter) {
            this.x = (Gdx.graphics.getWidth() - this.width) / 2;
            this.y = (Gdx.graphics.getHeight() - this.height) / 2;
        } else {
            this.x = (Gdx.graphics.getWidth() - this.width) / 2 + 160;
            this.y = 20;
        }

        // Font generation
        font = generateFont("fonts/PressStart-Regular.ttf", 20, textColor);
        layout.setText(font, "A"); // Use a sample character to get accurate line height
        lineHeight = layout.height;
    }

    public void render(SpriteBatch batch, float delta) {
        // Draw the border box
        textBox.draw(batch, x, y, width, height);

        // Draw speaker name (top left, inside padding)
        font.draw(batch, speaker + ":", x + PADDING, y + height - PADDING);

        // Update displayed text with typing effect
        updateDisplayedText(delta);

        // Wrap text to fit within the box
        wrapTextToFit();

        // Draw wrapped text lines
        drawTextLines(batch);

        handleInput();
    }

    private void updateDisplayedText(float delta) {
        if (!isComplete) {
            timer += delta;
            soundTimer += delta;

            if (timer >= textSpeed && charIndex < fullText.length()) {
                char currentChar = fullText.charAt(charIndex);
                displayedText += currentChar;

                if (speaker.equals("Nova") && currentChar != ' ' && soundTimer >= 0.05f) {
                    sfx.playSoundEffect("NovaTalk.wav", 0);
                    soundTimer = 0;
                }

                charIndex++;
                timer = 0;
            }
        }
    }

    private void wrapTextToFit() {
        wrappedLines.clear();
        float availableWidth = width - (2 * PADDING);

        if (displayedText.isEmpty()) return;

        String[] words = displayedText.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            layout.setText(font, currentLine.toString() + (currentLine.length() > 0 ? " " : "") + word);
            if (layout.width <= availableWidth) {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                wrappedLines.add(currentLine.toString());
                currentLine.setLength(0);
                currentLine.append(word);
            }
        }
        wrappedLines.add(currentLine.toString());
    }

    private void drawTextLines(SpriteBatch batch) {
        float availableHeight = height - (3 * PADDING) - lineHeight; // Subtract speaker name height
        float maxLines = (availableHeight + LINE_SPACING) / (lineHeight + LINE_SPACING);
        float startY = y + height - (2 * PADDING) - lineHeight; // Start below speaker name

        for (int i = 0; i < Math.min(wrappedLines.size, (int)maxLines); i++) {
            font.draw(batch, wrappedLines.get(i), x + PADDING, startY - i * (lineHeight + LINE_SPACING));
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (!isComplete) {
                completeTextDisplay();
            } else {
                shouldAdvance = true;
            }
        }
    }

    private void completeTextDisplay() {
        if (speaker.equals("Nova")) {
            for (int i = charIndex; i < fullText.length(); i++) {
                if (fullText.charAt(i) != ' ') {
                    sfx.playSoundEffect("NovaTalk.wav", 0);
                }
            }
        }
        displayedText = fullText;
        isComplete = true;
        wrapTextToFit(); // Re-wrap the full text
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
