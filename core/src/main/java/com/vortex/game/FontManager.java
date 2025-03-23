package com.vortex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Disposable;
import java.util.HashMap;
import java.util.Map;

public class FontManager implements Disposable {

    private Map<String, FreeTypeFontGenerator> fontGenerators; // Stores font generators for different font files
    private Map<String, BitmapFont> fonts; // Stores cached fonts

    public FontManager() {
        fontGenerators = new HashMap<>();
        fonts = new HashMap<>();
    }

    /**
     * Loads a font file and stores its generator for later use.
     *
     * @param fontName The name of the font (used as a key).
     * @param fontPath The path to the font file.
     */
    public void loadFont(String fontName, String fontPath) {
        if (fontGenerators.containsKey(fontName)) {
            Gdx.app.log("FontManager", "Font " + fontName + " is already loaded.");
            return;
        }
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        fontGenerators.put(fontName, generator);
    }

    /**
     * Gets a font with the specified properties. If the font is not cached, it will be generated.
     *
     * @param fontName The name of the font (must be loaded first).
     * @param size      The size of the font.
     * @param color     The color of the font.
     * @return The generated or cached font.
     */
    public BitmapFont getFont(String fontName, int size, Color color) {
        String key = fontName + size + color.toString();
        if (!fonts.containsKey(key)) {
            if (!fontGenerators.containsKey(fontName)) {
                throw new IllegalArgumentException("Font " + fontName + " is not loaded. Call loadFont() first.");
            }
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = size;
            parameter.color = color;
            fonts.put(key, fontGenerators.get(fontName).generateFont(parameter));
        }
        return fonts.get(key);
    }

    @Override
    public void dispose() {
        for (BitmapFont font : fonts.values()) {
            font.dispose();
        }
        for (FreeTypeFontGenerator generator : fontGenerators.values()) {
            generator.dispose();
        }
        fonts.clear();
        fontGenerators.clear();
    }
}
